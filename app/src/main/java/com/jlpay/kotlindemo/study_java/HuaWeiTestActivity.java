package com.jlpay.kotlindemo.study_java;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HJ5 进制转换
 * 0xAA: 10*16+10
 * Scanner类的next() 和nextLine()的区别:
 * https://blog.csdn.net/weixin_44718865/article/details/121572499?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0.pc_relevant_default&spm=1001.2101.3001.4242.1&utm_relevant_index=3
 * Scanner是一个扫描器，我们录取到键盘的数据，先存到缓存区等待读取，它判断读取结束的标示是 空白符；比如空格，回车，tab 等等
 * next():
 * 1、一定要读取到有效字符后才可以结束输入。
 * 2、对输入有效字符之前遇到的空白，next() 方法会自动将其去掉。
 * 3、只有输入有效字符后才将其后面输入的空白作为分隔符或者结束符。
 * 4、next() 不能得到带有空格的字符串。
 * <p>
 * nextLine()：
 * 1、以Enter为结束符,也就是说 nextLine()方法返回的是输入回车之前的所有字符。
 * 2、可以获得空白。
 * <p>
 * hasNext()方法会判断接下来是否有非空字符.如果有,则返回true,否则返回false
 * <p>
 * hasNextLine() 方法会根据行匹配模式去判断接下来是否有一行(包括空行),如果有,则返回true,否则返回false
 *
 * <p>
 * nextInt()：
 * 1、nextInt()方法接收一个整形数据，该方法以空白符或者换行符作为分隔符读取输入中的下一个整形数据，
 * 中间的多个空格符或者换行符都被跳过，读取完之后，光标依然停留在当前行。如需要让光标读取下一行的数据，
 * 则需要用nextLine()方法读取缓存中的换行符之后移动到下一行。
 */
public class HuaWeiTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_test);
    }

    public void onClickTest(View view) {
//        hj39TestIpConvert();
        arithmeticTest();
    }


    /**
     * HJ60 查找组成一个偶数最接近的两个素数
     */
    public static void hj60() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int num = scanner.nextInt();
            int i = count60(num);
            System.out.println(i);
            System.out.println(num - i);
        }
    }

    //判断是否为素数
    public static boolean isPrime60(int num) {
        if (num == 1) {
            return false;
        }
        for (int i = 2; i <= num / i; i++) {
//        for (int i = 2; i <= (int) Math.sqrt(num); i++) {//与上面等价
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int count60(int n) {
        int i = n / 2;
        int j = n - i;
        while (!isPrime60(i) || !isPrime60(j)) {
            i++;
            j--;
        }
        return j;
    }


    /**
     * HJ59 找出字符串中第一个只出现一次的字符
     */
    public static void hj59() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.next();
            boolean singal = false;//是否存在只出现一次的字符
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (str.indexOf(c) == str.lastIndexOf(c)) {
                    singal = true;
                    System.out.println(c);
                    break;
                }
            }
            if (!singal) {
                System.out.println(-1);
            }
            System.out.println();
        }
    }


    /**
     * HJ58 输入n个整数，输出其中最小的k个
     */
    public static void hj58() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//输入n个整数
            int k = scanner.nextInt();//找出其中最小的k个整数
            int[] numArray = new int[n];
            for (int i = 0; i < n; i++) {
                int num = scanner.nextInt();
                numArray[i] = num;
            }
            Arrays.sort(numArray);
            for (int i = 0; i < k; i++) {
                System.out.print(numArray[i] + " ");
            }
        }
    }


    /**
     * HJ57 高精度整数加法
     */
    public static void hj57() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s1 = scanner.next();
            String s2 = scanner.next();
            BigInteger bigInteger1 = new BigInteger(s1);
            BigInteger bigInteger2 = new BigInteger(s2);
            System.out.println(bigInteger1.add(bigInteger2));

            //Exception in thread "main" java.lang.NumberFormatException:
            // For input string: "56763992306440108823"
            //at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
            //Long aLong1 = Long.valueOf(s1);
            //Long aLong2 = Long.valueOf(s2);
            //System.out.println(aLong1 + aLong2);
        }
    }


    /**
     * HJ56 完全数计算
     */
    public static void hj56() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//
            int comNunCount = 0;//用来记录不超过n(含n)的完全数的个数
            ArrayList<Integer> approFactorList = new ArrayList<>();//用来保存每个测试数的真因子
            for (int i = 1; i <= n; i++) {
                //遍历从1<i<=n的所有数字，看它是不是完全数，i就是目前这个测试的这个数
                //完全数需要满足条件：它所有的真因子（即除了自身以外的约数）的和（即因子函数），恰好等于它本身
                //需要做2件事，

                // 一求出i的真因子，
//                for (int j = 1; j <= i/2; j++) {
                for (int j = 1; j < i; j++) {
                    if (i % j == 0) {
                        approFactorList.add(j);
                    }
                }
                // 二求i所有的真因子（即除了自身以外的约数）的和（即因子函数），判断是否等于i
                int approFactorSum = 0;
                for (Integer integer : approFactorList) {
                    approFactorSum += integer;
                }
                if (i == approFactorSum) {
                    comNunCount++;
                }
                approFactorList.clear();
            }
            System.out.println(comNunCount);
        }
    }


    /**
     * HJ55 挑7
     */
    public static void hj55() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            int count = 0;
            for (int i = 1; i <= nextInt; i++) {
                if (i % 7 == 0) {
                    count++;
                } else {
                    String s = String.valueOf(i);
                    if (s.contains("7")) {
                        count++;
                    }
                }
            }
            System.out.println(count);
        }
    }


    /**
     * HJ54 表达式求值
     * 和 HJ50 四则运算 的解法一样，并且 HJ50 更复杂一些
     */
    public static void hj54() {
        hj50();
    }


    /**
     * HJ53 杨辉三角的变形
     * 找规律的题，只要往下再写几行就可以看出奇偶的规律，而且每行只需要写前几个就可以了，
     * 因为题目问的是第一个偶数的index。
     * 于是我们会发现，只有n为1，2时，没有出现偶数，剩下的按照2 3 2 4的规律每四行循环一次。
     */
    public static void hj53() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            int num = scanner.nextInt();
            if (num == 1 || num == 2) {
                System.out.println(-1);
            } else if (num % 4 == 0) {
                System.out.println(3);
            } else if (num % 4 == 1 || num % 4 == 3) {
                System.out.println(2);
            } else if (num % 4 == 2) {
                System.out.println(4);
            }
        }
    }


    /**
     * TODO 没看懂
     * HJ52 计算字符串的编辑距离
     * Java版本,看到字符串修改代价第一时间想到动态规划
     * A[0,...i-1]最后修改为B[0,...j-1]，有以下两种情况：
     * （一）A[i-1] == B[j-1]时，最后一个元素不用动，只用考虑A[0,...i-2]编辑为B[0,...j-2]需要的代价,dp[i][j] = dp[i-1][j-1]
     * （二）A[i-1]!=B[j-1]时，又可以分成以下三种情况：
     * 1、从A[0,...i-2]编辑为B[0,...j-1]，再删除A[i-1]
     * 2、从A[0,...i-1]编辑为B[0,...j-2]，再插入B[j-1]
     * 3、从A[0,...i-2]编辑为B[0,...j-2]，再将A[i-1]修改为B[j-1]
     * 每次取三种情况最小值
     * 最后返回dp[n][m];
     */
    public static void hj52() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String s1 = sc.next();
            String s2 = sc.next();
            int dp[][] = new int[s1.length() + 1][s2.length() + 1];
            dp[0][0] = 0;
            //dp.length获取的是行数
            for (int i = 1; i < dp.length; i++) {
                dp[i][0] = i;
            }
            //dp[0].length获取的是列数
            for (int i = 1; i < dp[0].length; i++) {
                dp[0][i] = i;
            }
            for (int i = 1; i < dp.length; i++) {
                for (int j = 1; j < dp[0].length; j++) {
                    if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = Math.min(dp[i - 1][j - 1] + 1, Math.min(dp[i][j - 1] + 1, dp[i - 1][j] + 1));
                    }
                }
            }
            System.out.println(dp[s1.length()][s2.length()]);
        }
    }


    /**
     * HJ51 输出单向链表中倒数第k个结点
     * 思路：在遍历到整数第k个时标记头节点，然后标记点和遍历点同时向后走，此时标记点和遍历点永远距离k，
     * 当遍历完成后标记点走到倒数第k,直接输出。时间复杂度O(n)
     * 也就是快慢指针思路，只不过这里的快慢指针速度相同
     */
    public static void hj51() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = Integer.parseInt(scanner.next());
            ListNode51 head = new ListNode51(-1);
            ListNode51 temp = head;
            //生成链表
            for (int i = 0; i < n; i++) {
                ListNode51 node = new ListNode51(scanner.nextInt());
                temp.next = node;
                temp = temp.next;
            }
            int k = Integer.parseInt(scanner.next());
            //使用快慢指针
            if (getKthFromEnd(head.next, k) != null) {
                System.out.println(getKthFromEnd(head.next, k).val);
            } else {
                System.out.println(0);
            }
        }
    }

    static ListNode51 getKthFromEnd(ListNode51 head, int k) {
        if (head == null) {
            return null;
        }
        ListNode51 fast = head;
        ListNode51 slow = head;
        //快指针先走k步
        for (int i = 0; i < k; i++) {
            if (fast == null) {
                return fast;
            }
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    static class ListNode51 {
        int val;
        ListNode51 next;

        public ListNode51(int val) {
            this.val = val;
        }

        public ListNode51(int val, ListNode51 next) {
            this.val = val;
            this.next = next;
        }
    }

    /**
     * HJ50 四则运算
     * 注意：可能出现多个数字连在一起组成多位数，可能出现负数
     * 利用栈（先进后出）求解
     * TODO 没搞懂
     */
    public static void hj50() {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int n = s.length();
        //把加减乘除分成2组，分别是+-和*/
        //用o1和o2来储存当前符号，具体定义为 :+号，o1=1；-号, o1=-1；*号 , o2=1；/号 , o2=-1；
        int num1 = 0;
        int o1 = 1;
        int num2 = 1;
        int o2 = 1;
        Stack<Integer> stk = new Stack<>();
        //时间复杂度：O(n)，时间复杂度和表达式长度正相关
        //空间复杂度：O(n)，需要一个栈，长度和表达式长度正相关
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                //遇到数字则定义num2
                int cur = 0;
                while (i < n && Character.isDigit(s.charAt(i))) {
                    cur = cur * 10 + (s.charAt(i) - '0');
                    i++;
                }
                i--;//用在下面 “-”是负号还是减号的判断
                num2 = o2 == 1 ? num2 * cur : num2 / cur;

            } else if (c == '*' || c == '/') {
                //遇到×÷定义o2
                o2 = c == '*' ? 1 : -1;

            } else if (c == '{' || c == '[' || c == '(') {
                //遇到括号则保存当前结果，并初始化
                stk.push(num1);//入栈
                stk.push(o1);
                stk.push(num2);
                stk.push(o2);
                num1 = 0;
                o1 = 1;
                num2 = 1;
                o2 = 1;

            } else if (c == '+' || c == '-') {
                //遇到加减，说明可以开始计算，计算num1并对定义其他几个变量
                if (c == '-' &&
                        (i == 0 || s.charAt(i - 1) == '(' || s.charAt(i - 1) == '[' || s.charAt(i - 1) == '{')
                ) {
                    //“-”是负号还是减号的判断，减号就跳出本次循环
                    o1 = -1;
                    continue;
                }
                num1 = num1 + o1 * num2;
                o1 = (c == '+' ? 1 : -1);
                num2 = 1;
                o2 = 1;

            } else {
                //遇到右括号，则出栈，并计算num2
                int cur = num1 + o1 * num2;
                o2 = stk.pop();
                num2 = stk.pop();
                o1 = stk.pop();
                num1 = stk.pop();
                num2 = o2 == 1 ? num2 * cur : num2 / cur;
            }
        }
        System.out.println(num1 + o1 * num2);
    }


    //四则运算测试
    public static void arithmeticTest() {
        int a = -4 / 2; //-2
        System.out.println("结果是：" + a);
    }

    public static void hj48Two() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = Integer.parseInt(in.next());
            int val = Integer.parseInt(in.next());
            ListNode48 listNode48Head = new ListNode48(val);
            for (int i = 1; i < n; i++) {
                val = Integer.parseInt(in.next());
                int node = Integer.parseInt(in.next());
                insert48(val, node, listNode48Head);
            }
            int node = Integer.parseInt(in.next());
            ListNode48 newHead = delete48(node, listNode48Head);
            while (newHead != null) {
                System.out.print(newHead.val + " ");
                newHead = newHead.next;
            }
            System.out.println();
        }
    }

    //在2->3->4 链表中插入节点值为5的节点变为 2->5->3->4
    //val = 5, node = 2
    static ListNode48 insert48(int val, int node, ListNode48 head) {
        while (head.val != node) {
            head = head.next;
        }
        ListNode48 newNode = new ListNode48(val, head.next);
        head.next = newNode;
        return head;
    }

    //node：节点上的值为node的节点
    static ListNode48 delete48(int node, ListNode48 head) {
        ListNode48 dummy = new ListNode48(0, head);
        ListNode48 p = dummy;
        while (p.next != null && p.next.val != node) {
            p = p.next;
        }
        //如果找到了节点上的值为node的节点，就删除它
        if (p.next != null) {
            p.next = p.next.next;
        }
        //否则就不做任何处理
        head = dummy.next;
        return head;
    }

    static class ListNode48 {
        int val;
        ListNode48 next;

        public ListNode48(int val) {
            this.val = val;
        }

        public ListNode48(int val, ListNode48 next) {
            this.val = val;
            this.next = next;
        }
    }


    //不需要链表，一个有插入功能的数组就可以了。比如Java中直接用一个ArrayList即可。
    public static void hj48() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int total = sc.nextInt();
            int head = sc.nextInt();

            List<Integer> linkedlist = new ArrayList<>();
            linkedlist.add(head);
            for (int i = 0; i < total - 1; i++) {
                int value = sc.nextInt();
                int target = sc.nextInt();
                linkedlist.add(linkedlist.indexOf(target) + 1, value);
            }
            int remove = sc.nextInt();
            linkedlist.remove(linkedlist.indexOf(remove));
            for (int i = 0; i < linkedlist.size(); i++) {
                System.out.print(linkedlist.get(i) + " ");
            }
            System.out.println();
        }
    }


    /**
     * HJ48 从单向链表中删除指定值的节点
     * 自己的思路 未完成
     */
    public static void hj48Todo() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            //第一步：获取 输入链表结点个数，输入头结点的值
            int linkedListLen = scanner.nextInt();
            int headNodeValue = scanner.nextInt();
            //第二步：按照格式插入各个结点，完成链表
            //注意：某个节点的next是可能发生变化的
            Node<Integer> head = new Node<>(headNodeValue);
            for (int i = 0; i < linkedListLen; i++) {
                //value2->value1
                int value1 = scanner.nextInt();
                int value2 = scanner.nextInt();
                //遍历链表，找到链表上节点值等于value2的节点，将它的next节点改为value1，
                // 原来该节点后面的部分链接到新的value1节点后面
                //TODO 不会写了
            }
        }
        //第三步：获取要删除的结点的值，进行删除，打印删除节点后的新链表的值。
    }

    //定义一个单链表
    static class Node<T> {
        T value;
        Node<T> next;

        public Node(T value) {
            this.value = value;
        }

        public Node() {
        }
    }


    /**
     * HJ46 截取字符串
     */
    public static void hj46() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            int nextInt = Integer.parseInt(scanner.nextLine());
            if (str.length() >= nextInt) {
                String substring = str.substring(0, nextInt);
                System.out.println(substring);
            } else {
                System.out.println(str);
            }
        }
    }


    /**
     * HJ45 名字的漂亮度
     * 思路：
     * 1.开一个整型数组，统计字母出现的次数，下标对应字母ASCII码
     * 2.对字母出现次数排序
     * 3.计算漂亮度
     */
    public static void hj45() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            for (int i = 0; i < nextInt; i++) {
                String str = scanner.next().toLowerCase(Locale.ROOT);
                //ASCII 编码表 总共有 128 个字符。这样数组的每一位就可以代表每一个字符出现的次数
                int[] s = new int[128];
                for (int j = 0; j < str.length(); j++) {
                    //str.charAt(j)就是一个ASCII中的字符，可以看成对应的十进制数，比如a可以看成97
                    s[str.charAt(j)]++;
                }
                Arrays.sort(s);
                int mul = 26;
                int sum = 0;
                for (int j = s.length - 1; j >= 0; j--) {
                    sum += s[j] * mul;
                    mul--;
                }
                System.out.println(sum);
            }
        }
    }


    /**
     * HJ44 Sudoku
     */
    public static void hj44() {
        //第一步，获取输入
        Scanner in = new Scanner(System.in);
        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = in.nextInt();
            }
        }
        //第二步，处理输入数独
        solveSudoku(board);
        //第三步，打印输出
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            //换行，每一行的最后一个数字
            System.out.println(board[i][8]);
        }
    }

    public static boolean solveSudoku(int[][] board) {
        //「一个for循环遍历棋盘的行，一个for循环遍历棋盘的列，
        // 一行一***定下来之后，递归遍历这个位置放9个数字的可能性！」
        for (int i = 0; i < 9; i++) {// 遍历行
            for (int j = 0; j < 9; j++) {// 遍历列
                if (board[i][j] != 0) {// 跳过原始数字
                    continue;
                }
                for (int k = 1; k <= 9; k++) {// (i, j) 这个位置放k是否合适
                    if (isValidSudoku(i, j, k, board)) {//如果是有效的数独
                        board[i][j] = k;//将k放在（i，j）
                        if (solveSudoku(board)) {// 如果找到合适一组立刻返回
                            return true;
                        }
                        board[i][j] = 0;//回溯，走到这里说明之前放入的k值不合适，所以要回溯
                    }
                }
                // 9个数都试完了，都不行，那么就返回false
                return false;
                // 因为如果一行一***定下来了，这里尝试了9个数都不行，说明这个棋盘找不到解决数独问题的解！
                // 那么会直接返回， 「这也就是为什么没有终止条件也不会永远填不满棋盘而无限递归下去！」
            }
        }
        // 遍历完没有返回false，说明找到了合适棋盘位置了
        return true;
    }

    /**
     * 判断棋盘是否合法有如下三个维度:
     * 同行是否重复
     * 同列是否重复
     * 9宫格里是否重复
     */
    public static boolean isValidSudoku(int row, int col, int val, int[][] board) {
        // 同行是否重复
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == val) {
                return false;
            }
        }
        // 同列是否重复
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == val) {
                return false;
            }
        }
        // 9宫格里是否重复
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == val) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void hj43Two() {
        //TODO  题解里有 第一遍刷题暂时先不看了
    }

    /**
     * HJ43 迷宫问题
     * 思路：广度优先遍历矩阵。代价相同的图中，广度优先遍历可以保证遍历到的目标点就是经过最短路径到达的点。
     * 为此，我们可以创建一个Point类，属性为横纵坐标和父节点。从（0，0）出发，将经过的坐标点都设为1，
     * 避免重复经过而进入死循环。把当前点的上下左右值为0的点都加入队列中，
     * 直到遇见出口为止。遇到出口时，pos的father路径就是最短路径的逆路径。此时只需要把逆路径反转一下即可。
     * <p>
     * for循环中++i和i++的区别
     * https://blog.csdn.net/qq_36484670/article/details/106236573
     * 无论是i++ 还是 ++i，for循环中所引用的都是 i 计算后的值，因此在for循环中 ++i 和 i++ 的结果是一样的，
     * 但是性能是不同的。在大量数据的时候++i的性能要比i++的性能好
     */
    public static void hj43() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            int row = scanner.nextInt();
            int column = scanner.nextInt();
            int[][] grid = new int[row][column];
            //注意这里是++i和++j
            for (int i = 0; i < row; ++i) {
                for (int j = 0; j < column; ++j) {
                    grid[i][j] = scanner.nextInt();
                }
            }
            Queue<Point> que = new LinkedList<>();//队列，先进先出
            que.offer(new Point(0, 0, null));//入队。不超出容量限制的情况下插入队列
            grid[0][0] = 1;
            Point pos = null;
            while (true) {
                //获取当前点
                pos = que.poll();//出队。返回此队列的头部，如果此队列为空，则返回 null。
                if (pos == null) {
                    break;
                }
                int px = pos.px;
                int py = pos.py;
                //到达矩阵右下角了
                if (px == row - 1 && py == column - 1) {
                    break;
                } else {
                    //把当前点的上下左右值为0的点都加入队列中
                    //右
                    if (px + 1 < row && grid[px + 1][py] == 0) {
                        grid[px + 1][py] = 1;
                        que.offer(new Point(px + 1, py, pos));
                    }
                    //上
                    if (py - 1 >= 0 && grid[px][py - 1] == 0) {
                        grid[px][py - 1] = 1;
                        que.offer(new Point(px, py - 1, pos));
                    }
                    //左
                    if (px - 1 >= 0 && grid[px - 1][py] == 0) {
                        grid[px - 1][py] = 1;
                        que.offer(new Point(px - 1, py, pos));
                    }
                    //下
                    if (py + 1 < column && grid[px][py + 1] == 0) {
                        grid[px][py + 1] = 1;
                        que.offer(new Point(px, py + 1, pos));
                    }
                }
            }
            Stack<Point> stack = new Stack<>();//栈，先进后出
            //这时候的pos就是矩阵右下角的点
            //从尾到头入栈
            while (pos != null) {
                stack.push(pos);//入栈
                pos = pos.father;
            }
            //从头到尾出栈
            while (!stack.isEmpty()) {
                Point temp = stack.peek();//查看此堆栈顶部的对象而不将其从堆栈中移除。
                stack.pop();//出栈。移除此堆栈顶部的对象并将该对象作为此函数的值返回。
                System.out.println("(" + temp.px + "," + temp.py + ")");
            }
        }
    }

    static class Point {
        int px;
        int py;
        Point father;

        Point(int px, int py, Point father) {
            this.px = px;
            this.py = py;
            this.father = father;
        }

        Point() {
        }
    }


    /**
     * HJ42 学英语
     */
    public static void hj42() {
        //第一步，做数字和英文的映射表
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            long nextLong = scanner.nextLong();
            if (String.valueOf(nextLong).length() <= 9) {
                System.out.println(getNumEnglish((int) nextLong));
            } else {
                System.out.println("error");
            }
        }
        scanner.close();
    }

    public static String[] ones = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "forteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty"};

    public static String[] twieties = new String[]{"zero", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    public static String getNumEnglish(int num) {
        String stren = "error";
        if (num <= 20) {
            stren = ones[num];
        } else if (num < 100) {
            int remainder = num % 10;
            if (remainder == 0) {
                stren = twieties[num / 10];
            } else {
                stren = twieties[num / 10] + " " + ones[remainder];
            }
        } else if (num < 1000) {
            int remainder = num % 100;
            if (remainder == 0) {
                stren = ones[num / 100] + " hundred";
            } else {
                stren = ones[num / 100] + " hundred and " + getNumEnglish(remainder);
            }
        } else if (num < 1000000) {
            int remainder = num % 1000;
            if (remainder == 0) {
                stren = getNumEnglish(num / 1000) + " thousand";
            } else {
                stren = getNumEnglish(num / 1000) + " thousand " + getNumEnglish(remainder);
            }
        } else if (num < 1000000000) {
            int remainder = num % 1000000;
            if (remainder == 0) {
                stren = getNumEnglish(num / 1000000) + " million";
            } else {
                stren = getNumEnglish(num / 1000000) + " million " + getNumEnglish(remainder);
            }
        }
        return stren;
    }


    /**
     * HJ41 称砝码
     */
    public static void hj41() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            //第一步，获取三个参数，并把每种重量的砝码和对应的砝码数量放到Map中
            //TODO 最好做下参数做范围检查
            String weightTypeNum = scanner.nextLine();//砝码的种数(范围[1,10])
            String weightEveryOne = scanner.nextLine();//每种砝码的重量(范围[1,2000])
            String[] weightEveryOneSplit = weightEveryOne.split(" ");
            String numEveryOne = scanner.nextLine();//每种砝码对应的数量(范围[1,10])
            String[] numEveryOneSplit = numEveryOne.split(" ");
            if (weightEveryOneSplit.length != numEveryOneSplit.length) {
                return;
            }
            HashSet<Integer> resultSet = new HashSet<>();//存放所有可能的结果，不用担心重复问题
            resultSet.add(0);
            for (int i = 0; i < weightEveryOneSplit.length; i++) {
                List<Integer> list = new ArrayList<>(resultSet);
                for (int j = 0; j <= Integer.parseInt(numEveryOneSplit[i]); j++) {
                    for (int k = 0; k < list.size(); k++) {
                        resultSet.add(list.get(k) + Integer.parseInt(weightEveryOneSplit[i]) * j);
                    }
                }
            }
            System.out.println(resultSet.size());
        }
    }


    /**
     * HJ40 统计字符
     */
    public static void hj40Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            String s1 = nextLine.replaceAll("[A-Z]+[a-z]", "");
            System.out.println(nextLine.length() - s1.length());
            String s2 = s1.replaceAll(" ", "");
            System.out.println(s1.length() - s2.length());
            String s3 = s2.replaceAll("[0-9]", "");
            System.out.println(s2.length() - s3.length());
            System.out.println(s3.length());
        }
    }


    public static void hj40() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            char[] charArray = nextLine.toCharArray();
            int englishNum = 0;
            int numberNum = 0;
            int spaceNum = 0;
            int otherNum = 0;
            Pattern pattern = Pattern.compile("[A-Za-z]");
            Pattern pattern2 = Pattern.compile("[0-9]");
            for (char ch : charArray) {
                Matcher matcher = pattern.matcher(String.valueOf(ch));
                boolean matchEngOk = matcher.find();
                Matcher matcher2 = pattern2.matcher(String.valueOf(ch));
                boolean matchNumOk = matcher2.find();
                if (ch == ' ') {
                    spaceNum++;
                } else if (matchEngOk) {
                    englishNum++;
                } else if (matchNumOk) {
                    numberNum++;
                } else {
                    otherNum++;
                }
            }
            System.out.println(englishNum);
            System.out.println(spaceNum);
            System.out.println(numberNum);
            System.out.println(otherNum);
        }
    }


    /**
     * HJ39 判断两个IP是否属于同一子网
     */
    public static void hj39TestIpConvert() {
        String dataConvert = ipDataConvert("192.168.0.1");
        System.out.println(dataConvert);
        String ipAddress = ipDataConvert("3232235521");
        System.out.println(ipAddress);
    }

    public static void hj39() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String subNetMask = scanner.next();//子网掩码
            String ip1 = scanner.next();//ip地址1
            String ip2 = scanner.next();//ip地址2
            //第一步，若IP地址或子网掩码格式非法则输出1
            if (!isLegalIp(subNetMask, false) || !isLegalIp(ip1, true) || !isLegalIp(ip2, true)) {
                System.out.println(1);
                continue;
            }
            //第二步，將IP地址分别与它们的子网掩码进行逻辑“与”运算后比較结果，相同则说明这两台主机在同一子网中。
            //若IP1与IP2属于同一子网络输出0，若IP1与IP2不属于同一子网络输出2
            Long subNetNum = Long.valueOf(ipDataConvert(subNetMask));//ipDataConvert这个方法重点看下
            Long ip1Num = Long.valueOf(ipDataConvert(ip1));
            Long ip2Num = Long.valueOf(ipDataConvert(ip2));
            //这里直接使用十进制数进行按位与就可以了。
            if ((subNetNum & ip1Num) == (subNetNum & ip2Num)) {
                System.out.println(0);
            } else {
                System.out.println(2);
            }
        }
    }

    public static boolean isLegalIp(String ip, boolean isIp) {
        boolean result = true;
        if (ip == null || ip.length() == 0) {
            result = false;
            return result;
        }
        if (!ip.contains(".")) {
            result = false;
            return result;
        }
        String[] split = ip.split("\\.");
        int[] subNetMaskArray = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            Integer integer = Integer.valueOf(split[i]);
            subNetMaskArray[i] = integer;
            if (integer < 0 || integer > 255) {
                result = false;
                break;
            }
        }
        if (!isIp) {
            result = subNetMaskArray[0] >= subNetMaskArray[1] &&
                    subNetMaskArray[1] >= subNetMaskArray[2] &&
                    subNetMaskArray[2] >= subNetMaskArray[3];
        }
        return result;
    }


    /**
     * HJ38 求小球落地5次后所经历的路程和第5次反弹的高度
     */
    public static void hj38(int height) {
        int n = 5;//定义下面算的都是第5次
        //第5次落地时，共经过多少米
        double totalDistance = height;
        for (int i = 1; i < n; i++) {
            double heightN = height * 1.0d / (Math.pow(2, i));
            totalDistance += heightN * 2;
        }
        System.out.println(totalDistance);

        //第5次反弹高度
        double height5 = height * 1.0d / (Math.pow(2, n));//Math.pow(2, 5)是2的5次方
        System.out.println(height5);
    }

    /**
     * HJ37 统计每个月兔子的总数
     * 每太懂
     * 斐波那契数列
     */
    public static int hj37(int month) {
//        所有兔子就三种,每个月更新三种的数量，迭代完全部相加即为所有兔子数量
//        k3-第三个月及以上，可生育
//        k2-第二个月，不可生育
//        k1-第一个月，小萌新
        if (month < 3) {
            return 1;
        }
        return hj37(month - 2) + hj37(month - 1);
    }


    /**
     * HJ36 字符串加密
     * 自己做的，复习时可以看看答案（人家用的是LinkedHashSet集合）
     */
    public static void hj36() {
        //A B C D E F G H I G K L M N
        //N I H A O B C D E F G J K L
        //第一步，获取key，变成新字母表头部（需要经过字母去重）；按顺序补充未包含的26个其余字母，得到密码映射表。
        //第二步，获取要加密的字符串，将字符串中的每个字母依次按照密码映射表得到新的字母，即是加密后的字符串。
        Scanner scanner = new Scanner(System.in);
        //另外，这两个值的获取可以用while循环判断一下while (sc.hasNext())，然后
        //  {String s1 = sc.nextLine();
        //            String s2 = sc.nextLine();}
        String key = scanner.nextLine();//获取key
//        String key = "nihao";//获取key
        String str = scanner.nextLine().trim();//获取要加密的字符串
//        String str = "ni";//获取要加密的字符串
        char[] keyChars = key.toCharArray();
        char[] alphaBetChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        List<Character> alphaBetList = new ArrayList<Character>();
        List<Character> psdList = new ArrayList<>();//密码映射表
        for (char keyChar : keyChars) {
            if (!psdList.contains(keyChar)) {
                psdList.add(keyChar);
            }
        }
        for (char alphaBetChar : alphaBetChars) {
            if (!psdList.contains(alphaBetChar)) {
                psdList.add(alphaBetChar);
            }
            alphaBetList.add(alphaBetChar);
        }

        //第二步字符串加密
        String[] splitStr = str.split(" ");
        StringBuilder encodeStr = new StringBuilder();
        for (String segmentStr : splitStr) {
            char[] strChars = segmentStr.toCharArray();
            for (char strChar : strChars) {
                int index = alphaBetList.indexOf(strChar);
                if (index != -1) {
                    Character encodeStrChar = psdList.get(index);
                    encodeStr.append(encodeStrChar);
                }
            }
            encodeStr.append(" ");
        }
        System.out.println(encodeStr.toString());
    }


    /**
     * HJ35 蛇形矩阵
     * 只是简单看懂了
     */
    public static void hj35() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            //读入正整数n
            int n = scanner.nextInt();
            //建立数组（n行）
            int[][] result = new int[n][];
            //记录依次赋予的数组值
            int t = 1;
            for (int i = 0; i < n; i++) {
                //数组第i行有n-i个元素
                result[i] = new int[n - i];
                //对第i个对角线赋值，是按照1，2，3，4...的顺序依次赋值给对应位置
                for (int j = 0; j < i + 1; j++) {
                    result[i - j][j] = t;
                    t++;
                }
            }
            //输出数组值
            for (int[] a : result) {
                for (int a1 : a) {
                    System.out.print(a1 + " ");
                }
                System.out.println();
            }
        }
    }


    /**
     * HJ34 图片整理
     */
    public static void hj34() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.next();
            char[] charArray = nextLine.toCharArray();
            Arrays.sort(charArray);//从小到大的顺序
            for (char c : charArray) {
                System.out.print(c);
            }
        }
    }


    /**
     * HJ33 整数与IP地址间的转换
     * Java split()用法：https://www.cnblogs.com/xiaoxiaohui2015/p/5838674.html
     * 注意：以点分割要做转义
     */
    public static void hj33() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            String result = ipDataConvert(next);
            System.out.println(result);
        }
    }

    private static String ipDataConvert(String next) {
        // ipv4 -> int
        if (next.contains(".")) {
            String[] split = next.split("\\.");
            long result = 0;
            //ip地址以点分割开后共有四个字符串
            for (int i = 0; i < 4; i++) {
                result = result * 256 + Integer.parseInt(split[i]);
            }
            return "" + result;

        } else {
            // int -> ipv4
            long ipv4 = Long.parseLong(next);
            String result = "";
            for (int i = 0; i < 4; i++) {
                result = ipv4 % 256 + "." + result;
                ipv4 /= 256;
            }
            return result.substring(0, result.length() - 1);
        }
    }


    /**
     * HJ32 密码截取
     * 最长回文子串的中心扩散法，遍历每个字符作为中间位，进行左右比较
     * ABA型：只需要从当前字符向两边扩散，比较左右字符是否相等，找出以当前字符为中心的最长回文子串长度
     * BBA型：只需要从当前字符和下一个字符向两边扩散，比较左右字符是否相等，找出以当前字符和下一个字符为中心的最长回文子串长度
     * 最后比对两种类型的长度，取自较长的长度
     * <p>
     * 时间复杂度：O(N^2)，需要遍历每个字符，复杂度为 O(N)，对于每个字符的处理也需要 O(N) 的复杂度，因此总的时间复杂度为 O(N^2)
     * 空间复杂度：O(1)，只用到左右双指针，无需额外空间
     */
    public static void hj32() {
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();//可以获得空格
        System.out.println(solution(nextLine));
    }

    private static int solution(String s) {
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            // ABA型
            int len1 = longest(s, i, i);
            // ABBA型
            int len2 = longest(s, i, i + 1);
            res = Math.max(res, Math.max(len1, len2));
        }
        return res;
    }

    private static int longest(String s, int a, int b) {
        while (a >= 0 && b < s.length() && s.charAt(a) == s.charAt(b)) {
            a--;
            b++;
        }
        return b - a - 1;
    }


    /**
     * HJ31 单词倒排
     */
    public static void hj31() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            String result = convertTo(nextLine);
            System.out.println(result);
        }
    }

    public static String convertTo(String nextLine) {
        // 匹配非字母的字符进行分割
        String[] strings = nextLine.split("[^A-Za-z]");
        StringBuilder stringBuilder = new StringBuilder();
        // 逆序添加分割完的单词
        for (int i = strings.length - 1; i >= 0; i--) {
            stringBuilder.append(strings[i]).append(" ");
        }
        return stringBuilder.toString().trim();
    }


    /**
     * HJ30 字符串合并处理
     * 1.将输入的两个字符串str1和str2进行前后合并
     * 2.对合并后的字符串进行排序，要求为：下标为奇数的字符和下标为偶数的字符分别从小到大排序(注意要按ascii码进行排序)。
     * 3.对排序后的字符串进行转换操作，需要进行转换的字符所代表的十六进制用二进制表示并倒序，然后再转换成对应的十六进制大写字符
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void hj30() {
        //hash 保存十六进制反转的对应表（第三步要求）， 空间换时间
        Map<Character, Character> map = new HashMap<Character, Character>() {
            {
                put('0', '0');
                put('1', '8');
                put('2', '4');
                put('3', 'C');
                put('4', '2');
                put('5', 'A');
                put('6', '6');
                put('7', 'E');
                put('8', '1');
                put('9', '9');
                put('a', '5');
                put('b', 'D');
                put('c', '3');
                put('d', 'B');
                put('e', '7');
                put('f', 'F');
                put('A', '5');
                put('B', 'D');
                put('C', '3');
                put('D', 'B');
                put('E', '7');
                put('F', 'F');
            }
        };
        Scanner scanner = new Scanner(System.in);
        String s = "";
        while (scanner.hasNext()) {
            String s1 = scanner.next();
            String s2 = scanner.next();
            char[] ch = (s1 + s2).toCharArray();
            //偶数位，因为包含一个0，所以会比奇数位多1。
            char[] r1 = new char[ch.length - ch.length / 2];
            //奇数位
            char[] r2 = new char[ch.length / 2];
            for (int i = 0, j = 0, k = 0; i < ch.length; i++) {
                if (i % 2 == 0) {
                    r1[j] = ch[i];
                    j++;
                } else {
                    r2[k] = ch[i];
                    k++;
                }
            }
            Arrays.sort(r1);//从小到大的顺序
            Arrays.sort(r2);//从小到大的顺序
            for (int i = 0, j = 0, k = 0; i < ch.length; i++) {
                if (i % 2 == 0) {
                    //char是基本类型，基本类型是不会为null的，只有对象（如String）才可以为null
                    ch[i] = map.getOrDefault(r1[j], r1[j]);
                    j++;
                } else {
                    ch[i] = map.getOrDefault(r2[k], r2[k]);
                    k++;
                }
            }
            s = new String(ch);
            System.out.println(s);
        }
    }


    /**
     * HJ29 字符串加解密
     */
    public static void hj29() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            System.out.println(encode(sc.nextLine()));
            System.out.println(decode(sc.nextLine()));
        }
    }

    public static String encode(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] >= 'A' && charArray[i] < 'Z') {
                charArray[i] = (char) (charArray[i] - 'A' + 'a' + 1);
            } else if (charArray[i] == 'Z') {
                charArray[i] = 'a';
            } else if (charArray[i] >= 'a' && charArray[i] < 'z') {
                charArray[i] = (char) (charArray[i] - 'a' + 'A' + 1);
            } else if (charArray[i] == 'z') {
                charArray[i] = 'A';
            } else if (charArray[i] >= '0' && charArray[i] < '9') {
                charArray[i] = (char) (charArray[i] + 1);
            } else if (charArray[i] == '9') {
                charArray[i] = '0';
            }
        }

        return new String(charArray);
    }

    public static String decode(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] > 'A' && charArray[i] <= 'Z') {
                charArray[i] = (char) (charArray[i] - 'A' + 'a' - 1);
            } else if (charArray[i] == 'A') {
                charArray[i] = 'z';
            } else if (charArray[i] > 'a' && charArray[i] <= 'z') {
                charArray[i] = (char) (charArray[i] - 'a' + 'A' - 1);
            } else if (charArray[i] == 'a') {
                charArray[i] = 'Z';
            } else if (charArray[i] > '0' && charArray[i] <= '9') {
                charArray[i] = (char) (charArray[i] - 1);
            } else if (charArray[i] == '0') {
                charArray[i] = '9';
            }
        }

        return new String(charArray);
    }


    /**
     * HJ28 素数伴侣
     * 匈牙利算法
     * 思路：1.首先定义两个list容器，分别存储输入整数中的奇数和偶数
     * 2.然后利用匈牙利算法找到“素数伴侣”对数最多时的配对数。匈牙利算法的核心思想是先到先得，能让就让。
     * 3.最后输出“素数伴侣”最多时的对数。
     */
    public static void hj28() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//待挑选的自然数的个数
            int[] arr = new int[n];//用于记录输入的n个整数
            ArrayList<Integer> odds = new ArrayList<>();//用于存储所有的奇数
            ArrayList<Integer> evens = new ArrayList<>();//用于存储所有的偶数
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
                //将奇数添加到odds
                if (arr[i] % 2 == 1) {
                    odds.add(arr[i]);
                }
                //将偶数添加到evens
                if (arr[i] % 2 == 0) {
                    evens.add(arr[i]);
                }
            }
            //下标对应已经匹配的偶数的下标，值对应这个偶数的伴侣
            int[] matcheven = new int[evens.size()];
            //记录伴侣的对数
            int count = 0;
            for (int i = 0; i < odds.size(); i++) {
                //用于标记对应的偶数是否查找过
                boolean[] v = new boolean[evens.size()];
                //如果匹配上，则计数加1
                if (find28(odds.get(i), matcheven, evens, v)) {
                    count++;
                }
            }
            System.out.println(count);
        }
    }

    //判断奇数x能否找到伴侣
    public static boolean find28(int x, int[] matcheven, ArrayList<Integer> evens, boolean[] v) {
        for (int i = 0; i < evens.size(); i++) {
            //该位置偶数没被访问过，并且能与x组成素数伴侣
            if (isPrime28(x + evens.get(i)) && v[i] == false) {
                v[i] = true;
                //如果i位置偶数还没有伴侣，则与x组成伴侣，如果已经有伴侣，并且这个伴侣能重新找到新伴侣，
                //则把原来伴侣让给别人，自己与x组成伴侣
                if (matcheven[i] == 0 || find28(matcheven[i], matcheven, evens, v)) {
                    matcheven[i] = x;
                    return true;
                }
            }
        }
        return false;
    }

    //判断x是否是素数
    public static boolean isPrime28(int x) {
        if (x == 1) {
            return false;
        }
        //如果能被2到根号x整除，则一定不是素数
        for (int i = 2; i <= (int) Math.sqrt(x); i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * HJ27 查找兄弟单词
     */
    public static void hj27() {
        //1.解析输入数据
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String[] splitStr = str.split(" ");
        //字典中单词的个数
        Integer worldNum = Integer.parseInt(splitStr[0]);
        //获取输入的单词
        String world = splitStr[splitStr.length - 2];
        //获取K值，最后要打印输出第K个兄弟单词
        Integer k = Integer.parseInt(splitStr[splitStr.length - 1]);

        //2.找出输入字典中的单词中，符合兄弟单词的单词，并存储
        List<String> brotherWorld = new ArrayList<>();
        for (int i = 1; i <= worldNum; i++) {
            String currentWorld = splitStr[i];
            if (world.length() != currentWorld.length() ||
                    world.equals(currentWorld)) {
                continue;
            }
            char[] currentWorldArray = currentWorld.toCharArray();
            char[] worldArray = world.toCharArray();
            Arrays.sort(currentWorldArray);//从小到大的顺序
            Arrays.sort(worldArray);//从小到大的顺序
            if (new String(currentWorldArray).equals(new String(worldArray))) {
                brotherWorld.add(currentWorld);
            }
        }

        //3.打印兄弟单词的数目
        System.out.println(brotherWorld.size());

        //4.如果兄弟单词的数目大于要获取的第K个兄弟单词，则排序输出第K个兄弟单词
        if (brotherWorld.size() >= k) {
            Collections.sort(brotherWorld);
            System.out.println(brotherWorld.get(k - 1));
        }
    }


    /**
     * HJ26 字符串排序
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void hj26() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String nextLine = sc.nextLine();
            char[] chars = nextLine.toCharArray();
            //开始排序
            //1.把字母抽出来
            List<Character> letters = new ArrayList<>();
            for (char aChar : chars) {
                if (Character.isLetter(aChar)) {
                    letters.add(aChar);
                }
            }
            //2.把抽离出来的字母列表不区分大小写排序
//            Collections.sort(letters, new Comparator<Character>() {
//                @Override
//                public int compare(Character o1, Character o2) {
//                    return Character.toLowerCase(o1) - Character.toLowerCase(o2);
//                }
//            });
            letters.sort(new Comparator<Character>() {
                @Override
                public int compare(Character o1, Character o2) {
                    return Character.toLowerCase(o1) - Character.toLowerCase(o2);
                }
            });
            //把包括非字母的所有字符重新排序
            StringBuilder sb = new StringBuilder();
            for (int i = 0, j = 0; i < chars.length; i++) {
                if (Character.isLetter(chars[i])) {
                    //把原来位置上是字母的字符全部重排序，原来位置非字母的保留
                    sb.append(letters.get(j++));
                } else {
                    sb.append(chars[i]);
                }
            }
            System.out.println(sb.toString());
        }
    }


    /**
     * HJ25 数据分类处理
     * 根据题解可知：整数序列I 和 规则整数序列R
     * 1、是根据R中元素到I序列中进行匹配查询并将I序列中出现的R[i]的索引(index)和I[i]的值进行记录
     * 2、定义集合用于记录待查找条件R[i]和R[i]出现的次数(count),最后将第一步得到的集合放进来即可，此处也可使用StringBuffer
     */
    public static void hj25() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nI = scanner.nextInt();//整数序列I的个数
            String[] arrI = new String[nI];
            for (int i = 0; i < nI; i++) {
                arrI[i] = String.valueOf(scanner.nextInt());
            }
            int nR = scanner.nextInt();//规则整数序列R的个数
            Set<Integer> setR = new TreeSet<>();//使用TreeSet进行排序和去重
            for (int i = 0; i < nR; i++) {
                setR.add(scanner.nextInt());
            }

            List<Integer> listI = new ArrayList<>();//用于存储整数序列I
            List<Integer> listR = new ArrayList<>();//用于存储规则整数序列R
            for (Integer itemR : setR) {
                int count = 0;//统计R中元素在I中出现的次数
                for (int i = 0; i < arrI.length; i++) {
                    if (arrI[i].contains(String.valueOf(itemR))) {
                        count++;
                        listI.add(i);//记录I序列中出现的R[i]的索引(index)
                        listI.add(Integer.valueOf(arrI[i]));//记录I[i]的值
                    }
                }
                if (count > 0) {
                    listR.add(itemR);
                    listR.add(count);
                    listR.addAll(listI);
                }
                listI.clear();
            }
            System.out.print(listR.size() + " ");
            for (Integer itemR : listR) {
                System.out.print(itemR + " ");
            }
            System.out.println();
        }
    }

    /**
     * HJ24 合唱队
     * 动态规划
     * 思路：先找到每一个位置i左侧的最长上升子序列长度left[i]
     * 2、再找到每一个位置i右侧的最长下降子序列长度right[i]
     * 3、然后求出所有位置的最长序列长度=左侧最长子序列长度+右侧最长子序列长度-1（因为该位置被算了两次，所以减1）
     * 4、然后用数目减去最长序列长度就是答案，需要出队的人数
     */
    public static void hj24() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();//同学的总数 N
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                // N 位同学的身高，以空格隔开，获取并存放到数组中
                arr[i] = sc.nextInt();
            }
            //存储每个数左边小于其的数的个数
            int[] left = new int[n];
            //存储每个数右边小于其的数的个数
            int[] right = new int[n];
            left[0] = 1;//最左边的数设为1
            right[n - 1] = 1;//最右边的数设为1
            //计算每个位置i 左侧的最长递增
            for (int i = 0; i < n; i++) {
                left[i] = 1;
                for (int j = 0; j < i; j++) {
                    if (arr[i] > arr[j]) {//动态规划
                        left[i] = Math.max(left[j] + 1, left[i]);
                    }
                }
            }
            //计算每个位置i 右侧的最长递减
            for (int i = n - 1; i >= 0; i--) {
                right[i] = 1;
                for (int j = n - 1; j > i; j--) {
                    if (arr[i] > arr[j]) {//动态规划
                        right[i] = Math.max(right[i], right[j] + 1);
                    }
                }
            }
            // 记录每个位置的值
            int[] result = new int[n];
            for (int i = 0; i < n; i++) {
                //位置 i计算了两次 所以需要－1
                result[i] = left[i] + right[i] - 1;//两个都包含本身
            }
            //找到最大的满足要求的值
            int max = 1;
            for (int i = 0; i < n; i++) {
                max = Math.max(result[i], max);
            }
            System.out.println(n - max);
            //时间复杂度：O(n^2)，需要多次遍历数组，两层for循环嵌套
            //空间复杂度：O(n)，存每个位置的dp递增和递减数组。
        }
    }


    /**
     * HJ23 删除字符串中出现次数最少的字符
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void hj23() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String str = sc.nextLine();
            // Map记录每个字母的次数
            Map<Character, Integer> map = new HashMap<>();
            char[] chars = str.toCharArray();
            for (char aChar : chars) {
                map.put(aChar, map.getOrDefault(aChar, 0) + 1);
            }
            // 快速找出最少次数
            int min = Integer.MAX_VALUE;
            for (Integer value : map.values()) {
                min = Math.min(min, value);
            }
            StringBuilder sb = new StringBuilder();
            for (char aChar : chars) {
                if (map.get(aChar) != min) {
                    sb.append(aChar);
                }
            }
            System.out.println(sb.toString());
        }
    }


    /**
     * HJ22 汽水瓶
     */
    public static void hj22() {
        Scanner sc = new Scanner(System.in);
        int[] input = new int[10];
        int temp = 0;
        for (int i = 0; i < 10; i++) {
            temp = sc.nextInt();
            if (temp == 0) {
                break;
            }
            input[i] = temp;
        }
        for (int i = 0; i < 10; i++) {
            if (input[i] != 0) {
                System.out.println(input[i] / 2);
            }
        }
    }


    /**
     * HJ21 简单密码
     */
    public static void hj21() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("abc", "2");
        map.put("def", "3");
        map.put("ghi", "4");
        map.put("jkl", "5");
        map.put("mno", "6");
        map.put("pqrs", "7");
        map.put("tuv", "8");
        map.put("wxyz", "9");
        map.put("0", "0");

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String str = sc.nextLine();
            char[] chars = str.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (char aChar : chars) {
                if (aChar >= '0' && aChar <= '9') {
                    //如果是正整数则不需要进行加密
                    sb.append(aChar);
                } else if (aChar >= 'A' && aChar <= 'Y') {
                    //如果是A~Y的大写字母则需要将其+32位转换成小写再向后移1位
                    char newChar = (char) (aChar + 32 + 1);
                    sb.append(newChar);
                } else if (aChar == 'Z') {
                    //如果是Z则加密成a
                    sb.append('a');
                } else {
                    //取出map容器中的key与字符进行校验并加密
                    Set<String> keySet = map.keySet();
                    for (String s : keySet) {
                        if (s.contains(String.valueOf(aChar))) {
                            sb.append(map.get(s));
                        }
                    }
                }
            }
            System.out.println(sb.toString());
        }
    }


    /**
     * HJ20 密码验证合格程序
     */
    public static void hj20() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String str = sc.next();
            if (str.length() <= 8) {
                System.out.println("NG");
                continue;
            }
            // 检查是否满足正则
            if (!getMatch(str)) {
                System.out.println("NG");
                continue;
            }
            if (getString(str, 0, 3)) {
                System.out.println("NG");
                continue;
            }
            System.out.println("OK");
        }
    }

    // 校验是否有重复子串
    private static boolean getString(String str, int l, int r) {
        if (r >= str.length()) {
            return false;
        }
        if (str.substring(r).contains(str.substring(l, r))) {
            return true;
        } else {
            return getString(str, l + 1, r + 1);
        }
    }


    /**
     * 检查是否满足正则
     * Matcher  find()方法与matches()方法区别
     * https://www.cnblogs.com/hubingxu/archive/2012/02/17/2355516.html
     * https://blog.csdn.net/weixin_43718346/article/details/120424459
     * <p>
     * find()方法是部分匹配，是查找输入串中与模式匹配的子串，如果该匹配的串有组还可以使用group()函数
     * matches()是全部匹配，是将整个输入串与模式匹配，如果要验证一个输入的数据是否为数字类型或其他类型，一般要用matches()
     *
     * @param str
     * @return true 符合要求
     */
    private static boolean getMatch(String str) {
        int count = 0;
        Pattern pattern1 = Pattern.compile("[A-Z]");
        Matcher matcher1 = pattern1.matcher(str);
        if (matcher1.find()) {
            count++;
        }
        Pattern pattern2 = Pattern.compile("[a-z]");
        if (pattern2.matcher(str).find()) {
            count++;
        }
        Pattern pattern3 = Pattern.compile("[0-9]");
        if (pattern3.matcher(str).find()) {
            count++;
        }
        Pattern pattern4 = Pattern.compile("[^A-Za-z0-9]");
        if (pattern4.matcher(str).find()) {
            count++;
        }
        if (count >= 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * HJ19 简单错误记录
     */
    public static void hj19() {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            Map<String, Integer> map = new LinkedHashMap<>();
            String tstr = null;
            while ((tstr = bf.readLine()) != null &&
                    !tstr.equals("")) {
                //"\\s+"匹配任意空白字符，如换页，换行符，回车符，制表符，垂直制表符
                String[] str = tstr.split("\\s+");
                String fname = str[0].substring(str[0].lastIndexOf("\\") + 1);
                fname = fname.substring(Math.max(fname.length() - 16, 0)) + " " + str[1];
                Integer tmp = map.get(fname);
                if (tmp == null) {
                    map.put(fname, 1);
                } else {
                    map.put(fname, tmp + 1);
                }
            }
            int count = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (map.size() - count <= 8) {
                    System.out.println(entry.getKey() + " " + entry.getValue());
                }
                count++;//注意，这个不能放在if里面,比如map.size()是10，一开始count是0,10-0>8，没有输出
                //而最后只用输出最后出现的八条错误记录
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * HJ18 识别有效的IP地址和掩码并进行分类统计
     */
    public static void hj18() {
        //不会，过
    }

    /**
     * HJ17 坐标移动
     */
    public static void hj17() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = bufferedReader.readLine();
            String[] lineStrArray = line.split(";");
            int x = 0;
            int y = 0;
            for (String str : lineStrArray) {
                if (str == null || str.length() == 0) {
                    continue;
                }
                String direction = str.substring(0, 1);
                int val = 0;
                try {
                    val = Integer.parseInt(str.substring(1));
                } catch (NumberFormatException e) {
                    continue;
                }
                if (!("W".equals(direction) || "A".equals(direction) || "S".equals(direction) || "D".equals(direction))) {
                    continue;
                }
                switch (direction) {
                    case "W":
                        y += val;
                        break;
                    case "A":
                        x -= val;
                        break;
                    case "S":
                        y -= val;
                        break;
                    case "D":
                        x += val;
                        break;
                }
            }
            System.out.println(x + "," + y);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * TODO 看不懂
     * HJ16 购物单
     * 注意：1.如果要买归类为附件的物品，必须先买该附件所属的主件，且每件物品只能购买一次。
     */
    public static void hj16() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int money = scanner.nextInt();//总钱数N
            int m = scanner.nextInt();//希望购买的物品个数m
            scanner.nextLine();
            money /= 10;//每件物品的价格（都是 10 元的整数倍）
            //定义个数组用来存放接下来输入的每件（每行）物品的基本数据：价格，重要度，主件还是附件
            int[][] prices = new int[m + 1][3];
            int[][] weights = new int[m + 1][3];
            for (int i = 1; i <= m; i++) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                //从第 2 行到第 m+1 行，第 j 行给出了编号为 j-1 的物品的基本数据
                //q就是这里的c,如果 q=0 ，表示该物品为主件，如果 q>0 ，表示该物品为附件， q 是所属主件的编号
                int c = scanner.nextInt();
                a /= 10;//price
                b = b * a;//weight，该件商品的满意度
                if (c == 0) {
                    // 主件
                    prices[i][0] = a;
                    weights[i][0] = b;

                } else if (prices[c][1] == 0) {
                    // 附件1
                    prices[c][1] = a;
                    weights[c][1] = b;

                } else {
                    // 附件2
                    prices[c][2] = a;
                    weights[c][2] = b;
                }
                scanner.nextLine();
            }
            int[][] dp = new int[m + 1][money + 1];
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= money; j++) {
                    int a = prices[i][0];
                    int b = weights[i][0];
                    int c = prices[i][1];
                    int d = weights[i][1];
                    int e = prices[i][2];
                    int f = weights[i][2];

                    dp[i][j] = j - a >= 0 ? Math.max(dp[i - 1][j], dp[i - 1][j - a] + b) : dp[i - 1][j];
                    dp[i][j] = j - a - c >= 0 ? Math.max(dp[i][j], dp[i - 1][j - a - c] + b + d) : dp[i][j];
                    dp[i][j] = j - a - e >= 0 ? Math.max(dp[i][j], dp[i - 1][j - a - e] + b + f) : dp[i][j];
                    dp[i][j] = j - a - c - e >= 0 ? Math.max(dp[i][j], dp[i - 1][j - a - c - e] + b + d + f) : dp[i][j];
                }
            }
            System.out.println(dp[m][money] * 10);
        }
    }

    /**
     * HJ15 求int型正整数在内存中存储时1的个数
     */
    public static void hj15() {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int result = 0;
        for (int i = 0; i < 32; i++) {
            if ((num & 1) == 1) {
                result++;//如果末位为1则计数
            }
            num = num >>> 1;//无符号右移
        }
        System.out.println(result);
    }


    /**
     * HJ14 字符串排序
     * TODO 还有其他解法
     */
    public static void hj14() {
        Scanner sc = new Scanner(System.in);
        int nextInt = Integer.valueOf(sc.nextLine());
//        int nextInt = sc.nextInt();//TODO 这样有问题，原因暂时没搞清
        String[] array = new String[nextInt];
        for (int i = 0; i < nextInt; i++) {
            array[i] = sc.nextLine();
        }
        Arrays.sort(array);//从小到大的顺序
        for (String s : array) {
            System.out.println(s);
        }
    }


    /**
     * HJ13 句子逆序
     */
    public static void sentenceReverse() {
        Scanner sc = new Scanner(System.in);
        String nextLine = sc.nextLine();
        String[] splitStr = nextLine.split(" ");
        for (int i = splitStr.length - 1; i >= 0; i--) {
            if (i != 0) {
                System.out.print(splitStr[i] + " ");
            } else {
                System.out.print(splitStr[i]);
            }
        }
    }


    /**
     * HJ12 字符串反转
     */
    public static void strReverse() {
        Scanner sc = new Scanner(System.in);
        String nextLine = sc.nextLine();
        StringBuilder sb = new StringBuilder(nextLine);
        sb.reverse();
        System.out.println(sb.toString());
    }


    /**
     * HJ11 数字颠倒
     */
    public static void reverseOrder() {
        Scanner sc = new Scanner(System.in);
        String nextLine = sc.nextLine();
        StringBuilder sb = new StringBuilder(nextLine);
        sb.reverse();
        System.out.println(sb.toString());
    }


    /**
     * HJ10 字符个数统计
     */
    public static void getCharNum() {
        Scanner sc = new Scanner(System.in);
        String s = sc.next();

        //方法一
//        char[] charArray = s.toCharArray();
//        Set<Character> set = new HashSet<>();
//        for (char c : charArray) {
//            if (c >= 0 && c <= 127) {
//                set.add(c);
//            }
//        }
//        System.out.println(set.size());

        //方法二
        //凡是涉及到去重统计都可以用位图实现。因为每一个不同的数据只需要用二进制的一位存储即可，
        // 大大减小了统计所使用的存储空间
        BitSet bitSet = new BitSet(128);
        for (char c : s.toCharArray()) {
            //判断字符c是否已出现
            if (!bitSet.get(c)) {
                //未出现就设置为已出现
                bitSet.set(c);
            }
        }
        System.out.println(bitSet.cardinality());
    }


    /**
     * HJ9 提取不重复的整数
     */
    public static void getNotRepeating() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int nextInt = sc.nextInt();
            // 使用HashSet来判断是否是不重复的
            Set<Integer> set = new HashSet<>();

            while (nextInt != 0) {
                int temp = nextInt % 10;
                if (set.add(temp)) {// 如果能加入，就是说明没有重复
                    System.out.print(temp);
                }
                nextInt /= 10;// 除10能去掉最右边的数字
            }
        }
        System.out.println();
    }

    /**
     * HJ8 合并表记录
     * 数据表记录包含表索引index和数值value（int范围的正整数），请对表索引相同的记录进行合并，即将相同索引的数值进行求和运算，输出按照index值升序进行输出
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void mergeTable() {
        Scanner sc = new Scanner(System.in);

        //方法一
//        int tableNum = sc.nextInt();
//        Map<String, Integer> map = new HashMap<>();
//        for (int i = 0; i <= tableNum; i++) {
//            String s = sc.nextLine();
//            String[] keyAndValue = s.split(" ");
//            if (keyAndValue.length >= 2) {
//                String key = keyAndValue[0];
//                Integer value = Integer.valueOf(keyAndValue[1]);
//                if (!map.containsKey(key)) {
//                    map.put(key, value);
//                } else {
//                    Integer newValue = map.get(key) + value;
//                    map.put(key, newValue);
//                }
//            }
//        }
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }

        //方法二
        // 输出结果要求有序
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        while (sc.hasNextInt()) {
            int n = sc.nextInt();
            for (int i = 0; i < n; i++) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                treeMap.put(a, treeMap.getOrDefault(a, 0) + b);
            }
        }
        for (Integer i : treeMap.keySet()) {
            System.out.println(i + " " + treeMap.get(i));
        }
    }


    /**
     * HJ7 取近似值
     * 写出一个程序，接受一个正浮点数值，输出该数值的近似整数值。如果小数点后数值大于等于 0.5 ,向上取整；小于 0.5 ，
     * 则向下取整
     */
    public static void approNumber() {
        Scanner sc = new Scanner(System.in);
        double nextDouble = sc.nextDouble();
        long result = (long) (nextDouble + 0.5);
        System.out.println(result);
    }


    /**
     * HJ6 质数因子
     * 功能:输入一个正整数，按照从小到大的顺序输出它的所有质因子（重复的也要列举）（如180的质因子为2 2 3 3 5 ）
     */
    public static void primeFactor() {
        Scanner sc = new Scanner(System.in);
        long nextInt = sc.nextLong();

        //方法一
//        for (int i = 2; i <= nextInt; i++) {
//            while (nextInt % i == 0) {
//                System.out.print(i + " ");
//                nextInt /= i;
//            }
//        }
        //方法二
        //正如我们判断数 num 是不是质数时，没必要从 2 一直尝试到 num 一样，此题中的大循环也大可不必写一个到 num 的循环，
        // 写到  即可，如果此时数字还没有除数，则可判定其本身是一个质数，没有再除下去的必要了，直接打印其本身即可
        long k = (long) Math.sqrt(nextInt);
        for (int i = 2; i <= k; i++) {
            while (nextInt % i == 0) {
                System.out.print(i + " ");
                nextInt /= i;
            }
        }
        System.out.println(nextInt == 1 ? "" : nextInt + " ");
    }


    private static final int BASE = 16;

    public static void baseConvert() {
        Map<Character, Integer> map = new HashMap<>();
        map.put('0', 0);
        map.put('1', 1);
        map.put('2', 2);
        map.put('3', 3);
        map.put('4', 4);
        map.put('5', 5);
        map.put('6', 6);
        map.put('7', 7);
        map.put('8', 8);
        map.put('9', 9);
        map.put('A', 10);
        map.put('B', 11);
        map.put('C', 12);
        map.put('D', 13);
        map.put('E', 14);
        map.put('F', 15);
        map.put('a', 10);
        map.put('b', 11);
        map.put('c', 12);
        map.put('d', 13);
        map.put('e', 14);
        map.put('f', 15);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String nextStr = sc.next();
            String number = nextStr.substring(2);
            char[] charArray = number.toCharArray();
            int result = 0;
            for (char c : charArray) {
                result = result * BASE + map.get(c);
            }
            System.out.println(result);
        }
    }

    /**
     * HJ4 字符串分隔
     */
    public static void strSplit2() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String str = sc.nextLine();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            int size = str.length();
            int addZero = 8 - size % 8;
            while (addZero > 0 && addZero < 8) {
                sb.append("0");
                addZero--;
            }
            String str1 = sb.toString();
            while (str1.length() > 0) {
                System.out.println(str1.substring(0, 8));
                str1 = str1.substring(8);
            }
        }
    }


    /**
     * HJ4 字符串分隔
     */
    public static void strSplit() {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()) {
            String s = sc.nextLine();
            int length = s.length();

            if (length > 0 && length <= 8) {
                StringBuilder sb = new StringBuilder();
                sb.append(s);
                int addZeroTimes = 8 - length;
                for (int i = 0; i < addZeroTimes; i++) {
                    sb.append("0");
                }
                System.out.println(sb);

            } else if (length > 8) {
                String newStr = s;
                while (newStr.length() > 8) {
                    String substring = newStr.substring(0, 8);
                    System.out.println(substring);
                    newStr = newStr.substring(8);
                }
                //这部分可以优化一下，与上面那部分合一
                StringBuilder sb = new StringBuilder();
                sb.append(newStr);
                int addZeroTimes = 8 - newStr.length();
                for (int i = 0; i < addZeroTimes; i++) {
                    sb.append("0");
                }
                System.out.println(sb);
            }
        }
    }


    /**
     * HJ3 明明的随机数
     */
    public static void hj3() {
        Scanner sc = new Scanner(System.in);
        //获取个数
        int num = sc.nextInt();
        //创建TreeSet进行去重排序
        TreeSet<Integer> treeSet = new TreeSet<Integer>();
        //输入
        for (int i = 0; i < num; i++) {
            treeSet.add(sc.nextInt());
        }
        //输出
        for (Integer integer : treeSet) {
            System.out.println(integer);
        }
    }


    /**
     * HJ3 明明的随机数
     */
//    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void hj3two() {
        Scanner sc = new Scanner(System.in);
        int randomCount = Integer.parseInt(sc.nextLine());
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < randomCount; i++) {
            int anInt = Integer.parseInt(sc.nextLine());
            if (!integerList.contains(anInt)) {
                integerList.add(anInt);
            }
        }

        //排序方法一(直接排序)
        Collections.sort(integerList);//Collections.sort是用来排序集合的，从小到大的顺序
//        Arrays.sort(integerList);//Arrays.sort是用来排序数组的，从小到大的顺序

        //排序方法二  实现Comparable接口  该方法缺点就是只能对单一属性添加进行排序，而且写死在User类中

        //排序方法三
        //使用比较器来进行排序，优点可以自己定义排序规则，可以对多属性进行排序
        //创建比较器类
//        Collections.sort(integerList, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
////         返回负数的时候，第一个参数排在前面
////         返回正数的时候，第二个参数排在前面
////         返回0的时候，谁在前面无所谓
//                //升序，升序排列是把数据从小到大进行排列,而降序排列是把数据从大到小进行排列
//                if (o1 > o2) {
//                    return 1;//o2 o1，升序排列，即从小到大排列
//                }
//                if (o1 < o2){
//                    return -1;//o1 o2，升序排列，即从小到大排列
//                }
//                return 0;
//            }
//        });
        for (Integer i : integerList) {
            System.out.println(i);
        }
    }

    //排序方法二  实现Comparable接口  该方法缺点就是只能对单一属性添加进行排序，而且写死在User类中
    static class User implements Comparable<User> {

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(User user) {
//            return this.getName().compareTo(user.getName());//升序
            return user.getName().compareTo(this.getName());//降序
        }
    }

    /**
     * HJ2 计算某字符出现次数
     */
    public static void getCharTimes() {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine().toLowerCase();
        String s = sc.nextLine().toLowerCase();
        int times = str.length() - str.replaceAll(s, "").length();
        System.out.println(times);
    }


    /**
     * HJ1 字符串最后一个单词的长度
     */
    public static void getLastLength() {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();//控制台可以录入带空格的字符串
        if (str == null || str.length() == 0) {
            System.out.println(0);
        } else {
            String[] strSplits = str.split(" ");
            int lastStrLength = strSplits[strSplits.length - 1].length();
            System.out.println(lastStrLength);
        }
    }
}
