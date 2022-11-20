package com.jlpay.kotlindemo;

import org.junit.Test;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        hj32();
    }


    public static void hj32() {
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
        String s = "LONNNNP";
        int max = 0;
        for (int i = 0; i < s.length(); i++) {
            //ABA型
            int len1 = getLongestStr32(s, i, i);
            //ABBA型
            int len2 = getLongestStr32(s, i, i + 1);
            max = Math.max(max, Math.max(len1, len2));
        }
        System.out.println(max);
//        }
    }

    public static int getLongestStr32(String str, int left, int right) {
        while (left >= 0 && right <= str.length() - 1 && str.charAt(left) == str.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
}
