package utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AutoUpdate {

    String newVerName = "";//新版本名称
    int newVerCode = -1;//新版本号
    ProgressDialog pd = null;
    public static String UPDATE_SERVERAPK = "ApkUpdateAndroid.apk";
    public static String packageName = "com.azt.cardeivce";

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("版本号获取异常", e.getMessage());
        }
        return verCode;
    }


    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("版本名称获取异常", e.getMessage());
        }
        return verName;
    }

//
//    public boolean getServerVer(String server_path) {
//        try {
//            URL url = new URL(server_path);
//            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
//            httpConnection.setDoInput(true);
//            httpConnection.setDoOutput(true);
//            httpConnection.setRequestMethod("GET");
//            httpConnection.connect();
//            InputStreamReader reader = new InputStreamReader(httpConnection.getInputStream());
//            BufferedReader bReader = new BufferedReader(reader);
//            String json = bReader.readLine();
//            JSONArray array = new JSONArray(json);
//            JSONObject jsonObj = array.getJSONObject(0);
//            LogUtils.D("json:" + jsonObj.toString());
////            newVerCode = Integer.parseInt(jsonObj.getString("verCode"));
////            newVerName = jsonObj.getString("verName");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }


    public void notNewVersionUpdate(Context context) {
        int verCode = this.getVerCode(context);
        String verName = this.getVerName(context);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本：");
        sb.append(verName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append("\n已是最新版本，无需更新");
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).create();
        dialog.show();
    }


    public void doNewVersionUpdate(final Context context, final InputStream is, String newVerName) {
        int verCode = this.getVerCode(context);
        String verName = this.getVerName(context);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本：");
        sb.append(verName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append(",发现版本：");
        sb.append(newVerName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append(",是否更新");
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        pd = new ProgressDialog(context);
                        pd.setTitle("正在下载");
                        pd.setMessage("请稍后。。。");
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        downFile(is);
                    }
                })
                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).create();
        //显示更新框
        dialog.show();
    }


    public void downFile(final InputStream is) {
        LogUtils.D("downFile");
        pd.show();
        new Thread() {
            public void run() {
                try {
                    FileOutputStream fileOutputStream = null;
                    LogUtils.D("is==null:" + (is == null));

                    if (is != null) {
                        File file = new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        int charb = -1;
                        int count = 0;
                        LogUtils.D("write start");
                        while ((charb = is.read(b)) != -1) {
                            fileOutputStream.write(b, 0, charb);
                            count++;
                        }
                        LogUtils.D("write end count" + count);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    down();
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    LogUtils.D("error:" + e.getMessage());
                } finally {
                }
            }
        }.start();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            pd.cancel();
            update(pd.getContext());
        }
    };


    public void down() {
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }.start();
    }


    public void update(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK))
                , "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}