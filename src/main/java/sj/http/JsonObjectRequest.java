package sj.http;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/4/20.
 */
public class JsonObjectRequest extends ObjectRequest<JSONObject> {
    JSONObject mJSONObject;

    public JsonObjectRequest(int method, String url) {
        super(method, url);
    }

    public void postRequest() {
        try {
            HttpResponse mHttpResponse = mHttpClientConn.performRequest(this,false);
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            mJSONObject = new JSONObject(EntityUtils.toString(mHttpEntity));
            response.result = mJSONObject;
        } catch (IOException e) {
            LogUtils.D("JsonObjectRequest error: " + e.getMessage());
            response.error.setDescription(e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            LogUtils.D("JSONException error: " + e.getMessage());
            response.error.setDescription(e.getMessage());
            e.printStackTrace();
        }

}

    @Override
    Response<JSONObject> parseNetworkResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "JsonObjectRequest " + mURL;
    }
}