package com.ciist.xunxun.app.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ciist.xunxun.R;

/**
 * Created by hw on 2016/4/25.
 */
public class CoustomCleanEditText extends EditText implements
        View.OnFocusChangeListener, TextWatcher {
    /*
     * 删除按钮的引用
     */
    private Drawable mSearchDrawable;
    /*
     * 控件是否有焦点
     */
    private boolean hasFoucs;

    //private Context context;
    public CoustomCleanEditText(Context context) {
        this(context, null);
    }

    public CoustomCleanEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CoustomCleanEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mSearchDrawable = getCompoundDrawables()[2];
        if (mSearchDrawable == null) {
        // throw new NullPointerException("You can add drawableRight attribute in XML");
            mSearchDrawable = getResources().getDrawable(R.mipmap.ciist_xunxun_search);
        }
        mSearchDrawable.setBounds(0, 0, mSearchDrawable.getIntrinsicWidth(), mSearchDrawable.getIntrinsicHeight());
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /*
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    //里面写上自己想做的事情，也就是DrawableRight的触发事件
                    try {
                        this.setText("");
                        //Toast.makeText(getContext(), "您触发了事件", Toast.LENGTH_SHORT).show();
                        /*if (!this.getText().toString().contains(" ")){
                            mOnRightClickListener.onClick(this.getText().toString());
                        }else{
                            Toast.makeText(getContext(), "搜索关键词格式错误", Toast.LENGTH_SHORT).show();
                        }
                        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 回调接口
     *//*
    public interface OnRightClickListener{
        void onClick(String text);
    }
    private OnRightClickListener mOnRightClickListener;
    public void setOnRightClickListener(OnRightClickListener onRightClickListener){
        if (onRightClickListener != null){
            this.mOnRightClickListener = onRightClickListener;

        }
    }*/

    /*
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }


    /*
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        //如果你想让它一直显示DrawableRight的图标，并且还需要让它触发事件，直接把null改成mClearDrawable即可
        Drawable right = visible ? mSearchDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**-----------------------后面的代码为动画效果代码，如果有不需要的可以删除，当然不删除也不会出错。------------------------**/

    /*
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if(hasFoucs){
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    /*
     * 设置晃动动画
     */
    public void setShakeAnimation(){
        this.setAnimation(shakeAnimation(5));
    }


    /*
     * 晃动动画
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }
}
