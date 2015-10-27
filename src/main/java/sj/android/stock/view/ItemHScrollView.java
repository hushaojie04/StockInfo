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
        setHorizontalScrollBarEnabled(false);
        Row = new LinearLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        Row.setLayoutParams(layoutParams);
        addView(Row);
    }

    public void setSelectedItem(int position) {
        LogUtils.D("setSelectedItem " + position);
        currentPosition = position;
//        scrollToItem(position);
//        doIndicator(position);
    }

    public void setItemHScrollViewIndicator(ItemHScrollViewIndicator indicator) {
        this.indicator = indicator;
        indicator.setItemPadding(30, 30);
    }

    public void onPageScrolled(int position, float percent, boolean toRight) {
        if (percent != 0) {
            doIndicator(position, percent, toRight);
            scrollByItem(position, percent, toRight);
        }
    }

    private void doIndicator(int position) {
        indicator.work(Row.getChildAt(position).getX() - getScrollX(), Row.getChildAt(position).getMeasuredWidth());
        indicator.invalidate();
    }

    float lastScrollX = 0;
    boolean isScrolling = false;
    float scroll_space = 0;
    float positionX = 0;

    private void doIndicator(int position, float percent, boolean toRight) {
//        LogUtils.D("#######position " + position + " " + currentPosition);
        float a0 = Row.getChildAt(position).getMeasuredWidth();
        float b0 = 0;
        float left = Row.getChildAt(position).getLeft();
        if (lastScrollX == 0) {
            lastScrollX = getScrollX();
            return;
        }
        float width_diff0;
        if (!toRight) {//
            if (position < adapter.getCount() - 1) {
                b0 = Row.getChildAt(position + 1).getMeasuredWidth();
                width_diff0 = b0 - a0;
                float width_ = a0 + width_diff0 * percent;
                if (getScrollX() > 0 && getScrollX() < Row.getMeasuredWidth() - getMeasuredWidth()) {
                    LogUtils.D("#######lastScrollX###############");
                    isScrolling = true;
                    indicator.work(positionOffset, positionOffset + width_);
                } else {
                    LogUtils.D("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    if (isScrolling) {
                        isScrolling = false;
                        positionX = left;
                        scroll_space = Row.getChildAt(position + 1).getLeft() - positionX;
                    }
                    float xxx = positionX + scroll_space * percent;
                    indicator.work(xxx, xxx + width_);
                }
            }
        } else {
            if (position > 0) {
                b0 = Row.getChildAt(position).getMeasuredWidth();
                width_diff0 = b0 - a0;
                float x = Row.getChildAt(position).getLeft() - getScrollX();
                float diff = left + (x - left) * (1 - percent);
                indicator.work(diff, diff + a0 + width_diff0 * (1 - percent));
            }
        }
        lastScrollX = getScrollX();
        indicator.invalidate();
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
            item.setPadding(30, 0, 30, 0);
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
        float delta;
        float x = 0;

//        LogUtils.D("scrollByItem " + position);
//        LogUtils.D("onPageScrolled " + x + " " + scrollByX + " " + delta);
        if (toRight) {//
            if (position < adapter.getCount()) {
                delta = -(lastX - scrollX - positionOffset) * (1 - percent);
                x = scrollX - delta;
            }
        } else {
            if (position >= 0) {
//                delta = (Row.getChildAt(position + 1).getMeasuredWidth()) * percent;
                delta = (nextX - scrollX - positionOffset) * percent;
                x = scrollX + delta;
//                LogUtils.D("scrollX " + scrollX+" "+delta);
            }
        }
        if (x != 0) {
            scrollTo((int) (x), 0);
        }
    }

    public void touchDown() {
        LogUtils.D("##############touchDown#############");

        if (currentPosition < adapter.getCount() - 1) {
            nextX = Row.getChildAt(currentPosition + 1).getX();
        }
        if (currentPosition > 0) {
            lastX = Row.getChildAt(currentPosition - 1).getX();
        }
        currentX = Row.getChildAt(currentPosition).getX();
        scrollX = getScrollX();
//        LogUtils.D("currentPosition " + currentPosition);
//        LogUtils.D("touchDown " + nextX + " " + lastX);
//        LogUtils.D("scrollX " + scrollX);


        lastScrollX = 0;
        isScrolling = false;
        scroll_space = 0;
        positionX = 0;
    }

    boolean isup;

    public void touchUp() {
        isup = true;
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
