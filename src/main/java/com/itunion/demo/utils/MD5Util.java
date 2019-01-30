package com.itunion.demo.utils;

import java.security.MessageDigest;

/**
 * Created by qiaosj on 2018/1/22.
 */
public class MD5Util {

    public static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * @param @param  s
     * @param @param  encodingType
     * @param @return
     * @return String
     * @throws
     * @Title: MD5
     * @Description: 根据不同编码进行MD5转换
     * @author tanglei
     */
    public final static String MD5(String text) {

        try {
            // 按照相应编码格式获取byte[]
            byte[] btInput = text.getBytes("ASCII");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式

            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return "-1";
        }
    }

    public static void main(String[] args) {
        String pwd = "admin123";
        String salt = "28LH48";
        String md5 = "77F992940A0EFD8025F5571B133BA6D5";
        System.out.println(MD5(pwd));
        System.out.println(MD5(pwd).equals(md5));

    }
}
