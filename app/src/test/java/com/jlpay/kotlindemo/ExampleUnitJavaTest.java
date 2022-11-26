package com.jlpay.kotlindemo;

import org.junit.Test;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        mergeSort(new int[]{3, 2, 5, 8, 1});
    }


    public void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        splitArr(arr, 0, arr.length - 1);
        for (int temp : arr) {
            System.out.print(temp + " ");
        }
    }

    public void splitArr(int[] arr, int left, int right) {
        if (left == right) {
            return;
        }
        int mid = left + ((right - left) >> 1);//left+(right-left)/2
        splitArr(arr, left, mid);
        splitArr(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    public void merge(int[] arr, int left, int mid, int right) {
        int p1 = left;
        int p2 = mid + 1;
        int[] helpArr = new int[right - left + 1];
        int i = 0;
        while (p1 <= mid && p2 <= right) {
            helpArr[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid) {
            helpArr[i++] = arr[p1++];
        }
        while (p2 <= right) {
            helpArr[i++] = arr[p2++];
        }
        for (int j = 0; j < helpArr.length; j++) {
            arr[left + j] = helpArr[j];
        }
    }

    void charu(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                }
            }
        }
        for (int i : arr) {
            System.out.println(i);
        }
    }

    void xuanze(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        for (int i : arr) {
            System.out.println(i);
        }
    }

    void maopao(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        for (int i : arr) {
            System.out.println(i);
        }
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
