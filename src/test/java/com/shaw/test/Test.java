package com.shaw.test;

import com.shaw.util.CodecUtils;
import com.shaw.util.DesUtils;

public class Test {
    public static void main(String[] args) throws Exception {
//        String encrpt = DesUtils.getDefaultInstance().encrypt("112");
//        System.out.println(encrpt);
//        System.out.println(DesUtils.getDefaultInstance().decrypt(encrpt));

        simpleDecode();
    }

    public static void simpleDecode() throws Exception {
        String encodeStr = CodecUtils.getEncodeId(114);
        System.out.println(encodeStr);
        int decodeId = CodecUtils.getDecodeId(encodeStr);
        System.out.println(decodeId);
    }

}

