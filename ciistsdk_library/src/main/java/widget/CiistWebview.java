package widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 具有滑动监听的webview
 * Created by xieke on 2016/5/10 0010.
 */
public class CiistWebview extends WebView{

    public ScrollInterface mScrollInterface;
    public CiistWebview(Context context) {
        super(context);
    }

    public CiistWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CiistWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollInterface != null){
            mScrollInterface.onSChanged(l, t, oldl, oldt);
        }

    }

    public void setOnCustomScroolChangeListener(ScrollInterface scrollInterface) {

        this.mScrollInterface = scrollInterface;

    }

    public interface ScrollInterface {

        public void onSChanged(int l, int t, int oldl, int oldt);

    }
}
