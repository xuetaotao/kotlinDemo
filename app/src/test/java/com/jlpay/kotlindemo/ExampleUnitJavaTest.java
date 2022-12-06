package com.jlpay.kotlindemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
//        quickSort(new int[]{3, 2, 5, 8, 1});
        hj48();
    }


    public static void hj48() {
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
            int num = 5;//输入链表结点个数
            int headValue = 2;//输入头结点的值
            int[] input = new int[]{3, 2, 4, 3, 5, 2, 1, 4};
            List<Integer> list = new ArrayList<>();
            list.add(headValue);
            for (int i = 0; i < num - 1; i++) {
                int nextValue = input[i];
                int curValue = input[i+1];
                int index = list.indexOf(curValue);
                list.add(index + 1, nextValue);
            }
            int removeValue = 3;
            list.remove((Integer) removeValue);
            for (Integer temp : list) {
                System.out.print(temp + " ");
            }
//        }
    }

    public static class TestNode {
        int x;
        int y;
        TestNode pre;

        public TestNode(int x, int y, TestNode pre) {
            this.x = x;
            this.y = y;
            this.pre = pre;
        }
    }

    public void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    public void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int temp = left + (int) (Math.random() * (right - left + 1));
            swap(arr, temp, right);
            int[] p = partition(arr, left, right);
            quickSort(arr, left, p[0] - 1);
            quickSort(arr, p[1] + 1, right);
        }
    }

    public int[] partition(int[] arr, int left, int right) {
        int less = left - 1;
        int more = right;
        while (left < more) {
            if (arr[left] < arr[right]) {
                swap(arr, ++less, left++);
            } else if (arr[left] > arr[right]) {
                swap(arr, --more, left);
            } else {
                left++;
            }
        }
        swap(arr, more, right);
        return new int[]{less + 1, more - 1};
    }

    public void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }

}
