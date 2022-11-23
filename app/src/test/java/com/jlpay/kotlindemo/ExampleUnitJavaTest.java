package com.jlpay.kotlindemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        hj36Two();
    }


    public static void hj36Two() {
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
            String key = "trailblazers";
            String str = "Attack AT DAWN";
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
                    int index = (int) (c - 'A');
                    res.append((char) (list.get(index) - 'a' + 'A'));
                } else if (c >= 'a' && c <= 'z') {
                    int index = (int) (c - 'a');
                    res.append(list.get(index));
                }
            }
            System.out.println(res);
//        }
    }

    public static String binaryRadixToTen(String binaryStr) {
        long res = 0;
        for (int i = 0; i < binaryStr.length(); i++) {
            int anInt = Integer.parseInt(String.valueOf(binaryStr.charAt(i)));
            res = res * 2 + anInt;
        }
        return String.valueOf(res);
    }


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
}
