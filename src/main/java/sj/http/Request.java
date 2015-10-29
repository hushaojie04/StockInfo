package sj.http;

import android.content.Context;

import com.azt.cardeivce.utils.LogUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2015/4/17.
 */
public abstract class Request<T> implements Network {
    public static HttpClientConn mHttpClientConn;
    public Response response = new Response();
    public static void clear() {
        mHttpClientConn = null;
    }
    public static Context mContext;
    /**
     * Supported request methods.
     */
    public interface Method {
        int DEPRECATED_GET_OR_POST = -1;
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    /**
     * 请求URL
     */
    protected String mURL;
    /**
     * 请求方式
     */
    private int mMethod;
    private int mTimeoutMs;
    private static AtomicInteger newId = new AtomicInteger(0);
    private int id;
    private Object object;
    public List<NameValuePair> params = new ArrayList<NameValuePair>();

    public Request(int method, String url) {
        if (mHttpClientConn == null)
            mHttpClientConn = new HttpClientConn();
        mURL = url;
        id = newId.getAndIncrement();
        LogUtils.D("id=" + id);
    }

    public String getURL() {
        return mURL;
    }

    public int getMethod() {
        return mMethod;
    }

    public int getId() {
        return id;
    }

    public void setTag(Object object) {
        this.object = object;
    }

    public Object getTag() {
        return object;
    }

    public void addParam(String key, String value) {
        NameValuePair pair = new BasicNameValuePair(key, value);
        params.add(pair);
    }

    //    public int getTimeoutMs()
//    {
//        return mTimeoutMs;
//    }
    abstract void deliverResponse(Response<T> response);

    abstract Response<T> parseNetworkResponse();

}
