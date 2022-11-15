package com.jlpay.kotlindemo.study_java;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
    //****hj16******hj24***************************************

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


    //**********************************中等(看答案后可以做对)***************************************************
    //********hj6*****hj18****hj26***************************************


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
