package com.ciist.xunxun.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciist.common.Common;
import com.ciist.entites.CiistObjects;
import com.ciist.xunxun.R;
import com.ciist.xunxun.app.util.AsyncImageLoader;
import com.ciist.xunxun.app.widget.SystemBarTintManager;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HelloActivity extends AppCompatActivity {
    private boolean ISLOADED = false;
    private Timer tmpTimer = new Timer(true);
    private String AdImageUrl = "";
    private String CurrentFloderName = "ad";
    private TextView ktv;
    private int adcount = 4;
    private List<CiistObjects> mdata = new ArrayList<>();
    private ImageView kiv;
    private String imgPath;//图片地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        String appkey = "Aqc1105267809";
        StatConfig.setDebugEnable(true);
        // 初始化并启动MTA
        // 第三方SDK必须按以下代码初始化MTA，其中appkey为规定的格式或MTA分配的代码。
        // 其它普通的app可自行选择是否调用
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(this, appkey,
                    com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败
            Log.e("MTA", "MTA start failed.");

        }


        SharedPreferences mySharedPreferences = getSharedPreferences("CIIST", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("SENDMSG", false);
        editor.commit();
        kiv = (ImageView) findViewById(R.id.adImgView);
        ktv = (TextView) findViewById(R.id.copyrightTxtView);
        getData("建材迅讯引导页",1,1);
        CheckPrepareSystem();
        initSystemBar(this);
        LoadLastAd();
        final Handler timerhandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        ktv.setText(String.valueOf(adcount));
                        break;
                }
            }
        };
        final TimerTask tmpTask = new TimerTask() {
            @Override
            public void run() {
                adcount--;
                Message message = new Message();
                message.what = 1;
                timerhandler.sendMessage(message);
                if (adcount <= 0) {
                    tmpTimer.cancel();
                    ISLOADED = true;
                    LoadMain();
                }
            }
        };
        tmpTimer.schedule(tmpTask, 1000, 1000);
    }

    private void LoadLastAd() {
        try {
            if (AdImageUrl == "") return;
            AsyncImageLoader asyImg = new AsyncImageLoader(CurrentFloderName);
            asyImg.LoadImage(AdImageUrl, (ImageView) findViewById(R.id.adImgView));
        } catch (Exception e) {
        }
    }

    private void LoadMain() {
        if (ISLOADED) {
            Intent tmpIntent = new Intent(HelloActivity.this, MainActivity.class);
            startActivity(tmpIntent);
            HelloActivity.this.finish();
        }
    }

    private void CheckPrepareSystem() {
        String pathRoot = Environment.getExternalStorageDirectory()
                .getPath() + "/ciist";
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            File maiduo = new File(pathRoot);
            if (!maiduo.exists()) {
                maiduo.mkdir();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mdata.size() > 0){
                imgPath = mdata.get(0).getImagePath();
                if (imgPath.length() > 5){
                    imgPath = formatImgPath(imgPath);
                    AsyncImageLoader imageLoader = new AsyncImageLoader("meilimalongerweima");
                    imageLoader.LoadImage(imgPath, kiv);
                }
            }
        }
    };


    /**
     * 转换反斜杠
     *
     * @param path
     * @return
     */
    private String formatImgPath(String path) {
        return "http://www.ciist.com:2016" + path.replace("\\", "/");
    }


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
        tintManager.setStatusBarTintResource(R.color.translation);// 使用颜色资源
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


    /**
     * @param c    code
     * @param page 页数
     * @param size 条数
     */
    private void getData(String c, int page, int size) {
        if (Common.getNetState(this)) {
            try {
                String strUTF8 = URLEncoder.encode(c, "UTF-8");
                final String url = "http://www.ciist.com:2015/chinabmserver/xx/getdata/ciistkey/" + strUTF8 + "/" + page + "/" + size;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = Common.fromNetgetData(url);
                        try {
                            List<CiistObjects> data = new ArrayList<>();
                            JSONArray array = new JSONArray(s);
                            for (int i = 0; i < array.length(); i++) {
                                CiistObjects o = new CiistObjects();
                                JSONObject object = array.getJSONObject(i);
                                o.setAbs(object.getString("Abs"));
                                o.setAuthor(object.getString("Author"));
                                o.setAuthorFlag(object.getString("AuthorFlag"));
                                o.setBannerbuttonFlag(object.getString("BannerbuttonFlag"));
                                o.setBannerbuttonUrl(object.getString("BannerbuttonUrl"));
                                o.setBannerbuttontitle(object.getString("Bannerbuttontitle"));
                                o.setBannertitle(object.getString("Bannertitle"));
                                o.setBannertitleColor(object.getString("BannertitleColor"));
                                o.setByuids(object.getString("Byuids"));
                                o.setContents(object.getString("Contents"));
                                o.setGuids(object.getString("Guids"));
                                o.setImageFlag(object.getString("ImageFlag"));
                                o.setImageHeight(object.getString("ImageHeight"));
                                o.setImageLabels(object.getString("ImageLabels"));
                                o.setImageLabelsFlag(object.getString("ImageLabelsFlag"));
                                o.setImagePath(object.getString("ImagePath"));
                                o.setImageSearch(object.getString("ImageSearch"));
                                o.setIsort(object.getString("Isort"));
                                o.setKeywords(object.getString("Keywords"));
                                o.setLinkCode(object.getString("LinkCode"));
                                o.setLinkModel(object.getString("LinkModel"));
                                o.setLongitue(object.getString("Longitue"));
                                o.setModeType(object.getString("ModeType"));
                                o.setPubdataFlag(object.getString("PubdataFlag"));
                                o.setSelfids(object.getString("Selfids"));
                                o.setShortTitle(object.getString("ShortTitle"));
                                o.setShortTitleFlag(object.getString("ShortTitleFlag"));
                                o.setSourcefrom(object.getString("Sourcefrom"));
                                o.setSourcefromFlag(object.getString("SourcefromFlag"));
                                o.setSubTitleFlag(object.getString("SubTitleFlag"));
                                o.setSubtitle(object.getString("Subtitle"));
                                o.setTatidue(object.getString("Tatidue"));
                                o.setTimestamp(object.getString("Timestamp"));
                                o.setTitle(object.getString("Title"));
                                o.setTitleFlag(object.getString("TitleFlag"));
                                o.setVisible(object.getString("Visible"));
                                o.setVisitCount(object.getString("VisitCount"));
                                o.setVisitCountFlag(object.getString("VisitCountFlag"));
                                data.add(o);
                            }
                            mdata.clear();
                            mdata = data;
                            handler.sendMessage(new Message());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "请打开网络", Toast.LENGTH_LONG).show();
        }


    }

}
