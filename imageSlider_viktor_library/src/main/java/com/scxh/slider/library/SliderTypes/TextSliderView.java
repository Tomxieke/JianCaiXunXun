package com.scxh.slider.library.SliderTypes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scxh.slider.library.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView{

    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(getDescription());
        if (getShowDescription()){
            description.setVisibility(View.VISIBLE);
        }else{
            description.setVisibility(View.GONE);
        }
        Log.e("TAG",getShowDescription()+"");
        bindEventAndShow(v, target);
        return v;
    }
}
