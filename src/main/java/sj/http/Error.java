package sj.http;

/**
 * Created by Administrator on 2015/7/13.
 */
public class Error {
    public static final int ERROR = 0;
    public static final int NOT_FIND_SERVER = 1;
    public static final int NO_JSON_FORMAT = 2;
    public static final int CONNECT_TIME_OUT = 3;
    public static final int CONNTECTION_TIME_OUT = 4;

    private int error_code = -1;
    private String description = "";

    public void setDescription(String message) {
        if (message == null) return;
        description = message;
        error_code = checkErrorCode(message);
    }

    public int getError_code() {
        return error_code;
    }

    private int checkErrorCode(String message) {
        //Unable to resolve host "cardevice01.chinacloudapp.cn": No address associated with hostname
        if (message.contains("Unable to resolve host")) {
            return Error.NOT_FIND_SERVER;
        } else if (message.contains("Connect") && message.contains("timed out")) {
            //Connect to /42.159.134.30:7001 timed out\
            return Error.CONNECT_TIME_OUT;
        } else if (message.contains("ETIMEDOUT") && message.contains("timed out")) {
            //Exception recvfrom failed: ETIMEDOUT (Connection timed out)
            return Error.CONNTECTION_TIME_OUT;
        } else
            return ERROR;
    }

}
