package com.ciist.xunxun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.entity.DetailsPicsBean;
import com.ciist.xunxun.app.fragment.PicDetailesFragment;
import java.util.ArrayList;
import java.util.List;

public class PicDetailsActivity extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener,PicDetailesFragment.ImgClickCallBack{

    private ViewPager mPager;
    private TextView mTitleTxt,mCountTxt,mContent; //标题.图片总是，内容介绍
    private RelativeLayout mTopLayout,mMainLayout;
    private LinearLayout mTxtLayout;  //文字布局
    private PicDetailsPagerAdapter adapter;
    private String abs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_details);
        Intent intent = getIntent();
        abs = intent.getStringExtra("PICDATAIELS_ABS");
        initView();
    }

    //初始化
    private void initView(){
        findViewById(R.id.navigation_back_imgbtn).setOnClickListener(this);
        findViewById(R.id.navigation_more_imgbtn).setOnClickListener(this);

        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mTitleTxt = (TextView) findViewById(R.id.details_title_txt);
        mCountTxt = (TextView) findViewById(R.id.detailes_count_txt);
        mContent = (TextView) findViewById(R.id.details_content_txt);
        mTxtLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mTopLayout = (RelativeLayout) findViewById(R.id.pic_top_layout);
        mTxtLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mPager = (ViewPager) findViewById(R.id.details_viewpager);

        mPager.setOnPageChangeListener(this);

        adapter = new PicDetailsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        adapter.setData(getData());
        setText(adapter.getBean(0));
        mCountTxt.setText(1 + "/" + adapter.getCount());
    }


    private ArrayList<DetailsPicsBean> getData(){
        ArrayList<DetailsPicsBean> list = new ArrayList<>();
        String[] s = abs.split(";");
        for (int i = 0; i < s.length; i++){
            String[] as = s[i].split("-");
            DetailsPicsBean bean = new DetailsPicsBean();
            bean.setPicUrl(as[0]);//图片地址
            bean.setTitle(as[1]);//标题
            bean.setContent(as[2]);//副标题
            list.add(bean);
        }
        return list;
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.navigation_back_imgbtn){
            finish();
        }else if(v.getId() == R.id.navigation_more_imgbtn){
            Toast.makeText(this,"分享操作",Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.main_layout){
            Toast.makeText(PicDetailsActivity.this,"广播收到",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        DetailsPicsBean bean = adapter.getBean(position);
        setText(bean);
        mCountTxt.setText((position+1)+"/"+adapter.getCount());

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 给控件设置文本
     * @param bean
     */
    private void setText(DetailsPicsBean bean){
        mTitleTxt.setText(bean.getTitle());
        mContent.setText(bean.getContent());
    }

    @Override
    public void imgHasClicked() {
        mTxtLayout.setVisibility(mTxtLayout.getVisibility() == View.GONE ? View.VISIBLE:View.GONE);
        mTopLayout.setVisibility(mTopLayout.getVisibility() == View.INVISIBLE ? View.VISIBLE:View.INVISIBLE);
    }


    //viewPager适配器
    public class PicDetailsPagerAdapter extends FragmentStatePagerAdapter {
        private List<DetailsPicsBean> list = new ArrayList<>();

        public void setData(List<DetailsPicsBean> ml){
            this.list = ml;
            notifyDataSetChanged();
        }

        public PicDetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return PicDetailesFragment.newInstance(list.get(position),position,list.size());
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public DetailsPicsBean getBean(int position){
            return list.get(position);
        }
    }
}
