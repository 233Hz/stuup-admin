package com.poho.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 21:56 2018/7/18
 * @Modified By:
 */
public class DesUtils {
    private static Key key;
    private static String KEY_STR = "poho_des";

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(KEY_STR.getBytes());
            generator.init(secureRandom);
            key = generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对字符串进行加密，返回BASE64的加密字符串
     * <功能详细描述>
     *
     * @param str
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getEncryptString(String str) {
        Base64.Encoder base64Encoder = Base64.getEncoder();
        try {
            byte[] strBytes = str.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptStrBytes = cipher.doFinal(strBytes);
            return base64Encoder.encodeToString(encryptStrBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对BASE64加密字符串进行解密
     * <功能详细描述>
     *
     * @param str
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getDecryptString(String str) {
        Base64.Decoder base64Decoder = Base64.getDecoder();
        try {
            byte[] strBytes = base64Decoder.decode(str);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptStrBytes = cipher.doFinal(strBytes);
            return new String(encryptStrBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String name = "root";
        String password = "123456";
        String encryptName = getEncryptString(name);
        String encryptPassword = getEncryptString(password);
        System.out.println(encryptName);
        System.out.println(encryptPassword);

        System.out.println(getDecryptString(encryptName));
        System.out.println(getDecryptString(encryptPassword));
    }
}
