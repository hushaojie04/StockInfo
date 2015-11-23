package sj.android.stock;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/6/8.
 */
public class AppPreference {
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private static AppPreference mAppPreference = new AppPreference();
    private static final String PREFEREN_SHORT = "app_preference_short";
    private static final String PREFEREN_LONG = "app_preference_long";

    public void init(Context context) {
        mContext = context;
    }

    private void init_short() {
        mSharedPreferences = mContext.getSharedPreferences(PREFEREN_SHORT,
                Context.MODE_PRIVATE);
    }

    private void init_long() {
        mSharedPreferences = mContext.getSharedPreferences(PREFEREN_LONG,
                Context.MODE_PRIVATE);
    }

    private AppPreference() {

    }

    public static AppPreference getInstance() {
        return mAppPreference;
    }

    public void set(String type, String key, String value) {
        if (type.equals(PREFEREN_LONG)) {
            init_long();
        } else {
            init_short();
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void set(String type, String key, int value) {
        if (type.equals(PREFEREN_LONG)) {
            init_long();
        } else {
            init_short();
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void set(String type, String key, Boolean value) {
        if (type.equals(PREFEREN_LONG)) {
            init_long();
        } else {
            init_short();
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String get(String type, String key) {
        if (type.equals(PREFEREN_LONG)) {
            init_long();
        } else {
            init_short();
        }
        String value = mSharedPreferences.getString(key, "");
        return value;
    }

    public Boolean get(String type, String key, Boolean bl) {
        if (type.equals(PREFEREN_LONG)) {
            init_long();
        } else {
            init_short();
        }
        Boolean value = mSharedPreferences.getBoolean(key, bl);
        return value;
    }

    public int get(String type, String key, int defaultValue) {
        if (type.equals(PREFEREN_LONG)) {
            init_long();
        } else {
            init_short();
        }
        int value = mSharedPreferences.getInt(key, defaultValue);
        return value;
    }

    public void saveAccountAndPassWord(String account, String password) {
        init_short();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (editor == null) {
            return;
        }
        if (account != null)
            editor.putString("account", account);
        if (password != null)
            editor.putString("password", password);
        editor.commit();
    }

    public void saveTheme(int theme) {
        set(PREFEREN_LONG, "theme", theme);
    }


    public void saveAuthority(int authority) {
        set(PREFEREN_SHORT, "authority", authority);
    }

    public String getAccount() {
        return get(PREFEREN_SHORT, "account");
    }

    public String getSimNumber() {
        return get(PREFEREN_SHORT, "sim_number");
    }

    public void saveSimNumber(String sim_number) {
        set(PREFEREN_SHORT, "sim_number", sim_number);
    }

    public void setWelcome(boolean isNeedWelcome) {
        set(PREFEREN_LONG, "welcome_show", isNeedWelcome);
    }

    public boolean getWelcome() {
        return get(PREFEREN_LONG, "welcome_show", true);
    }

    public String getPassword() {
        return get(PREFEREN_SHORT, "password");
    }

    public void clearShort() {
        init_short();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void clearLong() {
        init_long();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void clearAll() {
        clearShort();
        clearLong();
    }
}
