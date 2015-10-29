package sj.utils;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2015/4/3.
 */
public class HttpUtils {
    private static HttpUtils mHttpUtils = null;
    AsyncTask mAsyncTask = null;

    private HttpUtils() {
    }


    public static synchronized HttpUtils getInstance() {
        if (mHttpUtils == null) mHttpUtils = new HttpUtils();
        return mHttpUtils;
    }
    public HttpEntity getData(String url)
    {
        try
        {
            ExecutorService exec = Executors.newCachedThreadPool();
            Future<HttpEntity> rusult = exec.submit(new GetData(url));
            return rusult.get();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }
    public void sendData(String url,String key,String value)
    {
        ExecutorService exec = Executors.newCachedThreadPool();
        Future<Object> rusult = exec.submit(new PostData());    }
    class GetData implements Callable<HttpEntity>
    {
        List<Object> mlist = new ArrayList<Object>();
        public GetData(String... params)
        {
            int len = params.length;
            for(int i = 0;i<len;i++)
            {
                mlist.add(params[i]);
            }
        }
        @Override
        public HttpEntity call() throws Exception {
            HttpGet httpRequest = new HttpGet(mlist.get(0).toString());
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    return httpResponse.getEntity();
                } else return null;
            } catch (ClientProtocolException e) {
            } catch (IOException e) {

            }
            finally {
                httpClient.getConnectionManager().shutdown();
            }
            return null;
        }
    }
    class PostData implements Callable<Object>
    {
        List<Object> mlist = new ArrayList<Object>();
        public PostData(String... params)
        {
            int len = params.length;
            for(int i = 0;i<len;i++)
            {
                mlist.add(params[i]);
            }
        }
        @Override
        public Object call() throws Exception {
            HttpPost httpRequest = new HttpPost(mlist.get(0).toString());
            HttpClient httpClient = new DefaultHttpClient();
            List<NameValuePair> lparams = new ArrayList<NameValuePair>();
            NameValuePair pair = new BasicNameValuePair(mlist.get(1).toString(),mlist.get(2).toString());
            lparams.add(pair);
            try {
                HttpEntity httpEntity = new UrlEncodedFormEntity(lparams,"gb2312");
                httpRequest.setEntity(httpEntity);
                HttpResponse httpResponse=httpClient.execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK)
                {
//                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    return httpResponse.getEntity();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch(Exception e)
            {
                e.printStackTrace();
            }finally {
                httpClient.getConnectionManager().shutdown();
            }
            return null;
        }
    }


}
