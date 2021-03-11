package com.jlpay.kotlindemo.ui.utils;

import android.content.Context;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA非对称加密
 * 每次最大加密长度不能超过128个字节，超过的话建议使用分段加密或者AES加密
 * 包括以下实现：生成公钥私钥、加密、解密、加签、验签、SHA-256摘要算法、MD5摘要
 */
public class RSAUtils {

    public static String RSA_PRIVATE_APP;
    public static String RSA_PUBLIC_SERVER;

    //分别是两对RSA密钥中的私钥(我的)和公钥(服务器的)(经过Base64编码)
    ///////////////////////////////测试环境密钥//////////////////////////////////////////////////////////////////////////////////////////////

    public static final String RSA_PRIVATE_DEV_APP = "";

    public static final String RSA_PUBLIC_DEV_APP = "";

    public static final String RSA_PUBLIC_DEV_SERVER = "";

    public static final String RSA_PRIVATE_DEV_SERVER = "";


    ///////////////////////////////生产验证环境密钥//////////////////////////////////////////////////////////////////////////////////////////////

    public static final String RSA_PRIVATE_PRE_APP = "";

    public static final String RSA_PUBLIC_PRE_APP = "";

    public static final String RSA_PUBLIC_PRE_SERVER = "";

    public static final String RSA_PRIVATE_PRE_SERVER = "";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String SIGNATURE_ALGORITHM_MD5 = "MD5withRSA";
    public static final String RSA_ALGORITHM = "RSA";//RSA 算法的密码工作模式只支持ECB
    public static final String CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding";


    /**
     * RSA密钥初始化
     * 暂时不用了，已在Config中做了初始化
     */
    public static void initRSAKey(String environment, Context context) {
        switch (environment) {
            case "0"://生产环境
            case "1"://生产验证环境
//                RSA_PRIVATE_APP = RSA_PRIVATE_PRE_APP + SecretUtils.genPri(1) + BuildConfig.RSA_PRIVATE_PRE_APP_PART3 + context.getResources().getString(R.string.rsa_private_pre_app_part4);
//                RSA_PUBLIC_SERVER = RSA_PUBLIC_PRE_SERVER + SecretUtils.genPub(1) + BuildConfig.RSA_PUBLIC_PRE_SERVER_PART3 + context.getResources().getString(R.string.rsa_public_pre_server_part4);
                break;
            case "2"://测试
//                RSA_PRIVATE_APP = RSA_PRIVATE_DEV_APP + SecretUtils.genPri(0) + BuildConfig.RSA_PRIVATE_DEV_APP_PART3 + context.getResources().getString(R.string.rsa_private_dev_app_part4);
//                RSA_PUBLIC_SERVER = RSA_PUBLIC_DEV_SERVER + SecretUtils.genPub(0) + BuildConfig.RSA_PUBLIC_DEV_SERVER_PART3 + context.getResources().getString(R.string.rsa_public_dev_server_part4);
                break;
        }
    }


    /**
     * 生成RSA公私钥对
     * 用的较少，一般都是后台直接给公私钥对过来
     *
     * @param keySize 密钥长度，建议用1024，或者2048
     * @return 存放经过Base64编码后的公钥私钥Map
     */
    public static Map<String, String> generateKeyPair(int keySize) {
        Map<String, String> keyMap = new HashMap<>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            // 初始化KeyPairGenerator对象,密钥长度
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyStr = byteToBase64Str(publicKey.getEncoded());
            PrivateKey privateKey = keyPair.getPrivate();
            String privateKeyStr = byteToBase64Str(privateKey.getEncoded());
            keyMap.put("publicKey", publicKeyStr);
            keyMap.put("privateKey", privateKeyStr);
            return keyMap;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 还原X509编码的公钥
     *
     * @param publicKey Base64编码后的RSA公钥字符串
     * @return X509编码的 PublicKey
     */
    public static PublicKey getX509PublicKey(String publicKey) {
        byte[] publicKeyBytes = base64StrToByte(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 还原PKCS8编码的私钥
     *
     * @param privateKey Base64编码后的RSA私钥字符串
     * @return PKCS8编码的 PrivateKey
     */
    public static PrivateKey getPKCS8PrivateKey(String privateKey) {
        byte[] privateKeyBytes = base64StrToByte(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 公钥加密，返回用Base64编码后的密文
     *
     * @param data      待加密数据
     * @param publicKey Base64编码后的RSA公钥字符串
     * @return 加密后的字符串
     */
    public static String encryptByPublicKey(String data, String publicKey) {
        PublicKey x509PublicKey = getX509PublicKey(publicKey);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, x509PublicKey);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byteToBase64Str(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 私钥解密，返回明文字符串
     *
     * @param data       待解密数据
     * @param privateKey Base64编码后的RSA私钥字符串
     * @return 解密后的字符串-明文
     */
    public static String decryptByPrivateKey(String data, String privateKey) {
        PrivateKey pkcs8PrivateKey = getPKCS8PrivateKey(privateKey);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, pkcs8PrivateKey);
            byte[] originBytes = base64StrToByte(data);
            byte[] bytes = cipher.doFinal(originBytes);
            return new String(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 私钥加密，返回用Base64编码后的密文
     *
     * @param data       待加密数据
     * @param privateKey Base64编码后的RSA私钥字符串
     * @return 加密后的字符串
     */
    public static String encryptByPrivateKey(String data, String privateKey) {
        PrivateKey pkcs8PrivateKey = getPKCS8PrivateKey(privateKey);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, pkcs8PrivateKey);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byteToBase64Str(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 公钥解密，返回明文字符串
     *
     * @param data      待解密数据
     * @param publicKey Base64编码后的RSA公钥字符串
     * @return 解密后的字符串-明文
     */
    public static String decryptByPublicKey(String data, String publicKey) {
        PublicKey x509PublicKey = getX509PublicKey(publicKey);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, x509PublicKey);
            byte[] originBytes = base64StrToByte(data);
            byte[] bytes = cipher.doFinal(originBytes);
            return new String(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 私钥签名
     * SHA256withRSA 签名数据
     *
     * @param data       待签名的字符串
     * @param privateKey Base64编码后的RSA私钥字符串
     * @return Base64编码后的签名字符串
     */
    public static String signByPrivateKey(String data, String privateKey) {
        PrivateKey pkey = getPKCS8PrivateKey(privateKey);
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);//也可用MD5withRSA
            signature.initSign(pkey);
            signature.update(data.getBytes());
            byte[] signData = signature.sign();
            return byteToBase64Str(signData);

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 公钥验签
     * SHA256withRSA
     *
     * @param data      待验签的字符串
     * @param publicKey Base64编码后的RSA公钥字符串
     * @param sign      Base64编码后的签名字符串
     * @return 验签是否通过 true：是
     */
    public static boolean verifySignByPublicKey(String data, String publicKey, String sign) {
        PublicKey x509PublicKey = getX509PublicKey(publicKey);
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);//也可用MD5withRSA
            signature.initVerify(x509PublicKey);
            signature.update(data.getBytes());
            return signature.verify(base64StrToByte(sign));

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 存储Base64编码后的RSA密钥字符串到本地文件
     *
     * @param key      Base64编码后的RSA密钥字符串
     * @param filePath 本地文件路径
     */
    public static void saveKeyToFile(String key, String filePath) {
        File file = new File(filePath + ".keystore");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(key);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从本地文件获取Base64编码后的RSA密钥字符串
     *
     * @param filePath 本地文件路径
     * @return Base64编码后的RSA密钥字符串
     */
    public static String getKeyFromFile(String filePath) {
        File file = new File(filePath);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readStr = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((readStr = bufferedReader.readLine()) != null) {
                stringBuilder.append(readStr);
            }
            bufferedReader.close();
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * SHA-256摘要
     *
     * @param data 要做摘要的原始字符串
     * @return 摘要字符串
     */
    public static String sha256(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes());
            byte[] digest = messageDigest.digest();
            return byteToHexString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * MD5摘要
     *
     * @param data 要做摘要的原始字符串
     * @return 摘要字符串
     */
    public static String md5(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(data.getBytes());
            byte[] digest = messageDigest.digest();
            return byteToHexString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * MD5摘要
     *
     * @param data 要做摘要的字节数组
     * @return 摘要字符串
     */
    public static String md5(byte[] data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(data);
            byte[] digest = messageDigest.digest();
            return byteToHexString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * byte[]换成16进制字符串
     *
     * @param data byte数组
     * @return 16进制字符串
     */
    public static String byteToHexString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        if (data == null || data.length == 0) {
            return null;
        }
        for (byte datum : data) {
            int v = datum & 0xFF;
            String hexString = Integer.toHexString(v);
            if (hexString.length() == 1) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hexString);
        }
        return stringBuilder.toString();
    }


    /**
     * 字节数组转为Base64编码字符串
     *
     * @param data 字节数组
     * @return Base64编码字符串
     */
    public static String byteToBase64Str(byte[] data) {
        byte[] encodeByte = Base64.encode(data, Base64.DEFAULT);
        return new String(encodeByte);
    }


    /**
     * Base64编码字符串转为字节数组
     *
     * @param data Base64编码字符串
     * @return 字节数组
     */
    public static byte[] base64StrToByte(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }
}
