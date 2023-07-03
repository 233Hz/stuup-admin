package com.poho.common.util;


import java.util.Random;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 17:18 2020/9/7
 * @Modified By:
 */
public class PasswordUtil {
    /**
     * @param password
     * @return
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = MD5Utils.GetMD5Code(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * @param password
     * @param md5
     * @return
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
        return MD5Utils.GetMD5Code(password + salt).equals(new String(cs1));
    }

    public static void main(String[] args) {
          /*String pass = "123456";
        String password = MD5Utils.GetMD5Code(pass + ProjectConstants.TEAM_SIGN);
        // 获取加盐后的密码
        String npassword = generate(password);
        System.out.println("加盐后MD5码：" + npassword);
        System.out.println("是否是同一字符串:" + verify(password, npassword));
        System.out.println(generate(pass)); */
        String pass = "123456";
        String encrPwd = generate(pass);
        System.out.println("pwd:" + pass + " encrPwd:" + encrPwd);
        System.out.println("是否是同一字符串:" + verify(pass, encrPwd));


    }
}
