package sj.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2015/11/9.
 */
public class FileUtils {
    public static Context mContext;

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (context == null) {
            context = mContext;
        }
        if (context == null) return null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
