package com.jlpay.kotlindemo.study_java;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
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
import java.util.Calendar;
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
 * 牛客网华为机试：
 * https://www.nowcoder.com/exam/oj/ta?page=1&tpId=37&type=37
 * <p>
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
//        arithmeticTest();
//        hj74();
        hj103();
    }


    /**
     * HJ108 求最小公倍数
     * 只要一个数逐渐累加自身到可以整除另一个数时就是既可以整除自己也可以整除另一个数，
     * 此时结果就是要得到的公倍数
     */
    public static void hj108() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int c = a;
            System.out.println(count108(a, b, c));
        }
    }

    public static int count108(int a, int b, int c) {
        if (a % b == 0) {
            return a;
        }
        return count108(a + c, b, c);
    }

    /**
     * HJ107 求解立方根
     */
    public static void hj107() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            double input = scanner.nextDouble();
            double num = input > 0 ? input : -input;//负数 当正数来处理
            double bottom = 0;
            double top = 0;
            while (top * top * top < num) {
                top++;
            }
            bottom = top - 1;
            double mid = bottom + (top - bottom) / 2;
            double mul = mid * mid * mid;
            while (top - bottom > 0.1) {// 因为只保留一位小数
                if (mul > num) {
                    top = mid;
                } else if (mul < num) {
                    bottom = mid;
                } else {
                    break;
                }
                mid = bottom + (top - bottom) / 2;
                mul = mid * mid * mid;
            }
            if (input < 0) {
                mid = -mid;// 原数为负,结果也应该为负
            }
            System.out.println(String.format("%.1f", mid));
        }
    }

    /**
     * HJ106 字符逆序
     */
    public static void hj106() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            StringBuilder sb = new StringBuilder(nextLine);
            System.out.println(sb.reverse());
        }
    }


    /**
     * HJ105 记负均正II
     */
    public static void hj105() {
        Scanner scanner = new Scanner(System.in);
        int countNegative = 0;
        int countOthers = 0;
        int sumOthers = 0;
        while (scanner.hasNext()) {
            int num = scanner.nextInt();
            if (num < 0) {
                countNegative++;
            } else {
                countOthers++;
                sumOthers += num;
            }
        }
        double aver = 0;
        if (countOthers == 0) {
            aver = 0.0;
        } else {
            aver = sumOthers / (countOthers * 1.0d);
        }
//            DecimalFormat decimalFormat = new DecimalFormat("0.0");
//            String format = decimalFormat.format(aver);
        //其中%表示小数点前的位数，1表示保留小数点后1位，f表示转换位float型（找过一下好像没有可以转换为double的）
        String format = String.format("%.1f", aver);
        System.out.println(countNegative + "\n" + format);
    }


    /**
     * HJ103 Redraiment的走法
     */
    public static void hj103() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int[] array = new int[n];
//            int n = 6;
//            int[] array = new int[]{2,5,1,5,4,5};
            for (int i = 0; i < n; i++) {
                array[i] = scanner.nextInt();
            }
            int[] kArray = new int[n];
            for (int i = 1; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (array[j] < array[i]) {
                        kArray[i] = Math.max(kArray[i], kArray[j] + 1);
                    }
                }
            }
            Arrays.sort(kArray);
            System.out.println(kArray[n - 1] + 1);
        }
    }

    //暴力递归
    public static void hj103Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = scanner.nextInt();
            }
            System.out.println(count103(array));
        }
    }


    public static int count103(int[] array) {
        int[] dp = new int[array.length + 1];
        Arrays.fill(dp, 1);//初始化为1
        int max = 1;
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < i; j++) {
                if (array[j] < array[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
                max = Math.max(dp[i], max);
            }
        }
        return max;
    }


    /**
     * HJ102 字符统计
     */
    public static void hj102Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String inputStr = scanner.nextLine();
//            String inputStr = "aaddccdc";
            //创建字符ascll码对应的整型数组，ascll码有128个
            int[] charArray = new int[129];
            for (int i = 0; i < inputStr.length(); i++) {
                int cToInt = (int) inputStr.charAt(i);
                charArray[cToInt]++;
            }
            int charMax = 0;
            //找出字符数量最多的ascll码值
            for (int i : charArray) {
                charMax = Math.max(i, charMax);
            }
            StringBuilder sb = new StringBuilder();
            for (int i = charMax; i > 0; i--) {
                for (int j = 0; j < charArray.length; j++) {
                    if (charArray[j] == i) {
                        sb.append((char) j);
                    }
                }
            }
            System.out.println(sb.toString());
        }
    }


    public static void hj102() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String inputStr = scanner.nextLine();
            HashMap<Character, Integer> hashMap = new HashMap<>();
            for (int i = 0; i < inputStr.length(); i++) {
                char c = inputStr.charAt(i);
                if (hashMap.containsKey(c)) {
                    Integer integer = hashMap.get(c);
                    hashMap.put(c, integer + 1);
                } else {
                    hashMap.put(c, 1);
                }
            }
            Set<Map.Entry<Character, Integer>> entries = hashMap.entrySet();
            List<Map.Entry<Character, Integer>> list = new ArrayList<>(entries);
            Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
                @Override
                public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                    int value = o1.getValue() - o2.getValue();
                    if (value != 0) {//不同字母出现次数的降序表示
                        return o2.getValue() - o1.getValue();//负数 o1.getValue() o2.getValue()
                    } else {//若出现次数相同，则按ASCII码的升序输出
                        int o1Key = o1.getKey() - '0';
                        int o2Key = o2.getKey() - '0';
                        return o1Key - o2Key;//负数 o1Key o2Key
                    }
                }
            });
            for (Map.Entry<Character, Integer> temp : list) {
                System.out.print(temp.getKey());
            }
        }
    }


    /**
     * HJ101 输入整型数组和排序标识，对其元素按照升序或降序进行排序
     */
    public static void hj101() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            scanner.nextLine();//指针移到下一行开头
            Integer[] array = new Integer[n];
            for (int i = 0; i < n; i++) {
                array[i] = scanner.nextInt();
            }
            scanner.nextLine();
            int order = scanner.nextInt();
            if (0 == order) {//0代表升序排序
                Arrays.sort(array);

            } else if (1 == order) {//1代表降序排序
                Arrays.sort(array, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2 - o1;//负数： o1  o2
                    }
                });
            }
            for (Integer temp : array) {
                System.out.print(temp + " ");
            }
        }
    }


    /**
     * HJ100 等差数列
     */
    public static void hj100() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            long sum = n * 2 + (n * (n - 1) / 2) * 3;
            System.out.println(sum);
        }
    }


    /**
     * HJ99 自守数
     */
    public static void hj99() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i <= nextInt; i++) {
                String iStr = String.valueOf(i);
                int x2 = (int) Math.pow(i, 2);
                String x2Str = String.valueOf(x2);
                //方式一
//                String substring = x2Str.substring(x2Str.length() - iStr.length(), x2Str.length());
//                if (substring.equals(iStr)) {
//                    list.add(i);
//                }
                //方式二
                if (x2Str.endsWith(iStr)) {
                    list.add(i);
                }
            }
            System.out.println(list.size());
        }
    }

    /**
     * HJ98 自动售货系统
     */
    public static void hj98() {
        //不会，暂时略过
    }


    /**
     * HJ97 记负均正
     */
    public static void hj97() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int[] array = new int[n];
            int countNegative = 0;
            int countPositive = 0;
            int sumPositive = 0;
            for (int i = 0; i < n; i++) {
                array[i] = scanner.nextInt();
                if (array[i] < 0) {
                    countNegative++;
                } else if (array[i] > 0) {
                    sumPositive += array[i];
                    countPositive++;
                }
            }

            double aver = 0;
            if (countPositive == 0) {
                aver = 0.0;
            } else {
                aver = sumPositive / (countPositive * 1.0d);
            }

//            DecimalFormat decimalFormat = new DecimalFormat("0.0");
//            String format = decimalFormat.format(aver);
            String format = String.format("%.1f", aver);
            System.out.println(countNegative + " " + format);
        }
    }


    /**
     * HJ96 表示数字
     */
    public static void hj96() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String result = nextLine.replaceAll("([0-9]+)", "*$1*");
            System.out.println(result);
        }
    }

    /**
     * HJ95 人民币转换
     */
    public static String[] ten95 = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    public static String[] power95 = {"万", "亿"};
    public static String[] daiwei95 = {"元", "角", "分", "整"};

    public static void hj95() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //分割为整数部分和小数部分
            String[] split = scanner.nextLine().split("\\.");
            if (split[1].equals("00")) {
                System.out.println("人民币" + solveZheng95(Double.parseDouble(split[0])) + "元整");
            } else if (split[0].equals("0")) {
                System.out.println("人民币" + solveXiao95(split[1]));
            } else {
                System.out.println("人民币" + solveZheng95(Double.parseDouble(split[0])) + "元"
                        + solveXiao95(split[1]));
            }
        }
    }

    public static String solveXiao95(String s2) {
        StringBuilder stringBuilder = new StringBuilder();
        int jiao = Integer.parseInt(s2.substring(0, 1));
        int fen = Integer.parseInt(s2.substring(1, 2));
        if (jiao != 0) {
            stringBuilder.append(ten95[jiao]);
            stringBuilder.append("角");
        }
        if (fen != 0) {
            stringBuilder.append(ten95[fen]);
            stringBuilder.append("分");
        }
        return stringBuilder.toString();
    }

    public static String solveZheng95(double zheng) {
        StringBuilder sb = new StringBuilder();
        int pow = 0;
        while ((int) zheng != 0) {
            if (pow != 0) {
                sb.append(power95[pow - 1]);
            }
            int temp = (int) (zheng % 10000);
            //个位
            int gewei = temp % 10;
            int shiwei = (temp / 10) % 10;
            int baiwei = (temp / 100) % 10;
            int qianwei = (temp / 1000) % 10;
            if (gewei != 0) {
                sb.append(ten95[gewei]);
            }
            if (shiwei != 0) {
                sb.append("拾");
                if (shiwei != 1) {
                    sb.append(ten95[shiwei]);
                }
            } else {
                if (gewei != 0 && (temp > 99 || (int) zheng > 10000)) {
                    sb.append(ten95[0]);
                }
            }
            if (baiwei != 0) {
                sb.append("佰");
                sb.append(ten95[baiwei]);
            } else {
                if (shiwei != 0 && (temp > 999 || (int) zheng > 10000)) {
                    sb.append(ten95[0]);
                }
            }
            if (qianwei != 0) {
                sb.append("仟");
                sb.append(ten95[qianwei]);
            } else {
                if (baiwei != 0 && ((int) zheng > 10000)) {
                    sb.append(ten95[0]);
                }
            }
            zheng /= 10000;
            pow++;
            if (pow > 2) {
                pow = 1;
            }
        }
        return sb.reverse().toString();
    }


    /**
     * HJ94 记票统计
     * 这个题目的答案里有nextLine的换行使用方法
     */
    public static void hj94() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//候选人的人数n
            LinkedHashMap<String, Integer> hashMap = new LinkedHashMap<>();
            for (int i = 0; i < n; i++) {
                String name = scanner.next();
                hashMap.put(name, 0);
            }
            hashMap.put("Invalid", 0);
            int peopleNum = scanner.nextInt();//投票人的人数
            for (int i = 0; i < peopleNum; i++) {
                String str = scanner.next();
                boolean isInvalid = false;
                for (String s : hashMap.keySet()) {
                    if (s.equals(str)) {
                        Integer integer = hashMap.get(s);
                        hashMap.put(s, integer + 1);
                        isInvalid = true;
                    }
                }
                if (!isInvalid) {
                    Integer invalid = hashMap.get("Invalid");
                    hashMap.put("Invalid", invalid + 1);
                }
            }
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    /**
     * HJ93 数组分组
     */
    public static void hj93() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            ArrayList<Integer> list = new ArrayList<>();
            int sum3 = 0;
            int sum5 = 0;
            int sum = 0;
            for (int i = 0; i < n; i++) {
                int anInt = scanner.nextInt();
                if (anInt % 3 == 0) {
                    sum3 += anInt;
                } else if (anInt % 5 == 0) {
                    sum5 += anInt;
                } else {
                    list.add(anInt);
                }
                sum += anInt;
            }

            int target = sum / 2 - sum3;
            if (sum % 2 != 0) {
                System.out.println("false");
            } else {
                System.out.println(helper93(0, list, target));
            }
        }
    }

    public static boolean helper93(int i, List<Integer> list, int target) {
        if (i == list.size()) {
            return target == 0;
        }
        return helper93(i + 1, list, target - list.get(i)) || helper93(i + 1, list, target);
    }


    /**
     * HJ92 在字符串中找出连续最长的数字串
     * String的split（String regex，int limit）方法：
     * https://blog.csdn.net/wx1528159409/article/details/90243537
     * limit 参数通过控制分割次数从而影响分割结果
     * 如果传入 n(n>0) 那么字符串最多被分割 n-1 次,分割得到数组长度最大是 n
     * 如果 n = -1 将会以最大分割次数分割
     * 如果 n = 0 将会以最大分割次数分割,但是分割结果会舍弃末位的空串
     */
    public static void hj92Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String inputStr = scanner.nextLine();
            String[] ss = inputStr.split("[^0-9]+");
            int max = 0;
            List<String> list = new ArrayList<>();
            for (String s : ss) {
                if (s.length() > max) {
                    max = s.length();
                    list.clear();
                    list.add(s);
                } else if (s.length() == max) {
                    list.add(s);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : list) {
                stringBuilder.append(s);
            }
            stringBuilder.append(",").append(max);
            System.out.println(stringBuilder);
        }
    }


    //自己做的
    public static void hj92() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String inputStr = scanner.nextLine();
//            String inputStr = "abcd12345ed125ss123058789";
            List<StringBuilder> list = new ArrayList<>();
            StringBuilder stringBuilder;
            int maxNumLen = 0;
            for (int i = 0; i < inputStr.length(); i++) {
                char c = inputStr.charAt(i);
                if (Character.isDigit(c)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(c);
                    for (int j = i + 1; j < inputStr.length(); j++) {
                        char c1 = inputStr.charAt(j);
                        if (Character.isDigit(c1)) {
                            stringBuilder.append(c1);
                            if (j + 1 == inputStr.length()) {
                                if (stringBuilder.length() > maxNumLen) {
                                    maxNumLen = stringBuilder.length();
                                    list.clear();
                                    list.add(stringBuilder);
                                } else if (stringBuilder.length() == maxNumLen) {
                                    list.add(stringBuilder);
                                }
                            }

                        } else {
                            if (stringBuilder.length() > maxNumLen) {
                                maxNumLen = stringBuilder.length();
                                list.clear();
                                list.add(stringBuilder);
                            } else if (stringBuilder.length() == maxNumLen) {
                                list.add(stringBuilder);
                            }
                            break;
                        }
                    }
                }
            }
            if (list.size() > 1) {
                StringBuilder result = new StringBuilder();
                int len = 0;
                for (StringBuilder sb : list) {
                    result.append(sb);
                    len = sb.length();
                }
                result.append(",").append(len);
                System.out.println(result);

            } else if (list.size() == 1) {
                System.out.println(list.get(0).toString() + "," + list.get(0).toString().length());
            }
        }
    }


    /**
     * HJ91 走方格的方案数
     */
    public static void hj91() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            System.out.println(calc91(n, m));
        }
    }

    public static int calc91(int n, int m) {
        if (n == 1 || m == 1) {
            return n + m;
        } else {
            return calc91(n - 1, m) + calc91(n, m - 1);
        }
    }

    /**
     * HJ90 合法IP
     */
    public static void hj90() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String[] split = nextLine.split("\\.");
            String result = "YES";
            if (split.length == 4) {
                for (String s : split) {//遍历每个元素，合不合法
                    if (s.length() == 0 || s.length() > 3) {//每段长度等于0，或者长度大于4，都不合法
                        result = "NO";
                        break;
                    }
                    for (char c : s.toCharArray()) {//每段的字符必须是数字
                        if (!Character.isDigit(c)) {
                            result = "NO";
                            break;
                        }
                    }
                    if (s.charAt(0) == '0' && s.length() != 1) {//除0以外，所有0开头的字符串都是非法的
                        result = "NO";
                        break;
                    }
                    if (Integer.parseInt(s) > 255) {//每段对应的数大于255，也是非法的
                        result = "NO";
                        break;
                    }
                }
            } else {
                result = "NO";
            }
            System.out.println(result);
        }
    }


    /**
     * HJ89 24点运算
     */
    public static void hj89() {
        Scanner scanner = new Scanner(System.in);
        //用于初步读取String储存
        LinkedList<String> list1 = new LinkedList<>();
        //用于将String转化成int储存
        LinkedList<Integer> list = new LinkedList<>();
        //用于还原：（1->A; 13->K）
        LinkedList<String> list2 = new LinkedList<>();
        list2.add("0");
        list2.add("A");
        for (int i = 2; i <= 10; i++) {
            list2.add(Integer.toString(i));
        }
        list2.add("J");
        list2.add("Q");
        list2.add("K");
        while (scanner.hasNext()) {
            list1.add(scanner.next());
        }
        int flag = 0;
        //转换成Integer：//遇到JOKER 直接输出ERROR
        for (int i = 0; i < 4; i++) {
            String cur = list1.get(i);
            if (cur.equals("JOKER") || cur.equals("joker")) {
                System.out.println("ERROR");
                flag = -1;
                break;
            } else if (cur.equals("A")) {
                list.add(1);
            } else if (cur.equals("K")) {
                list.add(13);
            } else if (cur.equals("Q")) {
                list.add(12);
            } else if (cur.equals("J")) {
                list.add(11);
            } else {
                list.add(Integer.valueOf(cur));
            }
        }
        //暴力遍历四个数的排列组合
        if (flag == 0) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (j == i) {
                        continue;
                    }
                    if (flag == 1) {
                        break;
                    }
                    for (int k = 0; k < 4; k++) {
                        if (k == i || k == j) {
                            continue;
                        }
                        if (flag == 1) {
                            break;
                        }
                        for (int p = 0; p < 4; p++) {
                            if (p == i || p == j || p == k) {
                                continue;
                            }
                            if (flag == 1) {
                                break;
                            }
                            //将四个数给如helper function做判断：
                            String out = helper89(list.get(i), list.get(j), list.get(k), list.get(p));
                            //输出非NONE，生成答案：
                            if (!out.equals("NONE")) {
                                String res = "";
                                res += list2.get(list.get(i));
                                res += out.substring(0, 1);
                                res += list2.get(list.get(j));
                                res += out.substring(1, 2);
                                res += list2.get(list.get(k));
                                res += out.substring(2, 3);
                                res += list2.get(list.get(p));
                                System.out.println(res);
                                flag = 1;
                            }
                        }
                    }
                }
            }
            //helepr function输出为NONE输出NONE：
            if (flag == 0) {
                System.out.println("NONE");
            }
        }
    }

    //重载：输出的是三个有序运算符号
    public static String helper89(int a, int b, int c, int d) {
        if (!helper89(a * b, c, d).equals("NONE")) {
            return "*" + helper89(a * b, c, d);

        } else if (!helper89(a + b, c, d).equals("NONE")) {
            return "+" + helper89(a + b, c, d);

        } else if (!helper89(a - b, c, d).equals("NONE")) {
            return "-" + helper89(a - b, c, d);

            //TODO 这里不懂为什么要a % b == 0，也就是要必须整除，2/3=0，5/3=1啊
            //自己测试之后不要这个&& a % b条件也可以
        } else if (b != 0 && a % b == 0 && !helper89(a / b, c, d).equals("NONE")) {
            return "/" + helper89(a / b, c, d);
        } else {
            return "NONE";
        }
    }

    //重载
    static public String helper89(int a, int b, int c) {
        if (!helper89(a * b, c).equals("NONE")) {
            return "*" + helper89(a * b, c);

        } else if (!helper89(a + b, c).equals("NONE")) {
            return "+" + helper89(a + b, c);

        } else if (!helper89(a - b, c).equals("NONE")) {
            return "-" + helper89(a - b, c);

        } else if (b != 0 && a % b == 0 && !helper89(a / b, c).equals("NONE")) {
            return "/" + helper89(a / b, c);

        } else {
            return "NONE";
        }
    }

    static public String helper89(int a, int b) {
        if (a * b == 24) {
            return "*";
        } else if (a + b == 24) {
            return "+";
        } else if (a - b == 24) {
            return "-";
        } else if (b != 0 && a % b == 0 && a / b == 24) {
            return "/";
        } else {
            return "NONE";
        }
    }

    /**
     * HJ88 扑克牌大小
     */
    public static void hj88() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            String[] split = input.split("-");
            System.out.println(helper88(split[0], split[1]));
        }
    }

    public static String helper88(String s1, String s2) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("A", 14);
        hashMap.put("2", 15);
        hashMap.put("3", 3);
        hashMap.put("4", 4);
        hashMap.put("5", 5);
        hashMap.put("6", 6);
        hashMap.put("7", 7);
        hashMap.put("8", 8);
        hashMap.put("9", 9);
        hashMap.put("10", 10);
        hashMap.put("J", 11);
        hashMap.put("Q", 12);
        hashMap.put("K", 13);
        hashMap.put("joker", 16);
        hashMap.put("JOKER", 17);
        if (s1.equals("joker JOKER") || s1.equals("JOKER joker")) {
            return s1;
        } else if (s2.equals("joker JOKER") || s2.equals("JOKER joker")) {
            return s2;
        }
        String[] s1Array = s1.split(" ");
        Integer n1 = hashMap.get(s1Array[0]);
        String[] s2Array = s2.split(" ");
        Integer n2 = hashMap.get(s2Array[0]);
        if (isBoom88(s1) && isBoom88(s2)) {
            return n1 > n2 ? s1 : s2;
        } else if (isBoom88(s1)) {
            return s1;
        } else if (isBoom88(s2)) {
            return s2;
        } else if (s1Array.length == s2Array.length) {
            return n1 > n2 ? s1 : s2;
        } else {
            return "ERROR";
        }
    }

    public static boolean isBoom88(String s1) {
        String[] split = s1.split(" ");
        if (split.length != 4) {
            return false;
        }
        String s0 = split[0];
        for (int i = 1; i < split.length; i++) {
            if (!s0.equals(split[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * HJ87 密码强度等级
     */
    public static void hj87() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String password = scanner.nextLine();
//            String password = "Aa1(";
            int score = 0;
            //一、密码长度
            int length = password.length();
            if (length <= 4) {
                score += 5;
            } else if (length <= 7) {
                score += 10;
            } else {
                score += 25;
            }
            //二、字母
            String lowercase = password.replaceAll("[a-z]", "");
            int lowercaseLen = password.length() - lowercase.length();
            String upperCase = password.replaceAll("[A-Z]", "");
            int upperCaseLen = password.length() - upperCase.length();
            if (lowercaseLen == 0 && upperCaseLen == 0) {
                score += 0;
            } else if (lowercaseLen == 0 || upperCaseLen == 0) {
                score += 10;
            } else {
                score += 20;
            }
            //三、数字
            int numLen = password.length() - password.replaceAll("[0-9]", "")
                    .length();
            if (numLen == 0) {
                score += 0;
            } else if (numLen == 1) {
                score += 10;
            } else {
                score += 20;
            }
            //四、符号
            char[] symbolStr = password.replaceAll("[0-9a-zA-Z]", "").toCharArray();
            int symbolNum = 0;
            symbolNum = symbolStr.length;
            //可以直接用上面的
//            for (int i = 0; i < symbolStr.length; i++) {
//                char c = symbolStr[i];
//                if ((c >= 0x21 && c <= 0x2F) || (c >= 0x3A && c <= 0x40) || (c >= 0x5B && c <= 0x60) || (c >= 0x7B && c <= 0x7E)) {
//                    symbolNum++;
//                }
//            }
            if (symbolNum == 0) {
                score += 0;
            } else if (symbolNum == 1) {
                score += 10;
            } else {
                score += 25;
            }
            //五、奖励（只能选符合最多的那一种奖励），注意这里的先后顺序
            if (lowercaseLen > 0 && upperCaseLen > 0 && numLen > 0 && symbolNum > 0) {
                score += 5;
            } else if (lowercaseLen + upperCaseLen > 0 && numLen > 0 && symbolNum > 0) {
                score += 3;
            } else if (lowercaseLen + upperCaseLen > 0 && numLen > 0) {
                score += 2;
            }
            //最后的评分标准
            if (score >= 90) {
                System.out.println("VERY_SECURE");
            } else if (score >= 80) {
                System.out.println("SECURE");
            } else if (score >= 70) {
                System.out.println("VERY_STRONG");
            } else if (score >= 60) {
                System.out.println("STRONG");
            } else if (score >= 50) {
                System.out.println("AVERAGE");
            } else if (score >= 25) {
                System.out.println("WEAK");
            } else if (score >= 0) {
                System.out.println("VERY_WEAK");
            }
        }
    }

    public static void hj87Two() {
        Scanner in = new Scanner(System.in);

        while (in.hasNextLine()) {
            char[] ch = in.nextLine().toCharArray();
            int score = 0;

            //统计长度
            int len = ch.length;
            if (len <= 4) score += 5;
            else if (len > 4 && len < 8) score += 10;
            else score += 25;

            //遍历获取大小写字母、数字、符号数目
            int upCount = 0;
            int lowCount = 0;
            int numCount = 0;
            int sigCount = 0;

            for (int i = 0; i < len; i++) {
                if (Character.isUpperCase(ch[i])) ++upCount;
                else if (Character.isLowerCase(ch[i])) ++lowCount;
                else if (Character.isDigit(ch[i])) ++numCount;
                else ++sigCount;
            }

            //字母分数
            if ((upCount > 0 && lowCount == 0) || (upCount == 0 && lowCount > 0)) score += 10;
            else if (upCount > 0 && lowCount > 0) score += 20;
            else score += 0;

            //数字分数
            if (numCount == 1) score += 10;
            else if (numCount > 1) score += 20;
            else score += 0;

            //符号分数
            if (sigCount == 1) score += 10;
            else if (sigCount > 1) score += 25;
            else score += 0;

            //奖励分数
            if (numCount > 0 && upCount > 0 && lowCount > 0 && sigCount > 0) score += 5;
            else if (numCount > 0 && sigCount > 0 && (upCount > 0 || lowCount > 0)) score += 3;
            else if (numCount > 0 && (upCount > 0 || lowCount > 0)) score += 2;

            //评分
            if (score >= 90) System.out.println("VERY_SECURE");
            else if (score >= 80) System.out.println("SECURE");
            else if (score >= 70) System.out.println("VERY_STRONG");
            else if (score >= 60) System.out.println("STRONG");
            else if (score >= 50) System.out.println("AVERAGE");
            else if (score >= 25) System.out.println("WEAK");
            else System.out.println("VERY_WEAK");
        }
    }

    /**
     * HJ86 求最大连续bit数
     */
    public static void hj86() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            String binaryString = Integer.toBinaryString(nextInt);//整型转二进制
//            System.out.println(binaryString);
            //按0分割，比如 11001000 分割后就是 11 和 1 两个字符串
            String[] split = binaryString.split("0");
            int max = 0;
            for (int i = 0; i < split.length; i++) {
//                System.out.println(split[i]);
                max = Math.max(split[i].length(), max);
            }
            System.out.println(max);
        }
    }

    //java直接用位运算&，用当前数字和1做“&”操作，如果结果是1，说明此时的二进制第一位为1，然后右移一位，直至数字为0。
    //注意java的>>是有符号右移，也就是说，负数用>>右移的话，会在左侧补1而不是0，这就会影响最终对1的计数。所以这里
    // 我们要使用无符号右移>>>。
    //另外题目的意思是我们需要接收一个byte数字，然而我们提交时第一个没通过的测试用例为200。
    // 很显然，byte的取值范围为-128 ~ 127，题目有些莫名其妙，直接用int来接收是没有问题的
    public static void hj86Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            int count = 0;
            int max = 0;
            while (nextInt != 0) {
                //用当前数字和1做“&”操作，如果结果是1，说明此时的二进制第一位为1
                if ((nextInt & 1) == 1) {
                    count++;
                    max = Math.max(max, count);
                } else {
                    count = 0;
                }
                nextInt >>>= 1;
            }
            System.out.println(max);
        }
    }

    /**
     * HJ85 最长回文子串
     */
    public static void hj85() {
        hj32();
    }

    public static void hj85Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            int max = 0;
            for (int i = 0; i < nextLine.length(); i++) {
                for (int j = nextLine.length(); j >= i; j--) {
                    String substring = nextLine.substring(i, j);
                    if (substring.equals(new StringBuilder(substring).reverse().toString())) {
                        //这样不对，因为使用break也没法直接跳出两个for循环，所以后面更短的回文串也可能进来
//                        max = substring.length();
                        max = Math.max(max, substring.length());
                    }
                }
            }
            System.out.println(max);
        }
    }


    /**
     * HJ84 统计大写字母个数
     */
    public static void hj84() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String s = nextLine.replaceAll("[A-Z]", "");
            System.out.println(nextLine.length() - s.length());
        }
    }


    /**
     * HJ83 二维数组操作
     */
    public static void hj83() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int m = scanner.nextInt();//行数
            int n = scanner.nextInt();//列数
            //初始化表格是否成功，若成功则返回0， 否则返回-1
            if (m <= 9 && n <= 9) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }
            //要交换的两个单元格的行列值
            int x1 = scanner.nextInt();
            int y1 = scanner.nextInt();
            int x2 = scanner.nextInt();
            int y2 = scanner.nextInt();
            //输出交换单元格是否成功
            if ((x1 >= 0 && x1 <= m - 1) &&
                    (y1 >= 0 && y1 <= n - 1) &&
                    (x2 >= 0 && x2 <= m - 1) &&
                    (y2 >= 0 && y2 <= n - 1)) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }
            //输入要插入的行的数值
            int x = scanner.nextInt();
            //这里注意第二个条件
            if (m < 9 && x < m) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }
            int y = scanner.nextInt();
            //这里注意第二个条件
            if (n < 9 && y < n) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }
            int xn = scanner.nextInt();
            int yn = scanner.nextInt();
            if ((xn >= 0 && xn <= m - 1) &&
                    (yn >= 0 && yn <= n - 1)) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }
        }
    }

    public static void hj83Two() {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) { // 注意 while 处理多个 case
            int m = in.nextInt();
            int n = in.nextInt();
            int[][] a = new int[m][n];
            System.out.println((m > 9 || n > 9) ? -1 : 0);
            int x1 = in.nextInt();
            int y1 = in.nextInt();
            int x2 = in.nextInt();
            int y2 = in.nextInt();
            System.out.println(((Math.max(x1, x2) >= m) || (Math.max(y1, y2) >= n)) ? -1 : 0);
            int x = in.nextInt();
            System.out.println((x >= m || (m + 1) > 9) ? -1 : 0);
            int y = in.nextInt();
            System.out.println((y >= n || (n + 1) > 9) ? -1 : 0);
            x = in.nextInt();
            y = in.nextInt();
            System.out.println((x >= m || y >= n) ? -1 : 0);
        }
    }


    /**
     * HJ82 将真分数分解为埃及分数
     */
    public static void hj82() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String[] split = scanner.nextLine().split("/");
            int f1 = Integer.parseInt(split[0]);//分子
            int f2 = Integer.parseInt(split[1]);//分母
            for (int i = 0; i < f1; i++) {
                if (i + 1 < f1) {
                    System.out.print("1/" + f2 + "+");
                } else {
                    System.out.print("1/" + f2);
                }
            }
            System.out.println();
        }
    }


    /**
     * HJ81 字符串字符匹配
     */
    public static void hj81() {
        Scanner scanner = new Scanner(System.in);
        String shortStr = scanner.nextLine();
        String longStr = scanner.nextLine();
        boolean flag = true;
        for (int i = 0; i < shortStr.length(); i++) {
            char c = shortStr.charAt(i);
            if (!longStr.contains(String.valueOf(c))) {
                flag = false;
                break;
            }
        }
        System.out.println(flag);
    }

    /**
     * HJ80 整型数组合并
     */
    public static void hj80() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            Set<Integer> integerSet = new TreeSet<>();
            int array1Len = scanner.nextInt();
            for (int i = 0; i < array1Len; i++) {
                integerSet.add(scanner.nextInt());
            }
            //有人给了下面这个建议，但是貌似不加没什么影响
            //需要去除回车键，增加： sc.nextLine();
            int array2Len = scanner.nextInt();
            for (int i = 0; i < array2Len; i++) {
                integerSet.add(scanner.nextInt());
            }
            for (Integer integer : integerSet) {
                System.out.print(integer);
            }
        }
    }

    /**
     * HJ77 火车进站
     * 答案没看懂
     */
    public static void hj77() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            List<String> list = new ArrayList<>();
            int nums = scanner.nextInt();
            int[] ids = new int[nums];
            Stack<Integer> stack = new Stack<>();
            for (int i = 0; i < nums; i++) {
                ids[i] = scanner.nextInt();
            }
            trainOut77(ids, 0, stack, "", 0, list);
            //对结果集排序
            Collections.sort(list);
            for (String s : list) {
                System.out.println(s);
            }
        }
        scanner.close();
    }

    //i为入栈次数，n为出栈次数，str存储一趟结果
    public static void trainOut77(int[] id, int i, Stack<Integer> stack, String str, int n,
                                  List<String> list) {
        if (n == id.length) {
            //如果所有火车均出栈则将当前结果保存
            list.add(str);
        }
        if (!stack.empty()) {//栈非空时出栈
            int temp = stack.pop();
            trainOut77(id, i, stack, str + temp + " ", n + 1, list);
            stack.push(temp);//恢复现场
        }
        if (i < id.length) {
            stack.push(id[i]);
            trainOut77(id, i + 1, stack, str, n, list);
            stack.pop();//恢复现场
        }
    }

    /**
     * HJ76 尼科彻斯定理
     * 高中的我们看到这个题的一瞬间，一定会觉得这是个弱智题。然而现在的我，想了好久。。
     * 题目的意思是已知等差数列和 S{n}为m^{3}，项数n为m，公差d为2，求首项a{1}
     * 公式：S{n}=na{1}+n*(n-1)/2*d
     */
    public static void hj76Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int m = scanner.nextInt();//正整数m
            long sum = (long) Math.pow(m, 3);
            int a1 = (int) sum / m - (m - 1);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < m - 1; i++) {
                sb.append(a1).append("+");
                a1 += 2;
            }
            sb.append(a1);
            System.out.println(sb);
        }
    }

    //自己写的
    public static void hj76() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int m = scanner.nextInt();//正整数m
            int pow = (int) Math.pow(m, 3);
            int[] array = new int[m];
            int middle = pow / m;
            //m的立方写成m个连续奇数之和
            if (m % 2 == 0) {//m是偶数
                array[m / 2] = middle + 1;
            } else {//m是奇数
                array[m / 2] = middle;
            }
            for (int i = m / 2 - 1; i >= 0; i--) {
                array[i] = array[m / 2] - 2 * (m / 2 - i);
            }
            for (int i = m / 2 + 1; i < m; i++) {
                array[i] = array[m / 2] + 2 * (i - m / 2);
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < array.length - 1; i++) {
                stringBuilder.append(array[i]).append("+");
            }
            stringBuilder.append(array[array.length - 1]);
            System.out.println(stringBuilder.toString());
        }
    }


    /**
     * HJ75 公共子串计算
     */
    public static void hj75() {
        //把最后的打印输出改为maxLen即可。
        hj65();
    }

    /**
     * HJ74 参数解析
     */
    public static void hj74() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String cmd = scanner.nextLine();//这里注意到读一整行，带空格的字符串
//            String cmd = "xcopy /s c:\\\\ d:\\\\e";
//            String cmd = "xcopy /s \"C:\\\\program files\" \"d:\\\"";
            ArrayList<String> list = new ArrayList<>();//存放分割后的结果
            StringBuilder stringBuilder = new StringBuilder();
            boolean flag = false;
            for (int i = 0; i < cmd.length(); i++) {
                char c = cmd.charAt(i);
                //双引号判断
                if (String.valueOf(c).equals("\"")) {
                    flag = !flag;
                    continue;
                }
                if (String.valueOf(c).equals(" ") && !flag) {
                    //空格判断，完成一次分割
                    list.add(stringBuilder.toString());
                    stringBuilder = null;
                    stringBuilder = new StringBuilder();
                } else {
                    stringBuilder.append(c);
                }
            }
            list.add(stringBuilder.toString());
            System.out.println(list.size());
            for (String s : list) {
                System.out.println(s);
            }
        }
    }


    /**
     * HJ73 计算日期到天数转换
     */
    public static void hj73() {
        Scanner scanner = new Scanner(System.in);
        int[] monthDay = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        while (scanner.hasNext()) {
            int year = scanner.nextInt();
            int month = scanner.nextInt();
            int day = scanner.nextInt();
            int result = 0;
            //判断是否是闰年，能被400整除，或者能被4整除但不能被100整除。
            if ((year % 4 == 0 && year % 100 != 0) ||
                    (year % 400 == 0)) {
                monthDay[1] = 29;
            } else {
                monthDay[1] = 28;
            }
            for (int i = 0; i < month - 1; i++) {
                result += monthDay[i];
            }
            result += day;
            System.out.println(result);
        }
    }

    public static void hj73Two() {
        Scanner in = new Scanner(System.in);
        int y = in.nextInt();
        int m = in.nextInt();
        int d = in.nextInt();
        Calendar calendar = Calendar.getInstance();
        //注意月份从0开始
        calendar.set(y, m - 1, d);
        int result = calendar.get(Calendar.DAY_OF_YEAR);
        System.out.println(result);
    }


    /**
     * HJ72 百钱买百鸡问题
     */
    public static void hj72() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            //5x+3y+z/3=100，x+y+z=100
            //得出7x+4y=100
            int x, y, z;
            for (int i = 0; i <= 14; i++) {
                x = i;
                if ((100 - 7 * x) % 4 == 0) {
                    y = (100 - 7 * x) / 4;
                    z = 100 - x - y;
                    System.out.println(x + " " + y + " " + z);
                }
            }
        }
    }


    /**
     * HJ71 字符串通配符
     */
    public static void hj71() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String value;//带有通配符的字符串
        try {
            while ((value = bufferedReader.readLine()) != null) {
                String target = bufferedReader.readLine();//需要匹配的字符串
                value = value.toLowerCase(Locale.ROOT);
                target = target.toLowerCase(Locale.ROOT);
                //带有通配符的字符串，将 * 字符最少出现过2次的，替换为 1个 * 号
                String regex = value.replaceAll("\\*{2,}", "\\*");
                //将 ? 字符 替换为 0到9或a到z的所有中的某一个字符 1次
                regex = regex.replaceAll("\\?", "[0-9a-z]{1}");
                //将 * 字符 替换为 0到9或a到z的所有中的某一个字符 0次或以上
                regex = regex.replaceAll("\\*", "[0-9a-z]{0,}");
                boolean matchesOrNot = target.matches(regex);
                System.out.println(matchesOrNot);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * HJ70 矩阵乘法计算量估算
     */
    public static void hj70() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//矩阵个数n
            int[][] a = new int[n][2];
            for (int i = 0; i < n; i++) {
                a[i][0] = scanner.nextInt();
                a[i][1] = scanner.nextInt();
            }
            String s = scanner.next();//计算的法则
            Stack<Integer> stack = new Stack<>();// 存放矩阵行数和列数
            int sum = 0;
            for (int i = s.length() - 1, j = n - 1; i >= 0; i--) {
                if (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') {// 属于字母则把相应的矩阵列数和行数入栈
                    stack.push(a[j][1]);
                    stack.push(a[j][0]);
                    j--;

                } else if (s.charAt(i) == '(') {// 括号：推出计算
                    // 矩阵尺寸x0*y0
                    int x0 = stack.pop();
                    int y0 = stack.pop();
                    // 矩阵尺寸x1*y1
                    int x1 = stack.pop();
                    int y1 = stack.pop();
                    // 两个矩阵的乘法次数为x0*y0*y1或x0*x1*y1（其中y0==x1）
                    sum += x0 * y0 * y1;
                    // 把相乘后得到的矩阵列数入栈
                    stack.push(y1);
                    // 把相乘后得到的矩阵行数入栈
                    stack.push(x0);
                }
            }
            System.out.println(sum);
        }
    }

    /**
     * HJ69 矩阵乘法
     */
    public static void hj69() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            int[][] mat1 = new int[x][y];
            int[][] mat2 = new int[y][z];
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    mat1[i][j] = scanner.nextInt();
                }
            }
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < z; j++) {
                    mat2[i][j] = scanner.nextInt();
                }
            }
            int[][] res = mul69(mat1, mat2);
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < z; j++) {
                    System.out.print(res[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    public static int[][] mul69(int[][] mat1, int[][] mat2) {
        int x = mat1.length;//获取mat1矩阵行数x
        int y = mat2.length;//获取mat2矩阵行数y
        int z = mat2[0].length;//获取mat2矩阵列数z
        int[][] res = new int[x][z];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < z; j++) {
                for (int k = 0; k < y; k++) {
                    res[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return res;
    }


    /**
     * HJ68 成绩排序
     */
    public static void hj68() {
        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, String> map = new HashMap<>();//TODO 这里Map的用法得好好理一下
        while (scanner.hasNext()) {
            int n = Integer.parseInt(scanner.nextLine());//要排序的人的个数
            int flag = Integer.parseInt(scanner.nextLine());//排序的方式，0代表降序，1代表升序
            int[][] score = new int[n][2];//姓名编号，成绩
            for (int i = 0; i < n; i++) {
                String[] nameAndScore = scanner.nextLine().split("\\s+");
                score[i][0] = i;//输入的第i个人名和成绩
                score[i][1] = Integer.parseInt(nameAndScore[1]);//成绩
                map.put(i, nameAndScore[0]);//姓名
            }
            Arrays.sort(score, new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    if (flag == 0) {//降序
                        return o2[1] - o1[1];
                    } else {//升序
                        return o1[1] - o2[1];
                    }
                }
            });
            for (int i = 0; i < n; i++) {
                System.out.println(map.get(score[i][0]) + " " + score[i][1]);
            }
        }
    }

    //这种好理解一点
    public static void hj68Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = Integer.parseInt(scanner.nextLine());//要排序的人的个数
            int flag = Integer.parseInt(scanner.nextLine());//排序的方式，0代表降序，1代表升序
            Student67[] student67s = new Student67[n];
            for (int i = 0; i < n; i++) {
                String[] nameAndScore = scanner.nextLine().split("\\s+");
                Student67 student67 = new Student67(nameAndScore[0], Integer.parseInt(nameAndScore[1]));
                student67s[i] = student67;
            }
            //传入comparator时，使用的是TimSort，是稳定排序
            Arrays.sort(student67s, new Comparator<Student67>() {
                @Override
                public int compare(Student67 o1, Student67 o2) {
                    if (flag == 0) {//降序
                        return o2.grade - o1.grade;
                    } else {//升序
                        return o1.grade - o2.grade;
                    }
                }
            });
            for (int i = 0; i < student67s.length; i++) {
                System.out.println(student67s[i].toString());
            }
        }
    }

    static class Student67 {
        private int grade;
        private String name;

        @NonNull
        @Override
        public String toString() {
            return name + " " + grade;
        }

        public Student67(String name, int grade) {
            this.name = name;
            this.grade = grade;
        }
    }

    /**
     * HJ67 24点游戏算法
     */
    public static void hj67() {
        //不会，先过
    }


    /**
     * HJ66 配置文件恢复
     * 答案不对，暂时放弃
     */
    public static void hj66() {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> command = new HashMap<>();//建立命令哈希表
        //向哈希表里添加命令键值对
        command.put("reset", "reset what");
        command.put("reset board", "board fault");
        command.put("board add", "where to add");
        command.put("board delete", "no board at all");
        command.put("reboot backplane", "impossible");
        command.put("backplane abort", "install first");
        Set<String[]> order = new HashSet<>();//建立哈希命令视图
        //遍历哈希表的set视图,向哈希命令表里添加命令
        for (String s : command.keySet()) {
            order.add(s.split(" "));
        }
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            //将输入字符串用空格分隔，以便比较
            String[] inputChange = input.split(" ");
            //匹配的命令字符串
            String[] compitable = null;
            int count = 0;//匹配次数
            //开始遍历命令视图
            for (String[] cmpOrder : order) {
                //输入字符串数组长度为一
                if (inputChange.length == 1) {
                    //命令字符串数组长度为二，不匹配
                    if (cmpOrder.length == 2) {
                        continue;

                    } else {
                        //匹配成功
                        if (cmpOrder[0].startsWith(inputChange[0])) {
                            compitable = cmpOrder;
                            count++;
                            break;
                        }
                    }
                }
                //输入字符串数组长度为二的情况
                if (inputChange.length == 2) {
                    //如果待比较命令字符串长度为1，不匹配
                    if (cmpOrder.length == 1) {
                        continue;

                    } else {
                        //如果输入命令字符串与待比较命令字符串一一匹配，匹配成功
                        if (cmpOrder[0].startsWith(inputChange[0]) && cmpOrder[1].startsWith(inputChange[1])) {
                            compitable = cmpOrder;
                            count++;
                            break;
                        }
                    }
                }
            }
            //从哈希表中找出命令的执行结果并输出
            if (compitable == null || count > 1) {
                System.out.println("unknown command");

            } else if (compitable.length == 1) {
                System.out.println(command.get(compitable[0]));

            } else {
                System.out.println(command.get(compitable[0] + " " + compitable[1]));
            }
            count = 0;
        }
    }


    /**
     * HJ65 查找两个字符串a,b中的最长公共子串
     */
    public static void hj65() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s1 = scanner.nextLine();
            String s2 = scanner.nextLine();
            String shortStr = s1.length() - s2.length() > 0 ? s2 : s1;
            String longStr = s1.length() > s2.length() ? s1 : s2;
            int shortLen = shortStr.length();
            int longLen = longStr.length();
            int maxLen = 0;
            int start = 0;
            for (int i = 0; i < shortLen; i++) {
                // 剪枝，子串长度已经不可能超过maxLen，退出循环
                if (shortLen - i + 1 <= maxLen) {
                    break;
                }
                // 左指针i，右指针k, 右指针逐渐向左逼近
                for (int k = shortLen; k > i; k--) {
                    String subStr = shortStr.substring(i, k);
                    if (longStr.contains(subStr) && maxLen < subStr.length()) {
                        maxLen = subStr.length();
                        start = i;
                        // 找到就立即返回
                        break;
                    }
                }
            }
            System.out.println(shortStr.substring(start, start + maxLen));
        }
    }


    /**
     * HJ64 MP3光标位置
     * 解题思路：
     * 模拟整个过程处理。情况分为歌曲数小于等于4和大于4两种情况，每种情况都要考虑特殊翻页、一般翻页、其他。
     * 用n表示歌曲总数，first表示当前页面的第一首歌，num表示当前选中的歌。
     * <p>
     * 算法流程：
     * 当歌曲数小于等于4时：
     * 特殊向上翻页，移动光标到最后一首歌；
     * 特殊向下翻页，移动光标到第一首歌；
     * 一般向上翻页，光标向上移动一格；
     * 一般向下翻页，光标向下移动一格；
     * 当歌曲数大于4时：
     * 特殊向上翻页，光标移动到最后一首歌，最后一页第一首歌为n-3；
     * 特殊向下翻页，光标移动到第一首歌，第一页第一首歌为1；
     * 一般向上翻页，光标向上移一格，当前页第一首歌和光标位置相同；
     * 一般向下翻页，光标向下移一格，当前页第一首歌位置也向下移一格；
     */
    public static void hj64() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//歌曲数量
            String cmd = scanner.next();//输入命令 U或者D
            parseCmd64(cmd, n);
        }
    }

    public static void parseCmd64(String str, int n) {
        // 页面数据大小，默认4
        int pageSize = 4;
        // 页面的歌曲大小，最大为4
        if (n < pageSize) {
            pageSize = n;
        }
        // 根据指令移动current光标，可以当作歌曲编号
        int current = 1;
        // 记录光标在页面中的位置pageIndex，即歌曲编号
        int pageIndex = 1;
        for (int i = 0; i < str.length(); i++) {
            // 上移
            if (str.charAt(i) == 'U') {
                // 特殊情况，当前光标在歌曲中第一首
                if (current == 1) {
                    // 从第一行上移，移动到最后的歌曲
                    current = n;
                    // 光标在页面的位置
                    pageIndex = pageSize;

                } else {
                    // 一般情况，即光标不在第一行
                    // 光标上移
                    current--;
                    if (pageIndex != 1) {
                        pageIndex--;
                    }
                }

            } else {
                // 下移
                // 特殊情况，已经到最后一首歌曲，光标到第一首歌曲
                if (current == n) {
                    current = 1;
                    pageIndex = 1;

                } else {
                    // 一般情况，非最后一行，则光标下移即可
                    current++;
                    if (pageIndex != pageSize) {
                        pageIndex++;
                    }
                }
            }
        }
        // 计算光标前后数字个数
        int next = pageSize - pageIndex;
        int pre = pageSize - 1 - next;
        // 打印页面
        String page = "";
        // 从当前光标前一个元素开始向前打印
        for (int i = pre; i > 0; i--) {
            page += (current - i) + " ";
        }
        page += current + " ";
        for (int i = 1; i <= next; i++) {
            page += (current + i) + " ";
        }
        // 去除尾部空格
        page = page.substring(0, page.length() - 1);
        System.out.println(page);
        // 打印当前光标
        System.out.println(current);
    }


    /**
     * HJ63 DNA序列
     */
    public static void hj63() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.nextLine();
            int num = Integer.parseInt(scanner.nextLine());
            double cgLen = 0.0;
            String result = "";
            for (int i = 0; i < str.length() - num + 1; i++) {
                String substring = str.substring(i, i + num);
                //^表示求否，如[^abc]表示非a,b,c的任意字符
                String s = substring.replaceAll("[^CG]", "");
                double curLen = s.length() / (num * 1.0d);
                if (curLen > cgLen) {
                    cgLen = curLen;
                    result = substring;
                }
            }
            System.out.println(result);
        }
    }


    /**
     * HJ62 查找输入整数二进制中1的个数
     * （HJ86题目）注意java的>>是有符号右移，也就是说，负数用>>右移的话，会在左侧补1而不是0，这就会影响最终对1的计数。
     * 所以一般我们要使用无符号右移>>>
     */
    public static void hj62() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int i = scanner.nextInt();
            int count = 0;
            while (i != 0) {
                if ((i & 1) == 1) {
//                if (i % 2 == 1) {
                    count++;
                }
                i = i >> 1;//右移运算符，num >> 1,相当于num除以2
            }
            System.out.println(count);
        }
    }


    /**
     * HJ61 放苹果
     * 采用递归的思想将此事件无限细分，每个事件可以分为f(m,n)=f(m-n,n)+f(m,n-1);f(m-n,n)是当苹果数大于等于盘子数的情况，f(m,n-1)是当苹果数小于盘子数的情况。
     * 当此事件分到苹果数为0或苹果数为1或盘子数为1的时候返回1，当苹果数小于0或盘子数小于等于0返回0.
     */
    public static void hj61() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int m = scanner.nextInt();
            int n = scanner.nextInt();
            System.out.println(count61(m, n));
        }
        scanner.close();
    }

    //m:苹果数  n:盘子数
    public static int count61(int m, int n) {
        //苹果数小于0或盘子数小于等于0返回0
        if (m < 0 || n <= 0) {
            return 0;
        }
        //苹果数为0或苹果数为1或盘子数为1的时候返回1
        if (m == 1 || n == 1 || m == 0) {
            return 1;
        }
        //将此事件无线细分
        //f(m-n,n)是当苹果数大于等于盘子数的情况
        //f(m,n-1)是当苹果数小于盘子数的情况
        return count61(m, n - 1) + count61(m - n, n);
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

    //cabad，ccaddab
    //dabac，baddacc
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

    //Integer.toBinaryString() ： 将十进制转二进制
    //Integer.parseInt(s, 16)：将16进制的字符串s转化为十进制
    //Integer.parseInt(binary.toString(), 2)： //把二进制字符串转成10进制
    //Integer.toHexString(n)：将十进制整数n转化为16进制字符串
    //String.toUpperCase()：将字符串转化为大写的
    //StringBuilder.replace(i, i + 1, hexString)：将sb的i到i+1位替换为hexString
    public static void hj30Two() {
        Scanner scanner = new Scanner(System.in);
        String str1 = scanner.next();
        String str2 = scanner.next();
        //1.合并
        String str = str1 + str2;
        //2.排序
        ArrayList<Character> list1 = new ArrayList<>();//存放偶数位字符
        ArrayList<Character> list2 = new ArrayList<>();//存放奇数位字符
        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) {
                list1.add(str.charAt(i));
            } else {
                list2.add(str.charAt(i));
            }
        }
        Collections.sort(list1);
        Collections.sort(list2);
        //重新拼接
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list1.size(); i++) { //list1.size()>=list2.size()
            builder.append(list1.get(i));
            if (i <= list2.size() - 1) { //防止越界
                builder.append(list2.get(i));
            }
        }
        //3.对字符进行转换操作
        for (int i = 0; i < builder.length(); i++) {
            String s = builder.substring(i, i + 1);
            if (s.matches("[0-9a-fA-F]")) {
                //把16进制转成10进制，再转成二进制
                StringBuilder binary = new StringBuilder(Integer.toBinaryString(Integer.parseInt(s, 16)));
                int len = binary.length();
                for (int j = 0; j < 4 - len; j++) { //补零
                    binary.insert(0, "0");
                }
                binary = binary.reverse();//翻转
                int n = Integer.parseInt(binary.toString(), 2); //把二进制转成10进制
                String hexString = Integer.toHexString(n).toUpperCase();//转成16进制字符串大写
                builder.replace(i, i + 1, hexString);//替换
            }
        }
        System.out.println(builder.toString());
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
                    //这一步是将char转换成小写后再排序，那么A和a转换后相减等于0，就不排序，保持原有的顺序
                    //jdk帮我们排序采用的算法是“稳定”的，也就是a和A会保持原顺序
                    return Character.toLowerCase(o1) - Character.toLowerCase(o2);//负数 o1 o2
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
    public static void hj11() {
        Scanner sc = new Scanner(System.in);
        String nextLine = sc.nextLine();
        StringBuilder sb = new StringBuilder(nextLine);
        sb.reverse();
        System.out.println(sb.toString());
    }


    /**
     * HJ10 字符个数统计
     */
    public static void hj10() {
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
    public static void hj9() {
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
    public static void hj8() {
        Scanner sc = new Scanner(System.in);

        //方法一
//        int tableNum = sc.nextInt();
        //用到了hashmap底层原理 其中最关键的是楼主直接创建的是tablesize的集合这样每个key占用一个位置 不会生成链表
        // 否则最后得到的hashmap会产生链表不是一一对应顺序不能实现排序
        //hashmap默认是不排序的，但是由于键入的key是int,int值的hash还是Int，只要键入的值小于此时hashmap的初始化大小的数组的时候，
        // 就会将键入的值放入数组中，放入的规则是根据hash值也就是键入的int值的大小。所以会排序。
//        Map<String, Integer> map = new HashMap<>(tableNum);
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
        //对于顺序验证过： Hashtable.keySet()-降序；TreeMap.keySet()-升序;HashMap.keySet()-乱序;
        // LinkedHashMap.keySet() 原序。
        // 此处要求升序，可用TreeMap
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
    public static void hj7() {
        Scanner sc = new Scanner(System.in);
        double nextDouble = sc.nextDouble();
        long result = (long) (nextDouble + 0.5);
        System.out.println(result);
    }


    /**
     * HJ6 质数因子
     * 功能:输入一个正整数，按照从小到大的顺序输出它的所有质因子（重复的也要列举）（如180的质因子为2 2 3 3 5 ）
     */
    public static void hj6() {
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

    public static void hj5() {
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
    public static void hj4Two() {
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
    public static void hj4() {
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
    public static void hj3() throws IOException {
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


//        Scanner scanner = new Scanner(System.in);
//        Set<Integer> set = new HashSet();
//        while (scanner.hasNext()){
//            int n = scanner.nextInt();
//            for (int i = 0; i < n; i++) {
//                set.add(scanner.nextInt());
//            }
//        }
//        List<Integer> list = new ArrayList(set);
//        Collections.sort(list);
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i));
//        }
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
    public static void hj2() {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine().toLowerCase();
        String s = sc.nextLine().toLowerCase();
        int times = str.length() - str.replaceAll(s, "").length();
        System.out.println(times);
    }


    /**
     * HJ1 字符串最后一个单词的长度
     */
    public static void hj1() {
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
