package com.lego.survey.user.impl.utils;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.util.Random;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
public class Md5SaltUtil {

    /*
     * 生成含有随机盐的密码
     */
    public static String generate(String password){
        String salt = getSalt(16);
        if(StringUtils.isBlank(password)){
            return password;
        }
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /*
     * 生成随机盐（一般大于等于16位）
     */
    private static String getSalt(int len){
        Random r = new Random();
        StringBuilder sb = new StringBuilder(len);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int length = sb.length();
        if (length < len) {
            for (int i = 0; i < len - length; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        return salt;
    }

    /**
     * 校验密码是否正确
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }

    /*
     * 获取十六进制字符串形式的MD5
     */
    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(Hex.encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

}
