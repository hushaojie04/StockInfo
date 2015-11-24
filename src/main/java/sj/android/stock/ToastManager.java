package sj.android.stock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import sj.utils.LogUtils;


/**
 * Created by Administrator on 2015/7/13.
 */
public class ToastManager {
    public static ToastManager mToastManager = new ToastManager();

    private ToastManager() {

    }

    public static ToastManager getManager() {
        return mToastManager;
    }

    public void init(Context context) {
        mContext = context;
    }

    public static String checkCode = "";
    Toast mToast;
    TextView mTextView;
    View root;
    Context mContext;
    String content;
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showToast(content);
        }
    };
    private boolean isResume = false;

    public void onResume() {
        isResume = true;
    }

    public void onPause() {
        isResume = false;
        cancelToast();
    }


    public void postShowToast(String text) {
        content = text;
        mhandler.sendEmptyMessage(0);
    }

    public void showToast(int resource_id) {
        showToast(mContext.getResources().getString(resource_id));
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }


}
