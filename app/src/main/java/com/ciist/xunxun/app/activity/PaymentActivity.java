package com.ciist.xunxun.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.entity.WeixinParentId;
import com.ciist.xunxun.app.game.Config;
import com.ciist.xunxun.app.util.Constants;
import com.ciist.xunxun.app.util.MD5;
import com.ciist.xunxun.app.util.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import widget.CiistTitleView;

/**
 * Created by hw on 2016/5/11.
 */
public class PaymentActivity extends Activity {

    public static PaymentActivity instance = null;
    private IWXAPI api;  //微信支付

    private EditText payment_phoneNum;//手机号
    private String singlePrice;//单价
    private TextView payment_num;//份数
    private int n = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //注册微信支付
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);

        instance = this;

        //获取上面传下来的单价数值
        //getPrice
        singlePrice = "0.01";

        payment_num = (TextView) findViewById(R.id.payment_num);
        payment_phoneNum = (EditText) findViewById(R.id.payment_phoneNum);

        TextView payment_price = (TextView) findViewById(R.id.payment_price);
        payment_price.setText("￥" + singlePrice);

        findViewById(R.id.payment_jian).setOnClickListener(new View.OnClickListener() {//减
            @Override
            public void onClick(View v) {
                if (payment_num.getText().toString().equals("1")){
                    Toast.makeText(PaymentActivity.this,"已是最小值",Toast.LENGTH_SHORT).show();
                }else{
                    n = n - 1;
                    payment_num.setText("" + n);
                }
            }
        });

        findViewById(R.id.payment_jia).setOnClickListener(new View.OnClickListener() {//加
            @Override
            public void onClick(View v) {
                if (payment_num.getText().equals("10")){
                    Toast.makeText(PaymentActivity.this,"已是最大值",Toast.LENGTH_SHORT).show();
                }else{
                    n = n + 1;
                    payment_num.setText("" + n);
                }
            }
        });

        findViewById(R.id.payment_pay_wx).setOnClickListener(new View.OnClickListener() {//跳转微信支付
            @Override
            public void onClick(View v) {
                if (payment_phoneNum.getText().toString().length() < 11){
                    Toast.makeText(PaymentActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }

                gotoWechat();

                //设定账户ID
                Config.user = payment_phoneNum.getText().toString();
                //Toast.makeText(PaymentActivity.this,"正在跳转微信支付中",Toast.LENGTH_LONG).show();
                ProgressDialog pb = new ProgressDialog(PaymentActivity.this);
                pb.setMessage("请稍等，正在跳转微信支付");
                pb.setCanceledOnTouchOutside(false);
                pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb.show();
            }
        });

        CiistTitleView payment_two_title = (CiistTitleView) findViewById(R.id.payment_title);
        payment_two_title.setOnLiftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    //调用支付获取id
    public void gotoWechat() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {   //获取Prepay_id
                String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
                String entity = genProductArgs();   //获取订单信息
                byte[] buf = Util.httpPost(url, entity);
                String content = new String(buf);  //请求成功返回的信息
                //Log.e("orion", content);
                try {
                    xmlParseTest(content);  //解析返回的信息
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                wechatPay();
            }
        }.execute();
    }


    //获取到perpay_id之后吊起微信支付
    protected void wechatPay() {
        PayReq req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = book.getPrepay_id();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        sb.append("sign\n" + req.sign + "\n\n");
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        //Log.e("test","book.getPrepay_id()----------"+book.getPrepay_id()+"-------genNonceStr()-------"+genNonceStr()+"--------genTimeStamp()-------"+genTimeStamp()+"---genAppSign(signParams)--"+genAppSign(signParams));
        api.sendReq(req);
        // dialog.dismiss();

    }

    //获取时间搓
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    //获取产品订单信息
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();
        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();

            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID)); //APPID

            packageParams.add(new BasicNameValuePair("body", "单价：" + singlePrice + " x " + payment_num.getText().toString() + "份"));  //简单描述

            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));  //商户ID

            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));   //随机字符串

            packageParams.add(new BasicNameValuePair("notify_url","http://www.weixin.qq.com/wxpay/pay.php")); //通知地址

            packageParams.add(new BasicNameValuePair("out_trade_no",getTrade()));  //商户订单号

            packageParams.add(new BasicNameValuePair("spbill_create_ip",getLocalHostIp())); //终端IP

            //double price = Double.parseDouble(payment_num.getText().toString()) * (Integer.parseInt(singlePrice) * 100);
            double price = Double.parseDouble(singlePrice) * 100 * n;
            int priceInt = (int) price;
            packageParams.add(new BasicNameValuePair("total_fee", priceInt+""));    //微信接收int型价格

            packageParams.add(new BasicNameValuePair("trade_type", "APP"));  //支付类型

            String sign = genAppSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));  //签名

            String xmlstring = parseNodeToXML(packageParams);   //转化成xml

            return xmlstring;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    //获取订单号
    private String getTrade(){
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        return format.format(new Date(nowTime));
    }



    //获取支付签名Sign
    StringBuilder sb = new StringBuilder();
    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

    //获取随机字符串
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 解析为xml格式
     * @param treeNodes
     * @return
     */
    public String parseNodeToXML(List<NameValuePair> treeNodes) {
        StringBuffer xmlnodes = new StringBuffer();
        if (treeNodes != null && treeNodes.size() > 0) {
            xmlnodes.append("<xml>");
            for (int i = 0; i < treeNodes.size(); i++) {
                NameValuePair node = treeNodes.get(i);
                xmlnodes.append("<"+node.getName()+">").append(node.getValue()).append("</"+node.getName()+">");
            }
            xmlnodes.append("</xml>");
        }
        //return xmlnodes.toString();
        String xml = xmlnodes.toString();
        try {
            xml = new String(xml.toString().getBytes(), "ISO8859-1");  //商品详情为中文，将其转化为统一编码，不然获取perpred_id失败
            return xml;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    //获取手机IP
    public String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException e) {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;
    }


    /**
     * 解析xml
     * 返回prepay_id
     * 通过对象Books获取数据
     */
    WeixinParentId book = null;                //通过对象Books获取数据
    public void xmlParseTest(String str) throws IOException, XmlPullParserException {
        XmlPullParser pullParser = Xml.newPullParser();            //获取XmlPullParser对象
        //InputStream is = getContext().getAssets().open("parse.xml");   //解析文本
        ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
        ArrayList<WeixinParentId> books = null ;

        pullParser.setInput(is, "UTF-8");
        int type = pullParser.getEventType();    //获取事件类型
        while (type != pullParser.END_DOCUMENT) {   //结束文本</books>
            switch(type){
                case XmlPullParser.START_DOCUMENT:    //开始文本<books>
                    books = new ArrayList<WeixinParentId>();
                    break;
                case XmlPullParser.START_TAG:    //开始标记   <book>
                    if (pullParser.getName().equals("xml")) {
                        book = new WeixinParentId();
                    }else if (pullParser.getName().equals("return_msg")) {
                        type = pullParser.next();    //指向下一个位置，不然无法获取数据
                        book.setReturn_msg(pullParser.getText());
                    }else if (pullParser.getName().equals("appid")) {
                        type = pullParser.next();
                        book.setAppid(pullParser.getText());
                    }else if (pullParser.getName().equals("prepay_id")) {
                        type = pullParser.next();
                        book.setPrepay_id(pullParser.getText());
                    }

                    break;
                case XmlPullParser.END_TAG:   //结束标记      </books>
                    if (pullParser.getName().equals("book")) {
                        books.add(book);
                        book = null;    //置为空释放资源
                    }
                    break;
            }
            type = pullParser.next();    //指向下一个标记

        }
        //Log.e("test", "book------id----" + book.getPrepay_id());
    }



}
