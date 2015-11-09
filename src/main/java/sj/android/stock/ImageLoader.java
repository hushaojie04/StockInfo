package sj.android.stock;

import android.app.ActivityManager;
import android.content.AsyncQueryHandler;
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

import com.squareup.okhttp.internal.DiskLruCache;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import sj.utils.FileUtils;
import sj.utils.MD5Util;

/**
 * Created by Administrator on 2015/11/9.
 */
public class ImageLoader implements AsyncHandler.Callback, AsyncHandler.Work {
    private static MyApplication myapp;
    private static ImageLoader mImageLoader;
    private DiskLruCache mDiskLruCache;
    private LruCache<String, Bitmap> mMemoryCache;
    private static final String DISK_CACHE_SUBDIR = "thumbnails";
    private Stack<ImageRef> displayQueue = new Stack<ImageRef>();
    private Queue<ImageRef> requestQueue = new LinkedList<ImageRef>();
    private AsyncHandler mAsyncHandler;
    private boolean mImageLoaderIdle = true;

    private ImageLoader(Context context) {
        try {
            mDiskLruCache = DiskLruCache.open(FileUtils.getDiskCacheDir(context, DISK_CACHE_SUBDIR), getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memClass = memClass > 32 ? 32 : memClass;
        final int cacheSize = memClass * 1024 * 1024 / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        mAsyncHandler = AsyncHandler.getInstance();
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

    public static ImageLoader from(Context context) {
        if (myapp == null)
            myapp = (MyApplication) context.getApplicationContext();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(context);
        }
        return mImageLoader;
    }

    public void displayImage(ImageView imageView, String url, int width, int height) {
        String key = MD5Util.MD5(url);
        if (key == null) key = String.valueOf(key.hashCode());
        Bitmap bitmap = mMemoryCache.get(key);
        if (bitmap != null) {
            setImageBitmap(imageView, bitmap, true);
            return;
        }
        ImageRef ref = new ImageRef();
        ref.imageView = imageView;
        ref.url = url;
        ref.width = width;
        ref.height = height;
        ref.key = key;
        queueImage(ref);
    }

    /**
     * 入队，后进先出
     *
     * @param imageRef
     */
    public void queueImage(ImageRef imageRef) {

        //把在队列中的，放到队列最上面，优先显示
        Iterator<ImageRef> iterator = displayQueue.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().imageView == imageRef.imageView) {
                iterator.remove();
            }
        }

        // 添加请求
        displayQueue.push(imageRef);
        requestNext();
    }

    private void requestNext() {
        ImageRef ref = displayQueue.pop();
        if (ref != null) {
            requestQueue.add(ref);
        }
        if (mImageLoaderIdle && requestQueue.size() > 0) {
            mAsyncHandler.handle(this, this);
        }
    }

    @Override
    public void onCallback(Object object) {
        ImageRef imageRef = requestQueue.poll();
        if (object instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) object;
            if (bitmap != null) {
                setImageBitmap(imageRef.imageView, bitmap, true);
                mMemoryCache.put(imageRef.key, bitmap);
            }
        }
        if (mImageLoaderIdle)
            requestNext();
    }

    @Override
    public Object work() {
        Bitmap bitmap;
        ImageRef imageRef = requestQueue.peek();
        String url = imageRef.url;
        bitmap = readBitmapFromDirk(imageRef.key);
        if (bitmap != null) {
            return bitmap;
        }

        if (url == null) return null;
        // 如果本地url即读取sd相册图片，则直接读取，不用经过DiskCache
        if (url.toLowerCase().contains("dcim")) {
        } else {
            InputStream inputStream = load(url);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }

    private Bitmap readBitmapFromDirk(String key) {
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
//        HttpURLConnection urlConnection = null;
//        BufferedOutputStream out = null;
//        BufferedInputStream in = null;
//        try {
//            final URL url = new URL(urlString);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
//            out = new BufferedOutputStream(outputStream, 8 * 1024);
//            int b;
//            while ((b = in.read()) != -1) {
//                out.write(b);
//            }
//            return true;
//        } catch (final IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        }
        return false;
    }

    /**
     * 添加图片显示渐现动画
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap,
                                boolean isTran) {
        if (isTran) {
            final TransitionDrawable td = new TransitionDrawable(
                    new Drawable[]{
                            new ColorDrawable(myapp.getResources().getColor(android.R.color.transparent)),
                            new BitmapDrawable(bitmap)});
            td.setCrossFadeEnabled(true);
            imageView.setImageDrawable(td);
            td.startTransition(300);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * @param url
     * @return
     */
    private InputStream load(String url) {
        try {
            HttpGet method = new HttpGet(url);
            HttpResponse response = myapp.getHttpClient().execute(method);
            HttpEntity entity = response.getEntity();
            return entity.getContent();

        } catch (Exception e) {
            return null;
        }

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
        filePath.append(myapp.getCacheDir().toString()).append('/');
        // 图片文件名
        filePath.append(MD5Util.MD5(url)).append(url.substring(index));

        return filePath.toString();
    }

    class ImageRef {
        ImageView imageView;
        String url;
        String key;
        int width;
        int height;
    }

}
