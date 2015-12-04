package sj.android.stock.update;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sj.android.stock.R;
import sj.android.stock.ToastManager;
import sj.utils.LogUtils;

/**
 * 检测安装更新文件的助手类
 *
 * @author G.Y.Y
 */
public class UpdataService extends Service {

    String apk_url, apk_name;
    String apk_path;
    MyAsyncTask mMyAsyncTask;
    public final static String TAG = "UpdataService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.D(TAG, "updateService onStartCommand!!!" + getAndroidOSVersion());
        if (intent != null) {
            LogUtils.D("updateService onStartCommand!!!" + getAndroidOSVersion());
            if (apk_path != null) {
                update(UpdataService.this, apk_path);
            } else {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    // 调用下载
                    apk_url = bundle.getString("apk_path");
                    apk_name = bundle.getString("apk_name");
                    LogUtils.D("apk_name=" + apk_name);

                    if (apk_name != null && !apk_name.equals("")) {
                        if (mMyAsyncTask != null) {
                            mMyAsyncTask.interrupt();
                        }
                        mMyAsyncTask = new MyAsyncTask();
                        mMyAsyncTask.execute(apk_url, apk_name);
                    }

                }
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public static int getAndroidOSVersion() {
        int osVersion;
        try {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            osVersion = 0;
        }

        return osVersion;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyAsyncTask extends AsyncTask<String, Float, String> {
        HttpURLConnection mHttpURLConnection;
        boolean isInterrupt = false;

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                File file = new File(Environment.getExternalStorageDirectory(), params[1]);
                LogUtils.D("Environment.getExternalStorageDirectory()= " + Environment.getExternalStorageDirectory().getPath());

                LogUtils.D("file.exists " + file.exists());
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        ToastManager.getManager().postShowToast(getResources().getString(R.string.cant_find_sd));
                        return null;
                    }
                }
                URL url = new URL(params[0]);
                mHttpURLConnection = (HttpURLConnection) url.openConnection();
                mHttpURLConnection.setConnectTimeout(30000);
                mHttpURLConnection.setReadTimeout(30000);
                mHttpURLConnection.setDoInput(true);
                mHttpURLConnection.setUseCaches(false);
                mHttpURLConnection.setDoInput(true);
                mHttpURLConnection.connect();
                int len = mHttpURLConnection.getContentLength();
                InputStream inputStream = mHttpURLConnection.getInputStream();
                FileOutputStream fileOutputStream = null;
                if (inputStream != null) {
                    result = file.toString();
                    fileOutputStream = new FileOutputStream(file);
                    byte[] b = new byte[1024];
                    int charb = -1;
                    int count = 0;
                    while ((charb = inputStream.read(b)) != -1) {
                        fileOutputStream.write(b, 0, charb);
                        count += charb;
                        float progress = (float) count / (float) len;
                        publishProgress(progress);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (MalformedURLException e) {

            } catch (IOException e) {

            } finally {
                if (mHttpURLConnection != null)
                    mHttpURLConnection.disconnect();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            LogUtils.D("progress:" + values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
            } else {
                if (!isInterrupt) {
                    apk_path = s;
                    update(UpdataService.this, s);
                }
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isInterrupt = false;
        }

        public void interrupt() {
            isInterrupt = true;
            if (mHttpURLConnection != null) {
                mHttpURLConnection.disconnect();
            }
        }
    }

    public void update(Context context, String path) {
        LogUtils.D("update apk path=" + path);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {

        }
    }
}