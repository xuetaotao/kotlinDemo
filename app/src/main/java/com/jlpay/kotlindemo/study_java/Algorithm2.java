package com.jlpay.kotlindemo.study_java;

/**
 * 算法的逐步学习
 * 字符串，链表（单和双），二叉树，堆，栈，队列，哈希，图
 * 排序算法，查找算法，递归，动态规划，贪心算法
 * 线性表：数组和链表
 */
public class Algorithm2 {

    //线性表：数组和链表
    //////////////////////////////////链表//////////////////////////////////////////////////////////////////
    //单向链表，双向链表


    /////////////////////////////////排序算法/////////////////////////////////////////////////////////////////////

    /**
     * 归并排序法
     * 时间复杂度：O(N*logN)，空间复杂度：O(N)，具有稳定性
     * 实质就是一个简单递归，将要排序的数组等分，左边排好序，右边排好序，然后进行合并让其整体有序
     * https://www.runoob.com/w3cnote/merge-sort.html
     */
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
        int[] helpArr = new int[right - left + 1];//注意这里的数组长度
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
            arr[left + j] = helpArr[j];//这里arr的数组初始长度不一定是0
        }
    }


    /**
     * 插入排序法
     * 时间复杂度：O(N^2)，空间复杂度：O(1)，具有稳定性
     * https://www.runoob.com/w3cnote/insertion-sort.html
     */
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
        //方式二
//        for (int i = 1; i < arr.length; i++) {
//            for (int j = i; j > 0; j--) {
//                if (arr[j] < arr[j - 1]) {
//                    int temp = arr[j];
//                    arr[j] = arr[j - 1];
//                    arr[j - 1] = temp;
//                }
//            }
//        }
        for (int temp : arr) {
            System.out.print(temp + " ");
        }
    }


    /**
     * 选择排序法
     * 时间复杂度：O(N^2)，空间复杂度：O(1)，不具有稳定性
     * https://www.runoob.com/w3cnote/selection-sort.html
     */
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


    /**
     * 冒泡排序法
     * 时间复杂度：O(N^2)，空间复杂度：O(1)，具有稳定性
     * https://www.runoob.com/w3cnote/bubble-sort.html
     */
    public void bubbleSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        //总共比较arr.length-1轮
        for (int i = 0; i < arr.length - 1; i++) {
            //每一轮可以把一个数放到正确位置上，下一轮比较就可以少比一个数
            for (int j = 0; j < arr.length - 1 - i; j++) {
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
}
