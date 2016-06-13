package com.ciist.xunxun.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.ciist.entites.CiistObjects;
import com.ciist.xunxun.app.fragment.ContentFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private List<CiistObjects> mdata = new ArrayList<>();
    public void setData(List<CiistObjects> data){
        this.mdata = data;
        notifyDataSetChanged();
    }
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mdata.get(position).getTitle();
    }
    @Override
    public int getCount() {
        return mdata.size();
    }
    @Override
    public Fragment getItem(int position) {
        return ContentFragment.newInstance(mdata.get(position).getLinkCode());
    }


}
