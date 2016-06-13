package widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.ciistsdk_library.R;


/**
 * Created by xieke on 2016/2/29.
 */
public class CiistEditText extends EditText implements TextWatcher {
    private Drawable draw;

    private int leftDrawableRes = 0;
    private Drawable leftDrawable = null;

    public CiistEditText(Context context) {
        super(context);
        initClearDrawable();
    }

    public CiistEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLeftDrawable(context,attrs);
        initClearDrawable();
    }

    public CiistEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initLeftDrawable(context,attrs);
        initClearDrawable();  //加载清除图片

    }

    /**
     *初始化左边的图标
     * @param context
     * @param attrs
     */
    private void initLeftDrawable(Context context, AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CiistEditText,
                0,0);
        leftDrawableRes = array.getResourceId(R.styleable.CiistEditText_leftDrawable,0);
        array.recycle();
        try {
            leftDrawable = getResources().getDrawable(leftDrawableRes);
        }catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化清除图片
     */
    private void initClearDrawable(){
        draw = getCompoundDrawables()[2];
        if (draw == null){
            draw = getResources().getDrawable(R.mipmap.announcement_close_normal);
        }
    //    this.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
    }



    @Override
    //判断接触的位置是否为点击清除的位置
    public boolean onTouchEvent(MotionEvent event) {
        // 判断触碰是否结束
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 判断所触碰的位置是否为清除的按钮
            if (event.getX() > (getWidth() - getTotalPaddingRight())
                    && event.getX() < (getWidth() - getPaddingRight())) {
                // 将editText里面的内容清除
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    //没有焦点的时候让清除图标消失
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        // TODO Auto-generated method stub
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        // 判断焦点失去和得到时的操作
        if (focused && !this.getText().toString().equals("")) {
            this.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, draw, null);
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() > 0){  //有内容显示清除图片
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(leftDrawable,null,draw,null);
        }else {
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(leftDrawable,null,null,null);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
