package sj.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/9.
 */
public class FileUtils {
    public static Context mContext;
    private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
    private final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签
    private final static String regxpForImaTagSrcAttrib = "src=\"(\\s|\\S)*?\""; // 找出IMG标签的SRC属性

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



    //src=\"/uploads/allimg/150521/101H9C32-0.png\"
    public static List getImgStr(String htmlStr) {
        Pattern p_image;
        Matcher m_image;
        List pics = new ArrayList();
        p_image = Pattern.compile(regxpForImgTag, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            LogUtils.D("hello:" + m_image.group());
            String src = m_image.group();
            Matcher m = Pattern.compile(regxpForImaTagSrcAttrib).matcher(src);
            while (m.find()) {
                pics.add(m.group());
            }
        }
        return pics;
    }
}
