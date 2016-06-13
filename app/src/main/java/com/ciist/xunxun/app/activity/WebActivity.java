package com.ciist.xunxun.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.game.PuzzGameActivity;
import com.ciist.xunxun.app.widget.SystemBarTintManager;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import widget.CiistTitleView;
import widget.CiistWebview;
import widget.WebViewWithProgress;

public class WebActivity extends AppCompatActivity {

    private CiistTitleView titleView;
    private WebViewWithProgress webwp;
    private CiistWebview mWebView;
    private ImageView callImgbtn;  // 电话拨号键
    private TextView er_canyu;//支付按键
    private String url;
    private String title;
    private String img;
    private String shareUrl;  //分享链接
    private String telNum;  //电话号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_erweima);
        ShareSDK.initSDK(this);

        initSystemBar(this);

        titleView=(CiistTitleView)findViewById(R.id.er_fenxiang);
        callImgbtn = (ImageView) findViewById(R.id.call_imgbtn);
        er_canyu = (TextView) findViewById(R.id.er_canyu);
        webwp=(WebViewWithProgress) findViewById(R.id.er_webview);
        mWebView = (CiistWebview) webwp.getWebView();
        WebSettings ws = mWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);//能够访问内部数据
        ws.setAppCacheEnabled(true);  //设置缓存
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式</span>
        //设置webview为单列显示，是一些大图片适应屏幕宽度
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);



        Intent intent = getIntent();
        img = intent.getStringExtra("IMGPATH");
        url = intent.getStringExtra("WEB_URL");
        title = intent.getStringExtra("CONTENT");
        shareUrl = intent.getStringExtra("SHARELINK");
        telNum = intent.getStringExtra("Tel");



        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String murl) {
                view.loadUrl(murl);
                //调用拨号程序
                if (murl.startsWith("mailto:") || murl.startsWith("geo:") || murl.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(murl));
                    startActivity(intent);
                    view.loadUrl(url);
                }
                return true;
            }

            //没网络或者服务器出错是处理
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mWebView.getSettings().setDefaultTextEncodingName("UTF-8"); //防止中文乱码
                String errHtml = "<html><body><h2>网络错误</h2><h5>请检查网络是否链接或者刷新重新加载<h5></body><html>";
            //    mWebView.loadData(errHtml, "text/html", "UTF-8");
                mWebView.loadDataWithBaseURL(null,errHtml,"text/html","UTF-8",null);
                //第一种加载方式只能解决部分手机乱码问题，第二种加载方式可适配更多手机

            }

        });

        //设置webview为单列显示，是一些大图片适应屏幕宽度
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.loadUrl(url.replace("{ciistuid}", getDeviceId()));

        titleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareUrl != null && !shareUrl.equals("") && shareUrl.startsWith("http://")){
                    showShare(shareUrl.replace("{ciistuid}", getDeviceId()));
                }else {
                    showShare(url.replace("{ciistuid}", getDeviceId()));
                }
            }
        });

        titleView.setOnLiftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showCallBtn();  //是否显示拨号按钮
        showPaymentBtn();//是否显示支付按钮

        callImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+telNum));
                startActivity(intent);
            }
        });
        initSystemBar(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mWebView.reload();
    }

    //是否在底端显示拨号键
    private void showCallBtn(){
        if (telNum != null){
            telNum = telNum.replace(" ","");
        }else{
            return;
        }
        if (null != telNum && telNum.length() > 5 ) {   //长度大于5就可以吊起拨号
            callImgbtn.setVisibility(View.VISIBLE);
//            mWebView.setOnCustomScroolChangeListener(new CiistWebview.ScrollInterface() {  //滑动到最底部时显示拨号键
//                @Override
//                public void onSChanged(int l, int t, int oldl, int oldt) {
//                    //WebView的总高度
//                    float webViewContentHeight = mWebView.getContentHeight() * mWebView.getScale();
//                    //WebView的现高度
//                    float webViewCurrentHeight = (mWebView.getHeight() + mWebView.getScrollY());
//                    //Log.e("test", "contentHeighe--:" + webViewContentHeight + "    currentHeight:" + webViewCurrentHeight);
//                    if ((webViewContentHeight - webViewCurrentHeight) < 300 || webViewContentHeight == webViewCurrentHeight) {
//                    //Toast.makeText(WebActivity.this, "webview滑动到低端了", Toast.LENGTH_SHORT).show();
//                    // callImgbtn.setVisibility(View.VISIBLE);
//                    } else {
//                    //callImgbtn.setVisibility(View.GONE);
//                    }
//                }
//            });
        }
    }


    //是否在底端显示拨号键
    private void showPaymentBtn(){
        if (null != url && url.contains("Y")){
            er_canyu.setVisibility(View.VISIBLE);
        }
        er_canyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WebActivity.this,PuzzGameActivity.class));
            }
        });
    }

    /**
     * 获取手机唯一识别码
     * @return  手机唯一识别码。
     */
    private String getDeviceId(){
        //获取Device_id
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        return DEVICE_ID;
    }

    //------------------------------------------------------------------------------------------------------------

    /**
     * 改变状态栏颜色
     * @param activity
     */
    public static void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.title_color);// 使用颜色资源
    }
    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //------------------------------------------------------------------------------------------------------------


    /**
     * 确保网页操作后的连接一层一层退出
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 转换反斜杠
     * @param path
     * @return
     */
    private String formatImgPath(String path){
        return "http://www.ciist.com:2016" + path.replace("\\","/");
    }

    /**
     * 分享方法
     */

    private void showShare(String fianlurl) {
        ShareSDK.initSDK(this);
        //---------------------------------------------------------------
        HashMap<String, Object> wechat = new HashMap<String, Object>();
        wechat.put("Id", "4");
        wechat.put("SortId", "4");
        wechat.put("AppId", "wx211c06186ca98b90");
        wechat.put("AppSecret", "62214ed91afe72e5f0467a10d793e6c6");
        wechat.put("BypassApproval", "false");
        wechat.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, wechat);
        // 代码配置第三方平台
        HashMap<String, Object> WechatMoment = new HashMap<String, Object>();
        WechatMoment.put("Id", "5");
        WechatMoment.put("SortId", "5");
        WechatMoment.put("AppId", "wx211c06186ca98b90");
        WechatMoment.put("AppSecret", "62214ed91afe72e5f0467a10d793e6c6");
        WechatMoment.put("BypassApproval", "false");
        WechatMoment.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, WechatMoment);
        //----------------------------------------------------------------
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("建材迅讯");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用

        oks.setTitleUrl(fianlurl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);  //所有平台就用这个字段就可以搞定

        if (img.contains("http")){
            oks.setImageUrl(img);
        }else{
            oks.setImageUrl(formatImgPath(img));
        }
        //此方法是用来分享网络图片的，ImagePath是用来分享本地图片的，必须要在sdcard下的图片路径才可测试分享。
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/storage/emulated/test.jpg");//确保SDcard下面存在此张图片/
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(fianlurl);
        //针对微信朋友圈只有setTitle文本有用，setText（）没用的回调
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setTitle(title);
                }
            }
        });
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // oks.setComment("建材迅讯");
        // site是分享此内容的网站名称，仅在QQ空间使用
        // oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        // oks.setSiteUrl(url);
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }



}
