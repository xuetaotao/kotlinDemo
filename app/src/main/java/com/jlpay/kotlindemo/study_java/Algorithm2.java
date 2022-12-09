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


    /////////////////////////////////查找算法/////////////////////////////////////////////////////////////////////

    /**
     * 二分查找
     * 时间复杂度：O(logN)
     * 优势：最省内存，对数时间复杂度
     * 局限性：依赖于顺序表，依赖于数据有序，不适用于数据量太大的场景
     * https://juejin.cn/post/6933241413341708296
     * https://leetcode.cn/problems/binary-search/
     */
    public int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /////////////////////////////////排序算法/////////////////////////////////////////////////////////////////////

    /**
     * 堆排序
     * 时间复杂度：O(N*logN)，空间复杂度：O(1)，不具有稳定性
     * https://www.runoob.com/w3cnote/heap-sort.html
     */
    public void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        //方式一:
        for (int i = 0; i < arr.length; i++) {//O(N)
            heapInsert(arr, i);//O(logN)
        }
        //方式二：可以将上面过程的时间复杂度O（N*logN）变成 O（N）
//        for (int i = arr.length - 1; i >= 0; i--) {
//            heapify(arr, i, arr.length);
//        }
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


    /**
     * 快速排序法
     * 时间复杂度：O(N*logN)，空间复杂度：O(logN)，不具有稳定性
     * https://www.runoob.com/w3cnote/quick-sort-2.html
     */
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
        int mid = left + ((right - left) >> 1);//注意移位操作部分必须括号包住，等价于left+(right-left)/2
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
