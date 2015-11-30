package sj.android.stock;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/10/31.
 */
public class MURL {
//        public static final String HOST = "http://192.168.1.181";
//    private static final String BASE = "http://192.168.1.181:8080/JavaWebApp/action";
    public static final String HOST = "http://www.qushiw.com";
    private static final String BASE = "http://www.qushiw.com:8080/WebServerForLOL/action";
    public static final String SERVER_URL = "http://www.qushiw.com";
    public static final String YOUKU_PALYVIDEO = "http://player.youku.com/embed/XXXXXXXX==?";
    public static final String QQ_PALYVIDEO = "http://cache.tv.qq.com/qqplayerout.swf?vid=XXXXXXXX";

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
