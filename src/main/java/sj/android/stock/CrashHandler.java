package sj.android.stock;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/12/16.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtils.D("CrashHandler","uncaughtException "+ex.getMessage());
    }
}
