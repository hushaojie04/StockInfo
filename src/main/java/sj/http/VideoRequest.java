package sj.http;

import android.content.Context;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/5/5.
 */
public class VideoRequest extends ObjectRequest<Object> {
    Object object;
    Context mContext;

    public VideoRequest(Context context, int method, String url) {
        super(method, url);
        mContext = context;
    }

    @Override
    Response<Object> parseNetworkResponse() {
        Response response = new Response();
        response.result = object;
        return response;
    }
    @Override
    public void postRequest() {
        LogUtils.D("##########VideoRequest postRequest");
        try {
            HttpResponse mHttpResponse = mHttpClientConn.performRequest(this,false);
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            InputStream inputStream = mHttpEntity.getContent();
            LogUtils.D("##########VideoRequest postRequest   " + mHttpEntity.getContentLength());
            downFile("/sdcard/android_tools/test.mp4",mHttpEntity.getContentLength(),inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.D("########## postRequest IOException " + e.getMessage());
        }
    }
    public void downFile(String path, long len, InputStream input)
    {
        LogUtils.D("!!!!!!!!!!!!!!!downFile!!!!!!!!!!!!!!!!!");

        byte[] b = new byte[1024];
        //读取网络文件,写入指定的文件中
        int nStartPos = 0;
        int nRead = 0;
        RandomAccessFile oSavedFile = null;
        try {
            oSavedFile = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.D(" ##########SaveFileUtils FileNotFoundException "+e.getMessage());
        }

        try {
            while ((nRead = input.read(b, 0, 1024)) > 0
                    && nStartPos < len) {
                oSavedFile.write(b, 0, nRead);
                nStartPos += nRead;
                if (mBufferUpdating != null) {
                    mBufferUpdating.onUpdate(nStartPos,len);
                }
            }
            oSavedFile.close();;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.D(" ##########SaveFileUtils IOException "+e.getMessage());
        }
    }
    @Override
    public String toString() {
        return "ImageRequest " + mURL;
    }
}
