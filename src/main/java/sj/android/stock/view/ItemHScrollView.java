package sj.android.stock.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sj.utils.LogUtils;


/**
 * Created by Administrator on 2015/10/26.
 */
public class ItemHScrollView extends HorizontalScrollView {
    Adapter adapter;
    ItemHScrollViewIndicator Row;
    OnItemClickListener mOnItemClickListener;
    int currentPosition;
    float positionOffset;

    public ItemHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        Row = new ItemHScrollViewIndicator(context);
        Row.setBackgroundColor(0xffffffff);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        Row.setLayoutParams(layoutParams);
        Row.setItemPadding(30, 30);
        addView(Row);
    }

//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//        LogUtils.D("onScrollChanged " + l + " " + t + " " + oldl + " " + oldt);
//        int left = l - t;
//        indicator.work(Row.getChildAt(currentPosition).getX() - getScrollX(), Row.getChildAt(currentPosition).getX() - getScrollX() + Row.getChildAt(currentPosition).getMeasuredWidth());
//    }

    boolean ischange = false;

    public void setSelectedItem(int position) {
        LogUtils.D("setSelectedItem " + position);
        currentPosition = position;
        scrollToItem(position);
        doIndicator(position);
        ischange = true;
        for (int i = 0; i < Row.getChildCount(); i++) {
            TextView item = (TextView) Row.getChildAt(i);
            if (i == position) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
    }

    public void onPageScrolled(int position, float percent, boolean toRight) {
        if (!isFromUser && percent != 0) {
//            doIndicator(position, percent, toRight);
            scrollByItem(position, percent, toRight);
        }
    }

    private void doIndicator(int position) {
        Row.work(Row.getChildAt(position).getX(), Row.getChildAt(position).getX() + Row.getChildAt(position).getMeasuredWidth());
        Row.invalidate();
    }

    float lastScrollX = 0;
    float curVisiblePosision;
    boolean isMoving;
    boolean isScrolling = false;
    boolean isMovingB;

    //    private void doIndicator(int position, float percent, boolean toRight) {
//
//
//        indicator.work(xxx, xxx + width_);
//        indicator.invalidate();
//    }
    boolean isFromUser = false;

    public void setAdpater(Adapter adapter) {
        this.adapter = adapter;
        for (int i = 0; i < adapter.getCount(); i++) {
            final View item = adapter.getView(Row, i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) item.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(10, 0, 10, 0);
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                item.setLayoutParams(layoutParams);
            }
            item.requestLayout();
            item.setPadding(30, 0, 30, 0);
            final int position = i;
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(Row, item, position);
                    }
                    isFromUser = true;
                    setSelectedItem(position);
                }
            });
            Row.addView(item);
        }
        Row.postDelayed(new Runnable() {
            @Override
            public void run() {
//                setSelectedItem(0);
                doIndicator(0);
            }
        }, 1000);
    }

    public void setPositionOffset(float offset) {
        positionOffset = offset;
    }

    private void scrollToItem(int position) {
//        LogUtils.D("scrollToItem " + position);
        float x = Row.getChildAt(position).getX();
        scrollTo((int) (x - positionOffset), 0);
    }


    private float nextX, lastX, currentX;
    private float scrollX;

    private void scrollByItem(int position, float percent, boolean toRight) {
        float delta = 0;
        float x = 0;
        if (toRight) {//
            if (!ischange && isup) {
                LogUtils.D("toRight " + "  middel");
                delta = (currentX - scrollX - positionOffset) * (1 - percent);
            } else if (position < adapter.getCount()) {
                delta = (lastX - scrollX - positionOffset) * (1 - percent);
            }
            x = scrollX + delta;
        } else {
            if (!ischange && isup) {
                LogUtils.D("toLeft " + "  middel");
                delta = (currentX - scrollX - positionOffset) * percent;
            } else if (position >= 0 && position != currentPosition) {
//                delta = (Row.getChildAt(position + 1).getMeasuredWidth()) * percent;
                delta = (nextX - scrollX - positionOffset) * percent;
            }
            x = scrollX + delta;
        }
        if (x != 0) {
            scrollTo((int) (x), 0);
        }
    }


    public void touchDown() {
        isFromUser = false;
        isMoving = false;
        isScrolling = false;
        isMovingB = false;
        if (adapter == null) return;
        if (currentPosition < adapter.getCount() - 1) {
            nextX = Row.getChildAt(currentPosition + 1).getX();
        }
        if (currentPosition > 0) {
            lastX = Row.getChildAt(currentPosition - 1).getX();
        }
        currentX = Row.getChildAt(currentPosition).getX();
        scrollX = getScrollX();
        lastScrollX = 0;
        curVisiblePosision = currentX - scrollX;
        isdown = true;
        isup = false;
        ischange = false;
    }

    boolean isup;
    boolean isdown;

    public void touchUp() {
        isup = true;
        isdown = false;
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
