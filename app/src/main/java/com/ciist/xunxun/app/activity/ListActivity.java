package com.ciist.xunxun.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;
import com.ciist.common.Common;
import com.ciist.entites.CiistObjects;
import com.ciist.xunxun.R;
import com.ciist.xunxun.app.adapter.CiistListAdapter;
import com.ciist.xunxun.app.widget.PullUpListView;
import com.ciist.xunxun.app.widget.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import widget.CiistTitleView;

/**
 * Created by hw on 2016/3/23.
 */
public class ListActivity extends Activity {

    private static final int PAGE_SIZE = 10;

    private PullUpListView list_lv;
    private CiistTitleView list_title;
    private String url;//key
    private CiistListAdapter adapter;
    private List<CiistObjects> mdata = new ArrayList<>();
    private List<CiistObjects> listdata = new ArrayList<>();
    private int page = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //设置适配器
            listdata.addAll(mdata);
            if (adapter == null){
                adapter = new CiistListAdapter(ListActivity.this);
                adapter.setData(listdata);
                list_lv.setAdapter(adapter);
            }else{
                if (mdata.size() <= 0) {
                    list_lv.setFooterVisibily(false);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initSystemBar(this);
        list_lv = (PullUpListView) findViewById(R.id.list_lv);
        list_title = (CiistTitleView) findViewById(R.id.list_title);
        url = getIntent().getStringExtra("LIST_URL");
        getData(url, page, PAGE_SIZE);
        list_title.setTitle(url.substring(1));//设置标题
        //刷新
        list_lv.setOnPullUpListViewCallBack(new PullUpListView.OnPullUpListViewCallBack() {
            @Override
            public void scrollBottomState() {
                page++;
                getData(url, page, PAGE_SIZE);
            }
        });
        //返回监听
        list_title.setOnLiftClickListener(new View.OnClickListener() {
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

    /**
     *
     * @param c code
     * @param page  页数
     * @param size  条数
     */
    private void getData(String c,int page,int size){
        try {
            String strUTF8 = URLEncoder.encode(c, "UTF-8");
            final String url = "http://www.ciist.com:2015/chinabmserver/xx/getdata/ciistkey/"+ strUTF8 +"/" + page +"/" + size;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String s = Common.fromNetgetData(url);
                    try {
                        List<CiistObjects> data = new ArrayList<>();
                        JSONArray array = new JSONArray(s);
                        for (int i = 0;i < array.length();i++){
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
                            data.add(o);
                        }
                        mdata.clear();
                        mdata = data;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendMessage(new Message());
                }
            }).start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
