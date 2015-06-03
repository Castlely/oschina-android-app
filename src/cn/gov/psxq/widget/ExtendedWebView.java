package cn.gov.psxq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ExtendedWebView extends WebView {

    public ExtendedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public boolean canScrollHorizontally(int direction) {
        final int offset = computeHorizontalScrollOffset();
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0)
            return false;
        else
            return (direction < 0) ? (offset > 0) : (offset < range - 1);
    }
}
