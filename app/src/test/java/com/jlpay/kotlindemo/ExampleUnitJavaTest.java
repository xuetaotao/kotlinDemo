package com.jlpay.kotlindemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        hj23("aabcddd");
    }


    public static void hj23(String nextLine) {
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            String nextLine = scanner.nextLine();
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
