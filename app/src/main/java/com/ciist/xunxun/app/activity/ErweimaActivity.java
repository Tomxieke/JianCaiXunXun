package com.ciist.xunxun.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ciist.xunxun.R;
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

public class ErweimaActivity extends AppCompatActivity {

    private CiistTitleView titleView;
    private WebViewWithProgress webwp;
    private CiistWebview mWebView;

//    private String url;
//    private String title;
//    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erweima);
        ShareSDK.initSDK(this);
        initSystemBar(this);
        titleView=(CiistTitleView)findViewById(R.id.er_fenxiang);
        webwp=(WebViewWithProgress)findViewById(R.id.er_webview);
        mWebView = (CiistWebview) webwp.getWebView();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);  //能够访问内部数据

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
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

//		//设置webview为单列显示，是一些大图片适应屏幕宽度
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

//        img = getIntent().getStringExtra("IMGPATH");
//        url = getIntent().getStringExtra("WEB_URL");
//        title = getIntent().getStringExtra("CONTENT");
//        if (url != null) {
//        }
        mWebView.loadUrl("http://www.ciist.com:2016/download/");

        titleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        titleView.setOnLiftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    //------------------------------------------------------------------------------------------------------------------

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
     * 分享方法
     */
    private void showShare() {
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
        oks.setTitle("建材迅讯" );
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.ciist.com:2016/download/");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("资讯快一步" + "\n" + "内容深一度"+"\n"+"建材行业人士专业读物");  //所有平台就用这个字段就可以搞定
        oks.setImageUrl("http://211.149.212.154:2016/Content/File/d9f4b922-fd95-438f-9a1d-c7b2fd893c70.png");
        //此方法是用来分享网络图片的，ImagePath是用来分享本地图片的，必须要在sdcard下的图片路径才可测试分享。

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/storage/emulated/test.jpg");//确保SDcard下面存在此张图片/

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.ciist.com:2016/download/");

        //针对微信朋友圈只有setTitle文本有用，setText（）没用的回调
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {

                if ("WechatMoments".equals(platform.getName())) {

                    paramsToShare.setTitle("做有深度的资讯" + "\n" + "献给爱阅读的你");
                }
            }
        });

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // oks.setComment("建材迅讯");

        // site是分享此内容的网站名称，仅在QQ空间使用
        // oks.setSite(getString(R.string.app_name));

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        // oks.setSiteUrl("http://www.ciist.com:2016/download/");

        // 启动分享GUI
        oks.show(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }
}
