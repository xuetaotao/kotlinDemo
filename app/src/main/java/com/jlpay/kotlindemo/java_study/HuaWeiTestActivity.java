package com.jlpay.kotlindemo.java_study;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class HuaWeiTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onClickTest(View view) {

    }

//    import java.util.Scanner;
//    public class Main {
//        public static void main(String[] args){
//
//        }
//    }

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
     * nextLine()：
     * 1、以Enter为结束符,也就是说 nextLine()方法返回的是输入回车之前的所有字符。
     * 2、可以获得空白。
     */
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
    public static void randomSort2() {
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
    public static void randomSort() {
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
        Collections.sort(integerList);

        //排序方法二  实现Comparable接口  该方法缺点就是只能对单一属性添加进行排序，而且写死在User类中

        //排序方法三
        //使用比较器来进行排序，优点可以自己定义排序规则，可以对多属性进行排序
        //创建比较器类
//        Collections.sort(integerList, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                //升序
//                if (o1 > o2) {
//                    return 1;
//                } else {
//                    return -1;
//                }
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
