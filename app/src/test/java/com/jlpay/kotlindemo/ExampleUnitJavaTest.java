package com.jlpay.kotlindemo;

import org.junit.Test;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        hj32();
    }


    public static void hj32() {
        String binaryString = Integer.toBinaryString(167773121);//1010000000000000001111000001
        System.out.println(binaryString);
        System.out.println("========");
        System.out.println(tenRadixToBinary("167773121"));
        System.out.println("========");
        System.out.println(binaryRadixToTen(tenRadixToBinary("167773121")));
    }

    public static String binaryRadixToTen(String binaryStr) {
        long res = 0;
        for (int i = 0; i < binaryStr.length(); i++) {
            int anInt = Integer.parseInt(String.valueOf(binaryStr.charAt(i)));
            res = res * 2 + anInt;
        }
        return String.valueOf(res);
    }


    public static String tenRadixToBinary(String tenRadixStr) {
        long aLong = Long.parseLong(tenRadixStr);
        StringBuilder sb = new StringBuilder();
        while (aLong != 0) {
//            if ((aLong % 2) == 1) {
            if ((aLong & 1) == 1) {
                sb.append("1");
            } else {
                sb.append("0");
            }
            aLong = aLong >>> 1;
        }
        return sb.reverse().toString();
    }
}
