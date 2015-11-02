package sj.android.stock.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import sj.android.stock.R;
import sj.utils.BitmapUtils;


/**
 * Created by Administrator on 2015/10/26.
 */
public class ItemHScrollViewIndicator extends LinearLayout {
    Bitmap backgroud;
    Bitmap foreground;
    Paint paint;
    float startX, endX;
    int paddingLeft, paddingRight;

    public ItemHScrollViewIndicator(Context context) {
        this(context, null);
    }

    public ItemHScrollViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        backgroud = BitmapFactory.decodeResource(getResources(),
                R.drawable.bottom_line_gray);
        foreground = BitmapFactory.decodeResource(getResources(),
                R.drawable.bottom_line_blue);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(BitmapUtils.resizeBitmap(backgroud, getMeasuredWidth(), 15), 0, getHeight() - 15, paint);
        if ((int) Math.abs(endX - startX - paddingLeft - paddingRight) > 0) {
            canvas.drawBitmap(BitmapUtils.resizeBitmap(foreground, (int) Math.abs(endX - startX - paddingLeft - paddingRight), 15), startX + paddingLeft, getHeight() - 15, paint);
        }
    }

    public void work(float x1, float x2) {
        startX = x1;
        endX = x2;
    }

    public void setItemPadding(int paddingLeft, int paddingRight) {
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
    }

    public float getX0() {
        return startX;
    }

    public float getX1() {
        return endX;

    }
}
