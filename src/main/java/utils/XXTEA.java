package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class XXTEA {
    static long DELTA = 0x9e3779b9;
    static long UIFILTER = Long.decode("0xffffffff");
    private static int MIN_LENGTH = 32;
    private static char SPECIAL_CHAR = '\0';

    static {
        DELTA = Long.decode("0x9e3779b9");
    }


    public static long[] encrypt(long[] data, long k[]) {
        int n = data.length;
        if (n < 1) {
            return data;
        }
        long z = data[n - 1], y = data[0], sum = 0, e;
        int p;
        int q;
        q = 6 + 52 / n;
        while (q-- > 0) {
            sum += DELTA;
            sum &= UIFILTER;

            e = (sum >> 2) & 3;
            for (p = 0; p < n - 1; p++) {
                y = data[p + 1];
                y &= UIFILTER;
                data[p + 1] = y;

                z = data[p] += ((z >> 5) ^ (y << 2))
                        + (((y >> 3) ^ (z << 4)) ^ (sum ^ y))
                        + (k[(int) ((p & 3) ^ e)] ^ z);
                z &= UIFILTER;
                data[p] = z;

            }
            y = data[0];
            y &= UIFILTER;
            data[0] = y;
            z = data[n - 1] += ((z >> 5) ^ (y << 2))
                    + (((y >> 3) ^ (z << 4)) ^ (sum ^ y))
                    + (k[(int) ((p & 3) ^ e)] ^ z);
            z &= UIFILTER;
            data[n - 1] = z;

        }
        return data;
    }

    public static long[] decrypt(long[] data, long k[]) {
        int n = data.length;
        if (n < 1) {
            return data;
        }
        long z = data[n - 1], y = data[0], sum = 0, e;
        int p;
        int q;

        q = 6 + 52 / n;
        sum = q * DELTA;
        sum &= UIFILTER;
        while (sum != 0) {
            e = (sum >> 2) & 3;
            for (p = n - 1; p > 0; p--) {
                z = data[p - 1];
                z &= UIFILTER;
                data[p - 1] = z;

                y = data[p] -= ((z >> 5) ^ (y << 2))
                        + (((y >> 3) ^ (z << 4)) ^ (sum ^ y))
                        + (k[(int) ((p & 3) ^ e)] ^ z);
                y &= UIFILTER;
                data[p] = y;

            }
            z = data[n - 1];
            z &= UIFILTER;
            data[n - 1] = z;

            y = data[0] -= ((z >> 5) ^ (y << 2))
                    + (((y >> 3) ^ (z << 4)) ^ (sum ^ y))
                    + (k[(int) ((p & 3) ^ e)] ^ z);
            y &= UIFILTER;
            data[0] = y;
            sum += ((~DELTA) + 1);
            sum &= UIFILTER;
        }
        return data;
    }

    public static long[] encrypt(byte[] data, long k[]) {
        long[] array = ToLongArray(data);
        encrypt(array, k);
        return array;
    }

    public static long[] decrypt(byte[] data, long k[]) {
        long[] array = ToLongArray(data);
        decrypt(array, k);
        return array;
    }


    private static long[] ToLongArray(byte[] data) {
        int n = (data.length % 4 == 0 ? 0 : 1) + data.length / 4;
        long[] result = new long[n];
        for (int i = 0; i < n - 1; i++) {
            result[i] = bytes2long(data, i * 4);
        }
        byte[] buffer = new byte[8];
        for (int i = 0, j = (n - 1) * 4; j < data.length; i++, j++) {
            buffer[i] = data[j];
        }
        result[n - 1] = bytes2long(buffer, 0);
        return result;
    }

    public static byte[] ToByteArray(long[] data) {
        List<Byte> result = new ArrayList<Byte>();

        for (int i = 0; i < data.length; i++) {
            byte[] bs = long2bytes(data[i]);
            for (int j = 0; j < bs.length; j++) {
                result.add(bs[j]);
            }
        }

        while (result.get(result.size() - 1) == SPECIAL_CHAR) {
            result.remove(result.size() - 1);
        }

        byte[] ret = new byte[result.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = result.get(i);
        }
        return ret;
    }

    public static byte[] long2bytes(long num) {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(num);
        byte[] barray = buffer.array();
        byte[] temp = new byte[4];
        System.arraycopy(barray, 4, temp, 0, 4);
        return temp;
    }

    //
    public static long bytes2long(byte[] b, int index) {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        buffer.put(b, index, 4);
        return buffer.getLong(0) >>> 8 * 4;
    }
//    public static void main(String[] args) {
//
//        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
//            System.out.println("BIG_ENDIAN");
//        } else {
//            System.out.println("LITTLE_ENDIAN");
//        }
//        long[] key = {1, 2, 3, 4};
//        long[] data = {1234567, 7654321};
//        btea(data,true);
//        System.out.println();
////		System.out.print("" + Long.toHexString(data[0]));
////		System.out.print(" " + Long.toHexString(data[1]));
//        System.out.print("" + data[0]);
//        System.out.print(" " + data[1]);
//        btea(data,false);
//        System.out.println();
//        System.out.print("" + data[0]);
//        System.out.print(" " + data[1]);
//    }
}
