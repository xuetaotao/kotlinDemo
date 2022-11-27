package com.jlpay.kotlindemo;

import org.junit.Test;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        quickSort(new int[]{3, 2, 5, 8, 1});
    }

    public void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.println(i + " ");
        }
    }

    public void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            //Math.random()：[0,1)所有的小数，等概率返回一个
            //Math.random()*N：[0,N)所有的小数，等概率返回一个
            //(int)(Math.random()*N)：[0,N-1]所有的整数，等概率返回一个
            //right - left + 1：数组的长度
            //(int) (Math.random() * (right - left + 1))：数组中元素下标从最左侧到最右侧任意返回一个整数下标
            int randomIndex = left + (int) (Math.random() * (right - left + 1));
            //将这个随机选择的数与数组最后一个数互换
            swap(arr, randomIndex, right);
            int[] p = partition(arr, left, right);
            quickSort(arr, left, p[0] - 1);//小于区
            quickSort(arr, p[1] + 1, right);//大于区
        }
    }

    //返回等于区域（左边界，右边界），所以返回一个长度为2的数组res，res[0]和res[1]
    public int[] partition(int[] arr, int left, int right) {
        int less = left - 1;//小于区右边界
        int more = right;//大于区左边界
        while (left < more) {
            if (arr[left] < arr[right]) {//当前数 < 划分的标准值
                swap(arr, ++less, left++);
            } else if (arr[left] > arr[right]) {//当前数 > 划分的标准值
                swap(arr, --more, left);
            } else {
                left++;
            }
        }
        swap(arr, more, right);
        return new int[]{less + 1, more};
    }

    public void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }



    public void guibing(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        splitArr(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    public void splitArr(int[] arr, int left, int right) {
        if (left == right) {
            return;
        }
        int mid = left + ((right - left) >> 1);
        splitArr(arr, left, mid);
        splitArr(arr, mid + 1, right);
        merge(arr, left, right, mid);
    }

    public void merge(int[] arr, int left, int right, int mid) {
        int p1 = left;
        int p2 = mid + 1;
        int i = 0;
        int[] help = new int[right - left + 1];
        while (p1 <= mid && p2 <= right) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid) {
            help[i++] = arr[p1++];
        }
        while (p2 <= right) {
            help[i++] = arr[p2++];
        }
        for (int j = 0; j < help.length; j++) {
            arr[left + j] = help[j];
        }
    }

}
