package sj.android.stock;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/10/31.
 */
public class MURL {
    public static final String HOST = "http://192.168.1.181";
    private static final String BASE = "http://192.168.1.181:8080/JavaWebApp/action/read";
    public static final String SERVER_URL = "http://www.qushiw.com";

    public static String getURL(String cmd) {
        LogUtils.D("cmd " + cmd);
        return BASE + "?" + cmd;
    }

    public static String getURL() {
        return BASE;
    }
}
