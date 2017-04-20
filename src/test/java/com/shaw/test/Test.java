package com.shaw.test;

import com.shaw.util.CodecUtils;
import com.shaw.util.CodesImgUtil;
import com.shaw.util.NumberUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
//        final String path = "/usr/myfile/logs/html/";
//        File htmlPath = new File(path);
//        FilenameFilter filter = FileFilterUtils.suffixFileFilter(".html");
//        if (htmlPath.isDirectory()) {
//            File[] accessFiles = htmlPath.listFiles(filter);
//            List<File> fileList = Arrays.asList(accessFiles);
//            if (fileList.size() > 0) {
//                for (File accessFile : accessFiles) {
//                    FileUtils.readFileToString(accessFile, "UTF-8");
//                    System.out.println(accessFile.getName());
//                }
//            }
//        }
        simpleDecode();
    }

    public static void simpleDecode() {
        String encodeStr = CodecUtils.getEncodeId(21323);
        int decodeId = CodecUtils.getDecodeId(encodeStr);
        System.out.println(encodeStr);
        System.out.println(decodeId);
    }

    public static void testBit() {

    }
}
