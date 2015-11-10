package sj.android.stock;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.LruCache;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import sj.utils.FileUtils;
import sj.utils.LogUtils;
import sj.utils.MD5Util;

/**
 * Created by Administrator on 2015/11/9.
 */
public class Cache {
    private static Cache mImageLoader;
    private DiskLruCache mDiskLruCache;
    private static final String DISK_CACHE_SUBDIR = "cache";
    private Context mContext;

    private Cache(Context context) {
        try {
            mContext = context;
            mDiskLruCache = DiskLruCache.open(FileUtils.getDiskCacheDir(context, DISK_CACHE_SUBDIR), getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static Cache from(Context context) {
        if (mImageLoader == null) {
            mImageLoader = new Cache(context);
        }
        return mImageLoader;
    }


    public String getData(String key) {
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i;
                while ((i = is.read()) != -1) {
                    baos.write(i);
                }
                String str = baos.toString();
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(String key, String data) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (writeToStream(data, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean writeToStream(String data, OutputStream outputStream) {
        try {
            outputStream.write(data.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据url生成缓存文件完整路径名
     *
     * @param url
     * @return
     */
    public String urlToFilePath(String url) {
        // 扩展名位置
        int index = url.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        StringBuilder filePath = new StringBuilder();
        // 图片存取路径
        filePath.append(mContext.getCacheDir().toString()).append('/');
        // 图片文件名
        filePath.append(MD5Util.MD5(url)).append(url.substring(index));
        return filePath.toString();
    }


}
