package sj.android.stock.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import utils.LogUtils;

/**
 * Created by Administrator on 2015/10/26.
 */
public class ItemHScrollView extends HorizontalScrollView {
    ItemHScrollViewIndicator indicator;
    Adapter adapter;
    LinearLayout Row;
    OnItemClickListener mOnItemClickListener;
    int currentPosition;
    float positionOffset;

    public ItemHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Row = new LinearLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        Row.setLayoutParams(layoutParams);
        addView(Row);
    }

    public void setSelectedItem(int position) {
        currentPosition = position;
        scrollToItem(position);
    }

    public void setItemHScrollViewIndicator(ItemHScrollViewIndicator indicator) {
        this.indicator = indicator;
    }

    public void onPageScrolled(int position, float percent, boolean toRight) {

//        View item = Row.getChildAt(position);
//        if (item.getX() - item.getScrollX() != indicator.getX0()) {
//            indicator.work();
//        }
        if (percent != 0)
            scrollByItem(position, percent, toRight);
    }

    public void setAdpater(Adapter adapter) {
        this.adapter = adapter;
        for (int i = 0; i < adapter.getCount(); i++) {
            final View item = adapter.getView(Row, i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) item.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(10, 0, 10, 0);
                item.setLayoutParams(layoutParams);
            }
            item.requestLayout();
            item.setPadding(10, 0, 10, 0);
            final int position = i;
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(Row, item, position);
                    }
                    setSelectedItem(position);
                }
            });
            Row.addView(item);
        }
    }

    public void setPositionOffset(float offset) {
        positionOffset = offset;
    }

    private void scrollToItem(int position) {
        LogUtils.D("scrollToItem " + position);
        float x = Row.getChildAt(position).getX();
//        scrollTo((int) (x - positionOffset), 0);
    }


    private float nextX, lastX;
    private float scrollX;

    private void scrollByItem(int position, float percent, boolean toRight) {
        float delta;
        float x = 0;
//        LogUtils.D("position " + position);
//        LogUtils.D("onPageScrolled " + x + " " + scrollByX + " " + delta);
        if (toRight) {//ÊÖÖ¸»¬ÏòÓÒ
            if (position < adapter.getCount()) {
                delta = (Row.getChildAt(position + 1).getMeasuredWidth()) * percent;
                delta = -(lastX - scrollX - positionOffset) * (1 - percent);
                x = scrollX - delta;
            }
        } else {
            if (position > 0) {
                delta = (Row.getChildAt(position + 1).getMeasuredWidth()) * percent;
                delta = (nextX - scrollX - positionOffset) * percent;
                x = scrollX + delta;
            }
        }
        if (x != 0) {
            scrollTo((int) (x), 0);
        }
    }

    public void touchDown() {
        if (currentPosition < adapter.getCount()) {
            nextX = Row.getChildAt(currentPosition + 1).getX();
        }
        if (currentPosition > 0) {
            lastX = Row.getChildAt(currentPosition - 1).getX();
        }
        scrollX = getScrollX();
        LogUtils.D("currentPosition " + currentPosition);
        LogUtils.D("touchDown " + nextX + " " + lastX);
        LogUtils.D("scrollX " + scrollX);

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface Adapter {
        public int getCount();

        public Object getItem(int position);

        View getView(ViewGroup parent, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(ViewGroup parent, View view, int position);
    }
}
