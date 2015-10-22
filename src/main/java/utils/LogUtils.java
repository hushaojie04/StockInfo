package utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/4/3.
 */
public class LogUtils {
    private static boolean isEnableDebug = true;
    private static final String LOG = "log";
    public  static final boolean  TEST = false;
    public static void D(String tag, String msg) {
        if (isEnableDebug)
            Log.d(tag, msg);
    }

    public static void E(String tag, String msg) {
        if (isEnableDebug)
            Log.e(tag, msg);
    }

    public static void E(String msg) {
        if (isEnableDebug)
            Log.d(LOG, msg);
    }

    public static void D(String msg) {
        if (isEnableDebug)
            Log.d(LOG, msg);
    }

    public static String D(byte[] b) {
        StringBuilder builder = new StringBuilder();
        for (byte m : b) {
            builder.append(Integer.toHexString(m) + " ");
        }
        LogUtils.D(builder.toString());
        return builder.toString();
    }

    public static void D(long[] b) {
        StringBuilder builder = new StringBuilder();
        for (long m : b) {
            builder.append(Long.toHexString(m) + " ");
//            builder.append(m + " ");
        }

        LogUtils.D(builder.toString());
    }
}
