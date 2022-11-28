package com.jlpay.kotlindemo.study_java;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 华为机试题目第二遍敲
 * 2022.11.11
 */
public class HuaWeiTest2 {


    //**********************************困难(看答案后也不太懂)***************************************************
    //****hj16******hj24****hj28***hj32********************************

    /**
     * HJ32 密码截取
     * 动态规划
     * https://blog.nowcoder.net/n/1d36e8f19915468db8e1375c61c3aa2c?f=comment
     * https://blog.nowcoder.net/n/74a83d052c2d4742a24984169aa3e244?f=comment
     */
    public static void hj32Two() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String s = "";
        try {
            while ((s = bufferedReader.readLine()) != null) {
                int len = s.length();
                // 状态：对比的两个字符索引起始和终止索引位置
                // 定义: 字符串s的i到j字符组成的子串是否为回文子串
                //回文子串：正着读和倒序读都一样
                boolean[][] dp = new boolean[len][len];
                int res = 0;
                // base case
                for (int i = 0; i < len - 1; i++) {
                    dp[i][i] = true;
                }
                for (int right = 1; right < len; right++) {
                    for (int left = 0; left < right; left++) {
                        // 状态转移：如果左右两字符相等,同时[l+1...r-1]范围内的字符是回文子串
                        // 则 [l...r] 也是回文子串
                        // right - left <= 2 是因为 要么left和right处相同，要么left和right+1相同
                        if (s.charAt(left) == s.charAt(right) &&
                                (right - left <= 2 || dp[left + 1][right - 1])) {
                            dp[left][right] = true;//s的子串(截取left到right)是回文子串
                            // 不断更新最大长度
                            res = Math.max(res, right - left + 1);
                        }
                    }
                }
                System.out.println(res);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * HJ28 素数伴侣
     * 匈牙利算法：先到先得，能让就让
     * TODO 这里自己写的有问题，后面再研究一下
     */
    public static void hj28() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            List<Integer> jiList = new ArrayList<>();
            List<Integer> ouList = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int temp = scanner.nextInt();
                if (temp % 2 == 0) {
                    ouList.add(temp);
                } else {
                    jiList.add(temp);
                }
            }
            boolean[] ouHasUsed = new boolean[ouList.size()];
            HashMap<Integer, Integer> hashMap = new HashMap<>();
            for (Integer tempJi : jiList) {
                helper28(tempJi, ouList, ouHasUsed, hashMap);
            }
            System.out.println(hashMap.size());
        }
    }


    public static boolean helper28(Integer tempJi, List<Integer> ouList, boolean[] ouHasUsed,
                                   HashMap<Integer, Integer> hashMap) {
        boolean res = false;
        for (int i = 0; i < ouList.size(); i++) {
            if (isPrime28(tempJi + ouList.get(i)) && ouHasUsed[i] == false) {
                ouHasUsed[i] = true;
                if (hashMap.get(ouList.get(i)) == null ||
                        helper28(hashMap.get(ouList.get(i)), ouList, ouHasUsed, hashMap)) {
                    hashMap.put(ouList.get(i), tempJi);
                    return true;
                }
            }

//            if (isPrime28(tempJi + ouList.get(i))) {
//                if (!ouHasUsed[i]) {//先到先得
//                    hashMap.put(ouList.get(i), tempJi);
//                    ouHasUsed[i] = true;
//                    res = true;
//                    break;
//                } else {//能让就让
//                    res = helper28(hashMap.get(ouList.get(i)), ouList, ouHasUsed, hashMap);
//                    if (res) {
//                        hashMap.put(ouList.get(i), tempJi);
//                        break;
//                    }
//                }
//            }
        }
        return res;
    }

    //素数判断
    public static boolean isPrime28(Integer temp) {
        if (temp == 1) {
            return false;
        }
        boolean res = true;
        for (int i = 2; i <= (int) Math.sqrt(temp); i++) {
            if (temp % i == 0) {
                res = false;
            }
        }
        return res;
    }


    /**
     * HJ24 合唱队
     * 动态规划 队列
     */
    public static void hj24() {

    }


    /**
     * HJ16 购物单
     * 动态规划
     */
    public static void hj16() {

    }


    //**********************************中等(看答案后可以自己默写写对)***************************************************
    //********hj6*****hj18****hj26***hj32****hj41********************************

    /**
     * HJ41 称砝码
     */
    public static void hj41() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//砝码的种数(范围[1,10])
            int[] weightArr = new int[n];//每种砝码的重量(范围[1,2000])
            for (int i = 0; i < n; i++) {
                weightArr[i] = scanner.nextInt();
            }
            int[] numArr = new int[n];//每种砝码对应的数量(范围[1,10])
            for (int i = 0; i < n; i++) {
                numArr[i] = scanner.nextInt();
            }
            Set<Integer> res = new HashSet<>();
            res.add(0);//这句必须要有，否则最内层的for循环就执行不了
            List<Integer> tempList = new ArrayList<>();
            //遍历每种质量的砝码，即weightArr数组
            for (int i = 0; i < weightArr.length; i++) {
                tempList.clear();
                tempList.addAll(res);
                //注意这里是<=，因为这里的意义是数量个数
                for (int j = 0; j <= numArr[i]; j++) {
                    int tempWeight = weightArr[i] * j;
                    for (int k = 0; k < tempList.size(); k++) {
                        res.add(tempList.get(k) + tempWeight);
                    }
                }
            }
            System.out.println(res.size());
        }
    }


    /**
     * HJ32 密码截取
     * 动态规划，循环迭代做法
     */
    public static void hj32() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            int max = 0;
            for (int i = 0; i < s.length(); i++) {
                //ABA型
                int len1 = getLongestStr32(s, i, i);
                //ABBA型
                int len2 = getLongestStr32(s, i, i + 1);
                max = Math.max(max, Math.max(len1, len2));
            }
            System.out.println(max);
        }
    }

    //循环做法
    public static int getLongestStr32(String str, int left, int right) {
        while (left >= 0 && right <= str.length() - 1 &&
                str.charAt(left) == str.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    /**
     * HJ26 字符串排序
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void hj26() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String s = bufferedReader.readLine();
            List<Character> list = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (Character.isLetter(c)) {
                    list.add(c);
                }
            }
            list.sort(new Comparator<Character>() {
                @Override
                public int compare(Character o1, Character o2) {
                    //这一步是将char转换成小写后再排序，那么A和a转换后相减等于0，就不排序，保持原有的顺序
                    //jdk帮我们排序采用的算法是“稳定”的，也就是a和A会保持原顺序
                    return Character.toLowerCase(o1) - Character.toLowerCase(o2);
                }
            });
            StringBuilder res = new StringBuilder();
            for (int i = 0, j = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (Character.isLetter(c)) {
                    //把原来位置上是字母的字符全部重排序，原来位置非字母的保留
                    res.append(list.get(j));
                    j++;
                    continue;
                }
                res.append(c);
            }
            System.out.println(res);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * HJ18 识别有效的IP地址和掩码并进行分类统计
     */
    public static void hj18() {
        Scanner scanner = new Scanner(System.in);
        int ipA = 0, ipB = 0, ipC = 0, ipD = 0, ipE = 0, ipOrMarkError = 0, ipPrivate = 0;
        while (scanner.hasNext()) {
            String[] ipAndMark = scanner.nextLine().split("\\~");
            String ip = ipAndMark[0];
            String mark = ipAndMark[1];
            //判断IP
            if (ip == null || ip.length() == 0) {
                ipOrMarkError++;
                continue;
            }
            String[] ipGroup = ip.split("\\.");
            if (ipGroup.length != 4) {
                ipOrMarkError++;
                continue;
            }
            boolean isLegalIp = true;
            for (int i = 0; i < ipGroup.length; i++) {
                if (ipGroup[i] == null || ipGroup[i].length() == 0) {
                    ipOrMarkError++;
                    isLegalIp = false;
                    continue;
                }
                int ipPer;
                try {
                    ipPer = Integer.parseInt(ipGroup[i]);
                } catch (NumberFormatException e) {
                    ipOrMarkError++;
                    isLegalIp = false;
                    continue;
                }
                if (ipPer < 0 || ipPer > 255) {
                    ipOrMarkError++;
                    isLegalIp = false;
                    continue;
                }
            }
            if (isLegalIp) {
                if ("0".equals(ipGroup[0]) || "127".equals(ipGroup[0])) {
                    continue;
                }
                //判断子网掩码是否合法
                StringBuilder sb = new StringBuilder();
                String[] markGroup = mark.split("\\.");
                for (int i = 0; i < markGroup.length; i++) {
                    int markPer = Integer.parseInt(markGroup[i]);
                    String binStr = Integer.toBinaryString(markPer);
                    //TODO  这里子网掩码转二进制后不足8位的要注意补足8位
                    if (binStr.length() < 8) {//不足8位补齐0
                        for (int j = 0; j < 8 - binStr.length(); j++) {
                            sb.append("0");
                        }
                    }
                    sb.append(binStr);
                }
                if (sb.lastIndexOf("1") > sb.indexOf("0")) {
                    ipOrMarkError++;
                    continue;
                }
                //判断是那种IP
                int ipPer0 = Integer.parseInt(ipGroup[0]);
                if (ipPer0 >= 1 && ipPer0 <= 126) {
                    ipA++;
                    int ipPer1 = Integer.parseInt(ipGroup[1]);
                    if (ipPer0 == 10 && ipPer1 >= 0 && ipPer1 <= 255) {
                        ipPrivate++;
                    }
                }
                if (ipPer0 >= 128 && ipPer0 <= 191) {
                    ipB++;
                    int ipPer1 = Integer.parseInt(ipGroup[1]);
                    if (ipPer0 == 172 && ipPer1 >= 16 && ipPer1 <= 31) {
                        ipPrivate++;
                    }
                }
                if (ipPer0 >= 192 && ipPer0 <= 223) {
                    ipC++;
                    int ipPer1 = Integer.parseInt(ipGroup[1]);
                    if (ipPer0 == 192 && ipPer1 == 168) {
                        ipPrivate++;
                    }
                }
                if (ipPer0 >= 224 && ipPer0 <= 239) {
                    ipD++;
                }
                if (ipPer0 >= 240 && ipPer0 <= 255) {
                    ipE++;
                }
            }
        }
        System.out.println(ipA + " " + ipB + " " + ipC + " " + ipD + " " + ipE + " " + ipOrMarkError + " " + ipPrivate);
    }


    /**
     * HJ6 质数因子
     */
    public static void hj6() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            long k = (long) Math.sqrt(nextInt);
            for (int i = 2; i <= k; i++) {
                while (nextInt % i == 0) {
                    System.out.print(i + " ");
                    nextInt /= i;
                }
            }
            String res = nextInt == 1 ? "" : nextInt + " ";
            System.out.print(res);
        }
    }


    //**********************************简单(不看答案可以做对)***************************************************

    /**
     * HJ42 学英语
     */
    public static void hj42() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            long num = scanner.nextLong();
            System.out.println(readEnglish42(num));
        }
    }

    //空格都只管自己前面的，也就是自己有值，自己前面加空格
    //另外加空格只管自己当前范围内如何加，不管其他范围怎么加
    public static String readEnglish42(long num) {
        String[] geArr = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven",
                "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
                "seventeen", "eighteen", "nineteen"};
        String[] shiArr = new String[]{"zero", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy",
                "eighty", "ninety"};
        StringBuilder res = new StringBuilder();
        if (num < 20) {
            res.append(geArr[(int) num]);
        } else if (num < 100) {
            int shi = (int) (num / 10);
            int ge = (int) (num % 10);
            res.append(shiArr[shi]);
            if (ge != 0) {
                res.append(" ").append(geArr[ge]);
            }
        } else if (num < 1000) {
            int bai = (int) (num / 100);
            res.append(geArr[bai]).append(" hundred");
            if ((int) (num % 100) != 0) {
                res.append(" and ").append(readEnglish42((int) (num % 100)));
            }
        } else if (num < 1000000) {
            int qian = (int) (num / 1000);
            res.append(readEnglish42(qian)).append(" thousand");
            if ((int) (num % 1000) != 0) {
                res.append(" ").append(readEnglish42((int) (num % 1000)));
            }
        } else if (num < 1000000000) {
            int baiwan = (int) (num / 1000000);
            res.append(readEnglish42(baiwan)).append(" million");
            if ((int) (num % 1000000) != 0) {
                res.append(" ").append(readEnglish42((int) (num % 1000000)));
            }
        }
//        else if (num < 1000000000000) {
//
//        }
        return res.toString();
    }

    /**
     * HJ40 统计字符
     */
    public static void hj40() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String engStr = nextLine.replaceAll("[^a-zA-Z]", "");
            int engStrLen = engStr.length();
            System.out.println(engStrLen);
            String spaceStr = nextLine.replace(" ", "");
            int spaceStrLen = nextLine.length() - spaceStr.length();
            System.out.println(spaceStrLen);
            String numStr = nextLine.replaceAll("[0-9]", "");
            int numStrLen = nextLine.length() - numStr.length();
            System.out.println(numStrLen);
            System.out.println(nextLine.length() - engStrLen - spaceStrLen - numStrLen);
        }
    }

    /**
     * HJ39 判断两个IP是否属于同一子网
     */
    public static void hj39() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String res = "";
            String netMark = scanner.nextLine();
            String ip1 = scanner.nextLine();
            String ip2 = scanner.nextLine();
            if (!checkIpAndMark39(netMark, false) || !checkIpAndMark39(ip1, true) ||
                    !checkIpAndMark39(ip2, true)) {
                res = "1";
            } else {
                String[] netMarkSplit = netMark.split("\\.");
                String[] ip1Split = ip1.split("\\.");
                String[] ip2Split = ip2.split("\\.");
                boolean isSucc = true;
                for (int i = 0; i < 4; i++) {
                    int netMarkInt = Integer.parseInt(netMarkSplit[i]);
                    int ip1Int = Integer.parseInt(ip1Split[i]);
                    int ip2Int = Integer.parseInt(ip2Split[i]);
                    if ((netMarkInt & ip1Int) != (netMarkInt & ip2Int)) {
                        isSucc = false;
                        res = "2";
                        break;
                    }
                }
                if (isSucc) {
                    res = "0";
                }
            }
            System.out.println(res);
        }
    }

    //校验子网掩码和IP地址的合法性
    //可以改造一下，因为错误的情况太多，正确的情况就一种，可以默认res为false，只写正确的情况
    //true:合法  false:非法
    public static boolean checkIpAndMark39(String str, boolean isIp) {
        boolean res = true;
        String[] split = str.split("\\.");
        if (split.length != 4) {
            res = false;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                if (split[i].length() == 0 || split[i] == null) {
                    res = false;
                    break;
                }
                try {
                    int parseInt = Integer.parseInt(split[i]);
                    if (parseInt < 0 || parseInt > 255) {
                        res = false;
                        break;
                    }
                    if (!isIp) {//子网掩码
                        //将子网掩码转化为二进制形式
                        String binaryStr = tenToBinaryStr39(parseInt);
                        if (binaryStr.length() < 8) {
                            for (int j = 8 - binaryStr.length(); j > 0; j--) {
                                sb.append("0");
                            }
                        }
                        sb.append(binaryStr);
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    res = false;
                    break;
                }
            }
            if (!isIp && (sb.lastIndexOf("1") > sb.indexOf("0"))) {
                res = false;
            }
        }
        return res;
    }

    //十进制转二进制
    public static String tenToBinaryStr39(int temp) {
        StringBuilder res = new StringBuilder();
        while (temp > 0) {
            if ((temp & 1) == 1) {
//            if (temp % 2 == 1) {
                res.append("1");
            } else {
                res.append("0");
            }
            temp = temp >>> 1;
        }
        return res.reverse().toString();
    }

    /**
     * HJ38 求小球落地5次后所经历的路程和第5次反弹的高度
     */
    public static void hj38() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            double m = scanner.nextDouble();//初始高度
            int n = 5;//第n次落地
            double res = 0.0;//第n次落地共经历多少米
            for (int i = 1; i <= n; i++) {
                //先落地后反弹
                //第i次落地时共经历多少米
                if (i == 1) {
                    res += m;
                } else {
                    res += (m * 2);
                }
                //第i次落地反弹多高
                m = m / 2;
            }
            System.out.println(res);
            System.out.println(m);
        }
    }

    /**
     * HJ37 统计每个月兔子的总数
     */
    public static void hj37() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int month = scanner.nextInt();
//            System.out.println(cal37(month));
            System.out.println(cal37Two(month));
        }
    }

    //递归方式实现
    public static int cal37(int month) {
        if (month == 1 || month == 2) {
            return 1;
        }
        return cal37(month - 2) + cal37(month - 1);
    }

    //动态规划方式实现，迭代
    public static int cal37Two(int month) {
        if (month < 3) {
            return 1;
        } else {
            int a = 1;//n-1个月
            int b = 1;//n-2个月
            int n = 3;//代表月份
            while (n <= month) {
                int temp = a + b;
                b = a;
                a = temp;
                n++;
            }
            return a;
        }
    }

    /**
     * HJ36 字符串加密
     * 自己写的虽然能通过机测，但是没有考虑空格和大写的情况，可以看看答案
     */
    public static void hj36() {
        Scanner scanner = new Scanner(System.in);
        char[] arr = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        while (scanner.hasNext()) {
            String key = scanner.next();
            char[] pasArr = new char[26];
            Set<Character> set = new HashSet<>();
            String str = scanner.next();
            int j = 0;
            for (int i = 0; i < key.length(); i++) {
                char c = key.charAt(i);
                if (set.add(c)) {
                    pasArr[j++] = c;
                }
            }
            for (int i = 0; i < arr.length; i++) {
                if (set.add(arr[i])) {
                    pasArr[j++] = arr[i];
                }
            }
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                res.append(pasArr[(int) (c - 'a')]);
            }
            System.out.println(res);
        }
    }

    public static void hj36Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String key = scanner.nextLine();
            String str = scanner.nextLine();
            LinkedHashSet<Character> linkedHashSet = new LinkedHashSet<>();
            for (int i = 0; i < key.length(); i++) {
                linkedHashSet.add(Character.toLowerCase(key.charAt(i)));
            }
            for (int i = 0; i < 26; i++) {
                linkedHashSet.add((char) ('a' + i));
            }
            List<Character> list = new ArrayList<>(linkedHashSet);
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == ' ') {
                    res.append(c);
                } else if (c >= 'A' && c <= 'Z') {//大写字母
                    int index = (int) (c - 'A');//密码映射表里都是小写
                    res.append((char) (list.get(index) - 'a' + 'A'));//注意这里是-('a'-'A')
                } else if (c >= 'a' && c <= 'z') {
                    int index = (int) (c - 'a');
                    res.append(list.get(index));
                }
            }
            System.out.println(res);
        }
    }

    /**
     * HJ35 蛇形矩阵
     */
    public static void hj35() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int anInt = scanner.nextInt();
            int[][] arr = new int[anInt][];
            for (int i = 0; i < anInt; i++) {
                int[] temp = new int[anInt - i];
                arr[i] = temp;
            }
            int num = 1;
            for (int x = 0; x < arr.length; x++) {
                int temp = x;
                for (int y = 0; temp >= 0; y++) {
                    arr[temp--][y] = num;
                    num++;
                }
            }
            for (int[] ints : arr) {
                for (int i : ints) {
                    System.out.print(i + " ");
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
            String s = scanner.nextLine();
            char[] charArray = s.toCharArray();
            Arrays.sort(charArray);
            System.out.println(new String(charArray));
        }
    }

    /**
     * HJ33 整数与IP地址间的转换
     */
    public static void hj33() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            String res = "";
            if (s.contains(".")) {
                //IP地址-->10进制整数
                String[] split = s.split("\\.");
                StringBuilder binarySb = new StringBuilder();
                for (int i = 0; i < split.length; i++) {
                    String temp = split[i];//十进制字符串
                    //注意：这个方法不会补足前面的0，只会从1开始
                    String binaryString = Integer.toBinaryString(Integer.parseInt(temp));
                    if (binaryString.length() < 8) {
                        for (int j = 8 - binaryString.length(); j > 0; j--) {
                            binarySb.append("0");
                        }
                    }
                    binarySb.append(binaryString);
                }
                //这里注意可能超过Integer的最大范围
                res = String.valueOf(Long.parseLong(binarySb.toString(), 2));

            } else {
                //10进制整数-->IP地址
                //这里注意可能超过Integer的最大范围
                String binaryString = Long.toBinaryString(Long.parseLong(s));
                StringBuilder binarySb = new StringBuilder();
                if (binaryString.length() < 32) {
                    for (int i = 32 - binaryString.length(); i > 0; i--) {
                        binarySb.append("0");
                    }
                }
                binarySb.append(binaryString);
                String binaryStr = binarySb.toString();
                int[] ipArr = new int[4];
                int p = 0;
                while (binaryStr.length() >= 8) {
                    String substring = binaryStr.substring(0, 8);
                    ipArr[p++] = Integer.parseInt(substring, 2);
                    binaryStr = binaryStr.substring(8);
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < ipArr.length; i++) {
                    sb.append(ipArr[i]);
                    if (i != ipArr.length - 1) {
                        sb.append(".");
                    }
                }
                res = sb.toString();
            }
            System.out.println(res);
        }
    }

    public static void hj33Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.contains(".")) {
                //ipv4-->int
                long res = 0;
                String[] split = s.split("\\.");
                for (int i = 0; i < split.length; i++) {
                    String temp = split[i];
                    res = res * 256 + Integer.parseInt(temp);
                }
                System.out.println(res);

            } else {
                //int-->ipv4
                long aLong = Long.parseLong(s);
                StringBuilder res = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    res.insert(0, aLong % 256 + ".");
                    aLong = aLong / 256;
                }
                System.out.println(res.substring(0, res.length() - 1));
            }
        }
    }

    //10进制字符串转为2进制字符串
    public static String tenRadixToBinary(String tenRadixStr) {
        long aLong = Long.parseLong(tenRadixStr);
        StringBuilder sb = new StringBuilder();
        while (aLong != 0) {
//            if ((aLong % 2) == 1) {
            if ((aLong & 1) == 1) {
                sb.append("1");
            } else {
                sb.append("0");
            }
            aLong = aLong >>> 1;
        }
        return sb.reverse().toString();
    }

    //2进制字符串转为10进制字符串
    public static String binaryRadixToTen(String binaryStr) {
        long res = 0;
        for (int i = 0; i < binaryStr.length(); i++) {
            int anInt = Integer.parseInt(String.valueOf(binaryStr.charAt(i)));
            res = res * 2 + anInt;
        }
        return String.valueOf(res);
    }

    /**
     * HJ31 单词倒排
     */
    public static void hj31() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String s = bufferedReader.readLine();
            String[] s1 = s.split("[^a-zA-Z]");
            StringBuilder res = new StringBuilder();
            for (int i = s1.length - 1; i >= 0; i--) {
                res.append(s1[i]).append(" ");
            }
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * HJ30 字符串合并处理
     * 还有一种做法是把'0'~'9'、'A'~'F'和'a'~'f'的结果直接方法Map中，第三步直接查
     */
    public static void hj30() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            StringBuilder res = new StringBuilder();
            String s1 = scanner.next();
            String s2 = scanner.next();
            res.append(s1).append(s2);
            //第二步：对合并后的字符串进行排序
            List<Character> jiList = new ArrayList<>();
            List<Character> ouList = new ArrayList<>();
            for (int i = 0; i < res.length(); i++) {
                char c = res.charAt(i);
                if (i % 2 == 0) {
                    ouList.add(c);
                } else {
                    jiList.add(c);
                }
            }
            Collections.sort(jiList);
            Collections.sort(ouList);
            int len = res.length();
            res.delete(0, res.length());
            for (int i = 0; i < len; i++) {
                if (i % 2 == 0) {
                    res.append(ouList.get(i / 2));
                } else {
                    res.append(jiList.get((i - 1) / 2));
                }
            }
            //第三步：对排序后的字符串中的'0'~'9'、'A'~'F'和'a'~'f'字符，需要进行转换操作
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < res.length(); i++) {
                char c = res.charAt(i);
                if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')) {
                    //将16进制字符串变成10进制整数
                    int cInt = Integer.parseInt(String.valueOf(c), 16);
                    //将10进制整数转换为2进制字符串
                    String twoRadixStr = Integer.toBinaryString(cInt);
                    StringBuilder temp = new StringBuilder();
                    //注意不足4位的要补0
                    if (twoRadixStr.length() < 4) {
                        for (int j = 4 - twoRadixStr.length(); j > 0; j--) {
                            temp.append("0");
                        }
                    }
                    temp.append(twoRadixStr);//TODO 如果把这行代码加到下面一行就会出问题，原因不详
                    //将2进制字符串倒序
                    String reverseTwoRadixStr = temp.reverse().toString();
                    //将倒序后的2进制字符串再转换成对应的十六进制大写字符
                    String s = Integer.toHexString(Integer.parseInt(reverseTwoRadixStr, 2)).toUpperCase();
                    result.append(s);
                } else {
                    result.append(c);
                }
            }
            System.out.println(result);
        }
    }


    /**
     * HJ29 字符串加解密
     */
    public static void hj29() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str1 = scanner.nextLine();
            String str2 = scanner.nextLine();
            System.out.println(encode29(str1));
            System.out.println(decode29(str2));
        }
    }

    public static String encode29(String str) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 'a' && c <= 'z') {
                if (c == 'z') {
                    res.append('A');
                } else {
                    res.append((char) (c - 'a' + 'A' + 1));
                }
            } else if (c >= 'A' && c <= 'Z') {
                if (c == 'Z') {
                    res.append('a');
                } else {
                    res.append((char) (c - 'A' + 'a' + 1));
                }
            } else if (c >= '0' && c <= '9') {
                if (c == '9') {
                    res.append('0');
                } else {
                    res.append((char) (c + 1));
                }
            }
        }
        return res.toString();
    }

    public static String decode29(String str) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 'a' && c <= 'z') {
                if (c == 'a') {
                    res.append('Z');
                } else {
                    res.append((char) (c - 'a' + 'A' - 1));
                }
            } else if (c >= 'A' && c <= 'Z') {
                if (c == 'A') {
                    res.append('z');
                } else {
                    res.append((char) (c - 'A' + 'a' - 1));
                }
            } else if (c >= '0' && c <= '9') {
                if (c == '0') {
                    res.append('9');
                } else {
                    res.append((char) (c - 1));
                }
            }
        }
        return res.toString();
    }

    /**
     * HJ27 查找兄弟单词
     */
    public static void hj27() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//字典中单词的个数n
            String[] strArr = new String[n];
            for (int i = 0; i < n; i++) {
                strArr[i] = scanner.next();
            }
            String str = scanner.next();
            int k = scanner.nextInt();
            List<String> resList = new ArrayList<>();
            int[] array = new int[128];
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                array[c]++;
            }
            for (int i = 0; i < strArr.length; i++) {
                if (strArr[i].equals(str)) {
                    continue;
                }
                if (strArr[i].length() != str.length()) {
                    continue;
                }
                int[] tempArr = new int[128];
                for (int j = 0; j < strArr[i].length(); j++) {
                    tempArr[strArr[i].charAt(j)]++;
                }
                boolean isBrother = true;
                for (int j = 0; j < 128; j++) {
                    if (array[j] != tempArr[j]) {
                        isBrother = false;
                        break;
                    }
                }
                if (isBrother) {
                    resList.add(strArr[i]);
                }
            }
            System.out.println(resList.size());
            if (k < resList.size()) {
                Collections.sort(resList);
                System.out.println(resList.get(k - 1));
            }
        }
    }


    //看答案后的思路
    public static void hj27Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();//字典中单词的个数n
            String[] strArr = new String[n];
            for (int i = 0; i < n; i++) {
                strArr[i] = scanner.next();
            }
            String str = scanner.next();
            int k = scanner.nextInt();
            List<String> resList = new ArrayList<>();
            for (int i = 0; i < strArr.length; i++) {
                if (strArr[i].equals(str) || strArr[i].length() != str.length()) {
                    continue;
                }
                char[] charsTemp = strArr[i].toCharArray();
                char[] chars = str.toCharArray();
                Arrays.sort(charsTemp);
                Arrays.sort(chars);
                if (new String(chars).equals(new String(charsTemp))) {
                    resList.add(strArr[i]);
                }
            }
            System.out.println(resList.size());
            if (k < resList.size()) {
                Collections.sort(resList);
                System.out.println(resList.get(k - 1));
            }
        }
    }

    /**
     * HJ25 数据分类处理
     */
    public static void hj25() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int iNum = scanner.nextInt();
            int[] iNumArray = new int[iNum];
            for (int i = 0; i < iNum; i++) {
                iNumArray[i] = scanner.nextInt();
            }
            int rNum = scanner.nextInt();
            TreeSet<Integer> treeSet = new TreeSet<>();
            for (int i = 0; i < rNum; i++) {
                treeSet.add(scanner.nextInt());//序列R排序并去重
            }
            StringBuilder res = new StringBuilder();
            List<Integer> resList = new ArrayList<>();
            List<Integer> iNumList = new ArrayList<>();
            for (Integer temp : treeSet) {
                String tempStr = String.valueOf(temp);
                for (int i = 0; i < iNumArray.length; i++) {
                    String iNumTemp = String.valueOf(iNumArray[i]);
                    if (iNumTemp.contains(tempStr)) {
                        iNumList.add(i);
                        iNumList.add(iNumArray[i]);
                    }
                }
                if (iNumList.size() > 0) {
                    resList.add(temp);
                    resList.add(iNumList.size() / 2);
                    resList.addAll(iNumList);
                    iNumList.clear();
                }
            }
            res.append(resList.size()).append(" ");
            for (Integer temp : resList) {
                res.append(temp).append(" ");
            }
            System.out.println(res);
        }
    }


    /**
     * HJ23 删除字符串中出现次数最少的字符
     */
    public static void hj23() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            HashMap<Character, Integer> hashMap = new HashMap<>();
            for (int i = 0; i < nextLine.length(); i++) {
                char c = nextLine.charAt(i);
                if (hashMap.containsKey(c)) {
                    hashMap.put(c, hashMap.get(c) + 1);
                } else {
                    hashMap.put(c, 1);
                }
            }
            int min = Integer.MAX_VALUE;
            List<Character> list = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : hashMap.entrySet()) {
                if (entry.getValue() < min) {
                    min = entry.getValue();
                    list.clear();
                    list.add(entry.getKey());
                } else if (entry.getValue() == min) {
                    list.add(entry.getKey());
                }
            }
            String res = nextLine;
            for (Character temp : list) {
                res = res.replace(String.valueOf(temp), "");
            }
            System.out.println(res);

        }
    }


    /**
     * HJ22 汽水瓶
     */
    public static void hj22() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            if (nextInt == 0) {
                break;
            }
            System.out.println(nextInt / 2);
        }
    }


    /**
     * HJ21 简单密码
     */
    public static void hj21() {
        char[] array = new char[]{'2', '2', '2', '3', '3', '3', '4', '4', '4', '5', '5', '5', '6',
                '6', '6', '7', '7', '7', '7', '8', '8', '8', '9', '9', '9', '9'};
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < nextLine.length(); i++) {
                char c = nextLine.charAt(i);
                if (Character.isDigit(c)) {
                    res.append(c);
                } else if (Character.isLowerCase(c)) {
                    res.append(array[c - 'a']);
                } else if (Character.isUpperCase(c)) {
                    if ('Z' == c) {
                        res.append('a');
                    } else {
                        res.append((char) (c - 'A' + 1 + 'a'));
                    }
                } else {
                    res.append(c);
                }
            }
            System.out.println(res);
        }
    }

    /**
     * HJ20 密码验证合格程序
     */
    public static void hj20() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String res = "OK";
            if (nextLine.length() <= 8) {
                res = "NG";
            } else if (!inLetterNumAndOther20(nextLine)) {
                res = "NG";
//            } else if (hasRepeatStr20(nextLine)) {
            } else if (hasRepeatStr20(nextLine, 0, 3)) {
                res = "NG";
            }
            System.out.println(res);
        }
    }

    //包括大小写字母.数字.其它符号,以上四种至少三种
    public static boolean inLetterNumAndOther20(String str) {
        int count = 0;
        if (str.replaceAll("[^a-z]", "").length() > 0) {
            count++;//包括小写字母
        }
        if (str.replaceAll("[^A-Z]", "").length() > 0) {
            count++;//包括大写字母
        }
        if (str.replaceAll("[^0-9]", "").length() > 0) {
            count++;//包括数字
        }
        if (str.replaceAll("[0-9a-zA-Z]", "").length() > 0) {
            count++;//包括其它符号
        }
        return count >= 3;
    }

    public static boolean hasRepeatStr20(String nextLine) {
        boolean res = false;
        boolean hasFinished = false;
        for (int i = 0; i < nextLine.length(); i++) {
            if (hasFinished) {
                break;
            }
            for (int j = i + 3; j < nextLine.length(); j++) {
                String substring = nextLine.substring(i, j);
                if (nextLine.substring(j).contains(substring)) {
                    res = true;
                    hasFinished = true;
                    break;
                }
            }
        }
        return res;
    }

    //递归实现
    public static boolean hasRepeatStr20(String nextLine, int left, int right) {
        if (right >= nextLine.length() - 1) {
            return false;
        }
        if (nextLine.substring(right).contains(nextLine.substring(left, right))) {
            return true;
        }
        return hasRepeatStr20(nextLine, left + 1, right + 1);
    }


    /**
     * HJ19 简单错误记录
     */
    public static void hj19() {
        Scanner scanner = new Scanner(System.in);
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            //String[] split = str.split("\\\\");
            //截取最后一个\后面的字符串，如cqzlyaszjvlsjmkwoqijggmybr 645
            String subStr = nextLine.substring((nextLine.lastIndexOf("\\") + 1));
            String[] subStrSplits = subStr.split(" ");
            //记录文件名
            String fileName = subStrSplits[0].length() - 16 >= 0 ?
                    subStrSplits[0].substring(subStrSplits[0].length() - 16) : subStrSplits[0];
            String errorLine = subStrSplits[1];
            String key = fileName + " " + errorLine;
            if (linkedHashMap.containsKey(key)) {
                linkedHashMap.put(key, linkedHashMap.get(key) + 1);
            } else {
                linkedHashMap.put(key, 1);
            }
        }
        int count = 0;
        for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
            if (linkedHashMap.size() - count <= 8) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
            //注意，这个不能放在if里面,比如map.size()是10，一开始count是0,10-0>8，没有输出
            //而最后只用输出最后出现的八条错误记录
            count++;
        }
    }

    /**
     * HJ17 坐标移动
     */
    public static void hj17() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String[] split = nextLine.split(";");
            int x = 0;
            int y = 0;
            for (int i = 0; i < split.length; i++) {
                if (split[i] == null || split[i].length() == 0) {
                    continue;
                }
                String direct = split[i].substring(0, 1);
                if (!direct.equals("W") && !direct.equals("A") && !direct.equals("S") && !direct.equals("D")) {
                    continue;
                }
                String valStr = split[i].substring(1);
                int val = 0;
                try {
                    val = Integer.parseInt(valStr);
                } catch (NumberFormatException e) {
                    continue;
                }
                switch (direct) {
                    case "W"://向上移动，y+val
                        y += val;
                        break;
                    case "A"://向左移动，x-val
                        x -= val;
                        break;
                    case "S"://向下移动，y-val
                        y -= val;
                        break;
                    case "D"://向右移动，x+val
                        x += val;
                        break;
                }
            }
            System.out.println(x + "," + y);
        }
    }


    /**
     * HJ15 求int型正整数在内存中存储时1的个数
     */
    public static void hj15() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            String s = Integer.toBinaryString(nextInt);
            String replace = s.replace("0", "");
            System.out.println(replace.length());
        }
    }


    public static void hj15Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            int count = 0;
            for (int i = 0; i < 32; i++) {
                if (nextInt % 2 == 1) {
//                if ((nextInt & 1) == 1) {
                    count++;
                }
                nextInt = nextInt >>> 1;
            }
            System.out.println(count);
        }
    }


    /**
     * HJ14 字符串排序
     */
    public static void hj14() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            String[] array = new String[nextInt];
            for (int i = 0; i < nextInt; i++) {
                String next = scanner.next();
                array[i] = next;
            }
            Arrays.sort(array);
            for (String s : array) {
                System.out.println(s);
            }
        }
    }

    //不可以，遇到相同的字符串就只能add一个
    public static void hj14Error() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            TreeSet<String> treeSet = new TreeSet<>();
            for (int i = 0; i < nextInt; i++) {
                String next = scanner.next();
                treeSet.add(next);
            }
            for (String s : treeSet) {
                System.out.println(s);
            }
        }
    }


    /**
     * HJ13 句子逆序
     */
    public static void hj13() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String[] split = nextLine.split("\\s+");
            for (int i = split.length - 1; i >= 0; i--) {
                System.out.print(split[i] + " ");
            }
        }
    }


    /**
     * HJ12 字符串反转
     */
    public static void hj12() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            StringBuilder sb = new StringBuilder(nextLine);
            System.out.println(sb.reverse());
        }
    }


    /**
     * HJ11 数字颠倒
     */
    public static void hj11() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            StringBuilder sb = new StringBuilder(nextLine);
            System.out.println(sb.reverse());
        }
    }


    /**
     * HJ10 字符个数统计
     */
    public static void hj10() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            int[] array = new int[128];
            for (int i = 0; i < nextLine.length(); i++) {
                array[nextLine.charAt(i)]++;
            }
            int count = 0;
            for (int i : array) {
                if (i > 0) {
                    count++;
                }
            }
            System.out.println(count);
        }
    }

    public static void hj10Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            HashMap<Character, Integer> hashMap = new HashMap<>();
            for (int i = 0; i < nextLine.length(); i++) {
                char c = nextLine.charAt(i);
                if (hashMap.containsKey(c)) {
                    hashMap.put(c, hashMap.get(c) + 1);
                } else {
                    hashMap.put(c, 1);
                }
            }
            System.out.println(hashMap.size());
        }
    }

    public static void hj10Three() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            HashSet<Character> hashSet = new HashSet<>();
            for (int i = 0; i < nextLine.length(); i++) {
                char c = nextLine.charAt(i);
                hashSet.add(c);
            }
            System.out.println(hashSet.size());
        }
    }


    /**
     * HJ9 提取不重复的整数
     */
    public static void hj9() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            Set<Character> set = new LinkedHashSet<>();
            for (int i = nextLine.length() - 1; i >= 0; i--) {
                char c = nextLine.charAt(i);
                set.add(c);
            }
            for (Character character : set) {
                System.out.print(character);
            }
        }
    }

    public static void hj9Two() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int nextInt = scanner.nextInt();
            HashSet<Integer> hashSet = new HashSet<>();
            while (nextInt != 0) {
                if (hashSet.add(nextInt % 10)) {
                    System.out.print(nextInt % 10);
                }
                nextInt /= 10;
            }
        }
    }

    /**
     * HJ8 合并表记录
     */
    public static void hj8() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            scanner.nextLine();//注意要加上这句才能换行
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            for (int i = 0; i < n; i++) {
                String[] s = scanner.nextLine().split(" ");
                if (s[0] == null || s[0].length() == 0 || s[1] == null || s[1].length() == 0) {
                    return;
                }
                int key = Integer.parseInt(s[0]);
                int value = Integer.parseInt(s[1]);
                if (treeMap.containsKey(key)) {
                    treeMap.put(key, treeMap.get(key) + value);
                } else {
                    treeMap.put(key, value);
                }
            }
            for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }


    /**
     * HJ7 取近似值
     */
    public static void hj7() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            double v = scanner.nextDouble();
            int v1 = (int) (v + 0.5);
            System.out.println(v1);
        }
    }


    /**
     * HJ5 进制转换
     */
    public static void hj5() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine().substring(2);
            int anInt = Integer.parseInt(nextLine, 16);
            System.out.println(anInt);
        }
    }

    public static void hj5Two() {
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
        map.put('a', 10);
        map.put('B', 11);
        map.put('b', 11);
        map.put('C', 12);
        map.put('c', 12);
        map.put('D', 13);
        map.put('d', 13);
        map.put('E', 14);
        map.put('e', 14);
        map.put('F', 15);
        map.put('f', 15);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine().substring(2);
            int res = 0;
            for (int i = 0; i < nextLine.length(); i++) {
                res = res * 16 + map.get(nextLine.charAt(i));
            }
            System.out.println(res);
        }
    }

    /**
     * HJ4 字符串分隔
     */
    public static void hj4() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.nextLine();
            if (str == null || str.length() == 0) {
                return;
            }
            int length = str.length();
            StringBuilder sb = new StringBuilder(str);
            if (length % 8 != 0) {
                for (int i = 0; i < 8 - length % 8; i++) {
                    sb.append("0");
                }
            }
            String subStr = sb.toString();
            while (subStr.length() >= 8) {
                String substring = subStr.substring(0, 8);
                System.out.println(substring);
                subStr = subStr.substring(8);
            }
        }
    }

    /**
     * HJ3 明明的随机数
     */
    public static void hj3() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            TreeSet<Integer> treeSet = new TreeSet<>();
            for (int i = 0; i < n; i++) {
                treeSet.add(scanner.nextInt());
            }
            for (Integer temp : treeSet) {
                System.out.println(temp);
            }
        }
    }

    /**
     * HJ2 计算某字符出现次数
     */
    public static void hj2() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.nextLine().toLowerCase(Locale.ROOT);
            String s = scanner.next().toLowerCase(Locale.ROOT);
            String newStr = str.replace(s, "");
            System.out.println(str.length() - newStr.length());
        }
    }


    /**
     * HJ1 字符串最后一个单词的长度
     */
    public static void hj1() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String[] s = nextLine.split(" ");
            System.out.println(s[s.length - 1].length());
        }
    }
}
