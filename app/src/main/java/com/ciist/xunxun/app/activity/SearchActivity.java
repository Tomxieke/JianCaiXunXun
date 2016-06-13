package com.ciist.xunxun.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ciist.common.Common;
import com.ciist.entites.CiistObjects;
import com.ciist.xunxun.R;
import com.ciist.xunxun.app.adapter.CiistListAdapter;
import com.ciist.xunxun.app.widget.CoustomSearchEditText;
import com.ciist.xunxun.app.widget.PullUpListView;
import com.ciist.xunxun.app.widget.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hw on 2016/4/25.
 */
public class SearchActivity extends Activity {
    private static final int PAGESIZE = 10;

    private List<CiistObjects> mdata = new ArrayList<>();
    private List<CiistObjects> listdata = new ArrayList<>();
    private ImageView search_back;
    private CoustomSearchEditText search_title_et;
    private PullUpListView search_lv;
    private ProgressBar search_pb;
    private String searchClass;
    private int pageIndex = 1;
    private CiistListAdapter adapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listdata.addAll(mdata);
            if (adapter == null){
                adapter = new CiistListAdapter(SearchActivity.this);
                adapter.setData(listdata);
                search_lv.setAdapter(adapter);
            }else{
                if (mdata.size() <= 0) {
                    search_lv.setFooterVisibily(false);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (mdata.size() <= 0){
                Toast.makeText(SearchActivity.this,"没有搜索到此类信息",Toast.LENGTH_SHORT).show();
            }
            search_pb.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search_title_et.getWindowToken(), 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initSystemBar(this);
        initView();
        searchClass = getIntent().getStringExtra("SEARCH_CLASS");
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
     *初始化控件
     */
    private void initView(){
        search_back = (ImageView) findViewById(R.id.search_back);
        search_title_et = (CoustomSearchEditText) findViewById(R.id.search_title_et);
        search_lv = (PullUpListView) findViewById(R.id.search_lv);
        search_pb = (ProgressBar) findViewById(R.id.search_pb);
        allListener();
    }

    /**
     * 所有控件的监听
     */
    private void allListener() {
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search_title_et.setOnRightClickListener(new CoustomSearchEditText.OnRightClickListener() {
            @Override
            public void onClick(String text) {
                listdata.clear();
                search_pb.setVisibility(View.VISIBLE);
                findData(searchClass, text, pageIndex, PAGESIZE);
            }
        });
    }

    /**
     * 搜索数据
     * @param KeyWord
     * @param findstr
     * @param PageIndex
     * @param PageSize
     */
    public void findData(String KeyWord, String findstr, int PageIndex, int PageSize){
        if (Common.getNetState(this)) {
            try {
                String keyWord = URLEncoder.encode(KeyWord, "UTF-8");
                String findStr = URLEncoder.encode(findstr, "UTF-8");
                final String url = "http://www.ciist.com:2015/chinabmserver/xx/finddata/ciistkey/"+ keyWord +"/" + findStr + "/" + PageIndex + "/" + PageSize;
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
                            mHandler.sendMessage(new Message());
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
            search_pb.setVisibility(View.GONE);
        }
    }

}
