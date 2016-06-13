package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.ciistsdk_library.R;


/**
 * Created by xieke on 2016/1/31.
 */
public class CiistSmallTitle extends RelativeLayout implements View.OnClickListener{

    private String titleTxt,rightTxt;
    private int icon_color,txtColor;
    private int rightbg_selector; //不使用默认的selector;  资源selector;

    /*左右两边的textview*/
    private TextView leftTv,rightTv;
    private View iconView;

    private OnClickListener onClickListener;
    public void setRightOnClickListener(OnClickListener o){
        this.onClickListener = o;
    }

    public CiistSmallTitle(Context context) {
        super(context);
    }

    public CiistSmallTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CiistSmallTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs){
        TypedArray array = null;
        try {
            array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CiistSmallTitle, 0, 0);
            titleTxt = array.getString(R.styleable.CiistSmallTitle_ciist_smalltitle_title);
            icon_color = array.getColor(R.styleable.CiistSmallTitle_ciist_smalltitle_icon_color,-1);
            rightTxt = array.getString(R.styleable.CiistSmallTitle_ciist_smalltitle_right_txt);
            txtColor = array.getColor(R.styleable.CiistSmallTitle_ciist_smalltitle_txt_color,-1);
            rightbg_selector = array.getResourceId(R.styleable.
                    CiistSmallTitle_ciist_smalltitle_right_bg_selector,0);
            LayoutInflater inflater = LayoutInflater.from(context);
            initView(inflater);
        }finally {
            array.recycle();
        }
    }

    /**
     * get XML sorc
     * @param inflater
     */
    private void initView(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.small_titleview_layout,this,true);
        if (null != titleTxt){
            leftTv = (TextView) view.findViewById(R.id.small_title_txt);
            leftTv.setText(titleTxt);
            if (-1 != txtColor){
                leftTv.setTextColor(txtColor);
            }
        }
        if (-1 != icon_color){
            iconView = view.findViewById(R.id.small_title_icon);
            iconView.setBackgroundColor(icon_color);
        }
        if (null != rightTxt){
            rightTv = (TextView) view.findViewById(R.id.small_title_rightText);
            rightTv.setVisibility(VISIBLE);
            rightTv.setOnClickListener(this);
            rightTv.setText(rightTxt);
            if (-1 != txtColor){
                rightTv.setTextColor(txtColor);
            }

            if (0 != rightbg_selector){
                rightTv.setBackgroundResource(rightbg_selector);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (null != onClickListener){
            onClickListener.onClick(v);
        }
    }

    /*左边小标题*/
    public void setLeftText(String leftText){
        leftTv.setText(leftText);
    }

    /*右边文本*/
    public void setRightTxt(String rightTxt){
        rightTv.setText(rightTxt);
    }

    /*左边颜色资源*/
    public void setIconColor(int res){
        iconView.setBackgroundResource(res);
    }



}
