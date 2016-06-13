package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ciistsdk_library.R;


/*
 * StrokeTextView的目标是给文字描边
 * 实现方法是两个TextView叠加,只有描边的TextView为底,实体TextView叠加在上面
 * 看上去文字就有个不同颜色的边框了
 */
public class StrokeTextView extends TextView {

    private TextView borderText = null;///用于描边的TextView
    private int strokeColor;  //描边颜色
    private int storkeWidth = 1;   //描边宽度 默认为1

    public StrokeTextView(Context context) {
        super(context);
        borderText = new TextView(context);
    //    init();
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new TextView(context,attrs);
        init(context,attrs);
    }

    public StrokeTextView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        borderText = new TextView(context,attrs,defStyle);
        init(context,attrs);
    }

   /* public void init(){
        TextPaint tp1 = borderText.getPaint();
        tp1.setStrokeWidth(4);                                  //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
        borderText.setTextColor(getResources().getColor(R.color.border_text));  //设置描边颜色
        borderText.setGravity(getGravity());
    }*/

    public void init(Context context, AttributeSet attrs){
        TextPaint tp1 = borderText.getPaint();
        TypedArray array = null;
        try {
            array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StrokeTextView, 0, 0);
            strokeColor = array.getColor(R.styleable.StrokeTextView_ciist_stroke_color,-1);
            storkeWidth = array.getDimensionPixelSize(R.styleable.StrokeTextView_ciist_stroke_width,1);
        }finally {
            array.recycle();
        }
        tp1.setStrokeWidth(storkeWidth);         //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE);       //对文字只描边
        if (strokeColor != -1){
            borderText.setTextColor(strokeColor);  //设置描边颜色
        }else {
            borderText.setTextColor(getResources().getColor(R.color.border_text));
        }
        borderText.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();
        //两个TextView上的文字必须一致
        if(tt== null || !tt.equals(this.getText())){
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }

}
