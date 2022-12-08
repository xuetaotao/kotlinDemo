package com.jlpay.kotlindemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        heapSort(new int[]{3, 2, 5, 8, 1});
    }


    public void search(int[] arr, int k) {
        Arrays.sort(arr);
        int mid = arr.length / 2;
        int top = arr.length - 1;
        int bottom = 0;
        while (true) {
            if (arr[mid] == k) {
                break;
            }
            if (arr[mid] > k) {
                top = mid;
            } else if (arr[mid] < k) {
                bottom = mid;
            }
            mid = (top - bottom) / 2;
        }
    }


    public void strFind(String str, Character character) {
        int index = str.indexOf(character);
    }


    public static class AA {

        public static AA aa;

        public static AA getInstance() {
            if (aa == null) {
                synchronized (AA.class) {
                    if (aa == null) {
                        aa = new AA();
                    }
                }
            }
            return aa;
        }
    }


    public void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
//        for (int i = 0; i < arr.length; i++) {//O(N)
//            heapInsert(arr, i);//O(logN)
//        }
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }
        int heapSize = arr.length;
        swap(arr, 0, --heapSize);
        while (heapSize > 0) {//O(N)
            heapify(arr, 0, heapSize);//O(logN)
            swap(arr, 0, --heapSize);//O(1)
        }
        for (int i : arr) {
            System.out.println(i + " ");
        }
    }

    //某个数在index位置，能否往下移动
    public void heapify(int[] arr, int index, int heapSize) {
        int leftChild = index * 2 + 1;//左孩子的下标，左孩子总比右孩子下标小1。
        //一直到当前节点不再有左孩子和右孩子，或者它到达的那个位置后，它的左孩子和右孩子都没有PK过它就可以停下来了
        while (leftChild < heapSize) {//下方还有孩子的话
            //两个孩子中，谁的值大，把下标给largest
            int largest = (leftChild + 1 < heapSize && arr[leftChild + 1] > arr[leftChild]) ? leftChild + 1 : leftChild;
            //父节点与较大的孩子之间，谁的值大，把下标给largest
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);
            index = largest;
            leftChild = index * 2 + 1;
        }
    }

    //某个数现在处在index位置，往上继续移动
    public void heapInsert(int[] arr, int index) {
        //当index位置的数不再比其父节点位置的数大了，循环停止。
        //或者index来到了整个树的头节点（0）位置，那么它也不会比它的父节点（0）位置大，循环也可以停止。
        //-1/2=0；如果index位置的数是最大的，那么循环最后index会变为0
        while (arr[index] > arr[(index - 1) / 2]) {
            //交换index和父节点位置的数，更新index的新位置为父节点的位置，然后继续循环
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
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
            int curValue = input[i + 1];
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
