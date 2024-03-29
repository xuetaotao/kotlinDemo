package com.jlpay.kotlindemo.study_java;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * 牛客网算法100题：
 * https://www.nowcoder.com/exam/oj
 */
public class AlgorithmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm);
    }

    public void algorDemo(View view) {
        Toast.makeText(this, "牛客网算法100题", Toast.LENGTH_SHORT).show();
    }


    /**
     * BM101 设计LFU缓存结构
     */
    public class bm101 {
        //设置节点结构
        class Node {
            int freq;
            int key;
            int val;

            //初始化
            public Node(int freq, int key, int val) {
                this.freq = freq;
                this.key = key;
                this.val = val;
            }
        }

        //频率到双向链表的哈希表
        private Map<Integer, LinkedList<Node>> freq_mp = new HashMap<>();
        //key到节点的哈希表
        private Map<Integer, Node> mp = new HashMap<>();
        //记录缓存剩余容量
        private int size = 0;
        //记录当前最小频次
        private int min_freq = 0;


        /**
         * lfu design
         *
         * @param operators int整型二维数组 ops
         * @param k         int整型 the k
         * @return int整型一维数组
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        public int[] LFU(int[][] operators, int k) {
            //构建初始化连接
            //链表剩余大小
            this.size = k;
            //获取操作数
            int len = (int) Arrays.stream(operators).filter(x -> x[0] == 2).count();
            int[] res = new int[len];
            //遍历所有操作
            for (int i = 0, j = 0; i < operators.length; i++) {
                if (operators[i][0] == 1)
                    //set操作
                    set(operators[i][1], operators[i][2]);
                else
                    //get操作
                    res[j++] = get(operators[i][1]);
            }
            return res;
        }

        //调用函数时更新频率或者val值
        private void update(Node node, int key, int value) {
            //找到频率
            int freq = node.freq;
            //原频率中删除该节点
            freq_mp.get(freq).remove(node);
            //哈希表中该频率已无节点，直接删除
            if (freq_mp.get(freq).isEmpty()) {
                freq_mp.remove(freq);
                //若当前频率为最小，最小频率加1
                if (min_freq == freq)
                    min_freq++;
            }
            if (!freq_mp.containsKey(freq + 1))
                freq_mp.put(freq + 1, new LinkedList<Node>());
            //插入频率加一的双向链表表头，链表中对应：freq key value
            freq_mp.get(freq + 1).addFirst(new Node(freq + 1, key, value));
            mp.put(key, freq_mp.get(freq + 1).getFirst());
        }

        //set操作函数
        private void set(int key, int value) {
            //在哈希表中找到key值
            if (mp.containsKey(key))
                //若是哈希表中有，则更新值与频率
                update(mp.get(key), key, value);
            else {
                //哈希表中没有，即链表中没有
                if (size == 0) {
                    //满容量取频率最低且最早的删掉
                    int oldkey = freq_mp.get(min_freq).getLast().key;
                    //频率哈希表的链表中删除
                    freq_mp.get(min_freq).removeLast();
                    if (freq_mp.get(min_freq).isEmpty())
                        freq_mp.remove(min_freq);
                    //链表哈希表中删除
                    mp.remove(oldkey);
                }
                //若有空闲则直接加入，容量减1
                else
                    size--;
                //最小频率置为1
                min_freq = 1;
                //在频率为1的双向链表表头插入该键
                if (!freq_mp.containsKey(1))
                    freq_mp.put(1, new LinkedList<Node>());
                freq_mp.get(1).addFirst(new Node(1, key, value));
                //哈希表key值指向链表中该位置
                mp.put(key, freq_mp.get(1).getFirst());
            }
        }

        //get操作函数
        private int get(int key) {
            int res = -1;
            //查找哈希表
            if (mp.containsKey(key)) {
                Node node = mp.get(key);
                //根据哈希表直接获取值
                res = node.val;
                //更新频率
                update(node, key, res);
            }
            return res;
        }
    }


    /**
     * BM100 设计LRU缓存结构
     * 方法：哈希表+双向链表（推荐使用）
     * <p>
     * 知识点1：哈希表
     * 哈希表是一种根据关键码（key）直接访问值（value）的一种数据结构。
     * 而这种直接访问意味着只要知道key就能在O(1)O(1)O(1)时间内得到value，因此哈希表常用来统计频率、
     * 快速检验某个元素是否出现过等。
     * <p>
     * 知识点2：双向链表
     * 双向链表是一种特殊的链表，它除了链表具有的每个节点指向后一个节点的指针外，还拥有一个每个节点
     * 指向前一个节点的指针，因此它可以任意向前或者向后访问，每次更改节点连接状态的时候，需要变动两个指针。
     */
    public class bm100 {

        //哈希表
        private Map<Integer, Node100> map = new HashMap<>();
        //设置一个头
        private Node100 head = new Node100(-1, -1);
        //设置一个尾
        private Node100 tail = new Node100(-1, -1);
        private int size = 0;

        public bm100(int capacity) {
            // write code here
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public int[] LRU(int[][] operators, int k) {
            //构建初始化连接
            //链表剩余大小
            this.size = k;
            this.head.next = this.tail;
            this.tail.pre = this.head;
            //获取操作数
            int len = (int) Arrays.stream(operators).filter(x -> x[0] == 2).count();
            int[] res = new int[len];
            //遍历所有操作
            for (int i = 0, j = 0; i < operators.length; i++) {
                if (operators[i][0] == 1) {
                    //set操作
                    set(operators[i][1], operators[i][2]);
                } else {
                    //get操作
                    res[j++] = get(operators[i][1]);
                }
            }
            return res;
        }

        //获取数据函数
        public int get(int key) {
            int res = -1;
            if (map.containsKey(key)) {
                Node100 node100 = map.get(key);
                res = node100.val;
                moveToHead(node100);
            }
            return res;
        }

        //插入函数
        public void set(int key, int value) {
            //没有见过这个key，新值加入
            if (!map.containsKey(key)) {
                Node100 node100 = new Node100(key, value);
                map.put(key, node100);
                //超出大小，移除最后一个
                if (size <= 0) {
                    removeLast();
                } else {
                    //大小还有剩余，大小减1
                    size--;
                }
                //加到链表头
                insertFirst(node100);
            } else {
                //哈希表中已经有了，即链表里也已经有了
                map.get(key).val = value;
                //访问过后，移到表头
                moveToHead(map.get(key));
            }
        }

        //移到表头函数
        private void moveToHead(Node100 node100) {
            //已经到了表头
            if (node100.pre == head) {
                return;
            }
            //将节点断开，取出来
            node100.pre.next = node100.next;
            node100.next.pre = node100.pre;
            //插入第一个前面
            insertFirst(node100);
        }

        //将节点插入表头函数
        private void insertFirst(Node100 node100) {
            node100.pre = head;
            node100.next = head.next;
            head.next.pre = node100;
            head.next = node100;
        }

        //删去表尾函数，最近最少使用
        private void removeLast() {
            //哈希表去掉key
            map.remove(tail.pre.key);
            //断连该节点
            tail.pre.pre.next = tail;
            tail.pre = tail.pre.pre;
        }
    }

    //设置双向链表结构
    static class Node100 {
        int key;
        int val;
        Node100 pre;
        Node100 next;

        //初始化
        public Node100(int key, int val) {
            this.key = key;
            this.val = val;
            this.pre = null;
            this.next = null;
        }
    }


    /**
     * BM99 顺时针旋转矩阵
     */
    public int[][] bm99(int[][] mat, int n) {
        int length = mat.length;
        //矩阵转置
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                //交换上三角与下三角对应的元素
                int temp = mat[i][j];
                mat[i][j] = mat[j][i];
                mat[j][i] = temp;
            }
        }
        //每行翻转
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length / 2; j++) {
                int temp = mat[i][j];
                mat[i][j] = mat[i][length - j - 1];
                mat[i][length - j - 1] = temp;
            }
        }
        return mat;
    }


    /**
     * BM98 螺旋矩阵
     * 方法：边界模拟法（推荐使用）
     */
    public ArrayList<Integer> bm98(int[][] matrix) {
        ArrayList<Integer> res = new ArrayList<>();
        //先排除特殊情况
        if (matrix.length == 0) {
            return res;
        }
        //左边界
        int left = 0;
        //右边界
        int right = matrix[0].length - 1;//列数-1
        //上边界
        int up = 0;
        //下边界
        int down = matrix.length - 1;//行数-1
        //直到边界重合
        while (left <= right && up <= down) {
            //上边界的从左到右
            for (int i = left; i <= right; i++) {
                res.add(matrix[up][i]);
            }
            //上边界向下
            up++;
            if (up > down) {
                break;
            }
            //右边界的从上到下
            for (int i = up; i <= down; i++) {
                res.add(matrix[i][right]);
            }
            //右边界向左
            right--;
            if (left > right) {
                break;
            }
            //下边界的从右到左
            for (int i = right; i >= left; i--) {
                res.add(matrix[down][i]);
            }
            //下边界向上
            down--;
            if (up > down) {
                break;
            }
            //左边界的从下到上
            for (int i = down; i >= up; i--) {
                res.add(matrix[i][left]);
            }
            //左边界向右
            left++;
            if (left > right) {
                break;
            }
        }
        return res;
    }


    /**
     * BM97 旋转数组
     */
    public int[] bm97(int n, int m, int[] a) {
        //取余，因为每次长度为n的旋转数组相当于没有变化
        m = m % n;
        //第一次逆转全部数组元素
        reverse97(a, 0, n - 1);
        //第二次只逆转开头m个
        reverse97(a, 0, m - 1);
        //第三次只逆转结尾m个
        reverse97(a, m, n - 1);
        return a;
    }

    //反转函数
    public void reverse97(int[] nums, int start, int end) {
        while (start < end) {
            swap97(nums, start++, end--);
        }
    }

    //交换函数
    public void swap97(int[] nums, int a, int b) {
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }


    /**
     * BM96 主持人调度（二）
     * 方法一：排序+遍历比较（推荐使用）
     */
    public int bm96(int n, int[][] startEnd) {
        int[] start = new int[n];
        int[] end = new int[n];
        //分别得到活动起始时间
        for (int i = 0; i < n; i++) {
            start[i] = startEnd[i][0];
            end[i] = startEnd[i][1];
        }
        //单独排序
//        Arrays.sort(start, 0, start.length);
//        Arrays.sort(end, 0, end.length);
        Arrays.sort(start);
        Arrays.sort(end);
        int res = 0;
        int j = 0;
        for (int i = 0; i < n; i++) {
            //新开始的节目大于上一轮结束的时间，主持人不变
            if (start[i] >= end[j]) {
                j++;
            } else {
                res++;//主持人增加
            }
        }
        return res;
    }

    //方法二：重载排序+大顶堆（扩展思路）
    public int bm96Two(int n, int[][] startEnd) {
//        int A[] = new int[n];
//        int k = 0;
        //按列排序，按开始时间递增排
        Arrays.sort(startEnd, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] > o2[0]) {
                    return 1;
                } else if (o1[0] == o2[0]) {
                    return 0;
                } else {
                    return -1;//o1 o2
                }
            }
        });
        //小顶堆
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>();
        //可能有负区间
        priorityQueue.offer(Integer.MIN_VALUE);
        for (int i = 0; i < n; i++) {
            //最近的活动结束时间小于当前活动开始时间
            if (priorityQueue.peek() <= startEnd[i][0]) {
                priorityQueue.poll();
            }
            priorityQueue.offer(startEnd[i][1]);
        }
        //剩余的活动等于主持人数
        return priorityQueue.size();
    }


    /**
     * BM95 分糖果问题
     * <p>
     * 知识点：贪心思想
     * 贪心思想属于动态规划思想中的一种，其基本原理是找出整体当中给的每个局部子结构的最优解，
     * 并且最终将所有的这些局部最优解结合起来形成整体上的一个最优解。
     */
    public int bm95(int[] arr) {
        int n = arr.length;
        if (n <= 1) {
            return n;
        }
        int[] nums = new int[n];
        //初始化
        Arrays.fill(nums, 1);
        //从左到右遍历
        for (int i = 1; i < arr.length; i++) {
            //如果右边在递增，每次增加一个
            if (arr[i] > arr[i - 1]) {
                nums[i] = nums[i - 1] + 1;
            }
        }
        //记录总糖果数
        int res = nums[arr.length - 1];
        //从右到左遍历
        for (int i = arr.length - 2; i >= 0; i--) {
            //如果左边更大但是糖果数更小
            if (arr[i] > arr[i + 1] && nums[i] <= nums[i + 1]) {
                nums[i] = nums[i + 1] + 1;
            }
            //累加和
            res += nums[i];
        }
        return res;
    }


    /**
     * BM94 接雨水问题
     */
    public long bm94(int[] arr) {
        //排除空数组
        if (arr.length == 0) {
            return 0;
        }
        long res = 0;
        //左右双指针
        int left = 0;
        int right = arr.length - 1;
        //中间区域的边界高度
        int maxL = 0;
        int maxR = 0;
        //直到左右指针相遇
        while (left < right) {
            //每次维护往中间的最大边界
            maxL = Math.max(maxL, arr[left]);
            maxR = Math.max(maxR, arr[right]);
            //较短的边界确定该格子的水量
            if (maxR > maxL) {
                res += maxL - arr[left++];
            } else {
                res += maxR - arr[right--];
            }
        }
        return res;
    }


    /**
     * BM93 盛水最多的容器
     * 直接用较短边长乘底部两边距离就可以得到当前情况下的容积。但是要怎么找最大值呢？
     * <p>
     * 可以利用贪心思想：我们都知道容积与最短边长和底边长有关，与长的底边一定以首尾为边，
     * 但是首尾不一定够高，中间可能会出现更高但是底边更短的情况，因此我们可以使用对撞双指针向中间靠，
     * 这样底边长会缩短，因此还想要有更大容积只能是增加最短变长，此时我们每次指针移动就移动较短的一边，
     * 因为贪心思想下较长的一边比较短的一边更可能出现更大容积。
     */
    public int bm93(int[] height) {
        //排除不能形成容器的情况
        if (height.length < 2) {
            return 0;
        }
        int res = 0;
        //双指针左右界
        int left = 0;
        int right = height.length - 1;
        //共同遍历完所有的数组
        while (left < right) {
            //计算区域水容量
            int capacity = Math.min(height[left], height[right]) * (right - left);
            //维护最大值
            res = Math.max(res, capacity);
            //优先舍弃较短的边
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return res;
    }

    /**
     * BM92 最长无重复子数组
     * 方法：滑动窗口(推荐使用)
     */
    public int bm92(int[] arr) {
        //哈希表记录窗口内非重复的数字
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        int res = 0;
        //设置窗口左右边界
        for (int left = 0, right = 0; right < arr.length; right++) {
            //窗口右移进入哈希表统计出现次数
            if (hashMap.containsKey(arr[right])) {
                hashMap.put(arr[right], hashMap.get(arr[right]) + 1);
            } else {
                hashMap.put(arr[right], 1);
            }
            //出现次数大于1，则窗口内有重复
            while (hashMap.get(arr[right]) > 1) {
                //窗口左移，同时减去该数字的出现次数
                hashMap.put(arr[left], hashMap.get(arr[left++]) - 1);
            }
            //维护子数组长度最大值
            res = Math.max(res, right - left + 1);
        }
        return res;
    }

    /**
     * BM91 反转字符串
     * 开辟一个和str长度大小相同的一个字符串ans，把传入的str倒序赋值到ans字符串上，
     */
    public String bm91(String str) {
        char[] ans = str.toCharArray();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            ans[i] = str.charAt(len - 1 - i);
        }
        return new String(ans);
    }

    public String bm91Two(String str) {
        char[] ans = str.toCharArray();
        int len = str.length();
        for (int i = 0; i < len / 2; i++) {
            char t = ans[i];
            ans[i] = ans[len - 1 - i];
            ans[len - 1 - i] = t;
        }
        return new String(ans);
    }

    public String bm91Three(String str) {
        StringBuffer sb = new StringBuffer(str);//此方法针对的是io流，不能针对字符串。
        return sb.reverse().toString();
    }


    /**
     * BM90 最小覆盖子串
     * 方法：哈希表匹配（推荐使用）
     */
    public String bm90(String S, String T) {
        int cnt = S.length() + 1;
        //记录目标字符串T的字符个数
        int[] hash = new int[128];
        for (int i = 0; i < T.length(); i++) {
            //初始化哈希表都为负数，找的时候再加为正
            hash[T.charAt(i)] -= 1;
        }
        int slow = 0, fast = 0;
        //记录左右区间
        int left = -1, right = -1;
        for (; fast < S.length(); fast++) {
            char c = S.charAt(fast);
            //目标字符匹配+1
            hash[c]++;
            //没有小于0的说明都覆盖了，缩小窗口
            while (check90(hash)) {
                //取最优解
                if (cnt > fast - slow + 1) {
                    cnt = fast - slow + 1;
                    left = slow;
                    right = fast;
                }
                c = S.charAt(slow);
                //缩小窗口的时候减1
                hash[c]--;
                //窗口缩小
                slow++;
            }
        }
        //找不到的情况
        if (left == -1) {
            return "";
        }
        return S.substring(left, right + 1);
    }

    //检查是否有小于0的
    public boolean check90(int[] hash) {
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] < 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * BM89 合并区间
     * 方法: 排序+贪心(推荐使用)
     * 知识点：贪心思想
     * 贪心思想属于动态规划思想中的一种，其基本原理是找出整体当中给的每个局部子结构的最优解，
     * 并且最终将所有的这些局部最优解结合起来形成整体上的一个最优解。
     */
    public ArrayList<Interval> bm89(ArrayList<Interval> intervals) {
        ArrayList<Interval> res = new ArrayList<>();
        //去除特殊情况
        if (intervals.size() == 0) {
            return res;
        }
        //重载比较，按照区间首排序
        Collections.sort(intervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval o1, Interval o2) {
                if (o1.start != o2.start) {
                    //按照起点位置升序排列
                    return o1.start - o2.start;//负数 o1  o2
                } else {
                    //如果起点位置相同，按照尾点升序排序
                    return o1.end - o2.end;//负数 o1  o2
                }
            }
        });
        //放入第一个区间,起点值最小的区间
        res.add(intervals.get(0));
        int count = 0;
        //遍历后续区间，查看是否与末尾有重叠
        for (int i = 1; i < intervals.size(); i++) {
            Interval o1 = intervals.get(i);
            Interval origin = res.get(count);
            if (o1.start > origin.end) {
                //没有区间重叠
                res.add(o1);
                count++;
            } else {
                //区间有重叠，更新结尾
                res.remove(count);
                Interval s = new Interval(origin.start, o1.end);
                if (o1.end < origin.end) {
                    s.end = origin.end;
                }
                res.add(s);
            }
        }
        return res;
    }

    public class Interval {
        int start;
        int end;

        Interval() {
            start = 0;
            end = 0;
        }

        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }

    /**
     * BM88 判断是否为回文字符串
     * 方法一：首尾依次比较法（推荐使用）
     * 知识点：双指针
     */
    public boolean bm88(String str) {
        //首指针
        int left = 0;
        //尾指针
        int right = str.length() - 1;
        //首尾往中间靠
        while (left < right) {
            //比较前后是否相同
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    //方法二：反转字符串比较法（扩展思路）
    public boolean bm88Two(String str) {
        StringBuffer sb = new StringBuffer(str);
        String s = sb.reverse().toString();
        if (s.equals(str)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * BM87 合并两个有序的数组
     * <p>
     * 方法：归并排序思想(推荐使用)
     * 知识点：双指针
     * 双指针指的是在遍历对象的过程中，不是普通的使用单个指针进行访问，而是使用两个指针（
     * 特殊情况甚至可以多个），两个指针或是同方向访问两个链表、或是同方向访问一个链表（快慢指针）、
     * 或是相反方向扫描（对撞指针），从而达到我们需要的目的。
     */
    public void bm87(int A[], int m, int B[], int n) {
        //指向数组A的结尾
        int i = m - 1;
        //指向数组B的结尾
        int j = n - 1;
        //指向数组A空间的结尾处
        int k = m + n - 1;
        //从两个数组最大的元素开始，直到某一个数组遍历完
        while (i >= 0 && j >= 0) {
            //将较大的元素放到最后
            if (A[i] > B[j]) {
                A[k--] = A[i--];
            } else {
                A[k--] = B[j--];
            }
        }
        //数组A遍历完了，数组B还有，则还需要添加到数组A前面
        if (i < 0) {
            while (j >= 0) {
                A[k--] = B[j--];
            }
        }
        //数组B遍历完了，数组A前面正好有，不用再添加
    }


    /**
     * BM86 大数加法
     * 方法：模拟法（建议使用）
     */
    public String bm86(String s, String t) {
        //若是其中一个为空，返回另一个
        if (s.length() <= 0) {
            return t;
        }
        if (t.length() <= 0) {
            return s;
        }
        //让s为较长的，t为较短的
        if (s.length() < t.length()) {
            String temp = s;
            s = t;
            t = temp;
        }
        int carry = 0;//进位标志
        char[] res = new char[s.length()];
        //从后往前遍历较长的字符串
        for (int i = s.length() - 1; i >= 0; i--) {
            //转数字加上进位
            int temp = s.charAt(i) - '0' + carry;
            //转较短的字符串相应的从后往前的下标
            int j = i - s.length() + t.length();
            //如果较短字符串还有
            if (j >= 0) {
                //转数组相加
                temp += t.charAt(j) - '0';
            }
            //取进位
            carry = temp / 10;
            //去十位
            temp = temp % 10;
            //修改结果
            res[i] = (char) (temp + '0');
        }
        String output = String.valueOf(res);
        //最后的进位
        if (carry == 1) {
            output = '1' + output;
        }
        return output;
    }

    /**
     * BM85 验证IP地址
     * 方法一：分割字符串比较法（推荐使用）
     * step 1：写一个split函数（或者内置函数）。
     * step 2：遍历IP字符串，遇到.或者:将其分开储存在一个数组中。
     * step 3：遍历数组，对于IPv4，需要依次验证分组为4，分割不能缺省，没有前缀0或者其他字符，数字在0-255范围内。
     * step 4：对于IPv6，需要依次验证分组为8，分割不能缺省，每组不能超过4位，不能出现除数字小大写a-f以外的字符。
     */
    public String bm85(String IP) {
        if (isIPv4EightyFive(IP)) {
            return "IPv4";
        } else if (isIPv6EightyFive(IP)) {
            return "IPv6";
        } else {
            return "Neither";
        }
    }

    public boolean isIPv4EightyFive(String ip) {
        if (ip.indexOf('.') == -1) {
            return false;
        }
        String[] s = ip.split("\\.");
        //IPv4必定为4组
        if (s.length != 4) {
            return false;
        }
        for (int i = 0; i < s.length; i++) {
            //不可缺省，有一个分割为零，说明两个点相连
            if (s[i].length() == 0) {
                return false;
            }
            //比较数字位数及不为零时不能有前缀零
            if (s[i].length() < 0 || s[i].length() > 3 || (s[i].charAt(0) == '0' && s[i].length() != 1)) {
                return false;
            }
            int num = 0;
            //遍历每个分割字符串，必须为数字
            for (int j = 0; j < s[i].length(); j++) {
                char c = s[i].charAt(j);
                if (c < '0' || c > '9') {
                    return false;
                }
                //转化为数字比较，0-255之间
                num = num * 10 + (int) (c - '0');
                if (num < 0 || num > 255) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isIPv6EightyFive(String ip) {
        if (ip.indexOf(':') == -1) {
            return false;
        }
        //https://blog.csdn.net/qq_39936292/article/details/89359855
        //字符串以regex方式进行split，获得数组长度：length（方便理解），若limit参数大于等于这个length或limit参数
        // 为非正数，则字符串以regex方式进行最大split；若limit参数处于0和length之间，则字符串以regex方式进行
        // limit- 1次split，split后剩下的字符不会再进行split。
        String[] s = ip.split(":", -1);
        //IPv6必定为8组
        if (s.length != 8) {
            return false;
        }
        for (int i = 0; i < s.length; i++) {
            //每个分割不能缺省，不能超过4位
            if (s[i].length() == 0 || s[i].length() > 4) {
                return false;
            }
            for (int j = 0; j < s[i].length(); j++) {
                //不能出现a-fA-F以外的大小写字符
                char c = s[i].charAt(j);
                boolean expr = (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
                if (!expr) {
                    return false;
                }
            }
        }
        return true;
    }

    //正则表达式（扩展思路）
    //step 1：IPv4的正则表达式限制数字在0-255，且没有前缀0，用3个点隔开共4组。
    //step 2：IPv6的正则表达式限制出现8组，0-9a-fA-F的数，个数必须是1-4个，用冒号隔开。
    //step 3：调用函数依次比较给定字符串与模板串之间是否匹配，匹配哪一个就属于哪一种IP地址，否则都不是。
    public String bm85Two(String IP) {
        //正则表达式限制0-255 且没有前缀0 四组齐全
        String ipv4 = "(([0-9][1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";
        Pattern ipv4Pattern = Pattern.compile(ipv4);
        //正则表达式限制出现8组，0-9a-fA-F的数，个数必须是1-4个
        String ipv6 = "([0-9a-fA-F]{1,4}\\:){7}[0-9a-fA-F]{1,4}";
        Pattern ipv6Pattern = Pattern.compile(ipv6);
        //调用正则匹配函数
        if (ipv4Pattern.matcher(IP).matches()) {
            return "IPv4";
        } else if (ipv6Pattern.matcher(IP).matches()) {
            return "IPv6";
        } else {
            return "Neither";
        }
    }


    /**
     * BM84 最长公共前缀
     * 方法：遍历查找(推荐使用)
     */
    public String bm84(String[] strs) {
        int n = strs.length;
        //空字符串数组
        if (n == 0) {
            return "";
        }
        //遍历第一个字符串的长度
        for (int i = 0; i < strs[0].length(); i++) {
            char temp = strs[0].charAt(i);
            //遍历后续的字符串
            for (int j = 1; j < n; j++) {
                //比较每个字符串该位置是否和第一个相同
                if (i == strs[j].length() || strs[j].charAt(i) != temp) {
                    //不相同则结束
                    return strs[0].substring(0, i);
                }
            }
        }
        //后续字符串有整个字一个字符串的前缀
        return strs[0];
    }


    /**
     * BM83 字符串变形
     * 方法一：双逆转（推荐使用）
     */
    public String bm83(String s, int n) {
        if (n == 0) {
            return s;
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < n; i++) {
            //大小写转换
            if (s.charAt(i) <= 'Z' && s.charAt(i) >= 'A') {
                res.append((char) (s.charAt(i) - 'A' + 'a'));
            } else if (s.charAt(i) <= 'z' && s.charAt(i) >= 'a') {
                res.append((char) (s.charAt(i) - 'a' + 'A'));
            } else {
                //空格直接复制
                res.append(s.charAt(i));
            }
        }
        //翻转整个字符串
        res = res.reverse();
        for (int i = 0; i < n; i++) {
            int j = i;
            //以空格为界，二次翻转
            while (j < n && res.charAt(j) != ' ') {
                j++;
            }
            String temp = res.substring(i, j);
            StringBuffer stringBuffer = new StringBuffer(temp);
            temp = stringBuffer.reverse().toString();
            res.replace(i, j, temp);
            i = j;
        }
        return res.toString();
    }

    //分割字符串+栈（扩展思路）
    public String bm83Two(String s, int n) {
        if (n == 0) {
            return s;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < n; i++) {
            //大小写转换
            if (s.charAt(i) <= 'Z' && s.charAt(i) >= 'A') {
                stringBuffer.append((char) (s.charAt(i) - 'A' + 'a'));
            } else if (s.charAt(i) <= 'z' && s.charAt(i) >= 'a') {
                stringBuffer.append((char) (s.charAt(i) - 'a' + 'A'));
            } else {
                //空格直接复制
                stringBuffer.append(s.charAt(i));
            }
        }
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            int j = i;
            while (j < n && s.charAt(j) != ' ') {
                j++;
            }
            //单词进栈
            stack.push(stringBuffer.substring(i, j));
            i = j;
        }
        //排除结尾空格的特殊情况
        if (s.charAt(n - 1) == ' ') {
            stringBuffer = new StringBuffer(" ");
        } else {
            stringBuffer = new StringBuffer();
        }
        //栈遵循先进后厨，单词顺序是反的
        while (!stack.isEmpty()) {
//            stringBuffer.append(stack.peek());
//            stack.pop();
            stringBuffer.append(stack.pop());
            if (!stack.isEmpty()) {
                stringBuffer.append(" ");
            }
        }
        return stringBuffer.toString();
    }


    /**
     * BM65 最长公共子序列(二)
     * 方法一：动态规划+递归获取（推荐使用）
     */
    private String x = "";
    private String y = "";

    public String bm65(String s1, String s2) {
        //特殊情况
        if (s1.length() == 0 || s2.length() == 0) {
            return "-1";
        }
        int len1 = s1.length();
        int len2 = s2.length();
        x = s1;
        y = s2;
        //dp[i][j]表示第一个字符串到第i位，第二个字符串到第j位为止的最长公共子序列长度
        int[][] dp = new int[len1 + 1][len2 + 1];
        //动态规划数组相加的方向
        int[][] b = new int[len1 + 1][len2 + 1];
        //遍历两个字符串每个位置求的最长长度
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                //遇到两个字符相等
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    //考虑由二者都向前一位
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    //来自于左上方
                    b[i][j] = 1;
                }
                //遇到的两个字符不同
                else {
                    //左边的选择更大，即第一个字符串后退一位
                    if (dp[i - 1][j] > dp[i][j - 1]) {
                        dp[i][j] = dp[i - 1][j];
                        //来自于左方
                        b[i][j] = 2;
                    }
                    //右边的选择更大，即第二个字符串后退一位
                    else {
                        dp[i][j] = dp[i][j - 1];
                        //来自于上方
                        b[i][j] = 3;
                    }
                }
            }
        }
        //获取答案字符串
        String res = ans65(len1, len2, b);
        //检查答案是否位空
        if (res.isEmpty()) {
            return "-1";
        } else {
            return res;
        }
    }

    //获取最长公共子序列
    String ans65(int i, int j, int[][] b) {
        String res = "";
        //递归终止条件
        if (i == 0 || j == 0)
            return res;
        //根据方向，往前递归，然后添加本级字符
        if (b[i][j] == 1) {
            res += ans65(i - 1, j - 1, b);
            res += x.charAt(i - 1);
        } else if (b[i][j] == 2)
            res += ans65(i - 1, j, b);
        else if (b[i][j] == 3)
            res += ans65(i, j - 1, b);
        return res;
    }


    /**
     * BM64 最小花费爬楼梯
     * 知识点：动态规划
     */
    public int bm64(int[] cost) {
        //dp[i]表示爬到第i阶楼梯需要的最小花费
        int[] dp = new int[cost.length + 1];
        for (int i = 2; i <= cost.length; i++) {
            //每次选取最小的方案
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }
        return dp[cost.length];
    }


    /**
     * BM63 跳台阶
     * 方法一：递归
     */
    public int bm63(int target) {
        if (target <= 1) {
            return 1;
        }
        return bm63(target - 1) + bm63(target - 2);
    }

    //记忆化搜索
    private int f63[] = new int[50];

    public int bm63Two(int target) {
        if (target <= 1) {
            return 1;
        }
        if (f63[target] > 0) {
            return f63[target];
        }
        return f63[target] = (bm63Two(target - 1) + bm63Two(target - 2));
    }

    //方法三：动态规划
    public int bm63Three(int target) {
        int a = 1, b = 1, c = 1;
        for (int i = 2; i <= target; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        return c;
    }

    /**
     * BM62 斐波那契数列
     * <p>
     * 知识点：动态规划
     * 动态规划算法的基本思想是：将待求解的问题分解成若干个相互联系的子问题，先求解子问题，
     * 然后从这些子问题的解得到原问题的解；对于重复出现的子问题，只在第一次遇到的时候对它进行求解，
     * 并把答案保存起来，让以后再次遇到时直接引用答案，不必重新求解。动态规划算法将问题的解决方案
     * 视为一系列决策的结果。
     * <p>
     * 具体做法：
     * step 1：低于2项的数列，直接返回n。
     * step 2：初始化第0项，与第1项分别为0，1.
     * step 3：从第2项开始，逐渐按照公式累加，并更新相加数始终为下一项的前两项。
     */
    public int bm62(int n) {
        //从0开始，第0项是0，第一项是1
        if (n <= 1) {
            return n;
        }
        int res = 0;
        int a = 0;
        int b = 1;
        //因n=2时也为1，初始化的时候把a=0，b=1
        for (int i = 2; i <= n; i++) {
            //第三项开始是前两项的和,然后保留最新的两项，更新数据相加
            res = (a + b);
            a = b;
            b = res;
        }
        return res;
    }

    /**
     * 知识点：递归
     * 递归是一个过程或函数在其定义或说明中有直接或间接调用自身的一种方法，
     * 它通常把一个大型复杂的问题层层转化为一个与原问题相似的规模较小的问题来求解。
     * 因此递归过程，最重要的就是查看能不能讲原本的问题分解为更小的子问题，这是使用递归的关键。
     */
    public int bm62Two(int n) {
        //从0开始，第0项是0，第一项是1
        if (n <= 1) {
            return n;
        } else {
            //根据公式递归调用函数
            return bm62Two(n - 1) + bm62Two(n - 2);
        }
    }

    public int bm62Three(int n) {
        //从0开始，第0项是0，第一项是1
        if (n <= 1) {
            return n;
        }
        int[] fib = new int[n + 1];
        fib[0] = 0;
        fib[1] = 1;
        //依次相加
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 2] + fib[i - 1];
        }
        return fib[n];
    }

    /**
     * BM61 矩阵最长递增路径
     * <p>
     * 知识点：深度优先搜索（dfs）
     * 既然是查找最长的递增路径长度，那我们首先要找到这个路径的起点，起点不好直接找到，就从上到下从左到右遍历矩阵的
     * 每个元素。然后以每个元素都可以作为起点查找它能到达的最长递增路径。如何查找以某个点为起点的最长递增路径呢？
     * 我们可以考虑深度优先搜索，因为我们查找递增路径的时候，每次选中路径一个点，然后找到与该点相邻的递增位置，
     * 相当于进入这个相邻的点，继续查找递增路径，这就是递归的子问题。
     */
    //记录四个方向
    private int[][] dirs61 = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private int n61, m61;

    public int bm61(int[][] matrix) {
        //矩阵不为空
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int res = 0;
        n61 = matrix.length;
        m61 = matrix[0].length;
        //i，j处的单元格拥有的最长递增路径
        int[][] dp = new int[m61 + 1][n61 + 1];
        for (int i = 0; i < n61; i++) {
            for (int j = 0; j < m61; j++) {
                //更新最大值
                res = Math.max(res, dfs61(matrix, dp, i, j));
            }
        }
        return res;
    }

    //深度优先搜索，返回最大单元格数
    public int dfs61(int[][] matrix, int[][] dp, int i, int j) {
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        dp[i][j]++;
        for (int k = 0; k < 4; k++) {
            int nexti = i + dirs61[k][0];
            int nextj = j + dirs61[k][1];
            //判断条件
            if (nexti >= 0 && nexti < n61 && nextj >= 0 && nextj < m61 && matrix[nexti][nextj] > matrix[i][j]) {
                dp[i][j] = Math.max(dp[i][j], dfs61(matrix, dp, nexti, nextj) + 1);
            }
        }
        return dp[i][j];
    }

    /**
     * 知识点：广度优先搜索（bfs）
     * 我们可以将矩阵看成是一个有向图，一个元素到另一个元素递增，代表有向图的箭头。
     * 这样我们可以根据有向图的出度入度找到最长的路径，且这个路径在矩阵中就是递增的。
     * <p>
     * 利用拓扑排序的思想，从所有出度为0的单元格开始进行广度优先搜索。
     */
    public int bm61Two(int[][] matrix) {
        int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int n, m;
        //矩阵不为空
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int res = 0;
        n = matrix.length;
        m = matrix[0].length;
        //i，j处的单元格拥有的最长递增路径
        int[][] outdegrees = new int[m + 1][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < 4; k++) {
                    int nexti = i + dirs[k][0];
                    int nextj = j + dirs[k][1];
                    if (nexti >= 0 && nexti < n && nextj >= 0 && nextj < m &&
                            matrix[nexti][nextj] > matrix[i][j]) {
                        //符合条件，记录出度
                        outdegrees[i][j]++;
                    }
                }
            }
        }
        //辅助队列
        Queue<Integer> q1 = new LinkedList<>();
        Queue<Integer> q2 = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (outdegrees[i][j] == 0) {
                    //找到出度为0的入队列
                    q1.offer(i);
                    q2.offer(j);
                }
            }
        }
        while (!q1.isEmpty()) {
            res++;
            int size = q1.size();
            for (int x = 0; x < size; x++) {
                int i = q1.poll();
                int j = q2.poll();
                //四个方向
                for (int k = 0; k < 4; k++) {
                    int nexti = i + dirs[k][0];
                    int nextj = j + dirs[k][1];
                    //逆向搜索，所以下一步是小于
                    if (nexti >= 0 && nexti < n && nextj >= 0 && nextj < m && matrix[nexti][nextj] < matrix[i][j]) {
                        //符合条件，出度递减
                        outdegrees[nexti][nextj]--;
                        if (outdegrees[nexti][nextj] == 0) {
                            q1.offer(nexti);
                            q2.offer(nextj);
                        }
                    }
                }
            }
        }
        return res;
    }


    /**
     * BM60 括号生成
     * 知识点：递归与回溯
     */
    public ArrayList<String> bm60(int n) {
        //记录结果
        ArrayList<String> res = new ArrayList<>();
        //递归
        recursion60(0, 0, "", res, n);
        return res;
    }

    public void recursion60(int left, int right, String temp, ArrayList<String> res, int n) {
        //左右括号都用完了，就加入结果
        if (left == n && right == n) {
            res.add(temp);
            return;
        }
        //使用一次左括号
        if (left < n) {
            recursion60(left + 1, right, temp + "(", res, n);
        }
        //使用右括号个数必须少于左括号
        if (right < n && left > right) {
            recursion60(left, right + 1, temp + ")", res, n);
        }
    }


    /**
     * BM59 N皇后问题
     * 方法：递归（推荐使用）
     * n个皇后，不同行不同列，那么肯定棋盘每行都会有一个皇后，每列都会有一个皇后。
     * 如果我们确定了第一个皇后的行号与列号，则相当于接下来在n−1行中查找n−1个皇后，
     * 这就是一个子问题，因此使用递归；
     */
    private int res59;

    public int bm59(int n) {
        res59 = 0;
        //下标为行号，元素为列号，记录皇后位置
        int[] pos = new int[n];
        Arrays.fill(pos, 0);
        //递归
        recursion59(n, 0, pos);
        return res59;
    }

    //递归查找皇后种类
    public void recursion59(int n, int row, int[] pos) {
        //完成全部行都选择了位置
        if (row == n) {
            res59++;
            return;
        }
        //遍历所有列
        for (int i = 0; i < n; i++) {
            //检查该位置是否符合条件
            if (isValid59(pos, row, i)) {
                //加入位置
                pos[row] = i;
                //递归继续查找
                recursion59(n, row + 1, pos);
            }
        }
    }

    //判断皇后是否符合条件
    public boolean isValid59(int[] pos, int row, int col) {
        //遍历所有已经记录的行
        for (int i = 0; i < row; i++) {
            //不能同行同列同一斜线
            if (row == i || col == pos[i] || Math.abs(row - i) == Math.abs(col - pos[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * BM58 字符串的排列
     * 方法：递归+回溯（推荐使用）
     */
    public ArrayList<String> bm58(String str) {
        ArrayList<String> res = new ArrayList<>();
        if (str == null || str.length() == 0) {
            return res;
        }
        //转字符数组
        char[] charStr = str.toCharArray();
        // 按字典序排序
        Arrays.sort(charStr);
        boolean[] vis = new boolean[str.length()];
        //标记每个位置的字符是否被使用过
        Arrays.fill(vis, false);
        StringBuffer temp = new StringBuffer();
        //递归获取
        recursion58(res, charStr, temp, vis);
        return res;
    }

    public void recursion58(ArrayList<String> res, char[] str, StringBuffer temp, boolean[] vis) {
        //临时字符串满了加入输出
        if (temp.length() == str.length) {
            res.add(new String(temp));
            return;
        }
        //遍历所有元素选取一个加入
        for (int i = 0; i < str.length; i++) {
            //如果该元素已经被加入了则不需要再加入了
            if (vis[i]) {
                continue;
            }
            if (i > 0 && str[i - 1] == str[i] && !vis[i - 1]) {
                //当前的元素str[i]与同一层的前一个元素str[i-1]相同且str[i-1]已经用过了
                continue;
            }
            //标记为使用过
            vis[i] = true;
            //加入临时字符串
            temp.append(str[i]);
            recursion58(res, str, temp, vis);
            //回溯
            vis[i] = false;
            temp.deleteCharAt(temp.length() - 1);
        }
    }


    /**
     * BM57 岛屿数量
     * <p>
     * 知识点：深度优先搜索（dfs） 深度优先搜索一般用于树或者图的遍历，其他有分支的（如二维矩阵）也适用。
     * 它的原理是从初始点开始，一直沿着同一个分支遍历，直到该分支结束，然后回溯到上一级继续沿着一个分支走到底，
     * 如此往复，直到所有的节点都有被访问到。
     * <p>
     * 递归实现
     */
    public int bm57(char[][] grid) {
        int n = grid.length;//获取行数
        //空矩阵的情况
        if (n == 0) {
            return 0;
        }
        int m = grid[0].length;//获取列数
        //记录岛屿数
        int count = 0;
        //遍历矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                //遍历到1的情况
                if (grid[i][j] == '1') {
                    //计数
                    count++;
                    //将与这个1相邻的所有1置为0
                    dfs57(grid, i, j);
                }
            }
        }
        return count;
    }

    //深度优先遍历与i，j相邻的所有1
    public void dfs57(char[][] grid, int i, int j) {
        int n = grid.length;//获取行数
        int m = grid[0].length;//获取列数
        // 置为0
        grid[i][j] = '0';
        //后续四个方向遍历
        if (i - 1 >= 0 && grid[i - 1][j] == '1') {
            dfs57(grid, i - 1, j);
        }
        if (i + 1 < n && grid[i + 1][j] == '1') {
            dfs57(grid, i + 1, j);
        }
        if (j - 1 >= 0 && grid[i][j - 1] == '1') {
            dfs57(grid, i, j - 1);
        }
        if (j + 1 < m && grid[i][j + 1] == '1') {
            dfs57(grid, i, j + 1);
        }
    }

    /**
     * 知识点：广度优先搜索（bfs）
     * <p>
     * 广度优先搜索与深度优先搜索不同，它是将与某个节点直接相连的其它所有节点依次访问一次之后，
     * 再往更深处，进入与其他节点直接相连的节点。bfs的时候我们常常会借助队列的先进先出，因为从某个节点出发，
     * 我们将与它直接相连的节点都加入队列，它们优先进入，则会优先弹出，在它们弹出的时候再将与它们直接相连的节点加入，
     * 由此就可以依次按层访问。
     */
    public int bm57Two(char[][] grid) {
        int n = grid.length;//获取行数
        //空矩阵的情况
        if (n == 0) {
            return 0;
        }
        int m = grid[0].length;
        //记录岛屿数
        int count = 0;
        //遍历矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                //遇到1要将这个1及与其相邻的1都置为0
                if (grid[i][j] == '1') {
                    //岛屿数增加
                    count++;
                    grid[i][j] = '0';
                    //记录后续bfs的坐标
                    Queue<Integer> queue1 = new LinkedList<>();
                    Queue<Integer> queue2 = new LinkedList<>();
                    queue1.offer(i);
                    queue2.offer(j);
                    //bfs
                    while (!queue1.isEmpty()) {
                        int row = queue1.poll();
                        int col = queue2.poll();
                        //四个方向依次检查：不越界且为1
                        if (row - 1 >= 0 && grid[row - 1][col] == '1') {
                            queue1.offer(row - 1);
                            queue2.offer(col);
                            grid[row - 1][col] = '0';
                        }
                        if (row + 1 < n && grid[row + 1][col] == '1') {
                            queue1.offer(row + 1);
                            queue2.offer(col);
                            grid[row + 1][col] = '0';
                        }
                        if (col - 1 >= 0 && grid[row][col - 1] == '1') {
                            queue1.offer(row);
                            queue2.offer(col - 1);
                            grid[row][col - 1] = '0';
                        }
                        if (col + 1 < m && grid[row][col + 1] == '1') {
                            queue1.offer(row);
                            queue2.offer(col + 1);
                            grid[row][col + 1] = '0';
                        }
                    }
                }
            }
        }
        return count;
    }


    /**
     * BM56 有重复项数字的全排列
     */
    public ArrayList<ArrayList<Integer>> bm56(int[] num) {
        //先按字典序排序
        Arrays.sort(num);
        Boolean[] vis = new Boolean[num.length];
        Arrays.fill(vis, false);
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();
        recursion56(res, num, temp, vis);
        return res;
    }

    public void recursion56(ArrayList<ArrayList<Integer>> res, int[] num, ArrayList<Integer> temp, Boolean[] vis) {
        //临时数组满了加入输出
        if (temp.size() == num.length) {
            res.add(new ArrayList<Integer>(temp));//这里不new的话会报错，原因待查
            return;
        }
        //遍历所有元素选取一个加入
        for (int i = 0; i < num.length; i++) {
            //如果该元素已经被加入了则不需要再加入了
            if (vis[i]) {
                continue;
            }
            if (i > 0 && num[i - 1] == num[i] && !vis[i - 1]) {
                //当前的元素num[i]与同一层的前一个元素num[i-1]相同且num[i-1]已经用过了
                continue;
            }
            //标记为使用过
            vis[i] = true;
            //加入数组
            temp.add(num[i]);
            recursion56(res, num, temp, vis);
            //回溯
            vis[i] = false;
            temp.remove(temp.size() - 1);
        }
    }


    /**
     * BM55 没有重复项数字的全排列
     * <p>
     * 知识点：递归与回溯
     * 递归是一个过程或函数在其定义或说明中有直接或间接调用自身的一种方法，
     * 它通常把一个大型复杂的问题层层转化为一个与原问题相似的规模较小的问题来求解。因此递归过程，
     * 最重要的就是查看能不能讲原本的问题分解为更小的子问题，这是使用递归的关键。
     * <p>
     * 如果是线型递归，子问题直接回到父问题不需要回溯，但是如果是树型递归，父问题有很多分支，
     * 我需要从子问题回到父问题，进入另一个子问题。因此回溯是指在递归过程中，从某一分支的子问题回到父问题
     * 进入父问题的另一子问题分支，因为有时候进入第一个子问题的时候修改过一些变量，因此回溯的时候会要求
     * 改回父问题时的样子才能进入第二子问题分支。
     */
    public ArrayList<ArrayList<Integer>> bm55(int[] num) {
        //先按字典序排序
        Arrays.sort(num);
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        ArrayList<Integer> numList = new ArrayList<>();
        //数组转ArrayList
        for (int i = 0; i < num.length; i++) {
            numList.add(num[i]);
        }
        recursion55(res, numList, 0);
        return res;
    }

    public void recursion55(ArrayList<ArrayList<Integer>> res, ArrayList<Integer> numList, int index) {
        //分枝进入结尾，找到一种排列
        if (index == numList.size() - 1) {
            res.add(numList);
        } else {
            //遍历后续的元素
            for (int i = index; i < numList.size(); i++) {
                //交换二者
                swap55(numList, i, index);
                //继续往后找
                recursion55(res, numList, index + 1);
                //回溯
                swap55(numList, i, index);
            }
        }
    }

    public void swap55(ArrayList<Integer> num, int i1, int i2) {
        int temp = num.get(i1);
        num.set(i1, num.get(i2));
        num.set(i2, temp);
    }

    /**
     * BM54 三数之和
     */
    public ArrayList<ArrayList<Integer>> bm54(int[] num) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        int n = num.length;
        //不够三元组
        if (n < 3) {
            return res;
        }
        //排序
        Arrays.sort(num);
        for (int i = 0; i < n - 2; i++) {
            if (i != 0 && num[i] == num[i - 1]) {
                continue;
            }
            //后续的收尾双指针
            int left = i + 1;
            int right = n - 1;
            //设置当前数的负值为目标
            int target = -num[i];
            while (left < right) {
                //双指针指向的二值相加为目标，则可以与num[i]组成0
                if (num[left] + num[right] == target) {
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(num[i]);
                    temp.add(num[left]);
                    temp.add(num[right]);
                    res.add(temp);
                    while (left + 1 < right && num[left] == num[left + 1]) {
                        left++;//去重
                    }
                    while (right - 1 > left && num[right] == num[right - 1]) {
                        right--;//去重
                    }
                    //双指针向中间收缩
                    left++;
                    right--;

                } else if (num[left] + num[right] > target) {
                    //双指针指向的二值相加大于目标，右指针向左
                    right--;

                } else {
                    //双指针指向的二值相加小于目标，左指针向右
                    left++;
                }
            }
        }
        return res;
    }


    /**
     * BM53 缺失的第一个正整数
     * n个长度的数组，没有重复，则如果数组填满了1～n，那么缺失n+1，
     * 如果数组填不满1～n，那么缺失的就是1～n中的数字。
     */
    public int bm53(int[] nums) {
        int n = nums.length;
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            hashMap.put(nums[i], 1);
        }
        int res = 1;
        while (hashMap.containsKey(res)) {
            res++;
        }
        return res;
    }

    //前面提到了数组要么缺失1～n1～n1～n中的某个数字，要么缺失n+1，
    // 而数组正好有下标0～n−1可以对应数字1～n，因此只要数字1～n中某个数字出现，我们就可以将对应下标的值做一个标记，
    // 最后没有被标记的下标就是缺失的值。
    public int bm53Two(int[] nums) {
        int n = nums.length;
        //遍历数组
        for (int i = 0; i < n; i++) {
            //负数全部记为n+1
            if (nums[i] <= 0) {
                nums[i] = n + 1;
            }
        }
        for (int i = 0; i < n; i++) {
            //对于1-n中的数字
            if (Math.abs(nums[i]) <= n) {
                //这个数字的下标标记为负数
                nums[Math.abs(nums[i]) - 1] = -1 * Math.abs(nums[Math.abs(nums[i]) - 1]);
            }
        }
        for (int i = 0; i < n; i++) {
            //找到第一个元素不为负数的下标
            if (nums[i] > 0) {
                return i + 1;
            }
        }
        return n + 1;
    }

    /**
     * BM52 数组中只出现一次的两个数字
     * 既然有两个数字只出现了一次，我们就统计每个数字的出现次数，利用哈希表的快速根据key值访问其频率值
     */
    public int[] bm52(int[] array) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        ArrayList<Integer> res = new ArrayList<>();
        //遍历数组
        for (int i = 0; i < array.length; i++) {
            //统计每个数出现的频率
            if (!hashMap.containsKey(array[i])) {
                hashMap.put(array[i], 1);
            } else {
                hashMap.put(array[i], hashMap.get(array[i]) + 1);
            }
        }
        //再次遍历数组
        for (int i = 0; i < array.length; i++) {
            if (hashMap.get(array[i]) == 1) {
                res.add(array[i]);
            }
        }
        //整理次序
        if (res.get(0) < res.get(1)) {
            return new int[]{res.get(0), res.get(1)};
        } else {
            return new int[]{res.get(1), res.get(0)};
        }
    }

    //异或运算（扩展思路）
    public int[] bm52Two(int[] array) {
        int res1 = 0;
        int res2 = 0;
        int temp = 0;
        //遍历数组得到a^b
        for (int i = 0; i < array.length; i++) {
            temp ^= array[i];
        }
        int k = 1;
        //找到两个数不相同的第一位
        while ((k & temp) == 0) {
            k <<= 1;
        }
        for (int i = 0; i < array.length; i++) {
            //遍历数组，对每个数分类
            if ((k & array[i]) == 0) {
                res1 ^= array[i];
            } else {
                res2 ^= array[i];
            }
        }
        //整理次序
        if (res1 < res2) {
            return new int[]{res1, res2};
        } else {
            return new int[]{res2, res1};
        }
    }

    /**
     * BM51 数组中出现次数超过一半的数字
     * 数组某个元素出现次数超过了数组长度的一半，那它肯定出现最多，
     * 而且只要超过了一半，其他数字不可能超过一半了，必定是它。
     */
    public int bm51(int[] array) {
        //哈希表统计每个数字出现的次数
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        //遍历数组
        for (int i = 0; i < array.length; i++) {
            //统计频率
            if (!hashMap.containsKey(array[i])) {
                hashMap.put(array[i], 1);
            } else {
                hashMap.put(array[i], hashMap.get(array[i]) + 1);
            }
            //一旦有个数大于长度一半的情况即可返回
            if (hashMap.get(array[i]) > array.length / 2) {
                return array[i];
            }
        }
        return 0;
    }


    /**
     * BM50 两数之和
     * 方法：哈希表（推荐使用）
     * 哈希表是一种根据关键码（key）直接访问值（value）的一种数据结构。而这种直接访问意味着
     * 只要知道key就能在O(1)O(1)O(1)时间内得到value，因此哈希表常用来统计频率、快速检验某个元素是否出现过等。
     * <p>
     * 对于数组中出现的一个数a，如果目标值减去a的值已经出现过了，那这不就是我们要找的一对元组
     */
    public int[] bm50(int[] numbers, int target) {
        int[] res = new int[0];
        //创建哈希表,两元组分别表示值、下标
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        //在哈希表中查找target-numbers[i]
        for (int i = 0; i < numbers.length; i++) {
            int temp = target - numbers[i];
            //若是没找到，将此信息计入哈希表
            if (!hashMap.containsKey(temp)) {
                hashMap.put(numbers[i], i);
            } else {
                //否则返回两个下标+1
                return new int[]{hashMap.get(temp) + 1, i + 1};
            }
        }
        return res;
    }

    /**
     * BM49 表达式求值
     * 方法：栈 + 递归（推荐使用）
     */
    public int bm49(String s) {
        ArrayList<Integer> res = function49(s, 0);
        return res.get(0);
    }

    public ArrayList<Integer> function49(String s, int index) {
        Stack<Integer> stack = new Stack<>();
        int num = 0;
        char op = '+';
        int i;
        for (i = index; i < s.length(); i++) {
            //数字转换成int数字
            //判断是否为数字
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                num = num * 10 + s.charAt(i) - '0';
                if (i != s.length() - 1) {
                    continue;
                }
            }
            //碰到'('时，把整个括号内的当成一个数字处理
            if (s.charAt(i) == '(') {
                //递归处理括号
                ArrayList<Integer> res = function49(s, i + 1);
                num = res.get(0);
                i = res.get(1);
                if (i != s.length() - 1) {
                    continue;
                }
            }
            switch (op) {
                //加减号先入栈
                case '+':
                    stack.push(num);
                    break;
                case '-':
                    //相反数
                    stack.push(-num);
                    break;
                //优先计算乘号
                case '*':
                    int temp = stack.pop();
                    stack.push(temp * num);
                    break;
            }
            num = 0;
            //右括号结束递归
            if (s.charAt(i) == ')') {
                break;
            } else {
                op = s.charAt(i);
            }
        }
        int sum = 0;
        //栈中元素相加
        while (!stack.isEmpty()) {
            sum += stack.pop();
        }
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(sum);
        temp.add(i);
        return temp;
    }

    /**
     * BM48 数据流中的中位数
     */
    public class bm48 {

        private ArrayList<Integer> val = new ArrayList<>();

        public void Insert(Integer num) {
            if (val.isEmpty()) {
                //val中没有数据，直接加入
                val.add(num);
            } else {
                //val中有数据，需要插入排序
                int i = 0;
                //遍历找到插入点
                for (; i < val.size(); i++) {
                    if (num <= val.get(i)) {
                        break;
                    }
                }
                //插入相应位置
                val.add(i, num);
            }
        }

        public Double GetMedian() {
            int n = val.size();
            //奇数个数字
            if (n % 2 == 1) {
                //类型转换
                return (double) val.get(n / 2);
            } else {
                //偶数个数字
                double a = val.get(n / 2);
                double b = val.get(n / 2 - 1);
                return (a + b) / 2;
            }
        }
    }

    //方法二：堆排序（扩展思路）
    //维护两个堆，分别是大顶堆min，用于存储较小的值，其中顶部最大；
    // 小顶堆max，用于存储较大的值，其中顶部最小，则中位数只会在两个堆的堆顶出现
    @RequiresApi(api = Build.VERSION_CODES.N)
    public class bm48Two {
        //小顶堆，元素数值都比大顶堆大
        private PriorityQueue<Integer> max = new PriorityQueue<>();
        private PriorityQueue<Integer> min = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;//负数 o1 o2，降序
//                return o2.compareTo(o1);
            }
        });

        //维护两个堆，取两个堆顶部即与中位数相关
        public void Insert(Integer num) {
            //先加入较小部分
            min.offer(num);
            //将较小部分的最大值取出，送入到较大部分
            max.offer(min.poll());
            //平衡两个堆的数量
            if (min.size() < max.size()) {
                min.offer(max.poll());
            }
        }

        public Double GetMedian() {
            //奇数个
            if (min.size() > max.size()) {
                return (double) min.peek();
            } else {
                //偶数个
                return (double) (min.peek() + max.peek()) / 2;
            }
        }
    }


    /**
     * BM47 寻找第K大
     * 方法：快排+二分查找（推荐使用）
     */
    public int bm47(int[] a, int n, int K) {
        return partition47(a, 0, n - 1, K);
    }

    public int partition47(int[] a, int low, int high, int k) {
        //随机快排划分
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % (high - low + 1) + low;
        swap47(a, low, x);
        int v = a[low];
        int i = low + 1;
        int j = high;
        while (true) {
            //小于标杆的在右
            while (j >= low + 1 && a[j] < v) {
                j--;
            }
            //大于标杆的在左
            while (i <= high && a[i] > v) {
                i++;
            }
            if (i > j) {
                break;
            }
            swap47(a, i, j);
            i++;
            j--;
        }
        swap47(a, low, j);
        //从0开始，所以为第j+1大
        if (j + 1 == k) {
            return a[j];
        } else if (j + 1 < k) {
            //继续划分右侧部分
            return partition47(a, j + 1, high, k);
        } else {
            //继续划分左侧部分
            return partition47(a, low, j - 1, k);
        }
    }

    public void swap47(int arr[], int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public int bm47Two(int[] a, int n, int K) {
        return quickSort47Two(a, 0, a.length - 1, K);
    }

    public int quickSort47Two(int[] arr, int left, int right, int k) {
        int p = partition47Two(arr, left, right);
        // 改进后，很特殊的是，p是全局下标，只要p对上topK坐标就可以返回
        if (p == arr.length - k) {
            return arr[p];
        } else if (p < arr.length - k) {
            // 如果基准在左边，这在右边找
            return quickSort47Two(arr, p + 1, right, k);
        } else {
            return quickSort47Two(arr, left, p - 1, k);
        }
    }

    public int partition47Two(int[] arr, int left, int right) {
        // 可优化成随机，或中位数
        int key = arr[left];
        while (left < right) {
            while (left < right && arr[right] >= key) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left] <= key) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = key;
        return left;
    }


    /**
     * BM46 最小的K个数
     * 方法一：堆排序（推荐使用）
     * 优先队列即PriorityQueue，是一种内置的机遇堆排序的容器，分为大顶堆与小顶堆，大顶堆的堆顶为最大元素，
     * 其余更小的元素在堆下方，小顶堆与其刚好相反。且因为容器内部的次序基于堆排序，因此每次插入元素时间复杂度
     * 都是O(log2n)，而每次取出堆顶元素都是直接取出。
     * <p>
     * 思路：
     * 要找到最小的k个元素，只需要准备k个数字，之后每次遇到一个数字能够快速的与这k个数字中最大的值比较，
     * 每次将最大的值替换掉，那么最后剩余的就是k个最小的数字了。
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Integer> bm46(int[] input, int k) {
        ArrayList<Integer> res = new ArrayList<>();
        //排除特殊情况
        if (k == 0 || input.length == 0) {
            return res;
        }
        //大根堆
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;//负数 o1 o2，从大到小排序
//                return o2.compareTo(o1);
            }
        });
        //构建一个k个大小的堆
        for (int i = 0; i < k; i++) {
            priorityQueue.offer(input[i]);
        }
        for (int i = k; i < input.length; i++) {
            //较小元素入堆
            //peek()方法用于返回第一个元素，而不从此PriorityQueue中删除一个元素
            if (priorityQueue.peek() > input[i]) {
                priorityQueue.poll();// 获取并移除第一个
                priorityQueue.offer(input[i]);
            }
        }
        //堆中元素取出入数组
        for (int i = 0; i < k; i++) {
            res.add(priorityQueue.poll());
        }
        return res;
    }

    //方法二：sort排序法（扩展思路）
    public ArrayList<Integer> bm46Two(int[] input, int k) {
        ArrayList<Integer> res = new ArrayList<>();
        //排除特殊情况
        if (k == 0 || input.length == 0) {
            return res;
        }
        //排序
        Arrays.sort(input);
        //因为k<=input.length,取前k小
        for (int i = 0; i < k; i++) {
            res.add(input[i]);
        }
        return res;
    }


    /**
     * BM45 滑动窗口的最大值
     * 方法一：双向队列（推荐使用）
     * <p>
     * 知识点：双向队列
     * 如果说队列是一种只允许从尾部进入，从头部出来的线性数据结构，那双向队列就是一种特殊的队列了，
     * 双向队列两边，即头部和尾部都可以进行插入元素和删除元素的操作，但是也只能插入到最尾部或者最头部，
     * 每次也只能取出头部元素或者尾部元素后才能取出里面的元素。
     */
    public ArrayList<Integer> bm45(int[] num, int size) {
        ArrayList<Integer> res = new ArrayList<>();
        //窗口大于数组长度的时候，返回空
        if (size <= num.length && size != 0) {
            //双向队列
            ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>();
            //先遍历一个窗口
            for (int i = 0; i < size; i++) {
                //去掉比自己先进队列的小于自己的值
                while (!arrayDeque.isEmpty() && num[arrayDeque.peekLast()] < num[i]) {
                    arrayDeque.pollLast();
                }
                arrayDeque.add(i);
            }
            //遍历后续数组元素
            for (int i = size; i < num.length; i++) {
                //取窗口内的最大值
                res.add(num[arrayDeque.peekFirst()]);
                while (!arrayDeque.isEmpty() && arrayDeque.peekFirst() < (i - size + 1)) {
                    //弹出窗口移走后的值
                    arrayDeque.pollFirst();
                }
                //加入新的值前，去掉比自己先进队列的小于自己的值
                while (!arrayDeque.isEmpty() && num[arrayDeque.peekLast()] < num[i]) {
                    arrayDeque.pollLast();
                }
                arrayDeque.add(i);
            }
            res.add(num[arrayDeque.pollFirst()]);
        }
        return res;
    }

    //方法二：暴力法（扩展思路）
    //step 1：第一次遍历数组每个位置作为窗口的起点
    //step 2：从每个起点开始遍历窗口长度，查找其中的最大值。
    public ArrayList<Integer> bm45Two(int[] num, int size) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        //窗口大于数组长度的时候，返回空
        if (size <= num.length && size != 0) {
            //数组后面要空出窗口大小，避免越界
            for (int i = 0; i <= num.length - size; i++) {
                //寻找每个窗口最大值
                int max = 0;
                for (int j = i; j < i + size; j++) {
                    if (num[j] > max) {
                        max = num[j];
                    }
                }
                arrayList.add(max);
            }
        }
        return arrayList;
    }

    /**
     * BM44 有效括号序列
     * 方法：栈（推荐使用）
     */
    public boolean bm44(String s) {
        //辅助栈
        Stack<Character> stack = new Stack<>();
        //遍历字符串
        for (int i = 0; i < s.length(); i++) {
            //遇到左小括号
            if (s.charAt(i) == '(') {
                //期待遇到右小括号
                stack.push(')');
            } else if (s.charAt(i) == '[') {
                //遇到左中括号，期待遇到右中括号
                stack.push(']');
            } else if (s.charAt(i) == '{') {
                //遇到左大括号，期待遇到右打括号
                stack.push('}');
            } else if (stack.isEmpty() || stack.pop() != s.charAt(i)) {
                return false;
            }
        }
        //栈中是否还有元素
        return stack.isEmpty();
    }


    /**
     * BM43 包含min函数的栈
     */
    public class bm43 {

        //用于栈的push 与 pop
        Stack<Integer> stack1 = new Stack<>();
        //用于存储最小min
        Stack<Integer> stack2 = new Stack<>();

        public void push(int node) {
            stack1.push(node);
            //空或者新元素较小，则入栈
            if (stack2.isEmpty() || stack2.peek() > node) {
                stack2.push(node);
            } else {
                //重复加入栈顶
                stack2.push(stack2.peek());
            }
        }

        public void pop() {
            stack1.pop();
            stack2.pop();
        }

        public int top() {
            return stack1.peek();
        }

        public int min() {
            return stack2.peek();
        }
    }


    /**
     * BM42 用两个栈实现队列
     * 队列：元素不可直接下标访问，先进先出
     * 栈：元素不可直接访问，先进后出
     * 方法：双栈法（推荐使用）
     * 栈是一种仅支持在表尾进行插入和删除操作的线性表，这一端被称为栈顶，另一端被称为栈底。
     * 元素入栈指的是把新元素放到栈顶元素的上面，使之成为新的栈顶元素；元素出栈指的是从一个栈
     * 删除元素又称作出栈或退栈，它是把栈顶元素删除掉，使其相邻的元素成为新的栈顶元素。
     * <p>
     * 队列是一种仅支持在表尾进行插入操作、在表头进行删除操作的线性表，插入端称为队尾，
     * 删除端称为队首，因整体类似排队的队伍而得名。它满足先进先出的性质，元素入队即
     * 将新元素加在队列的尾，元素出队即将队首元素取出，它后一个作为新的队首。·
     */
    public class bm42 {
        Stack<Integer> stack1 = new Stack<Integer>();
        Stack<Integer> stack2 = new Stack<Integer>();

        public void push(int node) {
            stack1.push(node);//入栈
        }

        //出栈
        public int pop() {
            //将第一个栈中内容弹出放入第二个栈中
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
            //第二个栈栈顶就是最先进来的元素，即队首
            int res = stack2.pop();
            //再将第二个栈的元素放回第一个栈
            while (!stack2.isEmpty()) {
                stack1.push(stack2.pop());
            }
            return res;
        }
    }


    /**
     * BM26 求二叉树的层序遍历
     * 方法一：队列
     * 队列是一种仅支持在表尾进行插入操作、在表头进行删除操作的线性表，插入端称为队尾，删除端称为队首，
     * 因整体类似排队的队伍而得名。它满足先进先出的性质，元素入队即将新元素加在队列的尾，
     * 元素出队即将队首元素取出，它后一个作为新的队首。
     */
    public ArrayList<ArrayList<Integer>> bm26(TreeNode root) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        if (root == null) {
            //如果是空，则直接返回空数组
            return res;
        }
        //队列存储，进行层次遍历
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            //记录二叉树的某一行
            ArrayList<Integer> row = new ArrayList<>();
            int n = queue.size();
            //因先进入的是根节点，故每层节点多少，队列大小就是多少
            for (int i = 0; i < n; i++) {
                TreeNode cur = queue.poll();//弹出元素
                row.add(cur.val);
                //若是左右孩子存在，则存入左右孩子作为下一个层次
                if (cur.left != null) {
                    queue.add(cur.left);
                }
                if (cur.right != null) {
                    queue.add(cur.right);
                }
            }
            //每一层加入输出
            res.add(row);
        }
        return res;
    }

    //方法二：递归（扩展思路）
    public ArrayList<ArrayList<Integer>> bm26Two(TreeNode root) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        if (root == null) {
            //如果是空，则直接返回
            return res;
        }
        //递归层次遍历
        traverse26(root, 1, res);
        return res;
    }

    public void traverse26(TreeNode root, int depth, ArrayList<ArrayList<Integer>> res) {
        if (root != null) {
            //新的一层
            if (res.size() < depth) {
                ArrayList<Integer> row = new ArrayList<>();
                res.add(row);
                row.add(root.val);

            } else {
                //读取该层的一维数组，将元素加入末尾
                ArrayList<Integer> row = res.get(depth - 1);
                row.add(root.val);
            }
        } else {
            return;
        }
        //递归左右时深度记得加1
        traverse26(root.left, depth + 1, res);
        traverse26(root.right, depth + 1, res);
    }

    /**
     * BM25 二叉树的后序遍历（左右根）
     * 方法一：递归（推荐使用）
     */
    public int[] bm25(TreeNode root) {
        //添加遍历结果的数组
        List<Integer> list = new ArrayList<>();
        //递归后序遍历
        postorder25(list, root);
        //返回的结果
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public void postorder25(List<Integer> list, TreeNode root) {
        //遇到空节点则返回
        if (root == null) {
            return;
        }
        //先去左子树
        postorder25(list, root.left);
        //再去右子树
        postorder25(list, root.right);
        //最后访问根节点
        list.add(root.val);
    }

    //栈思路
    public int[] bm25Two(TreeNode root) {
        //添加遍历结果的数组
        List<Integer> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode pre = null;
        while (root != null || !stack.isEmpty()) {
            //每次先找到最左边的节点
            while (root != null) {
                stack.push(root.left);
                root = root.left;
            }
            //弹出栈顶
            TreeNode node = stack.pop();
            //如果该元素的右边没有或是已经访问过
            if (node.right == null || node.right == pre) {
                //访问中间的节点
                list.add(node.val);
                //且记录为访问过了
                pre = node;
            } else {
                //该节点入栈
                stack.push(node);
                //先访问右边
                root = node.right;
            }
        }
        //返回的结果
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    /**
     * BM24 二叉树的中序遍历(左根右)
     * 方法一：递归（推荐使用）
     * <p>
     * 具体做法：
     * step 1：准备数组用来记录遍历到的节点值，Java可以用List，C++可以直接用vector。
     * step 2：从根节点开始进入递归，遇到空节点就返回，否则优先进入左子树进行递归访问。
     * step 3：左子树访问完毕再回到根节点访问。
     * step 4：最后进入根节点的右子树进行递归。
     */
    public int[] bm24(TreeNode root) {
        //添加遍历结果的数组
        List<Integer> list = new ArrayList<>();
        //递归中序遍历
        inorder24(list, root);
        //返回的结果
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public void inorder24(List<Integer> list, TreeNode root) {
        //遇到空节点则返回
        if (root == null) {
            return;
        }
        //先去左子树
        inorder24(list, root.left);
        //再访问根节点
        list.add(root.val);
        //最后去右子树
        inorder24(list, root.right);
    }

    /**
     * 栈思路
     */
    public int[] bm24Two(TreeNode root) {
        //添加遍历结果的数组
        List<Integer> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        //空树返回空数组
        if (root == null) {
            return new int[0];
        }
        //当树节点不为空或栈中有节点时
        while (root != null || !stack.isEmpty()) {
            //每次找到最左节点
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            //访问该节点
            TreeNode node = stack.pop();
            list.add(node.val);
            //进入右节点
            root = node.right;
        }
        //返回的结果
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    /**
     * BM23 二叉树的前序遍历(根左右)
     * 方法一：递归（推荐使用）
     * 具体做法：
     * step 1：准备数组用来记录遍历到的节点值，Java可以用List，C++可以直接用vector。
     * step 2：从根节点开始进入递归，遇到空节点就返回，否则将该节点值加入数组。
     * step 3：依次进入左右子树进行递归。
     */
    public int[] bm23(TreeNode root) {
        //添加遍历结果的数组
        List<Integer> list = new ArrayList<>();
        //递归前序遍历
        preorder23(list, root);
        //返回的结果
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public void preorder23(List<Integer> list, TreeNode root) {
        //遇到空节点则返回
        if (root == null) {
            return;
        }
        //先遍历根节点
        list.add(root.val);
        //再去左子树
        preorder23(list, root.left);
        //最后去右子树
        preorder23(list, root.right);
    }


    /**
     * 栈实现
     * 具体做法：
     * step 1：优先判断树是否为空，空树不遍历。
     * step 2：准备辅助栈，首先记录根节点。
     * step 3：每次从栈中弹出一个元素，进行访问，然后验证该节点的左右子节点是否存在，存的话的加入栈中，
     * 优先加入右节点。
     */
    public int[] bm23Two(TreeNode root) {
        //添加遍历结果的数组
        List<Integer> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        //空树返回空数组
        if (root == null) {
            return new int[0];
        }
        //根节点先进栈
        stack.push(root);
        while (!stack.isEmpty()) {
            //每次栈顶就是访问的元素
            TreeNode treeNode = stack.pop();//从栈中取出
            list.add(treeNode.val);
            //如果右边还有右子节点进栈
            if (treeNode.right != null) {
                stack.push(treeNode.right);
            }
            //如果左边还有左子节点进栈
            if (treeNode.left != null) {
                stack.push(treeNode.left);
            }
        }
        //返回的结果
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }


    /**
     * BM22 比较版本号
     * 方法一：双指针遍历截取（推荐使用）
     */
    public int bm22(String version1, String version2) {
        int n1 = version1.length();
        int n2 = version2.length();
        int i = 0, j = 0;
        //直到某个字符串结束
        while (i < n1 || j < n2) {
            long num1 = 0;
            //从下一个点前截取数字
            while (i < n1 && version1.charAt(i) != '.') {
                num1 = num1 * 10 + (version1.charAt(i) - '0');
                i++;
            }
            //跳过点
            i++;
            long num2 = 0;
            //从下一个点前截取数字
            while (j < n2 && version2.charAt(j) != '.') {
                num2 = num2 * 10 + (version2.charAt(j) - '0');
                j++;
            }
            //跳过点
            j++;
            //比较数字大小
            if (num1 > num2) {
                return 1;
            }
            if (num1 < num2) {
                return -1;
            }
        }
        //版本号相同
        return 0;
    }


    /**
     * BM21 旋转数组的最小数字
     * 方法一：二分法（推荐使用）
     */
    public int bm21(int[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (array[mid] > array[right]) {
                //最小的数字在mid右边
                left = mid + 1;
            } else if (array[mid] == array[right]) {
                //无法判断，一个一个试
                right--;
            } else {
                //最小数字要么是mid要么在mid左边
                right = mid;
            }
        }
        return array[left];
    }


    /**
     * BM20 数组中的逆序对
     * <p>
     * 具体做法：
     * step 1： 划分阶段：将待划分区间从中点划分成两部分，两部分进入递归继续划分，直到子数组长度为1.
     * step 2： 排序阶段：使用归并排序递归地处理子序列，同时统计逆序对，因为在归并排序中，我们会依次比较相邻两组子数组各个元素的大小，并累计遇到的逆序情况。而对排好序的两组，右边大于左边时，它大于了左边的所有子序列，基于这个性质我们可以不用每次加1来统计，减少运算次数。
     * step 3： 合并阶段：将排好序的子序列合并，同时累加逆序对。
     */
    public int bm20(int[] array) {
        int n = array.length;
        int[] res = new int[n];
        return mergeSort20(0, n - 1, array, res);
    }

    public int mergeSort20(int left, int right, int[] data, int[] temp) {
        int mod = 1000000007;
        //停止划分
        if (left >= right) {
            return 0;
        }
        //取中间
        int mid = left + (right - left) / 2;
        //左右划分合并
        int res = mergeSort20(left, mid, data, temp) + mergeSort20(mid + 1, right, data, temp);
        //防止溢出
        res %= mod;
        int i = left, j = mid + 1;
        for (int k = left; k <= right; k++) {
            temp[k] = data[k];
        }
        for (int k = left; k <= right; k++) {
            if (i == mid + 1) {
                data[k] = temp[j++];
            } else if (j == right + 1 || temp[i] <= temp[j]) {
                data[k] = temp[i++];
            } else {
                //左边比右边大，答案增加
                data[k] = temp[j++];
                // 统计逆序对
                res += mid - i + 1;
            }
        }
        return res % mod;
    }


    /**
     * BM19 寻找峰值
     * 方法：二分查找（推荐使用）
     * 具体做法：
     * step 1：二分查找首先从数组首尾开始，每次取中间值，直到首尾相遇。
     * step 2：如果中间值的元素大于它右边的元素，说明往右是向下，我们不一定会遇到波峰，但是那就往左收缩区间。
     * step 3：如果中间值大于右边的元素，说明此时往右是向上，向上一定能有波峰，那我们往右收缩区间。
     * step 4：最后区间收尾相遇的点一定就是波峰。
     */
    public int bm19(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        //二分法
        while (left < right) {
            int mid = left + (right - left) / 2;
            //右边是往下，不一定有坡峰
            if (nums[mid] > nums[mid + 1]) {
                right = mid;
            } else {
                //右边是往上，一定能找到波峰
                left = mid + 1;
            }
        }
        //其中一个波峰
        return right;
    }

    /**
     * BM18 二维数组中的查找
     * 方法：二分查找（推荐使用）
     * <p>
     * 具体做法：
     * step 1：首先获取矩阵的两个边长，判断特殊情况。
     * step 2：首先以左下角为起点，若是它小于目标元素，则往右移动去找大的，若是他大于目标元素，则往上移动去找小的。
     * step 3：若是移动到了矩阵边界也没找到，说明矩阵中不存在目标值。
     */
    public boolean bm18(int target, int[][] array) {
        //优先判断特殊
        if (array.length == 0) {
            return false;
        }
        int n = array.length;//获取行数
        if (array[0].length == 0) {
            return false;
        }
        int m = array[0].length;//获取列数
        //从最左下角的元素开始往右或往上
        for (int i = n - 1, j = 0; i >= 0 && j < m; ) {
            //元素较大，往上走
            if (array[i][j] > target) {
                i--;

            } else if (array[i][j] < target) {
                //元素较小，往右走
                j++;

            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * BM17 二分查找-I
     * 方法：二分法（推荐使用）
     * 具体做法：
     * <p>
     * step 1：从数组首尾开始，每次取中点值。
     * step 2：如果中间值等于目标即找到了，可返回下标，如果中点值大于目标，说明中点以后的都大于目标，因此目标在中点左半区间，如果中点值小于目标，则相反。
     * step 3：根据比较进入对应的区间，直到区间左右端相遇，意味着没有找到。
     */
    public static int bm17(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        //从数组首尾开始，直到二者相遇
        while (left <= right) {
            //每次检查中点的值
//            int mid = (left + right) / 2;
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            //进入左的区间
            if (nums[mid] > target) {
                right = mid - 1;
            } else {
                //进入右区间
                left = mid + 1;
            }
        }
        return -1;
    }


    /**
     * BM16 删除有序链表中重复的元素-II
     */
    public static ListNode bm16(ListNode head) {
        //空链表
        if (head == null) {
            return null;
        }
        ListNode res = new ListNode(0);
        //在链表前加一个表头
        res.next = head;
        ListNode cur = res;
        while (cur.next != null && cur.next.next != null) {
            //遇到相邻两个节点值相同
            if (cur.next.val == cur.next.next.val) {
                int temp = cur.next.val;
                //将所有相同的都跳过
                while (cur.next != null && cur.next.val == temp) {
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }
        //返回时去掉表头
        return res.next;
    }

    //哈希表（扩展思路）
    public static ListNode bm16Two(ListNode head) {
        //空链表
        if (head == null) {
            return null;
        }
        Map<Integer, Integer> map = new HashMap<>();
        ListNode cur = head;
        //遍历链表统计每个节点值出现的次数
        while (cur != null) {
            if (map.containsKey(cur.val)) {
                map.put(cur.val, (int) map.get(cur.val) + 1);
            } else {
                map.put(cur.val, 1);
            }
            cur = cur.next;
        }
        ListNode res = new ListNode(0);
        //在链表前加一个表头
        res.next = head;
        cur = res;
        //再次遍历链表
        while (cur.next != null) {
            //如果节点值计数不为1
            if (map.get(cur.next.val) != 1) {
                //删去该节点
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        //去掉表头
        return res.next;
    }

    /**
     * BM15 删除有序链表中重复的元素-I
     * 方法：遍历删除（推荐使用）
     */
    public static ListNode bm15(ListNode head) {
        //空链表
        if (head == null) {
            return null;
        }
        //遍历指针
        ListNode cur = head;
        //指针当前和下一位不为空
        while (cur != null && cur.next != null) {
            //如果当前与下一位相等则忽略下一位
            if (cur.val == cur.next.val) {
                cur.next = cur.next.next;
            } else {
                //否则指针正常遍历
                cur = cur.next;
            }
        }
        return head;
    }

    /**
     * BM14 链表的奇偶重排
     * 双指针（推荐使用）
     */
    public static ListNode bm14(ListNode head) {
        //如果链表为空，不用重排
        if (head == null) {
            return head;
        }
        //even开头指向第二个节点(偶数节点)，可能为空
        ListNode even = head.next;
        //odd开头指向第一个节点
        ListNode odd = head;
        //指向even开头
        ListNode evenHead = even;
        while (even != null && even.next != null) {
            //odd连接even的后一个，即奇数位
            odd.next = even.next;
            //odd进入后一个奇数位
            odd = odd.next;
            //even连接后一个奇数的后一位，即偶数位
            even.next = odd.next;
            //even进入后一个偶数位
            even = even.next;
        }
        //even整体接在odd后面
        odd.next = evenHead;
        return head;
    }


    /**
     * BM13 判断一个链表是否为回文结构
     * 反转后半部分链表
     */
    public static boolean bm13(ListNode head) {
        ListNode fast = head, slow = head;
        //通过快慢指针找到中点
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        //如果fast不为空，说明链表的长度是奇数个
        if (fast != null) {
            slow = slow.next;
        }
        //反转后半部分链表
        slow = reverse13(slow);
        fast = head;
        while (slow != null) {
            //然后比较，判断节点值是否相等
            if (fast.val != slow.val) {
                return false;
            }
            fast = fast.next;
            slow = slow.next;
        }
        return true;
    }

    //反转链表
    public static ListNode reverse13(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }

    //使用栈解决
    //栈是先进后出的一种数据结构，这里还可以使用栈先把链表的节点全部存放到栈中，
    // 然后再一个个出栈，这样就相当于链表从后往前访问了
    public static boolean bm13Two(ListNode head) {
        if (head == null) {
            return true;
        }
        ListNode temp = head;
        Stack<Integer> stack = new Stack<>();
        //链表的长度
        int len = 0;
        //把链表节点的值存放到栈中
        while (temp != null) {
            stack.push(temp.val);
            temp = temp.next;
            len++;
        }
        //len长度除以2
        len >>= 1;
        //然后再出栈
        for (int i = len; i >= 0; i--) {
            if (head.val != stack.pop()) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * BM12 单链表的排序
     * 方法一：归并排序（推荐使用）
     */
    public static ListNode bm12(ListNode head) {
        //链表为空或者只有一个元素，直接就是有序的
        if (head == null || head.next == null) {
            return head;
        }
        ListNode left = head;
        ListNode mid = head.next;
        ListNode right = head.next.next;
        //右边的指针到达末尾时，中间的指针指向该段链表的中间
        while (right != null && right.next != null) {
            left = left.next;
            mid = mid.next;
            right = right.next.next;
        }
        //左边指针指向左段的左右一个节点，从这里断开
        left.next = null;
        //分成两段排序，合并排好序的两段
        return merge12(bm12(head), bm12(mid));
    }

    //合并两段有序链表
    public static ListNode merge12(ListNode head1, ListNode head2) {
        //一个已经为空了，直接返回另一个
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        //加一个表头
        ListNode head = new ListNode(0);
        ListNode cur = head;
        //两个链表都要不为空
        while (head1 != null && head2 != null) {
            //取较小值的节点
            if (head1.val <= head2.val) {
                cur.next = head1;
                //只移动取值的指针
                head1 = head1.next;

            } else {
                cur.next = head2;
                //只移动取值的指针
                head2 = head2.next;
            }
            //指针后移
            cur = cur.next;
        }
        //哪个链表还有剩，直接连在后面
        if (head1 != null) {
            cur.next = head1;
        } else {
            cur.next = head2;
        }
        //返回值去掉表头
        return head.next;
    }

    //方法二：转化为数组排序（扩展思路）
    public static ListNode bm12Two(ListNode head) {
        ArrayList<Integer> numList = new ArrayList<>();
        ListNode p = head;
        //遍历链表，将节点值加入数组
        while (p != null) {
            numList.add(p.val);
            p = p.next;
        }
        p = head;
        //对数组元素排序
        Collections.sort(numList);
        //遍历数组
        for (int i = 0; i < numList.size(); i++) {
            //将数组元素依次加入链表
            p.val = numList.get(i);
            p = p.next;
        }
        return head;
    }


    /**
     * BM11 链表相加
     * 链表的问题大多借助栈和队列的结构思想
     * 申请两个栈空间和一个标记位，然后将两个栈中内容依次相加，将结果倒插入新节点中。
     */
    public static ListNode bm11(ListNode head1, ListNode head2) {
        LinkedList<Integer> list1 = new LinkedList<>();
        LinkedList<Integer> list2 = new LinkedList<>();
        putData11(list1, head1);//入栈
        putData11(list2, head2);
        ListNode newNode = null;
        ListNode head = null;
        int carry = 0;//标记进位
        while (!list1.isEmpty() || !list2.isEmpty() || carry != 0) {
            int x = (list1.isEmpty()) ? 0 : list1.pop();//依次从栈中取出
            int y = (list2.isEmpty()) ? 0 : list2.poll();
            int sum = x + y + carry;//与进位一起相加
            carry = sum / 10;//更新进位
            //将计算值放入节点
            newNode = new ListNode(sum % 10);
            //更新下一个节点的指向
            newNode.next = head;
            head = newNode;
        }
        return head;
    }

    public static void putData11(LinkedList<Integer> s1, ListNode head1) {
        if (s1 == null) {
            s1 = new LinkedList<>();
        }
        //遍历节点将其插入栈中
        while (head1 != null) {
            s1.push(head1.val);
            head1 = head1.next;
        }
    }


    /**
     * BM10 两个链表的第一个公共结点
     * 一个朴素的解法自然是两层枚举，逐个检查哪个节点相同。
     */
    public static ListNode bm10(ListNode head1, ListNode head2) {
        for (ListNode h1 = head1; h1 != null; h1 = h1.next) {
            for (ListNode h2 = head2; h2 != null; h2 = h2.next) {
                if (h1 == h2) {
                    return h1;
                }
            }
        }
        return null;
    }

    //栈解法，这是一种「从后往前」找的方式
    //将两条链表分别压入两个栈中，然后循环比较两个栈的栈顶元素，同时记录上一位栈顶元素。
    //当遇到第一个不同的节点时，结束循环，上一位栈顶元素即是答案。
    public static ListNode bm10Two(ListNode head1, ListNode head2) {
        Deque<ListNode> deque1 = new ArrayDeque<>();
        Deque<ListNode> deque2 = new ArrayDeque<>();
        while (head1 != null) {
            deque1.add(head1);
            head1 = head1.next;
        }
        while (head2 != null) {
            deque2.add(head2);
            head2 = head2.next;
        }
        ListNode ans = null;
        //peekLast：查看尾部元素
        //pollLast：从尾部出队
        while (!deque1.isEmpty() && !deque2.isEmpty() &&
                deque1.peekLast() == deque2.peekLast()) {
            ans = deque1.pollLast();
            deque2.pollLast();
        }
        return ans;
    }

    //Set 解法：这是一种「从前往后」找的方式。
    //使用 Set 数据结构，先对某一条链表进行遍历，同时记录下来所有的节点。
    //然后在对第二链条进行遍历时，检查当前节点是否在 Set 中出现过，第一个在 Set 出现过的节点即是交点。
    public static ListNode bm10Three(ListNode head1, ListNode head2) {
        Set<ListNode> set = new HashSet<>();
        while (head1 != null) {
            set.add(head1);
            head1 = head1.next;
        }
        while (head2 != null && !set.contains(head2)) {
            head2 = head2.next;
        }
        return head2;
    }

    //差值法
    //由于两条链表在相交节点后面的部分完全相同，因此我们可以先对两条链表进行遍历，分别得到两条链表的长度，
    // 并计算差值 d。
    // 让长度较长的链表先走 d 步，然后两条链表同时走，第一个相同的节点即是节点。
    public static ListNode bm10Four(ListNode head1, ListNode head2) {
        int c1 = 0, c2 = 0;
        ListNode ta = head1, tb = head2;
        while (ta != null && c1++ >= 0) {
            ta = ta.next;
        }
        while (tb != null && c2++ >= 0) {
            tb = tb.next;
        }
        int d = c1 - c2;
        if (d > 0) {
            while (d-- > 0) {
                head1 = head1.next;
            }

        } else if (d < 0) {
            d = -d;
            while (d-- > 0) {
                head2 = head2.next;
            }
        }
        while (head1 != head2) {
            head1 = head1.next;
            head2 = head2.next;
        }
        return head1;
    }

    /**
     * BM9 删除链表的倒数第n个节点
     * 方法一：双指针（推荐使用）
     * 具体做法：
     * step 1：给链表添加一个表头，处理删掉第一个元素时比较方便。
     * step 2：准备一个快指针，在链表上先走nnn步。
     * step 3：准备慢指针指向原始链表头，代表当前元素，前序节点指向添加的表头，这样两个指针之间相距就是一直都是nnn。
     * step 4：快慢指针同步移动，当快指针到达链表尾部的时候，慢指针正好到了倒数nnn个元素的位置。
     * step 5：最后将该节点前序节点的指针指向该节点后一个节点，删掉这个节点。
     */
    public static ListNode bm9(ListNode head, int n) {
        //添加表头
        ListNode res = new ListNode(-1);
        res.next = head;
        //当前节点
        ListNode cur = head;
        //前序节点
        ListNode pre = res;
        ListNode fast = head;
        //快指针先行n步
        while (n != 0) {
            fast = fast.next;
            n--;
        }
        //快慢指针同步，快指针到达末尾，慢指针就到了倒数第n个位置
        while (fast != null) {
            fast = fast.next;
            pre = cur;
            cur = cur.next;
        }
        //删除该位置的节点
        pre.next = cur.next;
        //返回去掉头
        return res.next;
    }


    /**
     * 方法二：长度统计法（思路扩展）
     * 具体做法：
     * step 1：给链表添加一个表头，处理删掉第一个元素时比较方便。
     * step 2：遍历整个链表，统计链表长度。
     * step 3：准备两个指针，一个指向原始链表头，表示当前节点，一个指向我们添加的表头，表示前序节点。两个指针同步遍历L−nL-nL−n次找到倒数第nnn个位置。
     * step 4：前序指针的next指向当前节点的next，代表越过当前节点连接，即删去该节点。
     * step 5：返回去掉添加的表头。
     */
    public static ListNode bm9Two(ListNode head, int n) {
        //记录链表长度
        int length = 0;
        //添加表头
        ListNode res = new ListNode(-1);
        res.next = head;
        //当前节点
        ListNode cur = head;
        //前序节点
        ListNode pre = res;
        //找到链表长度
        while (cur != null) {
            cur = cur.next;
            length++;
        }
        //回到头部
        cur = head;
        //从头遍历找到倒数第n个位置
        for (int i = 0; i < length - n; i++) {
            pre = cur;
            cur = cur.next;
        }
        //删去倒数第n个节点
        pre.next = cur.next;
        //返回去掉头节点
        return res.next;
    }

    /**
     * BM8 链表中倒数最后k个结点
     * 方法一：快慢双指针（推荐使用）
     * 具体做法：
     * step 1：准备一个快指针，从链表头开始，在链表上先走k步。
     * step 2：准备慢指针指向原始链表头，代表当前元素，则慢指针与快指针之间的距离一直都是k。
     * step 3：快慢指针同步移动，当快指针到达链表尾部的时候，慢指针正好到了倒数k个元素的位置。
     */
    public static ListNode bm8(ListNode head, int k) {
        int n = 0;
        ListNode fast = head;
        ListNode slow = head;
        //快指针先行k步
        for (int i = 0; i < k; i++) {
            if (fast != null) {
                fast = fast.next;
            } else {
                //达不到k步说明链表过短，没有倒数k
                slow = null;
                return slow;
            }
        }
        //快慢指针同步，快指针先到底，慢指针指向倒数第k个
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    //先找长度再找最后k（扩展思路）
    //思路：链表不能逆向遍历，也不能直接访问。但是对于倒数第kkk个位置，
    // 我们只需要知道是正数多少位还是可以直接遍历得到的。
//    具体做法：
//    step 1：可以先遍历一次链表找到链表的长度。
//    step 2：然后比较链表长度是否比k小，如果比k小返回一个空节点。
//    step 3：如果链表足够长，则我们从头节点往后遍历n−k次即可找到所求。
    public static ListNode bm8Two(ListNode head, int k) {
        int n = 0;
        ListNode p = head;
        //遍历链表，统计链表长度
        while (p != null) {
            n++;
            p = p.next;
        }
        //长度过小，返回空链表
        if (n < k) {
            return null;
        }
        p = head;
        //遍历n-k次
        for (int i = 0; i < n - k; i++) {
            p = p.next;
        }
        return p;
    }

    /**
     * BM7 链表中环的入口结点
     * 方法：双指针（推荐使用）
     * 具体做法：
     * step 1：使用BM6中的方法判断链表是否有环，并找到相遇的节点。
     * step 2：慢指针继续在相遇节点，快指针回到链表头，两个指针同步逐个元素逐个元素开始遍历链表。
     * step 3：再次相遇的地方就是环的入口。
     */
    public static ListNode bm7(ListNode head) {
        ListNode slow = hasCycle7(head);
        //没有环
        if (slow == null) {
            return null;
        }
        //快指针回到表头
        ListNode fast = head;
        //再次相遇即是环入口
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    //判断有没有环，返回相遇的地方
    public static ListNode hasCycle7(ListNode head) {
        //先判断链表为空的情况
        if (head == null) {
            return null;
        }
        //快慢双指针
        ListNode fast = head;
        ListNode slow = head;
        //如果没环快指针会先到链表尾
        while (fast != null && fast.next != null) {
            //快指针移动两步
            fast = fast.next.next;
            //慢指针移动一步
            slow = slow.next;
            //相遇则有环，返回相遇的位置
            if (slow == fast) {
                return slow;
            }
        }
        //到末尾说明没有环，返回null
        return null;
    }


    /**
     * BM6 判断链表中是否有环
     * 方法：双指针（推荐使用）
     * 双指针指的是在遍历对象的过程中，不是普通的使用单个指针进行访问，而是使用两个指针（特殊情况甚至可以多个），
     * 两个指针或是同方向访问两个链表、或是同方向访问一个链表（快慢指针）、或是相反方向扫描（对撞指针），
     * 从而达到我们需要的目的。
     * <p>
     * 具体做法：
     * step 1：设置快慢两个指针，初始都指向链表头。
     * step 2：遍历链表，快指针每次走两步，慢指针每次走一步。
     * step 3：如果快指针到了链表末尾，说明没有环，因为它每次走两步，所以要验证连续两步是否为NULL。
     * step 4：如果链表有环，那快慢双指针会在环内循环，因为快指针每次走两步，因此快指针会在环内追到慢指针，
     * 二者相遇就代表有环。
     */
    public static boolean bm6(ListNode head) {
        //先判断链表为空的情况
        if (head == null) {
            return false;
        }
        //快慢双指针
        ListNode fast = head;
        ListNode slow = head;
        //如果没环快指针会先到链表尾
        while (fast != null && fast.next != null) {
            //快指针移动两步
            fast = fast.next.next;
            //慢指针移动一步
            slow = slow.next;
            //相遇则有环
            if (fast == slow) {
                return true;
            }
        }
        //到末尾则没有环
        return false;
    }

    /**
     * BM5 合并k个已排序的链表
     * 方法一：归并排序思想（推荐使用）
     */
    public static ListNode bm5(ArrayList<ListNode> lists) {
        //k个链表归并排序
        return divideMerge5(lists, 0, lists.size() - 1);
    }

    //划分合并区间函数
    public static ListNode divideMerge5(ArrayList<ListNode> lists, int left, int right) {
        if (left > right) {
            return null;
        } else if (left == right) {
            //中间一个的情况
            return lists.get(left);
        }
        //从中间分成两段，再将合并好的两段合并
        int mid = (left + right) / 2;
        return merge5(divideMerge5(lists, left, mid), divideMerge5(lists, mid + 1, right));
    }

    //两个链表合并函数
    public static ListNode merge5(ListNode list1, ListNode list2) {
        //一个已经为空了，直接返回另一个
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        //加一个表头
        ListNode head = new ListNode(0);
        ListNode cur = head;
        //两个链表都要不为空
        while (list1 != null && list2 != null) {
            //取较小值的节点
            if (list1.val <= list2.val) {
                cur.next = list1;
                //只移动取值的指针
                list1 = list1.next;
            } else {
                cur.next = list2;
                //只移动取值的指针
                list2 = list2.next;
            }
            //指针后移
            cur = cur.next;
        }
        //哪个链表还有剩，直接连在后面
        if (list1 != null) {
            cur.next = list1;
        } else {
            cur.next = list2;
        }
        //返回值去掉表头
        return head.next;
    }

    //方法二：优先队列（扩展思路）
    //优先级队列结构，底层就是堆结构。不传比较器默认是小根堆，依次弹出的话，是按照较小值先弹出，从小到大输出。
    //优先队列即PriorityQueue，是一种内置的机遇堆排序的容器，分为大顶堆与小顶堆，大顶堆的堆顶为最大元素，
    // 其余更小的元素在堆下方，小顶堆与其刚好相反。且因为容器内部的次序基于堆排序，因此每次插入元素时间
    // 复杂度都是O(log2n)，而每次取出堆顶元素都是直接取出。
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ListNode bm5Two(ArrayList<ListNode> lists) {
        //小顶堆
        Queue<ListNode> queue = new PriorityQueue<>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val - o2.val;//负数, o1 o2，升序
            }
        });
        //遍历所有链表第一个元素
        for (int i = 0; i < lists.size(); i++) {
            //不为空则加入小顶堆
            if (lists.get(i) != null) {
                queue.add(lists.get(i));
            }
        }
        //加一个表头
        ListNode res = new ListNode(-1);
        ListNode head = res;
        //直到小顶堆为空
        while (!queue.isEmpty()) {
            //取出最小的元素
            ListNode temp = queue.poll();
            //连接
            head.next = temp;
            head = head.next;
            //每次取出链表的后一个元素加入小顶堆
            if (temp.next != null) {
                queue.add(temp.next);
            }
        }
        //去掉表头
        return res.next;
    }


    /**
     * BM4 合并两个排序的链表
     * 算法思想一：迭代，类似于归并排序法（答案的代码好像不是Java，略过）
     * 算法思想二：递归
     */
    public static ListNode bm4(ListNode list1, ListNode list2) {
        // list1 list2为空的情况
        if (list1 == null || list2 == null) {
            return list1 != null ? list1 : list2;
        }
        // 两个链表元素依次对比
        if (list1.val <= list2.val) {
            // 递归计算 list1.next, list2
            list1.next = bm4(list1.next, list2);
            return list1;

        } else {
            // 递归计算 list1, list2.next
//            list2.next = bm4(list1, list2.next);//与下面这句都可以
            list2.next = bm4(list2.next, list1);
            return list2;
        }
    }

    //算法思路三：借助额外数组
    public static ListNode bm4Two(ListNode list1, ListNode list2) {
        // list1 list2为空的情况
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        //将两个链表存放在list中
        ArrayList<Integer> list = new ArrayList<>();
        // 遍历存储list1
        while (list1 != null) {
            list.add(list1.val);
            list1 = list1.next;
        }
        // 遍历存储list2
        while (list2 != null) {
            list.add(list2.val);
            list2 = list2.next;
        }
        // 对 list 排序
        Collections.sort(list);
        // 将list转换为 链表
        ListNode newHead = new ListNode(list.get(0));
        ListNode cur = newHead;
        for (int i = 1; i < list.size(); i++) {
            cur.next = new ListNode(list.get(i));
            cur = cur.next;
        }
        // 输出合并链表
        return newHead;
    }


    /**
     * BM3 链表中的节点每k个一组翻转
     * 递归的三段式模版：
     * 终止条件： 当进行到最后一个分组，即不足k次遍历到链表尾（0次也算），就将剩余的部分直接返回。
     * 返回值： 每一级要返回的就是翻转后的这一分组的头，以及连接好它后面所有翻转好的分组链表。
     * 本级任务： 对于每个子问题，先遍历k次，找到该组结尾在哪里，然后从这一组开头遍历到结尾，依次翻转，结尾就可以作为下一个分组的开头，而先前指向开头的元素已经跑到了这一分组的最后，可以用它来连接它后面的子问题，即后面分组的头。
     */
    public static ListNode bm3(ListNode head, int k) {
        //找到每次翻转的尾部
        ListNode tail = head;
        //遍历k次到尾部
        for (int i = 0; i < k; i++) {
            //如果不足k到了链表尾，直接返回，不翻转
            if (tail == null) {
                return head;
            }
            tail = tail.next;
        }
        //翻转时需要的前序和当前节点
        ListNode pre = null;
        ListNode cur = head;
        //在到达当前段尾节点前
        while (cur != tail) {
            //翻转
            ListNode temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        //当前尾指向下一段要翻转的链表
        head.next = bm3(tail, k);
        return pre;
    }

    /**
     * BM2 链表内指定区间反转
     * 解法一：双指针(两次遍历)
     * 说明：方便理解，以下注释中将用left，right分别代替m,n节点
     */
    public static ListNode bm2(ListNode head, int m, int n) {
        //设置虚拟头节点
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;
        ListNode pre = dummyNode;
        //1.走left-1步到left的前一个节点
        for (int i = 0; i < m - 1; i++) {
            pre = pre.next;
        }
        //2.走right-left+1步到right节点
        ListNode rightNode = pre;
        for (int i = 0; i < n - m + 1; i++) {
            rightNode = rightNode.next;
        }
        //3.截取出一个子链表
        ListNode leftNode = pre.next;
        ListNode cur = rightNode.next;
        //4.切断链接
        pre.next = null;
        rightNode.next = null;
        //5.反转局部链表
        reverseLinkedList2(leftNode);
        //6.接回原来的链表
        pre.next = rightNode;
        leftNode.next = cur;
        return dummyNode.next;
    }

    //反转局部链表
    public static void reverseLinkedList2(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            //Cur_next 指向cur节点的下一个节点
            ListNode Cur_next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = Cur_next;
        }
    }

    //一次遍历（对解法一的优化）
    public static ListNode bm2Two(ListNode head, int m, int n) {
        //设置虚拟头节点
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;
        ListNode pre = dummyNode;
        for (int i = 0; i < m - 1; i++) {
            pre = pre.next;
        }
        ListNode cur = pre.next;
        ListNode Cur_next;
        for (int i = 0; i < n - m; i++) {
            Cur_next = cur.next;
            cur.next = Cur_next.next;
            Cur_next.next = pre.next;
            pre.next = Cur_next;
        }
        return dummyNode.next;
    }

    /**
     * BM1 反转链表
     * 1，使用栈解决。栈是先进后出的。实现原理就是把链表节点一个个入栈，当全部入栈完之后再一个个出栈，
     * 出栈的时候在把出栈的结点串成一个新的链表。
     */
    public static ListNode bm1(ListNode head) {
        Stack<ListNode> stack = new Stack<>();
        //把链表节点全部摘掉放到栈中
        while (head != null) {
            stack.push(head);//入栈
            head = head.next;
        }
        if (stack.isEmpty()) {
            return null;
        }
        ListNode node = stack.pop();//出栈
        ListNode dummy = node;
        //栈中的结点全部出栈，然后重新连成一个新的链表
        while (!stack.isEmpty()) {
            ListNode tempNode = stack.pop();
            node.next = tempNode;
            node = node.next;
        }
        //最后一个结点就是反转前的头结点，一定要让他的next
        //等于空，否则会构成环
        node.next = null;
        return dummy;
    }

    //双链表求解，把原链表的结点一个个摘掉，每次摘掉的链表都让他成为新的链表的头结点，然后更新新链表。
    //他每次访问的原链表节点都会成为新链表的头结点
    public static ListNode bm1Two(ListNode head) {
        //新链表
        ListNode newHead = null;
        while (head != null) {
            //先保存访问的节点的下一个节点，保存起来
            //留着下一步访问的
            ListNode temp = head.next;
            //每次访问的原链表节点都会成为新链表的头结点，
            //其实就是把新链表挂到访问的原链表节点的
            //后面就行了
            head.next = newHead;
            //更新新链表
            newHead = head;
            //重新赋值，继续访问
            head = temp;
        }
        //返回新链表
        return newHead;
    }

    public static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
