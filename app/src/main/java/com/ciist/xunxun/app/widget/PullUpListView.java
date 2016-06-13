package com.ciist.xunxun.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ciist.xunxun.R;

/**
 * Created by hw on 2016/4/20.
 */
public class PullUpListView extends ListView implements AbsListView.OnScrollListener {

    /** 底部显示正在加载的页面 */
    private View footerView = null;
    /** 存储上下文 */
    private Context context;
    /** 上拉刷新的ListView的回调监听 */
    private OnPullUpListViewCallBack onPullUpListViewCallBack;
    /** 记录第一行Item的数值 */
    private int firstVisibleItem;

    public PullUpListView(Context context) {
        super(context);
        this.context = context;
        initListView();
    }

    public PullUpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initListView();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {

        // 为ListView设置滑动监听
        setOnScrollListener(this);
        // 去掉底部分割线
        setFooterDividersEnabled(false);
        initBottomView();
    }

    /**
     * 初始化话底部页面
     */
    private void initBottomView() {

        if (footerView == null) {
            footerView = LayoutInflater.from(this.context).inflate(
                    R.layout.listview_loadbar, null);
        }
        addFooterView(footerView);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //当滑动到底部时
        try {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && firstVisibleItem != 0) {
                onPullUpListViewCallBack.scrollBottomState();
            }
        }catch (Exception e){
            e.printStackTrace();
            removeFooterView(footerView);//隐藏底部布局
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        this.firstVisibleItem = firstVisibleItem;
        if (footerView != null) {
            //判断可视Item是否能在当前页面完全显示
            if (visibleItemCount == totalItemCount) {
                // removeFooterView(footerView);
                footerView.setVisibility(View.GONE);//隐藏底部布局
            } else {
                // addFooterView(footerView);
                footerView.setVisibility(View.VISIBLE);//显示底部布局
            }
        }
    }

    public void setFooterVisibily(boolean isVisibily){
        if (isVisibily){
            addFooterView(footerView);//显示底部布局
        }else{
            removeFooterView(footerView);//隐藏底部布局
        }
    }

    public void setOnPullUpListViewCallBack(OnPullUpListViewCallBack onPullUpListViewCallBack) {
        this.onPullUpListViewCallBack = onPullUpListViewCallBack;
    }

    /**
     * 上拉刷新的ListView的回调监听
     *
     * @author xiejinxiong
     *
     */
    public interface OnPullUpListViewCallBack {
        void scrollBottomState();
    }
}
