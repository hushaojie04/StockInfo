package sj.http;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Iterator;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/4/17.
 */
public class HttpClientConn {
    protected final HttpClient mClient;
    private final static String HEADER_CONTENT_TYPE = "Content-Type";

    public HttpClientConn() {
        mClient = new DefaultHttpClient();
    }

    public void clear() {

    }

    public synchronized String performRequest(Request<?> request, boolean isSaveInputSteam) throws IOException {
        String response = null;
        synchronized (mClient) {
            HttpUriRequest httpRequest = createHttpRequest(request);
            HttpParams httpParams = httpRequest.getParams();
            // TODO: Reevaluate this connection timeout based on more wide-scale
            // data collection and possibly different for wifi vs. 3G.
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 5000);
            try {
                HttpResponse httpResponse = mClient.execute(httpRequest);
                response = EntityUtils.toString(httpResponse.getEntity(), "GBK");
            } catch (NullPointerException e) {
                LogUtils.D("NullPointerException " + e.getMessage());
            }
            httpRequest.abort();
        }
        return response;
    }

    /**
     * Creates the appropriate subclass of HttpUriRequest for passed in request.
     */
    /* protected */
    private HttpUriRequest createHttpRequest(Request<?> request) {
        LogUtils.D("request.getMethod()" + request.getMethod());
        switch (request.getMethod()) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                return null;
            case Request.Method.GET:
                return new HttpGet(getWholeURL(request));
            case Request.Method.DELETE:
                return null;
            case Request.Method.POST: {
                HttpPost postRequest = new HttpPost(request.getURL());
//                postRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                return postRequest;
            }
            case Request.Method.PUT:
            case Request.Method.HEAD:
            case Request.Method.OPTIONS:
            case Request.Method.TRACE:
            case Request.Method.PATCH:
                return null;
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }

    public String getWholeURL(Request<?> request) {
        Iterator<NameValuePair> iterator = request.params.iterator();
        StringBuilder builder = new StringBuilder();
        builder.append(request.getURL());
        LogUtils.D("GET:" + request.getId() + " " + request.getURL());

        int count = 0;
        while (iterator.hasNext()) {
            if (count++ != 0) {
                builder.append("&");
            } else {
                builder.append("?");
            }
            NameValuePair pair = iterator.next();
            builder.append(pair.getName());
            builder.append("=");
            builder.append(pair.getValue());
        }
        return builder.toString();
    }
}
