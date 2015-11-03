package sj.http;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/4/20.
 */
public class JsonArrayRequest extends ObjectRequest<JSONArray> {
    JSONArray mJSONArray;

    public JsonArrayRequest(int method, String url) {
        super(method, url);
    }

    public void postRequest() {
        try {
            HttpResponse mHttpResponse = mHttpClientConn.performRequest(this, false);
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            mJSONArray = new JSONArray(EntityUtils.toString(mHttpEntity, "GBK"));
            response.result = mJSONArray;
//            mHttpEntity.consumeContent();
        } catch (IOException e) {
            LogUtils.D(getId() + " JsonObjectRequest error: " + e.getMessage());
            response.error.setDescription(e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            LogUtils.D("JSONException error: " + e.getMessage());
            response.error.setDescription(e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            LogUtils.D("#####################NullPointerException error: " + e.getMessage());
            response.error.setDescription(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    Response<JSONArray> parseNetworkResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "JsonObjectRequest " + mURL;
    }
}