package sj.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/5/27.
 */
public class StringUtils {
    public static byte[] hexStringToBytes(String hexString) {
        LogUtils.D("hexString=" + hexString);
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String bytesToString(byte[] b) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String temp = Integer.toHexString(b[i]);
            if (temp.length() > 2) {
                temp = temp.substring(temp.length() - 2, temp.length());
            } else if (temp.length() == 1) {
                temp = "0" + temp;
            }
            builder.append(temp);
        }
        return builder.toString();
    }

    public static String bytesToString2(byte[] b) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String temp = Integer.toHexString(b[i]);
            if (temp.length() > 2) {
                temp = temp.substring(temp.length() - 2, temp.length());
            }
            builder.append(temp);
        }
        return builder.toString();
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    public static boolean isValidPassword(String passrword) {
        String pattern = ("^[0-9a-zA-Z_]{6,16}$");
        return passrword.matches(pattern);
    }

    public static boolean isValidAccount(String account) {
        boolean is = true;
        if (account.contains("%") || account.contains(" ") || account.equals("")) {
            is = false;
        }
        return is;
    }

    public static boolean isValidMAC(String passrword) {
        String pattern = ("^[0-9a-fA-F]{14}$");
        return passrword.matches(pattern);
    }

    public static void hideInputMethod(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static byte[] subBytes(byte[] bytes, int index, int len) {
        byte[] temp = new byte[len];
        System.arraycopy(bytes, index, temp, 0, len);
        return temp;
    }

    public static String bytesToIntString(byte[] bytes, String separator) {
        StringBuilder builder = new StringBuilder();
        for (byte m : bytes) {
            builder.append(m + separator);
        }
        return builder.toString();
    }

    public static String bytesToAsciiString(byte[] bytes, String separator) {
        StringBuilder builder = new StringBuilder();
        for (byte m : bytes) {
            builder.append((char) m + separator);
        }
        return builder.toString();
    }

    public static String bytesToHexString(byte[] bytes, String separator) {
        StringBuilder builder = new StringBuilder();
        for (byte m : bytes) {
            builder.append(Integer.toHexString(m & 0xff) + separator);
        }
        return builder.toString();
    }

    public static int bytesToInt(byte[] bytes) {
        return Integer.valueOf(bytesToHexString(bytes, ""), 16);
    }

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;
    }

    public static boolean hasChinese(String string) {
        char[] s = string.toCharArray();
        for (char c : s) {
            if (isChinese(c))
                return true;
        }
        return false;
    }

    public static String getPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = tm.getDeviceId();//��ȡ�����豸Ψһ���
        String te1 = tm.getLine1Number();//��ȡ��������
        String imei = tm.getSimSerialNumber();//���SIM�������
        String imsi = tm.getSubscriberId();//�õ��û�Id
        return imei;
    }

    public static String parseTimestamp(long timestamp) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//定义格式，不显示毫秒
        Timestamp now = new Timestamp(timestamp * 1000);//获取系统当前时间
        String str = df.format(now);
        return str;
    }

    public static String InputStreamTOString(InputStream in, String encoding) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, 1024)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray(), encoding);
    }
}
