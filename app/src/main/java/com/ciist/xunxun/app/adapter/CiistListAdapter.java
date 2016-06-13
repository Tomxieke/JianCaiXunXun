package com.ciist.xunxun.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciist.entites.CiistObjects;
import com.ciist.xunxun.R;
import com.ciist.xunxun.app.activity.ListActivity;
import com.ciist.xunxun.app.activity.PicDetailsActivity;
import com.ciist.xunxun.app.activity.WebActivity;
import com.ciist.xunxun.app.widget.CoustomGridView;
import com.ciist.xunxun.app.widget.HorizontalListView;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hw on 2016/3/30.
 */
public class CiistListAdapter extends BaseAdapter {

    private final int TYPE_COUNT = 8;

    private final int TYPE_0 = 0;//滑动
    private final int TYPE_1 = 1;//商标
    private final int TYPE_2 = 2;//间隔
    private final int TYPE_3 = 3;//小标题
    private final int TYPE_4 = 4;//大图
    private final int TYPE_5 = 5;//小图
    private final int TYPE_6 = 6;//横向列表
    private final int TYPE_7 = 7;//三图模式

    private Context mContext;
    private List<CiistObjects> mdata;
    private SliderLayout mSliderLayout;

    public CiistListAdapter(Context context) {
        this.mdata = new ArrayList();
        this.mContext = context;
    }

    public void setData(List<CiistObjects> data) {
        this.mdata = data;
        notifyDataSetChanged();
    }

    public void addData(List<CiistObjects> data) {
        this.mdata.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        String mt = mdata.get(position).getModeType();//显示模式modelType
        if (mt.equals("ScrollPicType")) {
            return TYPE_0;
        } else if (mt.equals("LogoPicType")) {
            return TYPE_1;
        } else if (mt.equals("SeprateType")) {
            return TYPE_2;
        } else if (mt.equals("BlockType")) {
            return TYPE_3;
        } else if (mt.equals("BigPicType")) {
            return TYPE_4;
        } else if (mt.equals("HoriPicType")) {
            return TYPE_6;
        } else if (mt.equals("TreePicType")) {
            return TYPE_7;
        } else {//小图模式smallType
            return TYPE_5;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            int type = getItemViewType(position);
            HolderType0 type0 = null;
            HolderType1 type1 = null;
            HolderType2 type2 = null;
            HolderType3 type3 = null;
            HolderType4 type4 = null;
            HolderType5 type5 = null;
            HolderType6 type6 = null;
            HolderType7 type7 = null;
            if (convertView == null) {
                switch (type) {
                    case TYPE_0:
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_banner, null);
                        type0 = new HolderType0(convertView);
                        convertView.setTag(type0);
                        break;
                    case TYPE_1:
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_logo, null);
                        type1 = new HolderType1(convertView);
                        convertView.setTag(type1);
                        break;
                    case TYPE_2:
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_separte, null);
                        type2 = new HolderType2(convertView);
                        convertView.setTag(type2);
                        break;
                    case TYPE_3:
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_title_s, null);
                        type3 = new HolderType3(convertView);
                        convertView.setTag(type3);
                        break;
                    case TYPE_4:
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_bigtype, null);
                        type4 = new HolderType4(convertView);
                        convertView.setTag(type4);
                        break;
                    case TYPE_5:
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_smalltype, null);
                        type5 = new HolderType5(convertView);
                        convertView.setTag(type5);
                        break;
                    case TYPE_6:
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_horizonlist, null);
                        type6 = new HolderType6(convertView);
                        convertView.setTag(type6);
                        break;
                    case TYPE_7:
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_threepictype, null);
                        type7 = new HolderType7(convertView);
                        convertView.setTag(type7);
                        break;
                }
            } else {
                switch (type) {
                    case TYPE_0:
                        type0 = (HolderType0) convertView.getTag();
                        break;
                    case TYPE_1:
                        type1 = (HolderType1) convertView.getTag();
                        break;
                    case TYPE_2:
                        type2 = (HolderType2) convertView.getTag();
                        break;
                    case TYPE_3:
                        type3 = (HolderType3) convertView.getTag();
                        break;
                    case TYPE_4:
                        type4 = (HolderType4) convertView.getTag();
                        break;
                    case TYPE_5:
                        type5 = (HolderType5) convertView.getTag();
                        break;
                    case TYPE_6:
                        type6 = (HolderType6) convertView.getTag();
                        break;
                    case TYPE_7:
                        type7 = (HolderType7) convertView.getTag();
                        break;
                }
            }
            switch (type) {
                case TYPE_0://滑动图片模式
                    try {
                        mSliderLayout = (SliderLayout) type0.banner_img.findViewById(R.id.banner_img);
                        mSliderLayout.removeAllSliders(); //移除原有数据
                        String[] st = mdata.get(position).getAbs().split(";");
                        for (int i = 0; i < st.length; i++) {
                            final String[] t = st[i].split("#");
                            TextSliderView sliderView = new TextSliderView(mContext);//向SliderLayout中添加控件
                            //Log.e("TAG",t[0]);
                            sliderView.image(t[0]);
                            if (!t[1].equals(" ")) {
                                sliderView.description(t[1], true);
                            } else {
                                sliderView.description(t[1], false);
                            }
                            sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    String model = t[3];
                                    if (model.equals("C")) {
                                        Intent intent = new Intent(mContext, WebActivity.class);
                                        intent.putExtra("IMGPATH", t[0]);
                                        intent.putExtra("CONTENT", t[1]);
                                        intent.putExtra("WEB_URL", t[2]);
                                        mContext.startActivity(intent);
                                    } else if (model.equals("P")) {
                                        Intent intent = new Intent(mContext, PicDetailsActivity.class);
                                        intent.putExtra("PICDATAIELS_URL", t[2]);
                                        mContext.startActivity(intent);
                                    } else if (model.equals("L")) {
                                        Intent intent = new Intent(mContext, ListActivity.class);
                                        intent.putExtra("LIST_URL", t[2]);
                                        mContext.startActivity(intent);
                                    }
                                }
                            });
                            mSliderLayout.addSlider(sliderView);
                        }
                        if (st.length == 1) {
                            mSliderLayout.stopAutoCycle();
                        }
                        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Top);  //将小圆点设置到右下方
                        //mSliderLayout.setDuration(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case TYPE_1://品牌展播模式
                    String[] logoStr = mdata.get(position).getAbs().split(";");
                    final List<String> los = new ArrayList<>();
                    final List<String> lis = new ArrayList<>();
                    final List<String> lcs = new ArrayList<>();
                    final List<String> ms = new ArrayList<>();
                    for (int i = 0; i < logoStr.length; i++) {
                        String[] ls = logoStr[i].split("#");
                        los.add(ls[0]);
                        lcs.add(ls[1]);
                        lis.add(ls[2]);
                        ms.add(ls[3]);
                    }
                    LogoAdapter logoAdapter = new LogoAdapter();
                    logoAdapter.setData(los);
                    type1.logo_gv.setAdapter(logoAdapter);
                    type1.logo_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String model = ms.get(position);
                            if (model.equals("C")) {
                                Intent intent = new Intent(mContext, WebActivity.class);
                                intent.putExtra("IMGPATH", los.get(position));
                                intent.putExtra("CONTENT", lcs.get(position));
                                intent.putExtra("WEB_URL", lis.get(position));
                                mContext.startActivity(intent);
                            } else if (model.equals("P")) {
                                Intent intent = new Intent(mContext, PicDetailsActivity.class);
                                intent.putExtra("PICDATAIELS_URL", lis.get(position));
                                mContext.startActivity(intent);
                            } else if (model.equals("L")) {
                                Intent intent = new Intent(mContext, ListActivity.class);
                                intent.putExtra("LIST_URL", lis.get(position));
                                mContext.startActivity(intent);
                            }
                        }
                    });
                    break;
                case TYPE_2://空格间隙模式
                    ViewGroup.LayoutParams vParams = type2.separt_view.getLayoutParams();
                    //Log.e("Taf", mdata.get(position).getImageHeight());
                    vParams.height = Integer.parseInt(mdata.get(position).getImageHeight());
                    vParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    type2.separt_view.setLayoutParams(vParams);
                    if (mdata.get(position).getBannertitleColor().replaceAll(" ", "").length() > 5) {
                        type2.separt_view.setBackgroundColor(Color.parseColor(mdata.get(position).getBannertitleColor().replaceAll(" ", "")));
                    }
                    break;
                case TYPE_3://小标题模式
                    type3.small_title_text.setText(mdata.get(position).getTitle());
                    //先用作者属性来判断显示小标题左面图片的类型 0表示默认并设置颜色，1表示设置活动专用发布图片
                    String color = mdata.get(position).getBannertitleColor().replaceAll(" ", "");
                    if (mdata.get(position).getAuthor().equals("0")) {
                        if (color.length() > 5) {
                            type3.small_title_left_color.setBackgroundColor(Color.parseColor(color));
                        }
                        type3.small_title_left_color.setVisibility(View.VISIBLE);
                        type3.small_title_left_icon.setVisibility(View.GONE);
                    } else if (mdata.get(position).getAuthor().equals("1")) {
                        type3.small_title_left_color.setVisibility(View.GONE);
                        type3.small_title_left_icon.setVisibility(View.VISIBLE);
                    } else {
                        if (color.length() > 5) {
                            type3.small_title_left_color.setBackgroundColor(Color.parseColor(color));
                        }
                        type3.small_title_left_color.setVisibility(View.VISIBLE);
                        type3.small_title_left_icon.setVisibility(View.GONE);
                    }
                    if (mdata.get(position).getBannerbuttonFlag().equals("1")) {
                        type3.small_title_rightText.setVisibility(View.VISIBLE);
                        type3.small_title_rightText.setText(mdata.get(position).getBannerbuttontitle());
                    }
                    type3.small_title_rightText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startIntent(position);
                        }
                    });
                    break;
                case TYPE_4://大图模式
                    if (mdata.get(position).getImagePath().length() > 3) {
                        Picasso.with(mContext).load(formatImgPath(mdata.get(position).getImagePath())).fit().into(type4.bigimg100);
                        Picasso.with(mContext).load(formatImgPath(mdata.get(position).getImagePath())).fit().into(type4.bigimg200);
                        Picasso.with(mContext).load(formatImgPath(mdata.get(position).getImagePath())).fit().into(type4.bigimg300);
                    } else {
                        type4.bigimg100.setImageResource(R.mipmap.ciist_moren);
                        type4.bigimg200.setImageResource(R.mipmap.ciist_moren);
                        type4.bigimg300.setImageResource(R.mipmap.ciist_moren);
                    }
                    type4.bigtext_title.setText(mdata.get(position).getTitle());
                    type4.bigtext_shortTitle.setText(mdata.get(position).getShortTitle());
                    type4.bigtext_subtitle.setText(mdata.get(position).getSubtitle());
                    type4.bigtext_time.setText(formatTime(mdata.get(position).getTimestamp()));
                    type4.bigtext_count.setText(mdata.get(position).getVisitCount());
                    type4.bigLabel.setText(mdata.get(position).getImageLabels());
                    //判断显示图片类型
                    if (mdata.get(position).getImageHeight().equals("100")) {
                        type4.bigimg100.setVisibility(View.VISIBLE);
                        type4.bigimg200.setVisibility(View.GONE);
                        type4.bigimg300.setVisibility(View.GONE);
                    } else if (mdata.get(position).getImageHeight().equals("200")) {
                        type4.bigimg100.setVisibility(View.GONE);
                        type4.bigimg200.setVisibility(View.VISIBLE);
                        type4.bigimg300.setVisibility(View.GONE);
                    } else if (mdata.get(position).getImageHeight().equals("300")) {
                        type4.bigimg100.setVisibility(View.GONE);
                        type4.bigimg200.setVisibility(View.GONE);
                        type4.bigimg300.setVisibility(View.VISIBLE);
                    }else{
                        type4.bigimg100.setVisibility(View.GONE);
                        type4.bigimg200.setVisibility(View.VISIBLE);
                        type4.bigimg300.setVisibility(View.GONE);
                    }
                    //判断标签颜色暂时随机变换
                    //if (position % 3 == 0) {
                    //    type4.bigLabel.setBackgroundColor(Color.parseColor("#b70e36"));
                    //} else if (position % 3 == 1) {
                    //    type4.bigLabel.setBackgroundColor(Color.parseColor("#fbed0d"));
                    //} else if (position % 3 == 2) {
                    //    type4.bigLabel.setBackgroundColor(Color.parseColor("#5cc731"));
                    //}
                    if (mdata.get(position).getBannertitleColor().replaceAll(" ", "").length() > 5) {
                        type4.bigLabel.setBackgroundColor(Color.parseColor(mdata.get(position).getBannertitleColor().replaceAll(" ", "")));
                    }else{
                        type4.bigLabel.setBackgroundColor(Color.parseColor("#b70e36"));
                    }
                    //判断图片标签
                    if (mdata.get(position).getImageLabelsFlag().equals("1")) {
                        type4.bigLabel.setVisibility(View.VISIBLE);
                    } else {
                        type4.bigLabel.setVisibility(View.GONE);
                    }
                    //判断标题
                    if (mdata.get(position).getTitleFlag().equals("1")) {
                        type4.bigtext_title.setVisibility(View.VISIBLE);
                    } else {
                        type4.bigtext_title.setVisibility(View.GONE);
                    }
                    //判断缩略标题
                    if (mdata.get(position).getShortTitleFlag().equals("1")) {
                        type4.bigtext_shortTitle.setVisibility(View.VISIBLE);
                    } else {
                        type4.bigtext_shortTitle.setVisibility(View.GONE);
                    }
                    //判断副标题
                    if (mdata.get(position).getSubTitleFlag().equals("1")) {
                        type4.bigtext_subtitle.setVisibility(View.VISIBLE);
                    } else {
                        type4.bigtext_subtitle.setVisibility(View.GONE);
                    }
                    //判断时间
                    //Log.e("TAG",mdata.get(position).getTimestamp());
                    if (mdata.get(position).getPubdataFlag().equals("1")) {
                        type4.bigtext_time.setVisibility(View.VISIBLE);
                    } else {
                        type4.bigtext_time.setVisibility(View.GONE);
                    }
                    //判断次数
                    if (mdata.get(position).getVisitCountFlag().equals("1")) {
                        type4.bigtext_count.setVisibility(View.VISIBLE);
                    } else {
                        type4.bigtext_count.setVisibility(View.GONE);
                    }
                    //判断时间和次数
                    if (mdata.get(position).getPubdataFlag().equals("1") && mdata.get(position).getVisitCountFlag().equals("1")) {
                        type4.big_rl.setVisibility(View.VISIBLE);
                    } else {
                        type4.big_rl.setVisibility(View.GONE);
                    }
                    //item 监听
                    type4.big_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startIntent(position);
                        }
                    });
                    break;
                case TYPE_5://小图模式
                    String imgPath = mdata.get(position).getImagePath();
                    if (imgPath.length() > 3) {
                        Picasso.with(mContext).load(formatImgPath(imgPath)).fit().centerCrop().into(type5.smallimg);
                    } else {
                        type5.smallimg.setImageResource(R.mipmap.ciist_moren);
                    }
                    type5.smalltext_title.setText(mdata.get(position).getTitle());
                    type5.smallLabel.setText(mdata.get(position).getImageLabels());
                    type5.smalltext_time.setText(formatTime(mdata.get(position).getTimestamp()));
                    type5.smalltext_count.setText(mdata.get(position).getVisitCount());
                    //判断标签颜色暂时随机变换
                    if (position % 3 == 0) {
                        type5.smallLabel.setBackgroundColor(Color.parseColor("#b70e36"));
                    } else if (position % 3 == 1) {
                        type5.smallLabel.setBackgroundColor(Color.parseColor("#fbed0d"));
                    } else if (position % 3 == 2) {
                        type5.smallLabel.setBackgroundColor(Color.parseColor("#5cc731"));
                    }
                    //判断图片标签
                    if (mdata.get(position).getImageLabelsFlag().equals("1")) {
                        type5.smallLabel.setVisibility(View.VISIBLE);
                    } else {
                        type5.smallLabel.setVisibility(View.GONE);
                    }
                    //判断标题
                    if (mdata.get(position).getTitleFlag().equals("1")) {
                        type5.smalltext_title.setVisibility(View.VISIBLE);
                    } else {
                        type5.smalltext_title.setVisibility(View.GONE);
                    }
                    //判断时间
                    if (mdata.get(position).getPubdataFlag().equals("1")) {
                        type5.smalltext_time.setVisibility(View.VISIBLE);
                    } else {
                        type5.smalltext_time.setVisibility(View.GONE);
                    }
                    //判断次数
                    if (mdata.get(position).getVisitCountFlag().equals("1")) {
                        type5.smalltext_count.setVisibility(View.VISIBLE);
                    } else {
                        type5.smalltext_count.setVisibility(View.GONE);
                    }
                    //item监听
                    type5.small_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startIntent(position);
                        }
                    });
                    break;
                case TYPE_6://横向滑动列表模式
                    final List<String> is = new ArrayList<>();//图片地址集合
                    final List<String> ts = new ArrayList<>();//描述内容集合
                    final List<String> us = new ArrayList<>();//下级链接集合
                    final List<String> abs = new ArrayList<>();//下级访问模式
                    String[] hStr = mdata.get(position).getAbs().split(";");
                    for (int i = 0; i < hStr.length; i++) {
                        String[] hs = hStr[i].split("#");
                        is.add(hs[0]);
                        ts.add(hs[1]);
                        us.add(hs[2]);
                        abs.add(hs[3]);
                    }
                    HorizonAdapter horizonAdapter = new HorizonAdapter();
                    horizonAdapter.setData(is, ts);
                    type6.horizonList_hl.setAdapter(horizonAdapter);//设置横向列表
                    type6.horizonList_hl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String model = abs.get(position);
                            if (model.equals("C")) {
                                Intent intent = new Intent(mContext, WebActivity.class);
                                intent.putExtra("WEB_URL", us.get(position));
                                intent.putExtra("CONTENT", ts.get(position));
                                intent.putExtra("IMGPATH", is.get(position));
                                mContext.startActivity(intent);
                            } else if (model.equals("P")) {
                                Intent intent = new Intent(mContext, PicDetailsActivity.class);
                                intent.putExtra("PICDATAIELS_URL", us.get(position));
                                mContext.startActivity(intent);
                            } else if (model.equals("L")) {
                                Intent intent = new Intent(mContext, ListActivity.class);
                                intent.putExtra("LIST_URL", us.get(position));
                                mContext.startActivity(intent);
                            }
                        }
                    });
                case TYPE_7://三图模式
                    String[] three = mdata.get(position).getAbs().split(";");
                    if (three[0].length() > 3) {
                        Picasso.with(mContext).load(three[0]).fit().into(type7.threePic_img1);
                        Picasso.with(mContext).load(three[1]).fit().into(type7.threePic_img2);
                        Picasso.with(mContext).load(three[2]).fit().into(type7.threePic_img3);
                    } else {
                        type7.threePic_img1.setImageResource(R.mipmap.ciist_moren);
                        type7.threePic_img2.setImageResource(R.mipmap.ciist_moren);
                        type7.threePic_img3.setImageResource(R.mipmap.ciist_moren);
                    }
                    type7.threePic_title.setText(mdata.get(position).getTitle());
                    type7.threePic_shortTitle.setText(mdata.get(position).getShortTitle());
                    type7.threePic_subtitle.setText(mdata.get(position).getSubtitle());
                    type7.threePic_time.setText(formatTime(mdata.get(position).getTimestamp()));
                    type7.threePic_count.setText(mdata.get(position).getVisitCount());
                    //判断标题
                    if (mdata.get(position).getTitleFlag().equals("1")) {
                        type7.threePic_title.setVisibility(View.VISIBLE);
                    } else {
                        type7.threePic_title.setVisibility(View.GONE);
                    }
                    //判断时间
                    if (mdata.get(position).getPubdataFlag().equals("1")) {
                        type7.threePic_time.setVisibility(View.VISIBLE);
                    } else {
                        type7.threePic_time.setVisibility(View.GONE);
                    }
                    //判断次数
                    if (mdata.get(position).getVisitCountFlag().equals("1")) {
                        type7.threePic_count.setVisibility(View.VISIBLE);
                    } else {
                        type7.threePic_count.setVisibility(View.GONE);
                    }
                    //判断副标题
                    if (mdata.get(position).getSubTitleFlag().equals("1")) {
                        type7.threePic_subtitle.setVisibility(View.VISIBLE);
                    } else {
                        type7.threePic_subtitle.setVisibility(View.GONE);
                    }
                    //判断时间和次数
                    if (mdata.get(position).getPubdataFlag().equals("1") && mdata.get(position).getVisitCountFlag().equals("1")) {
                        type7.threePic_rl.setVisibility(View.VISIBLE);
                    } else {
                        type7.threePic_rl.setVisibility(View.GONE);
                    }
                    //item监听
                    type7.threePic_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startIntent(position);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    /**
     * 格式化时间
     *
     * @param s
     * @return
     */
    public static String formatTime(String s) {
        String str = s.substring(6, 24);
        String time = str.substring(0, str.length() - 5);
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        //格式化的获取的时间
        String string = format.format(date);
        //当天时间
        String currentTime = format.format(new Date());
        //Log.e("currentTime:", "currentTime:" + currentTime);
        //比较确定返回的数据
        String[] a = string.split(" ");
        String[] b = currentTime.split(" ");
        String[] a1 = a[0].split("-");
        String[] b1 = b[0].split("-");
        if (a[0].equals(b[0])) {//如果年月日都相同，说明是今天，返回时间格式为 时：分
            string = a[1].replaceAll("-", ":");
        } else if (Integer.parseInt(a1[0]) < Integer.parseInt(b1[0])) {//如果年比当前时间的年份小，说明是往年，则实际返回格式为： 年-月-日
            string = a[0];
        } else if (a1[0].equals(b1[0])) {//如果年份相同，则开始比较月份
            if (a1[1].equals(b1[1])) {//如果月份相同，则开始比较日期
                if (Integer.parseInt(a1[2]) == Integer.parseInt(b1[2]) - 1) {//如果得到的天数等于当前日期天减去1的话，说明是昨天，则返回：昨天
                    string = "昨天";
                } else if (Integer.parseInt(a1[2]) == Integer.parseInt(b1[2]) - 2) {//如果得到的天数等于当前日期天减去2的话，说明是前天，则返回：前天
                    string = "前天";
                } else {//否则则返回格式为： 月-日
                    string = a1[1] + "-" + a1[2];
                }
            } else {//如果不相同，说明是今年，但不是当月，所以返回 月-日
                string = a1[1] + "-" + a1[2];
            }
        }
        return string;
    }

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
     * 跳转页面
     *
     * @param position
     */
    private void startIntent(int position) {
        String model = mdata.get(position).getLinkModel();
        Log.e("TAG", model);
        if (model.equals("C")) {
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra("WEB_URL", mdata.get(position).getLinkCode());
            intent.putExtra("CONTENT", mdata.get(position).getTitle());
            intent.putExtra("IMGPATH", mdata.get(position).getImagePath());
            intent.putExtra("SHARELINK",mdata.get(position).getShareLink());
            intent.putExtra("Tel",mdata.get(position).getTel());
            mContext.startActivity(intent);
        } else if (model.equals("P")) {
            Intent intent = new Intent(mContext, PicDetailsActivity.class);
            intent.putExtra("PICDATAIELS_ABS", mdata.get(position).getAbs());
            mContext.startActivity(intent);
        } else if (model.equals("L")) {
            Intent intent = new Intent(mContext, ListActivity.class);
            intent.putExtra("LIST_URL", mdata.get(position).getLinkCode());
            mContext.startActivity(intent);
        }
    }

    /**
     * banner
     **/
    class HolderType0 {
        SliderLayout banner_img;
        TextView banner_content;

        public HolderType0(View v) {
            banner_img = (SliderLayout) v.findViewById(R.id.banner_img);
            banner_content = (TextView) v.findViewById(R.id.banner_content);
        }
    }

    /**
     * logo
     **/
    class HolderType1 {
        CoustomGridView logo_gv;

        public HolderType1(View v) {
            logo_gv = (CoustomGridView) v.findViewById(R.id.logo_gv);
        }
    }

    /**
     * separte
     **/
    class HolderType2 {
        TextView separt_view;

        public HolderType2(View v) {
            separt_view = (TextView) v.findViewById(R.id.separt_view);
        }
    }

    /**
     * smallTitle
     **/
    class HolderType3 {
        TextView small_title_left_color;
        TextView small_title_text;
        TextView small_title_rightText;
        ImageView small_title_left_icon;

        public HolderType3(View v) {
            small_title_left_color = (TextView) v.findViewById(R.id.small_title_left_color);
            small_title_text = (TextView) v.findViewById(R.id.small_title_text);
            small_title_rightText = (TextView) v.findViewById(R.id.small_title_rightText);
            small_title_left_icon = (ImageView) v.findViewById(R.id.small_title_left_icon);
        }
    }

    /**
     * bigimg
     **/
    class HolderType4 {
        ImageView bigimg100;
        ImageView bigimg200;
        ImageView bigimg300;
        TextView bigtext_title;
        TextView bigtext_shortTitle;
        TextView bigLabel;
        TextView bigtext_subtitle;
        TextView bigtext_time;
        TextView bigtext_count;
        LinearLayout big_ll;
        RelativeLayout big_rl;

        public HolderType4(View v) {
            bigimg100 = (ImageView) v.findViewById(R.id.bigimg100);
            bigimg200 = (ImageView) v.findViewById(R.id.bigimg200);
            bigimg300 = (ImageView) v.findViewById(R.id.bigimg300);
            bigtext_title = (TextView) v.findViewById(R.id.bigtext_title);
            bigtext_shortTitle = (TextView) v.findViewById(R.id.bigtext_shortTitle);
            bigLabel = (TextView) v.findViewById(R.id.bigLabel);
            bigtext_subtitle = (TextView) v.findViewById(R.id.bigtext_subtitle);
            bigtext_time = (TextView) v.findViewById(R.id.bigtext_time);
            bigtext_count = (TextView) v.findViewById(R.id.bigtext_count);
            big_ll = (LinearLayout) v.findViewById(R.id.big_ll);
            big_rl = (RelativeLayout) v.findViewById(R.id.big_rl);
        }
    }

    /**
     * smallimg
     **/
    class HolderType5 {
        ImageView smallimg;
        TextView smalltext_describe;
        TextView smalltext_time;
        TextView smalltext_count;
        TextView smalltext_title;
        TextView smallLabel;
        RelativeLayout small_rl;

        public HolderType5(View v) {
            smallimg = (ImageView) v.findViewById(R.id.smallimg);
            smalltext_title = (TextView) v.findViewById(R.id.smalltext_title);
            smalltext_describe = (TextView) v.findViewById(R.id.smalltext_describe);
            smalltext_time = (TextView) v.findViewById(R.id.smalltext_time);
            smalltext_count = (TextView) v.findViewById(R.id.smalltext_count);
            small_rl = (RelativeLayout) v.findViewById(R.id.small_rl);
            smallLabel = (TextView) v.findViewById(R.id.smallLabel);
        }
    }

    /**
     * horizonlist
     **/
    class HolderType6 {
        HorizontalListView horizonList_hl;

        public HolderType6(View v) {
            horizonList_hl = (HorizontalListView) v.findViewById(R.id.horizonList_hl);
        }
    }

    /**
     * threePicType
     **/
    class HolderType7 {
        TextView threePic_shortTitle;
        TextView threePic_title;
        TextView threePic_subtitle;
        TextView threePic_time;
        TextView threePic_count;
        RelativeLayout threePic_rl;
        LinearLayout threePic_ll;
        ImageView threePic_img1;
        ImageView threePic_img2;
        ImageView threePic_img3;

        public HolderType7(View v) {
            threePic_shortTitle = (TextView) v.findViewById(R.id.threePic_shortTitle);
            threePic_title = (TextView) v.findViewById(R.id.threePic_title);
            threePic_subtitle = (TextView) v.findViewById(R.id.threePic_subtitle);
            threePic_time = (TextView) v.findViewById(R.id.threePic_time);
            threePic_count = (TextView) v.findViewById(R.id.threePic_count);
            threePic_rl = (RelativeLayout) v.findViewById(R.id.threePic_rl);
            threePic_img1 = (ImageView) v.findViewById(R.id.threePic_img1);
            threePic_img2 = (ImageView) v.findViewById(R.id.threePic_img2);
            threePic_img3 = (ImageView) v.findViewById(R.id.threePic_img3);
            threePic_ll = (LinearLayout) v.findViewById(R.id.threePic_ll);
        }

    }


    /**
     * logo模式的适配器
     */
    class LogoAdapter extends BaseAdapter {

        private List<String> l = new ArrayList<>();

        public void setData(List<String> a) {
            this.l = a;
        }

        @Override
        public int getCount() {
            return l.size();
        }

        @Override
        public Object getItem(int position) {
            return l.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                ImageView item_logo_iv;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_adapter_logo, null);
                    item_logo_iv = (ImageView) convertView.findViewById(R.id.item_logo_iv);
                    item_logo_iv.setTag(convertView);
                } else {
                    item_logo_iv = (ImageView) convertView.getTag();
                }

                Picasso.with(mContext).load(l.get(position)).fit().into(item_logo_iv);
            } catch (Exception a) {
                return convertView;
            }
            return convertView;
        }
    }

    /**
     * 横向列表适配器
     */
    class HorizonAdapter extends BaseAdapter {

        private List<String> imgPath = new ArrayList<>();
        private List<String> text = new ArrayList<>();

        private void setData(List<String> ip, List<String> t) {
            this.imgPath = ip;
            this.text = t;
        }

        @Override
        public int getCount() {
            return imgPath.size();
        }

        @Override
        public Object getItem(int position) {
            return imgPath.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GvHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_adapter_horizonlist, null);
                holder = new GvHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (GvHolder) convertView.getTag();
            }
            try {
                Picasso.with(mContext).load(imgPath.get(position)).fit().centerCrop().into(holder.item_horizonList_iv);
                holder.item_horizonList_title.setText(text.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class GvHolder {
            private ImageView item_horizonList_iv;
            private TextView item_horizonList_title;

            public GvHolder(View v) {
                item_horizonList_iv = (ImageView) v.findViewById(R.id.item_horizonList_iv);
                item_horizonList_title = (TextView) v.findViewById(R.id.item_horizonList_title);
            }
        }
    }
}
