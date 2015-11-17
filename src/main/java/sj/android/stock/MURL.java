package sj.android.stock;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/10/31.
 */
public class MURL {
    public static final String HOST = "http://192.168.1.181";
    private static final String BASE = "http://192.168.1.181:8080/JavaWebApp/action";
    public static final String SERVER_URL = "http://www.qushiw.com";

    public static String getReadURL(String cmd) {
        LogUtils.D("cmd " + cmd);
        return BASE + "/read" + "?" + cmd;
    }

    public static String getRefreshURL() {
        return BASE + "/refresh";
    }

    public static String getReadURL() {
        return BASE + "/read";
    }
}
