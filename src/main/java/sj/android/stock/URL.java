package sj.android.stock;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/10/31.
 */
public class URL {
    private static final String BASE = "http://192.168.1.106:8080/JavaWebApp/action/read?";

    public static String getURL(String cmd) {
        LogUtils.D("cmd " + cmd);
        return BASE + cmd;
    }
}
