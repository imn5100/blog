package com.shaw.util;


import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by shaw on 2017/4/19.
 */
public final class NumberUtils {
    private static Random RANDOM = new Random(System.currentTimeMillis());
    private static final Pattern CLEAR_PATTERN = Pattern.compile("[^0-9]");
    private static final NumberFormat DEFAULT_FLOAT_FORMAT = NumberFormat.getInstance();

    public NumberUtils() {
    }

    public static String format(double value) {
        return DEFAULT_FLOAT_FORMAT.format(value);
    }

    public static String format(double value, int fractionDigits) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(fractionDigits);
        numberFormat.setMinimumFractionDigits(fractionDigits);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(value);
    }

    public static boolean between(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean between(long value, long min, long max) {
        return value >= min && value <= max;
    }

    public static boolean is(String value) {
        if (value != null && value.length() > 0) {
            char[] chars = value.toCharArray();

            for (int i = 0; i < chars.length; ++i) {
                if (!Character.isDigit(chars[i])) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static String getPercent(int value, int total) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        if (value <= 0 || total <= 0) {
            nf.format(0L);
        }

        return nf.format((double) value * 1.0D / (double) total);
    }

    public static boolean not(String value) {
        return !is(value);
    }

    public static int parseIntQuietly(Object value) {
        return parseIntQuietly(value, 0);
    }

    public static int parseIntQuietly(Object value, int def) {
        if (value != null) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }

            try {
                return Integer.valueOf(value.toString()).intValue();
            } catch (Throwable var3) {
                ;
            }
        }

        return def;
    }

    public static int parseIntQuietlyAfterClear(Object value, int def) {
        if (value != null) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }

            try {
                return Integer.valueOf(CLEAR_PATTERN.matcher(value.toString()).replaceAll("")).intValue();
            } catch (Throwable var3) {
                ;
            }
        }

        return def;
    }

    public static long parseLongQuietly(Object value, long def) {
        if (value != null) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }

            try {
                return Long.valueOf(value.toString()).longValue();
            } catch (Throwable var4) {
                ;
            }
        }

        return def;
    }

    public static int nextInt() {
        return RANDOM.nextInt();
    }

    public static int nextInt(int n) {
        return RANDOM.nextInt(n);
    }

    public static int getIntByPosition(int value, int index) {
        if (value <= 0) {
            return 0;
        } else {
            if (index <= 0) {
                index = 1;
            }

            String strValue = String.valueOf(value);
            return strValue.length() < index ? 0 : Integer.valueOf("" + strValue.charAt(strValue.length() - index)).intValue();
        }
    }

    public static int setIntByPosition(int source, int index, int value) {
        if (index <= 0) {
            index = 1;
        }

        if (value <= 9 && value >= 0) {
            StringBuilder buff = new StringBuilder(String.valueOf(source));
            if (buff.length() >= index) {
                buff.setCharAt(buff.length() - index, Integer.valueOf(value).toString().charAt(0));
            } else {
                int maxIndex = index - buff.length() - 1;

                for (int i = 0; i < maxIndex; ++i) {
                    buff.insert(0, "0");
                }

                buff.insert(0, value);
            }

            return Integer.valueOf(buff.toString()).intValue();
        } else {
            return source;
        }
    }

    public static boolean isEQ(int source, int index, int value) {
        return getIntByPosition(source, index) == value;
    }

    public static boolean isGT(int source, int index, int value) {
        return getIntByPosition(source, index) > value;
    }

    public static boolean isGE(int source, int index, int value) {
        return getIntByPosition(source, index) >= value;
    }

    public static int[] toArray(Collection<Integer> ints) {
        if (ints != null && !ints.isEmpty()) {
            int[] result = new int[ints.size()];
            int idx = 0;
            Iterator var3 = ints.iterator();

            while (var3.hasNext()) {
                Integer _int = (Integer) var3.next();
                if (_int != null) {
                    result[idx++] = _int.intValue();
                }
            }

            return result;
        } else {
            return new int[0];
        }
    }

    public static byte[] toBytes(long value) {
        byte[] bytes = new byte[8];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.putLong(value);
        return bytes;
    }

    public static byte[] toBytes(int value) {
        byte[] bytes = new byte[4];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.putInt(value);
        return bytes;
    }

    public static long toLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static boolean isPositive(Integer value) {
        return value != null && value.intValue() > 0;
    }

    public static boolean isPositive(Long value) {
        return value != null && value.longValue() > 0L;
    }

    public static boolean isPositive(int value) {
        return value > 0;
    }

    public static boolean isPositive(long value) {
        return value > 0L;
    }

    public static boolean isNegative(int value) {
        return value < 0;
    }

    public static boolean nullOrEqualsTo(Integer source, Integer target) {
        return source == null || source.equals(target);
    }

    public static String padding(long value, int length) {
        return padding(value, length, '0');
    }

    public static String padding(long value, int length, char padding) {
        String str = String.valueOf(value);
        int size = length - str.length();
        if (size <= 0) {
            return str;
        } else {
            StringBuilder buff = new StringBuilder(length);

            for (int i = 0; i < size; ++i) {
                buff.append(padding);
            }

            buff.append(str);
            return buff.toString();
        }
    }

    public static long merge(int hi, int lo) {
        return (long) hi << 32 | (long) lo;
    }

    public static int hi(long value) {
        return (int) (value >> 32);
    }

    public static int lo(long value) {
        return (int) (value & -1L);
    }

    static {
        DEFAULT_FLOAT_FORMAT.setMaximumFractionDigits(1);
        DEFAULT_FLOAT_FORMAT.setMinimumFractionDigits(0);
        DEFAULT_FLOAT_FORMAT.setGroupingUsed(false);
    }
}
