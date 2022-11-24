package com.jlpay.kotlindemo;

import org.junit.Test;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        insertionSort(new int[]{5, 4, 3, 2, 1});
    }


    public void insertionSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        for (int temp : arr) {
            System.out.print(temp + " ");
        }
    }

    public void selectionSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        for (int temp : arr) {
            System.out.print(temp + " ");
        }
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
