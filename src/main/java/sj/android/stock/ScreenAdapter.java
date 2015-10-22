package sj.android.stock;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Administrator on 2015/6/11.
 */
public class ScreenAdapter {
    public static int screenWidth;
    public int screenHeight;
    private float screenRatio;
    //标题栏占屏幕的比例
    private float headHRatio = 0.5f / 7.8f;
    //底部TAB按键栏占屏幕的比例
    private float tailHRatio = 0.6f / 7.8f;
    //中间内容占屏幕的比例
    private static ScreenAdapter mScreenAdapter;
    private Context context;

    private ScreenAdapter(Activity context) {
        this.context = context;
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight() - statusBarHeight;
        screenRatio = screenWidth / screenHeight;
    }

    public static ScreenAdapter getInstance(Activity context) {
        if (mScreenAdapter == null)
            mScreenAdapter = new ScreenAdapter(context);
        return mScreenAdapter;
    }

    public int getHeadHeight() {
        return (int) (headHRatio * screenHeight);
    }

    public int getTailHeight() {
        int childWidth = screenWidth / 3;
        return (int) (tailHRatio * screenHeight);
    }

}
