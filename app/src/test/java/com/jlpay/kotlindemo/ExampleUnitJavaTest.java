package com.jlpay.kotlindemo;

import org.junit.Test;

import java.util.Scanner;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        hj35();
    }


    public static void hj35() {
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
            int anInt = 4;
            int[][] arr = new int[anInt][];
            for (int i = 0; i < anInt; i++) {
                int[] temp = new int[anInt - i];
                arr[i] = temp;
            }
            int num = 1;
            for (int x = 0; x < arr.length; x++) {
                int temp = x;
                for (int y = 0; temp >= 0; y++) {
                    arr[temp--][y] = num;
                    num++;
                }
            }
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    System.out.print(arr[i][j] + " ");
                }
                System.out.println();
            }
//        }
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
