package sj.android.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/7.
 */
public class ScreenManager {
    public static ScreenManager mScreenManager = null;

    private ScreenManager() {
    }

    public static synchronized ScreenManager getInstance() {
        if (mScreenManager == null) {
            synchronized (ScreenManager.class) {
                if (mScreenManager == null) {
                    mScreenManager = new ScreenManager();
                }
            }
        }
        return mScreenManager;
    }

    public void addActivityHandle(ActivityHandle handle) {
        mActivityHandles.put(handle.hashCode(), handle);
    }

    public void removeActivityHandle(ActivityHandle handle) {
        mActivityHandles.remove(handle.hashCode());
    }

    public boolean handle() {
        Iterator<ActivityHandle> iterator = mActivityHandles.values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onBack()) {
                return true;
            } else {

            }
        }
        return false;
    }

    private Map<Integer, ActivityHandle> mActivityHandles = new HashMap<Integer, ActivityHandle>();
}
