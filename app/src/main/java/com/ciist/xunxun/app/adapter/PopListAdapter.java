package com.ciist.xunxun.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciist.entites.CiistObjects;
import com.ciist.xunxun.R;
import com.ciist.xunxun.app.util.AsyncImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hw on 2016/3/29.
 */
public class PopListAdapter extends BaseAdapter {

    public List<CiistObjects> mdata;
    private LayoutInflater inflater;

    public PopListAdapter(Context context){
        this.mdata = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<CiistObjects> data){
        this.mdata = data;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.layout_popview_itemview,null);
        }
        RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.show_popview_Rl);
        TextView txt = (TextView) convertView.findViewById(R.id.show_popview_txt);
        TextView time = (TextView) convertView.findViewById(R.id.show_popview_time);
        //设置item背景颜色
        int val = position%3;
        if (val == 0){
            rl.setBackgroundResource(R.mipmap.ciist_jc_home_icon_bg1);
        }else if(val == 1){
            rl.setBackgroundResource(R.mipmap.ciist_jc_home_icon_bg2);
        }else if(val == 2){
            rl.setBackgroundResource(R.mipmap.ciist_jc_home_icon_bg3);
        }
        txt.setText(mdata.get(position).getTitle());
        time.setText(AsyncImageLoader.formatTime(mdata.get(position).getTimestamp()));
        return convertView;
    }


}