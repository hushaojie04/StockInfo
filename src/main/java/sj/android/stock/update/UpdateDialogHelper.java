package sj.android.stock.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sj.android.stock.MainActivity;
import sj.android.stock.R;
import sj.utils.AutoUpdate;
import sj.utils.LogUtils;
import sj.utils.StringUtils;

/**
 * Created by Administrator on 2015/12/4.
 */
public class UpdateDialogHelper {
    Context mContext;
    String versionCode;
    String versionName;
    String apk_url;
    public static final String TAG = "UpdateDialogHelper";

    public UpdateDialogHelper(Context context) {
        mContext = context;
        LogUtils.D(TAG, "UpdateDialogHelper ");
    }

    public void loadApkInfo() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String url = "http://www.qushiw.com:8080/WebServerForLOL/apk/version.json";
                LogUtils.D(TAG, "doInBackground ");
                return downloadUrlToStream(url);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                LogUtils.D(TAG, "onPostExecute ");
                if (result == null) return;
                LogUtils.D(TAG, "onPostExecute ");
                try {
                    JSONObject object = new JSONObject(result);
                    versionName = object.getString("versionName");
                    apk_url = object.getString("comment");

                    AutoUpdate mAutoUpdate = new AutoUpdate();
                    String currentVername = mAutoUpdate.getVerName(mContext);
                    if (versionName.compareToIgnoreCase(currentVername) > 0) {
                        show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private String downloadUrlToStream(String urlString) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        LogUtils.D(TAG, "downloadUrlToStream");
        String mVersionInfo = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            mVersionInfo = StringUtils.InputStreamTOString(inputStream, "UTF-8");
        } catch (final Exception e) {
            e.printStackTrace();
            LogUtils.D(TAG, "IOException " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mVersionInfo;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);  //先得到构造器
        builder.setTitle(mContext.getResources().getString(R.string.promt)); //设置标题
        builder.setMessage(mContext.getResources().getString(R.string.isupdate)); //设置内容
        builder.setPositiveButton(mContext.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                downApk(apk_url);
            }
        });
        builder.setNegativeButton(mContext.getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void downApk(String url) {
        if (url == null || url.equals("")) return;
        Intent intent = new Intent(mContext, UpdataService.class);
        Bundle bundle = new Bundle();
        bundle.putString("apk_path", url);
        bundle.putString("apk_name", mContext.getResources().getString(R.string.app_name));
        intent.putExtras(bundle);
        LogUtils.D(TAG, "downApk:" +url);
        mContext.startService(intent);
    }
}
