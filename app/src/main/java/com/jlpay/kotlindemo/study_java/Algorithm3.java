package com.jlpay.kotlindemo.study_java;

/**
 * 算法的逐步学习
 * 这块和Algorithm2的区别是：
 * Algorithm2的代码主要是跟着老师上课学习或者刷题百度等找到的标准答案
 * 而Algorithm3的代码主要是自己在掌握Algorithm2中的代码时候，自己默写的，中间会有些代码细节不一样
 */
public class Algorithm3 {

    /**
     * 堆排序
     * 时间复杂度：O(N*logN) 空间复杂度：O(1)
     */
    public void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        //将数组变成大根堆
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i);
        }
        for (int i = arr.length - 1; i >= 0; i--) {
            //将最大值放到数组最后，并与数组断开连接
            swap(arr, 0, i);
            //将新数组再次变成大根堆，使用heapify
            heapify(arr, 0, i);
        }
        for (int temp : arr) {
            System.out.println(temp + " ");
        }
    }

    public void heapInsert(int[] arr, int index) {
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }


    public void heapify(int[] arr, int index, int heapSize) {
        int leftChildIndex = 2 * index + 1;//右孩子=2*index+2，即左孩子+1
        while (leftChildIndex < heapSize) {
            int largest = leftChildIndex;
            if (leftChildIndex + 1 < heapSize && arr[leftChildIndex + 1] > arr[leftChildIndex]) {
                largest = leftChildIndex + 1;
            }
            if (arr[largest] <= arr[index]) {
                break;
            }
            swap(arr, index, largest);
            index = largest;
            leftChildIndex = 2 * index + 1;
        }
    }

    public void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }
}
