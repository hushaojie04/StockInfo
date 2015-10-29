package sj.android.stock.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2015/10/29.
 */
public class CatchTouchViewPager extends ViewPager {

    public CatchTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (oOnTouchListener != null) {
            oOnTouchListener.onTouch(this, ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    OnTouchListener oOnTouchListener;

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        oOnTouchListener = l;
    }
}
