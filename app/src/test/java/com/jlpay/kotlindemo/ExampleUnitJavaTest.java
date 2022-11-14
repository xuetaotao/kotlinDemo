package com.jlpay.kotlindemo;

import org.junit.Test;

public class ExampleUnitJavaTest {

    @Test
    public void hj01() {
        hj21("YUANzhi1987");
    }


    public static void hj21(String nextLine) {
        char[] array = new char[]{'2', '2', '2', '3', '3', '3', '4', '4', '4', '5', '5', '5', '6',
                '6', '6', '7', '7', '7', '7', '8', '8', '8', '9', '9', '9', '9'};
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            String nextLine = scanner.nextLine();
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
//    }
}
