package com.jlpay.kotlindemo.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    public static final String AES_KEY = "U6woFMMEYEjgb+Ws";//JDK目前只支持AES-128加密，也就是传入的密钥必须是长度为16的字符串，SP本地存储加密用
    public static final int KEY_SIZE = 128;//128位，对应16字节
    public static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";//Android默认密码工作模式为ECB，但不安全，这里用CBC
    public static final String CIPHER_ALGORITHM_ECB_NoPadding = "AES/ECB/NoPadding";//JL自定义AES加密使用
    public static final String AES = "AES";
    public static final String RNG_ALGORITHM = "SHA1PRNG";//随机数生成器（RNG）算法名称


    /**
     * 生成密钥对象
     *
     * @param key 密钥
     * @return SecretKey
     */
    public static SecretKey generateKey(byte[] key) {
        try {
            //每次传入相同的key也会生成不同的SecretKey，所以生成后务必保存后加解密同用一个
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom = SecureRandom.getInstance(RNG_ALGORITHM);
            secureRandom.setSeed(key);// 设置 密钥key的字节数组 作为安全随机数生成器的种子
            keyGenerator.init(KEY_SIZE, secureRandom);
            return keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * AES加密
     *
     * @param data 明文-待加密数据
     * @param key  密钥
     * @return 密文-加密后的数据
     */
    public static byte[] encryptByAES(byte[] key, byte[] data) {
        try {
            //每次传入相同的key，生成相同的SecretKey
            SecretKey secretKey = new SecretKeySpec(key, AES);
            //设置初始向量Iv，ECB以外的其他加密模式均需要传入一个初始向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(key);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(data);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * AES解密
     *
     * @param data 密文
     * @param key  密钥
     * @return 明文-解密后的数据
     */
    public static byte[] decryptByAES(byte[] key, byte[] data) {
        try {
            //每次传入相同的key，生成相同的SecretKey
            SecretKey secretKey = new SecretKeySpec(key, AES);
            //设置初始向量Iv，ECB以外的其他加密模式均需要传入一个初始向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(key);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(data);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 字符串异或算法
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 异或后的二进制
     */
    private static String xor(String str1, String str2) {
        byte[] b1 = str1.getBytes();
        byte[] b2 = str2.getBytes();
        byte[] longbytes;
        byte[] shortbytes;
        if (b1.length >= b2.length) {
            longbytes = b1;
            shortbytes = b2;
        } else {
            longbytes = b2;
            shortbytes = b1;
        }
        byte[] xorstr = new byte[longbytes.length];
        int i = 0;
        for (; i < shortbytes.length; i++) {
            xorstr[i] = (byte) (shortbytes[i] ^ longbytes[i]);
        }
        for (; i < longbytes.length; i++) {
            xorstr[i] = longbytes[i];
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(xorstr);
            return bytes2Hex(md.digest());
        } catch (Exception e) {
            return null;
        }
    }

    public static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp = null;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * 混淆秘钥
     *
     * @param key 密钥
     * @return 混淆秘钥
     */
    public static String getMix(String key) {
        int len = key.length() / 2;
        String key1 = key.substring(0, len);
        String key2 = key.substring(len);
        String xor = xor(key1, key2);
        return xor != null ? xor.substring(10, 26) : null;
    }
}
