package com.shaw.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {


    public static String replaceStr(String str) {
        return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    /**
     * 判断是否是空
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否不是空
     */
    public static boolean isNotEmpty(String str) {
        if ((str != null) && !"".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化模糊查询
     */
    public static String formatLike(String str) {
        if (isNotEmpty(str)) {
            return "%" + str + "%";
        } else {
            return null;
        }
    }

    /**
     * 过滤掉集合里的空格
     */
    public static List<String> filterWhite(List<String> list) {
        List<String> resultList = new ArrayList<String>();
        for (String l : list) {
            if (isNotEmpty(l)) {
                resultList.add(l);
            }
        }
        return resultList;
    }

    /**
     * 过滤特殊字符
     */
    public static String filterSpChar(String str) {
        if (isEmpty(str)) {
            return null;
        }
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    public static String getFileName(String filename) {
        if (isEmpty(filename)) {
            return null;
        }
        String name;
        int end = filename.lastIndexOf(".");
        if (end > 0) {
            name = filename.substring(0, end);
        } else {
            name = filename;
        }
        return name;
    }

    public static String getSuffix(String filename) {
        if (isEmpty(filename)) {
            return null;
        }
        String suffix;
        int start = filename.lastIndexOf(".");
        if (start > 0) {
            suffix = filename.substring(start + 1);
        } else {
            suffix = "";
        }
        return suffix;
    }

    public static List<Integer> parseToIntList(String ids) {
        List<Integer> idList = new ArrayList<Integer>();
        if (isEmpty(ids)) {
            return idList;
        }
        String[] idArgs = ids.split(",");
        if (idArgs.length == 0) {
            return idList;
        }
        for (String idstr : idArgs) {
            try {
                idList.add(Integer.parseInt(idstr));
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return idList;
    }

    private static Random RANDOM = new Random(System.currentTimeMillis());

    public static String randomNumbers(int length) {
        if (length <= 1) {
            return "";
        } else {
            char[] buff = new char[length];

            for (int i = 0; i < buff.length; ++i) {
                buff[i] = (char) (48 + RANDOM.nextInt(10));
            }

            return new String(buff);
        }
    }

    public static String emptyToDefault(String target, String s) {
        if (isEmpty(target)) {
            return s;
        } else {
            return target;
        }
    }
}
