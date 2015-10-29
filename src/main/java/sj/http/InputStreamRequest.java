package sj.http;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/5/5.
 */
public class InputStreamRequest extends ObjectRequest<InputStream> {
    InputStream object;

    public InputStreamRequest(int method, String url) {
        super(method, url);
    }

    @Override
    Response<InputStream> parseNetworkResponse() {
        Response response = new Response();
        response.result = object;
        return response;
    }

    @Override
    public void postRequest() {
        try {
            HttpResponse mHttpResponse = mHttpClientConn.performRequest(this, true);
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            object = mHttpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.D("########## postRequest IOException " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ImageRequest " + mURL;
    }
}
