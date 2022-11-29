package com.jlpay.kotlindemo;

import com.jlpay.kotlindemo.study_java.HuaWeiTest2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
//        quickSort(new int[]{3, 2, 5, 8, 1});
        hj43Two();
    }


    public static void hj43Two() {
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
            int m = 3;//m行
            int n = 2;//n列
            int[][] arr = new int[][]{{0,1},{0,1},{0,0}};
//            for (int i = 0; i < m; i++) {
//                for (int j = 0; j < n; j++) {
//                    arr[i][j] = scanner.nextInt();
//                }
//            }
            Queue<TestNode> queue = new LinkedList<>();
            queue.offer(new TestNode(0, 0, null));
            TestNode temp;
            while (true) {
                temp = queue.poll();
                if (temp == null) {
                    break;
                }
                int x = temp.x;
                int y = temp.y;
                arr[x][y] = 1;
                if (x == m - 1 && y == n - 1) {
                    break;
                }
                if (x + 1 < m && arr[x + 1][y] == 0) {
                    queue.offer(new TestNode(x + 1, y, temp));
                }
                if (x - 1 >= 0 && arr[x - 1][y] == 0) {
                    queue.offer(new TestNode(x - 1, y, temp));
                }
                if (y + 1 < n && arr[x][y + 1] == 0) {
                    queue.offer(new TestNode(x, y + 1, temp));
                }
                if (y - 1 >= 0 && arr[x][y - 1] == 0) {
                    queue.offer(new TestNode(x, y - 1, temp));
                }
            }
            List<TestNode> res = new ArrayList<>();
            while (temp != null) {
                res.add(temp);
                temp = temp.pre;
            }
            for (int i = res.size() - 1; i >= 0; i--) {
                TestNode node = res.get(i);
                System.out.println("(" + node.x + "," + node.y + ")");
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
