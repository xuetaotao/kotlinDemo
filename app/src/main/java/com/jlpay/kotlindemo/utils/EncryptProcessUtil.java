package com.jlpay.kotlindemo.utils;

public class EncryptProcessUtil {

    //测试demo
    public static void main(String[] args) throws Exception {
        String aesKey = "VseGsoH0jpkFMW+9";
        String appPriKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANd72Pd5Gqyb9G87" +
                "e5uskW/JfkrE5qLeAZXYdw10ZGOiHimUKwKYtWKoqKak/Ryeh7CreXTB4PEnntzV" +
                "ydJZLm/o9PvCtjHKTEaq6BW9q7jprfsoqMrTye4FuEKCB5W+6SRAjL/6ClTsaP4B" +
                "LRd8Qh0QfW+yyWOICzLbXLmZRTQBAgMBAAECgYEAyfXZtJhc18qTnm0xvQZQiWVg" +
                "aNODFsLc2YS4kHO2Y2teubmVVimqV32cFBQu5tPueTc97qCII33u9yuorO3JBoFm" +
                "bzDbcUMv79iHDn7EFs7BrPqnZGjZx+8plkkGfQg2xmlMx2RlyzkePEZxIpIsgkON" +
                "XQSDkHZTXMWQ11HPRAECQQD4miK0kcSCfmkyqX5qvu4kUDfQBNr+OE/BeWoZsXo/" +
                "4iv52vwj42XMZojazXBsgqYm9khKF5B3n+ZiLrPu7DSpAkEA3eVqMiyNsdOna5zJ" +
                "UAv6GtHgs9iN8Oq0b2I7WxRl/NgYwD7/tsFxS/tMyVw20YkZOIzR2HRjQq7WNJJl" +
                "AzXDmQJBAMJGOd7gly/fN7iGqisjQBkSszsVwEmwmes5RcgEYOOxjOo5zpcBKtl8" +
                "O3C54SR7SPrjtgRou9YwGCscEjMhVdkCQGXuEbdUog5S9LBJSGyd49jYGkljK+jE" +
                "V/hmfm+bUjIwrkX5aovwFnRZzu8cjg341X4Vqfutkz85gyqJKR+tXdkCQQCcVfsS" +
                "Opnjq10l7pcpgH4x5bUGg2xfKa4RoR/EeOW3WlHiLTrtayKIF9epjXI8RxGnWNf7" +
                "CryCU5LKloYndkUp";
        String appPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXe9j3eRqsm/RvO3ubrJFvyX5KxOai3gGV2HcNdGRjoh4plCsCmLViqKimpP0cnoewq3l0weDxJ57c1cnSWS5v6PT7wrYxykxGqugVvau46a37KKjK08nuBbhCggeVvukkQIy/+gpU7Gj+AS0XfEIdEH1vssljiAsy21y5mUU0AQIDAQAB";
        //加密过程
        /*

        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("nonce", System.currentTimeMillis());
        data.put("loginName", "张三");
        data.put("loginPwd", "123456");
        JSONObject common = new JSONObject();
        common.put("version", "1.6.7");
        System.out.println("原始data参数：" + data.toJSONString());
        String toSignParamStr = AESUtils.getToSignParamStr(data);
        System.out.println("排序后的data参数" + toSignParamStr);
        String paramSign = Md5.hexdigest(toSignParamStr);
        System.out.println("md5摘要：" + paramSign);
        String signBase64Str = RsaUtils.signByPrivateKey(paramSign, appPriKey);
        signBase64Str = URLEncoder.encode(signBase64Str, "UTF-8");
        System.out.println("签名:" + signBase64Str);
        data.put("paramSign", signBase64Str);
        request.put("common", common);
        request.put("data", data);
        System.out.println("经过签名后的报文" + request.toJSONString());
        byte[] response = AESUtils.encryptServer(request.toJSONString(), aesKey);
        String result = RsaUtils.byteToBase64Str(response);
        System.out.println("AES加密后的内容：" + result);
        JSONObject requestJson = new JSONObject();
        requestJson.put("encryptContext", result);
        System.out.println("需要发送的加密报文：" + requestJson.toJSONString());
        //解密过程
        System.out.println("收到的加密报文+" + requestJson.toJSONString());
        byte[] encryptContextByte = RsaUtils.base64StrToByte(requestJson.getString("encryptContext"));
        System.out.println("AES需要解密的内容：" + requestJson.getString("encryptContext"));
        String encryptContextEncode = AESUtils.decryptServer(encryptContextByte, aesKey);
        System.out.println("AES解密内容：" + encryptContextEncode);
        String encryptContext = URLDecoder.decode(encryptContextEncode, "UTF-8");
        System.out.println("转为中文：" + encryptContext);
        JSONObject context = JSONObject.parseObject(encryptContext);
        JSONObject data1 = context.getJSONObject("data");
        System.out.println("包含签名和nonce的data：" + data1);
        //验证签名
        String signParam1 = data1.getString("paramSign");
        System.out.println("验证签名：" + signParam1);
        data1.remove("paramSign");
        String toSignParamStr1 = AESUtils.getToSignParamStr(data1);
        //做MD5摘要
        String md5HexStr = Md5.hexdigest(toSignParamStr1);
        //判断是否与请求中的签名一致
        boolean signFlag = RsaUtils.verifySignByPublicKey(md5HexStr, appPubKey, signParam1);
        System.out.println("验证签名结果:" + signFlag);
        if (!signFlag) {
            System.out.println("报文被篡改");
        }
//        if (!checkNonce(nonce)) {
//            System.out.println("03", "请求已过期,请重新操作");
//        }

         */
    }

    /*
    控制台：
    原始data参数：{"loginPwd":"123456","loginName":"张三","nonce":1637637726271}
    排序后的data参数loginName=张三&loginPwd=123456
    md5摘要：f99f3b21128238e04f149eb7e464196a
    签名:qO1FVmnWO8gOQgNKh1QwMrSLIiUmWPU%2Bsx50KmUhfFUr%2BLZ%2FQagxWJNASJzwQVMXleHkNtQdYMHGiWOKDqgVWjsnxKcX7jJUCE%2F8%2BQ6xuXRNfDHjHnjvHlWiHgzHk%2BVeYbEYpAslunohiN0lK3F%2FWwNMe9dUNJOH1rwecjtSjek%3D
    经过签名后的报文{"common":{"version":"1.6.7"},"data":{"loginPwd":"123456","loginName":"张三","paramSign":"qO1FVmnWO8gOQgNKh1QwMrSLIiUmWPU%2Bsx50KmUhfFUr%2BLZ%2FQagxWJNASJzwQVMXleHkNtQdYMHGiWOKDqgVWjsnxKcX7jJUCE%2F8%2BQ6xuXRNfDHjHnjvHlWiHgzHk%2BVeYbEYpAslunohiN0lK3F%2FWwNMe9dUNJOH1rwecjtSjek%3D","nonce":1637637726271}}
    AES加密后的内容：fJWQhC8jJUldONlrE2W+j56YBEdf8D+mxx/Pfxr+V6WGkHzHedOjqq9xGubWx4IwYCeRrJpbIx3DYHdp+GjFiRKccRvYVTDk33vK34Wf/V1aOceVHQrSTDZGgPuwwEf2yU6Ri1zKUo3nW9HIqkc9ASJyehYeuCbHDZ8YdI9pnRGuFjMfSIe6vozWTpooK5RoRw4pYaE/mUHjz73NLrfHyPuv09T4UPA05UZSSjz9BowvsBLVOO+ebPC9fJZIQc/eYr0q8vGOK2/HwRtNAtYBECYnsusFt2mOLs0ayyg0q/K5WDKdANfw4lk0vAL4YzF6IDIIYtVobrjoxR+H5tc9PybvbQvnv80zHYG3JH/jpBDoUbJtnqsk5wje6ggkszeRIcJLYG/cj79pIgkYnDdCd0IT9tE6HS+grk4jobYwstk=
    需要发送的加密报文：{"encryptContext":"fJWQhC8jJUldONlrE2W+j56YBEdf8D+mxx/Pfxr+V6WGkHzHedOjqq9xGubWx4IwYCeRrJpbIx3DYHdp+GjFiRKccRvYVTDk33vK34Wf/V1aOceVHQrSTDZGgPuwwEf2yU6Ri1zKUo3nW9HIqkc9ASJyehYeuCbHDZ8YdI9pnRGuFjMfSIe6vozWTpooK5RoRw4pYaE/mUHjz73NLrfHyPuv09T4UPA05UZSSjz9BowvsBLVOO+ebPC9fJZIQc/eYr0q8vGOK2/HwRtNAtYBECYnsusFt2mOLs0ayyg0q/K5WDKdANfw4lk0vAL4YzF6IDIIYtVobrjoxR+H5tc9PybvbQvnv80zHYG3JH/jpBDoUbJtnqsk5wje6ggkszeRIcJLYG/cj79pIgkYnDdCd0IT9tE6HS+grk4jobYwstk="}
    收到的加密报文+{"encryptContext":"fJWQhC8jJUldONlrE2W+j56YBEdf8D+mxx/Pfxr+V6WGkHzHedOjqq9xGubWx4IwYCeRrJpbIx3DYHdp+GjFiRKccRvYVTDk33vK34Wf/V1aOceVHQrSTDZGgPuwwEf2yU6Ri1zKUo3nW9HIqkc9ASJyehYeuCbHDZ8YdI9pnRGuFjMfSIe6vozWTpooK5RoRw4pYaE/mUHjz73NLrfHyPuv09T4UPA05UZSSjz9BowvsBLVOO+ebPC9fJZIQc/eYr0q8vGOK2/HwRtNAtYBECYnsusFt2mOLs0ayyg0q/K5WDKdANfw4lk0vAL4YzF6IDIIYtVobrjoxR+H5tc9PybvbQvnv80zHYG3JH/jpBDoUbJtnqsk5wje6ggkszeRIcJLYG/cj79pIgkYnDdCd0IT9tE6HS+grk4jobYwstk="}
    AES需要解密的内容：fJWQhC8jJUldONlrE2W+j56YBEdf8D+mxx/Pfxr+V6WGkHzHedOjqq9xGubWx4IwYCeRrJpbIx3DYHdp+GjFiRKccRvYVTDk33vK34Wf/V1aOceVHQrSTDZGgPuwwEf2yU6Ri1zKUo3nW9HIqkc9ASJyehYeuCbHDZ8YdI9pnRGuFjMfSIe6vozWTpooK5RoRw4pYaE/mUHjz73NLrfHyPuv09T4UPA05UZSSjz9BowvsBLVOO+ebPC9fJZIQc/eYr0q8vGOK2/HwRtNAtYBECYnsusFt2mOLs0ayyg0q/K5WDKdANfw4lk0vAL4YzF6IDIIYtVobrjoxR+H5tc9PybvbQvnv80zHYG3JH/jpBDoUbJtnqsk5wje6ggkszeRIcJLYG/cj79pIgkYnDdCd0IT9tE6HS+grk4jobYwstk=
    AES解密内容：{"common":{"version":"1.6.7"},"data":{"loginPwd":"123456","loginName":"张三","paramSign":"qO1FVmnWO8gOQgNKh1QwMrSLIiUmWPU%2Bsx50KmUhfFUr%2BLZ%2FQagxWJNASJzwQVMXleHkNtQdYMHGiWOKDqgVWjsnxKcX7jJUCE%2F8%2BQ6xuXRNfDHjHnjvHlWiHgzHk%2BVeYbEYpAslunohiN0lK3F%2FWwNMe9dUNJOH1rwecjtSjek%3D","nonce":1637637726271}}
    转为中文：{"common":{"version":"1.6.7"},"data":{"loginPwd":"123456","loginName":"张三","paramSign":"qO1FVmnWO8gOQgNKh1QwMrSLIiUmWPU+sx50KmUhfFUr+LZ/QagxWJNASJzwQVMXleHkNtQdYMHGiWOKDqgVWjsnxKcX7jJUCE/8+Q6xuXRNfDHjHnjvHlWiHgzHk+VeYbEYpAslunohiN0lK3F/WwNMe9dUNJOH1rwecjtSjek=","nonce":1637637726271}}
    包含签名和nonce的data：{"loginPwd":"123456","loginName":"张三","paramSign":"qO1FVmnWO8gOQgNKh1QwMrSLIiUmWPU+sx50KmUhfFUr+LZ/QagxWJNASJzwQVMXleHkNtQdYMHGiWOKDqgVWjsnxKcX7jJUCE/8+Q6xuXRNfDHjHnjvHlWiHgzHk+VeYbEYpAslunohiN0lK3F/WwNMe9dUNJOH1rwecjtSjek=","nonce":1637637726271}
    验证签名：qO1FVmnWO8gOQgNKh1QwMrSLIiUmWPU+sx50KmUhfFUr+LZ/QagxWJNASJzwQVMXleHkNtQdYMHGiWOKDqgVWjsnxKcX7jJUCE/8+Q6xuXRNfDHjHnjvHlWiHgzHk+VeYbEYpAslunohiN0lK3F/WwNMe9dUNJOH1rwecjtSjek=
    验证签名结果:true
    */


}
