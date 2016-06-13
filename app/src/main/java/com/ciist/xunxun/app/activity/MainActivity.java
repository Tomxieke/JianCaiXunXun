package com.ciist.xunxun.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.ciist.common.Common;
import com.ciist.entites.CiistObjects;
import com.ciist.xunxun.R;
import com.ciist.xunxun.app.adapter.CiistListAdapter;
import com.ciist.xunxun.app.adapter.PagerAdapter;
import com.ciist.xunxun.app.adapter.PopListAdapter;
import com.ciist.xunxun.app.util.HttpUtil;
import com.ciist.xunxun.app.widget.PagerSlidingTabStrip;
import com.ciist.xunxun.app.widget.PullUpListView;
import com.ciist.xunxun.app.widget.SystemBarTintManager;
import com.mob.tools.utils.UIHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import widget.CircleImageView;

/**
 * Created by hw on 2016/3/28.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
        ,Handler.Callback,PlatformActionListener {

    //常量
    private static final int PAGE_SIZE = 10;
    private static final int HANDLER_NEWS = 0x45f0000;
    private static final int HANDLER_MERCHANTS = 0x46f0000;
    private static final int HANDLER_ACTIVE = 0x47f0000;
    private static final int HANDLER_BRAND = 0x48f0000;

    private static final int MSG_USERID_FOUND = 1;  //第三方登录
    private static final int MSG_LOGIN = 2;  //登录
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR= 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private boolean isAuthoriz = false;   //是否已授权微信  默认未授权

    //通用变量
    private CiistListAdapter newsAdapter;
    private CiistListAdapter activeAdapter;
    private ProgressBar main_pb;
    private long mExitTime;//记录退出时间
    //标题
    private ImageView xunxun_title_left_icon;
    private CircleImageView xunxun_title_right_icon;
    private TextView xunxun_title_text;

    //
    private List<CiistObjects> mdata = new ArrayList<>();
    private List<CiistObjects> newsdata = new ArrayList<>();
    private List<CiistObjects> merchantsdata = new ArrayList<>();
    private List<CiistObjects> branddata = new ArrayList<>();
    private List<CiistObjects> activedata = new ArrayList<>();
    private List<CiistObjects> blockdata = new ArrayList<>();
    //页面
    private LinearLayout layout_news;
    private LinearLayout layout_merchants;
    private LinearLayout layout_active;
    private LinearLayout layout_brand;
    //new 页面控件与变量
    private TextView xunxun_news_lag;
    //private CiistTitleView news_title;
    private PullUpListView xunxun_news_lv;
    private PopListAdapter popListAdapter;
    private int newsPage = 1;//news页面刷新

    private PagerSlidingTabStrip xunxun_news_tab;
    private ViewPager xunxun_news_pager;
    private PagerAdapter newsPagerAdapter;

    //merchants 页面控件与变量
    private PagerSlidingTabStrip xunxun_merchants_tab;
    private ViewPager xunxun_merchants_pager;
    private PagerAdapter merchantsPagerAdapter;
    private int merchantsPage = 1;//merchants 页面刷新
    //private CiistTitleView merchants_title;

    //active 页面控件与变量
    private PullUpListView xunxun_active_lv;
    private int activePage = 1;//active 页面刷新
    //private CiistTitleView active_title;

    //brand 页面控件与变量
    private ViewPager xunxun_brand_pager;
    private PagerSlidingTabStrip xunxun_brand_tab;
    private PagerAdapter brandPagerAdapter;
    private int brandPage = 1;
    //private CiistTitleView brand_title;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_NEWS:
                    newsdata.addAll(mdata);
                    if (newsPagerAdapter == null) {
                        newsPagerAdapter = new PagerAdapter(getSupportFragmentManager());
                    }
                    newsPagerAdapter.setData(newsdata);
                    xunxun_news_pager.setOffscreenPageLimit(newsdata.size());
                    xunxun_news_pager.setAdapter(newsPagerAdapter);
                    xunxun_news_tab.setViewPager(xunxun_news_pager);
                    initTabsValue(xunxun_news_tab);
                    main_pb.setVisibility(View.GONE);
                    layout_news.setVisibility(View.VISIBLE);
                    break;
                case HANDLER_MERCHANTS:
                    merchantsdata.addAll(mdata);
                    if (merchantsPagerAdapter == null) {
                        merchantsPagerAdapter = new PagerAdapter(getSupportFragmentManager());
                    }
                    merchantsPagerAdapter.setData(merchantsdata);
                    xunxun_merchants_pager.setOffscreenPageLimit(merchantsdata.size());
                    xunxun_merchants_pager.setAdapter(merchantsPagerAdapter);
                    xunxun_merchants_tab.setViewPager(xunxun_merchants_pager);
                    initTabsValue(xunxun_merchants_tab);
                    main_pb.setVisibility(View.GONE);
                    layout_merchants.setVisibility(View.VISIBLE);
                    break;
                case HANDLER_ACTIVE:
                    activedata.addAll(mdata);
                    if (activeAdapter == null) {
                        activeAdapter = new CiistListAdapter(MainActivity.this);
                        activeAdapter.setData(activedata);
                        xunxun_active_lv.setAdapter(activeAdapter);
                    } else {
                        if (mdata.size() <= 0) {
                            xunxun_active_lv.setFooterVisibily(false);
                        } else {
                            activeAdapter.notifyDataSetChanged();
                        }
                    }
                    main_pb.setVisibility(View.GONE);
                    layout_active.setVisibility(View.VISIBLE);
                    break;
                case HANDLER_BRAND:
                    branddata.addAll(mdata);
                    if (brandPagerAdapter == null) {
                        brandPagerAdapter = new PagerAdapter(getSupportFragmentManager());
                    }
                    brandPagerAdapter.setData(branddata);
                    xunxun_brand_pager.setOffscreenPageLimit(branddata.size());
                    xunxun_brand_pager.setAdapter(brandPagerAdapter);
                    xunxun_brand_tab.setViewPager(xunxun_brand_pager);
                    initTabsValue(xunxun_brand_tab);
                    main_pb.setVisibility(View.GONE);
                    layout_brand.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //UmengUpdateAgent.update(this);
        updateAppService();
        setContentView(R.layout.activity_main);

        ShareSDK.initSDK(this);

        initView();
        authorized(new Wechat(this));
        getData(HANDLER_NEWS, "@商界栏目分类", newsPage, PAGE_SIZE);
        refListView();
        initSystemBar(this);
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

    /**
     * 百度自动更新
     */
    private void updateAppService() {
        try {
            BDAutoUpdateSDK.uiUpdateAction(this, new UICheckUpdateCallback() {
                @Override
                public void onCheckComplete() {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("检测", "检测更新失败");
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //总用控件初始化
        findViewById(R.id.xunxun_tab_newsBtn).setOnClickListener(this);//咨询首页
        findViewById(R.id.xunxun_tab_merchantsBtn).setOnClickListener(this);//招商
        findViewById(R.id.xunxun_tab_activeBtn).setOnClickListener(this);//活动
        findViewById(R.id.xunxun_tab_brandBtn).setOnClickListener(this);//品牌

        layout_news = (LinearLayout) findViewById(R.id.layout_news);
        layout_merchants = (LinearLayout) findViewById(R.id.layout_merchants);
        layout_active = (LinearLayout) findViewById(R.id.layout_active);
        layout_brand = (LinearLayout) findViewById(R.id.layout_brand);
        main_pb = (ProgressBar) findViewById(R.id.main_pb);
        xunxun_title_left_icon = (ImageView) findViewById(R.id.xunxun_title_left_icon);
        xunxun_title_right_icon = (CircleImageView) findViewById(R.id.xunxun_title_right_icon);
        xunxun_title_text = (TextView) findViewById(R.id.xunxun_title_text);

        //news 控件初始化
        xunxun_news_tab = (PagerSlidingTabStrip) findViewById(R.id.xunxun_news_tab);
        xunxun_news_pager = (ViewPager) findViewById(R.id.xunxun_news_pager);
        //xunxun_news_lv = (PullUpListView) findViewById(R.id.xunxun_news_lv);
        //xunxun_news_lag = (TextView) findViewById(R.id.xunxun_news_lag);

        //merchants 控件初始化
        //merchants_title = (CiistTitleView) findViewById(R.id.merchants_title);
        xunxun_merchants_tab = (PagerSlidingTabStrip) findViewById(R.id.xunxun_merchants_tab);
        xunxun_merchants_pager = (ViewPager) findViewById(R.id.xunxun_merchants_pager);

        //active 控件初始化
        xunxun_active_lv = (PullUpListView) findViewById(R.id.xunxun_active_lv);
        //active_title = (CiistTitleView) findViewById(R.id.active_title);

        //brand 控件初始化
        //brand_title = (CiistTitleView) findViewById(R.id.brand_title);
        xunxun_brand_tab = (PagerSlidingTabStrip) findViewById(R.id.xunxun_brand_tab);
        xunxun_brand_pager = (ViewPager) findViewById(R.id.xunxun_brand_pager);

        //二维码界面跳转
        xunxun_title_left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ErweimaActivity.class));
            }
        });

        //搜索界面跳转
        xunxun_title_right_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Intent intent = new Intent(MainActivity.this, SearchActivity.class);//跳转搜索

                //Intent intent = new Intent(MainActivity.this, LandActivity.class);//跳转登陆
                //Intent intent = new Intent(MainActivity.this, PaymentActivity.class);//跳转支付
                //Intent intent = new Intent(MainActivity.this, PuzzGameActivity.class);//跳转puzz游戏界面

                /*if (xunxun_title_text.getText().equals("建材迅讯")) {

                    intent.putExtra("SEARCH_CLASS", "@商界频道");

                } else if (xunxun_title_text.getText().equals("招商")) {

                    intent.putExtra("SEARCH_CLASS", "@招商频道");

                } else if (xunxun_title_text.getText().equals("活动")) {

                } else if (xunxun_title_text.getText().equals("品牌")) {

                    intent.putExtra("SEARCH_CLASS", "@品牌频道");

                }

                startActivity(intent);*/


                if (isAuthoriz){
//                    Intent intent = new Intent(MainActivity.this, PuzzGameActivity.class);//跳转puzz游戏界面
//                    intent.putExtra("SEARCH_CLASS", "@商界频道");
//                    startActivity(intent);
                    popShotSrceenDialog();
                 //   openDialog();
                }else {
                    Toast.makeText(MainActivity.this,"吊起微信登录",Toast.LENGTH_SHORT).show();
                    authorize(new Wechat(MainActivity.this));  //授权微信登录
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xunxun_tab_newsBtn://跳转咨询首页
                xunxun_title_text.setText("建材迅讯");
                xunxun_title_left_icon.setVisibility(View.VISIBLE);
                if (newsPagerAdapter == null) {
                    main_pb.setVisibility(View.VISIBLE);
                    layout_news.setVisibility(View.GONE);
                    layout_merchants.setVisibility(View.GONE);
                    layout_active.setVisibility(View.GONE);
                    layout_brand.setVisibility(View.GONE);
                    getData(HANDLER_MERCHANTS, "@商界栏目分类", merchantsPage, PAGE_SIZE);
                } else {
                    layout_news.setVisibility(View.VISIBLE);
                    layout_merchants.setVisibility(View.GONE);
                    layout_active.setVisibility(View.GONE);
                    layout_brand.setVisibility(View.GONE);
                }
                break;
            case R.id.xunxun_tab_merchantsBtn://跳转招商页面
                xunxun_title_text.setText("招商");
                xunxun_title_left_icon.setVisibility(View.GONE);
                if (merchantsPagerAdapter == null) {
                    main_pb.setVisibility(View.VISIBLE);
                    layout_news.setVisibility(View.GONE);
                    layout_merchants.setVisibility(View.GONE);
                    layout_active.setVisibility(View.GONE);
                    layout_brand.setVisibility(View.GONE);
                    getData(HANDLER_MERCHANTS, "@招商行业分类", merchantsPage, PAGE_SIZE);
                } else {
                    layout_news.setVisibility(View.GONE);
                    layout_merchants.setVisibility(View.VISIBLE);
                    layout_active.setVisibility(View.GONE);
                    layout_brand.setVisibility(View.GONE);
                }
                break;
            case R.id.xunxun_tab_activeBtn://跳转活动页面
                xunxun_title_text.setText("活动");
                xunxun_title_left_icon.setVisibility(View.GONE);
                if (activeAdapter == null) {
                    main_pb.setVisibility(View.VISIBLE);
                    layout_news.setVisibility(View.GONE);
                    layout_merchants.setVisibility(View.GONE);
                    layout_active.setVisibility(View.GONE);
                    layout_brand.setVisibility(View.GONE);
                    getData(HANDLER_ACTIVE, "@活动频道", activePage, PAGE_SIZE);
                } else {
                    layout_news.setVisibility(View.GONE);
                    layout_merchants.setVisibility(View.GONE);
                    layout_active.setVisibility(View.VISIBLE);
                    layout_brand.setVisibility(View.GONE);
                }
                break;
            case R.id.xunxun_tab_brandBtn://跳转品牌页面
                xunxun_title_text.setText("品牌");
                xunxun_title_left_icon.setVisibility(View.GONE);
                if (brandPagerAdapter == null) {
                    main_pb.setVisibility(View.VISIBLE);
                    layout_news.setVisibility(View.GONE);
                    layout_merchants.setVisibility(View.GONE);
                    layout_active.setVisibility(View.GONE);
                    layout_brand.setVisibility(View.GONE);
                    getData(HANDLER_BRAND, "@品牌行业分类", brandPage, PAGE_SIZE);
                } else {
                    layout_news.setVisibility(View.GONE);
                    layout_merchants.setVisibility(View.GONE);
                    layout_active.setVisibility(View.GONE);
                    layout_brand.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * ListView刷新
     */
    private void refListView() {
//        xunxun_news_lv.setOnPullUpListViewCallBack(new PullUpListView.OnPullUpListViewCallBack() {
//            @Override
//            public void scrollBottomState() {
//                newsPage++;
//                getData(HANDLER_NEWS, "@商界频道", newsPage, PAGE_SIZE);
//            }
//        });
        xunxun_active_lv.setOnPullUpListViewCallBack(new PullUpListView.OnPullUpListViewCallBack() {
            @Override
            public void scrollBottomState() {
                activePage++;
                getData(HANDLER_ACTIVE, "@活动频道", activePage, PAGE_SIZE);
            }
        });
    }

    /**
     * PagerSlidingTabStrip默认值配置
     */
    private void initTabsValue(PagerSlidingTabStrip strip) {
        try {
            strip.setIndicatorColor(Color.RED);// 底部游标颜色
            strip.setDividerColor(Color.TRANSPARENT);// tab的分割线颜色
            strip.setBackgroundColor(Color.parseColor("#FFFFFF")); // tab背景
            strip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics())); // tab底线高度
            strip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));// 游标高度
            strip.setSelectedTextColor(Color.RED);// 选中的文字颜色
            strip.setTextColor(Color.BLACK);// 正常文字颜色
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 往期更多弹出dialog形式弹出
     */
    private void openDialog() {
        View menuView = View.inflate(this, R.layout.layout_item_popview, null);
        // 创建AlertDialog
        final AlertDialog menuDialog = new AlertDialog.Builder(this).create();
        menuDialog.setView(menuView);
        menuView.findViewById(R.id.popview_back_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialog.dismiss();
            }
        });
        ListView listView = (ListView) menuView.findViewById(R.id.popview_list);
        popListAdapter = new PopListAdapter(this);
        popListAdapter.setData(blockdata);
        listView.setAdapter(popListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newsPage = 1;
                xunxun_news_lag.setText(blockdata.get(position).getTitle());
                getData(HANDLER_NEWS, blockdata.get(position).getLinkCode(), newsPage, PAGE_SIZE);
                menuDialog.dismiss();
            }
        });
        Window window = menuDialog.getWindow();
        //WindowManager.LayoutParams params = window.getAttributes();
        //window.getDecorView().setPadding(0, 0, 0, 0);
        //params.alpha = 0.6f;  //设置透明
        //params.y = 40;
        //window.setAttributes(params);
        //必须有下面这个设置，不然边缘会有个边框，很丑。
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.9); // 高度设置为屏幕的0.6
        p.gravity = Gravity.BOTTOM;//设置弹出框位置
        window.setAttributes(p);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        menuDialog.show();
    }



    /**
     * 游戏切图dialog分享
     */
    private void popShotSrceenDialog(){
        final AlertDialog cutDialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(this, R.layout.show_cut_screen_layout, null);
        ImageView showImg = (ImageView) dialogView.findViewById(R.id.show_cut_screen_img);
        dialogView.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cutDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.share_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击了share按钮",Toast.LENGTH_SHORT).show();
            }
        });
        //获取当前屏幕的大小
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
        //找到当前页面的跟布局
        View view = getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();
        showImg.setImageBitmap(temBitmap);

        cutDialog.setView(dialogView);
        Window window = cutDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.6
        p.gravity = Gravity.CENTER;//设置弹出框位置
        window.setAttributes(p);
        window.setWindowAnimations(R.style.cut_screen_popdialog_anim);
        cutDialog.show();
    }

    /**
     * 点击两次返回键退出程序
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * @param c    code
     * @param page 页数
     * @param size 条数
     */
    private void getData(final int tag, final String c, final int page, int size) {
        if (Common.getNetState(this) && HttpUtil.isCacheDataFailure(MainActivity.this,c+page)) {
            try {
                final String strUTF8 = URLEncoder.encode(c, "UTF-8");
                final String url = "http://www.ciist.com:2015/chinabmserver/xx/getdata/ciistkey/" + strUTF8 + "/" + page + "/" + size;
            //    Log.e("test","   url  :" + url);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = Common.fromNetgetData(url);
                        if (page == 1){   //如果是第一页就缓存起来
                            HttpUtil.saveObject(MainActivity.this,s,c+page);
                        }
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
                                o.setShareLink(object.getString("ShareLink"));
                                o.setTel(object.getString("Tel"));
                                Log.e("test","   shareLink:"+object.getString("ShareLink")+ "    telNum:"+object.getString("Tel"));
                                data.add(o);
                            }
                            mdata.clear();
                            mdata = data;
                            mHandler.sendEmptyMessage(tag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {   //如果没有网络，取缓存中的数据并提示打开网络
            if (!Common.getNetState(this)) {
                Toast.makeText(this, "请打开网络", Toast.LENGTH_LONG).show();
            }
            main_pb.setVisibility(View.GONE);
            String s = HttpUtil.readObject(MainActivity.this,c+page);
            if (s != null) {
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
                        o.setShareLink(object.getString("ShareLink"));
                        o.setTel(object.getString("Tel"));
                        Log.e("test", "   shareLink:" + object.getString("ShareLink") + "    telNum:" + object.getString("Tel"));
                        data.add(o);
                    }
                    mdata.clear();
                    mdata = data;
                    mHandler.sendEmptyMessage(tag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this, "请打开网络", Toast.LENGTH_LONG).show();
            }
        }
    }


    //-------------------------第三方授权登录------------

    /**
     * 判断是否已经授权，当已授权的情况下直接拉取用户信息显示
     */
    private void authorized(Platform plat){
        if(plat.isValid()) {  //判断是否已经授权
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
            //    UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getDb().getUserName(), plat.getDb().getUserIcon(), null);
                isAuthoriz = true;
            }
        }
    }

    /**
     * 登录授权
     * @param plat
     */
    private void authorize(Platform plat) {

        /*if(plat.isValid()) {  //判断是否已经授权
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getDb().getUserName(), plat.getDb().getUserIcon(), null);
                return;
            }
        }*/

        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
        isAuthoriz = true;
    }

    /**
     * 登录
     * @param userName
     * @param userIcon
     * @param userInfo
     */
    private void login(String usreName, String userIcon, HashMap<String, Object> userInfo) {
        /*Log.w("weixin","login");
        Message msg = new Message();
        msg.what = MSG_AUTH_COMPLETE;
        Bundle bundle = new Bundle();
        bundle.putString("userName",usreName);
        bundle.putString("userIcon",userIcon);
        msg.setData(bundle);
        UIHandler.sendMessage(msg, this);*/
        Picasso.with(this).load(userIcon).into(xunxun_title_right_icon);
    }

    //第三方授权登录Handle回调处理
    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_USERID_FOUND:

                break;
            case MSG_LOGIN:
                Log.w("weixin","MSG_LOGIN");
                Bundle bundle = msg.getData();
                String accunt = bundle.getString("userid");
                String name = bundle.getString("platname");
                //        getThirdAsyn(accunt,name);
                break;
            case MSG_AUTH_CANCEL:

                break;
            case MSG_AUTH_ERROR:

                break;
            case MSG_AUTH_COMPLETE:
                Bundle bundle1 = msg.getData();
                String userIcon = bundle1.getString("userIcon");
                Picasso.with(this).load(userIcon).into(xunxun_title_right_icon);
                Log.e("onComplete","------User Name ---------"+bundle1.getString("userName"));
                Log.e("onComplete","------User icon ---------"+bundle1.getString("userIcon"));
                break;
        }
        return false;

    }

    @Override  //成功登录处理
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.w("weixin","onComplete---"+i);
        if (i == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            Bundle bundle = new Bundle();
            bundle.putString("userName",platform.getDb().getUserName());
            bundle.putString("userIcon",platform.getDb().getUserIcon());
            msg.setData(bundle);
            UIHandler.sendMessage(msg, this);
        //    login(platform.getDb().getUserName(), platform.getDb().getUserIcon(), hashMap);
        }
        //所有用户信息从hasMap中取得。
        Log.e("onComplete","hashMap--------" + hashMap);
        Log.e("onComplete","------User Name ---------"+platform.getDb().getUserName());
        Log.e("onComplete","------User ID ---------"+platform.getDb().getUserId());
        Log.e("onComplete","------User Icon------"+platform.getDb().getUserIcon());
        Log.e("onComplete","------platform.getName() ---------"+platform.getName());

    }

    @Override  //错误处理
    public void onError(Platform platform, int i, Throwable throwable) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();

    }

    @Override  //取消
    public void onCancel(Platform platform, int i) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

}
