package com.shaw.test;

import com.shaw.util.CodecUtils;
import com.shaw.util.DesUtils;
import com.shaw.util.EmailUtils;
import com.shaw.util.PropertiesUtil;
import org.apache.commons.lang.exception.ExceptionUtils;

public class Test {
    public static void main(String[] args) throws Exception {
//        String encrpt = DesUtils.getDefaultInstance().encrypt("112");
//        System.out.println(encrpt);
//        System.out.println(DesUtils.getDefaultInstance().decrypt(encrpt));

        sendEmail();
    }

    public static void simpleDecode() throws Exception {
        String encodeStr = CodecUtils.getEncodeId(1154);
        System.out.println(encodeStr);
        int decodeId = CodecUtils.getDecodeId(encodeStr);
        System.out.println(decodeId);
    }

    public static void sendEmail() {
        try {
            DesUtils.getDefaultInstance().decrypt("OOOOOOO");
        } catch (Exception ex) {
            String[] emails = PropertiesUtil.getConfiguration().getStringArray("email.subscriber");
            System.out.println(emails);
            System.out.println(EmailUtils.sendEmail("Blog system exception", ExceptionUtils.getFullStackTrace(ex), emails));
        }


    }

}

