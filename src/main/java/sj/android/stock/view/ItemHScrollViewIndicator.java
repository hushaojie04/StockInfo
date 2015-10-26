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

import sj.android.stock.R;
import utils.BitmapUtils;

/**
 * Created by Administrator on 2015/10/26.
 */
public class ItemHScrollViewIndicator extends View {
    Bitmap backgroud;
    Bitmap foreground;
    Paint paint;
    float startX, endY;

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

//        NinePatch np = new NinePatch(bmp_9, bmp_9.getNinePatchChunk(), null);
//        Rect rect = new Rect(0, 0, getMeasuredWidth(), bmp_9.getHeight());
//        np.draw(canvas, rect);
        canvas.drawBitmap(BitmapUtils.resizeBitmap(backgroud, getMeasuredWidth(), getMeasuredHeight()), 0, 0, paint);
        if ((int) (endY - startX) > 0)
            canvas.drawBitmap(BitmapUtils.resizeBitmap(foreground, (int) (endY - startX), getMeasuredHeight()), startX, endY, paint);
    }

    public void work(float x1, float x2) {
        startX = x1;
        endY = x2;
    }

    public float getX0() {
        return startX;
    }

    public float getX1() {
        return endY;

    }
}
