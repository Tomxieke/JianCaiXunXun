package com.ciist.xunxun.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.entity.DetailsPicsBean;
import com.squareup.picasso.Picasso;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicDetailesFragment extends Fragment {
    private static final String PARAM =  "PARAM";
    private static final String PARAM_CURRENT_PAGE = "PARAM_CURRENT_PAGE"; //当前页
    private int current_page;
    private static final String PARAM_TOTLE_PAGE = "PARAM_TOTLE_PAGE"; //总图片数
    private int totle_page;
    private DetailsPicsBean mBean;  //传下来的对象
    private PhotoView mImg;  //图片
    //private TextView mTitleTxt,mCountTxt,mContent; //标题.图片总是，内容介绍
    //private LinearLayout mTxtLayout;  //文字布局
    private RelativeLayout mMainLayout;  //主布局

    private ImgClickCallBack mCallBack;
    public interface ImgClickCallBack{
        public void imgHasClicked();
    }
    public void setCallBack(ImgClickCallBack callBack){
        this.mCallBack = callBack;
    }

    public PicDetailesFragment() {
    }

    public static PicDetailesFragment newInstance(DetailsPicsBean bean,int current_page,int totle_page) {
        Bundle args = new Bundle();
        args.putSerializable(PARAM,bean);
        args.putInt(PARAM_CURRENT_PAGE,current_page);
        args.putInt(PARAM_TOTLE_PAGE,totle_page);
        PicDetailesFragment fragment = new PicDetailesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBack = (ImgClickCallBack) activity;
        Bundle args = getArguments();
        if (args != null){
            mBean = (DetailsPicsBean) args.getSerializable(PARAM);
            current_page = args.getInt(PARAM_CURRENT_PAGE);
            totle_page = args.getInt(PARAM_TOTLE_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic_detailes, container, false);
        mImg = (PhotoView) view.findViewById(R.id.details_img);
        mImg.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                mCallBack.imgHasClicked();
            }
        });
        //mTitleTxt = (TextView) view.findViewById(R.id.details_title_txt);
        //mCountTxt = (TextView) view.findViewById(R.id.detailes_count_txt);
        //mContent = (TextView) view.findViewById(R.id.details_content_txt);
        //mTxtLayout = (LinearLayout) view.findViewById(R.id.title_layout);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TAg",mBean.getPicUrl());
        if (mBean.getPicUrl().length() > 5){
            Picasso.with(getActivity()).load(mBean.getPicUrl()).into(mImg);
        }
    }

    /**
     * 转换反斜杠
     * @param path
     * @return
     */
    private String formatImgPath(String path){
        return "http://www.ciist.com:2016" + path.replace("\\","/");
    }


}
