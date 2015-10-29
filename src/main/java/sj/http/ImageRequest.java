package sj.http;

import android.graphics.drawable.Drawable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/5/5.
 */
public class ImageRequest extends ObjectRequest<Drawable> {
    Drawable mDrawable;

    public ImageRequest(int method, String url) {
        super(method, url);
    }

    @Override
    Response<Drawable> parseNetworkResponse() {
        Response response = new Response();
        response.result = mDrawable;
        return response;
    }

    @Override
    public void postRequest() {
        try {
            HttpResponse mHttpResponse = mHttpClientConn.performRequest(this,true);
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            InputStream inputStream = mHttpEntity.getContent();
            mDrawable = Drawable.createFromStream(
                    inputStream, this.getClass() + "image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ImageRequest "+mURL;
    }
}
