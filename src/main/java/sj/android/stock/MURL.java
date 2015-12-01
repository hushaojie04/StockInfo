package sj.android.stock;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/10/31.
 */
public class MURL {
//    public static final String HOST = "http://192.168.1.181";
//    private static final String BASE = "http://192.168.1.181:8080/JavaWebApp/action";
        public static final String HOST = "http://www.qushiw.com";
    private static final String BASE = "http://www.qushiw.com:8080/WebServerForLOL/action";
    public static final String SERVER_URL = "http://www.qushiw.com";
    public static final String YOUKU_PALYVIDEO = "http://player.youku.com/embed/XXXXXXXX==?";
    //    public static final String QQ_PALYVIDEO = "http://cache.tv.qq.com/qqplayerout.swf?vid=XXXXXXXX";
    public static final String QQ_PALYVIDEO = "http://v.qq.com/iframe/player.html?vid=XXXXXXXX&tiny=0&auto=0";
    public static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; " +
            "U; Intel Mac OS X 10_6_3; en-us) AppleWebKit/533.16 (KHTML, " +
            "like Gecko) Version/5.0 Safari/533.16";

    public static final String IPHONE_USERAGENT = "Mozilla/5.0 (iPhone; U; " +
            "CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 " +
            "(KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7";
    public static final String IE8_USERAGENT = "User-Agent:Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)";
    public static final String IPAD_USERAGENT = "Mozilla/5.0 (iPad; U; " +
            "CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 " +
            "(KHTML, like Gecko) Version/4.0.4 Mobile/7B367 Safari/531.21.10";

    public static final String FROYO_USERAGENT = "Mozilla/5.0 (Linux; U; " +
            "Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 " +
            "(KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

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
