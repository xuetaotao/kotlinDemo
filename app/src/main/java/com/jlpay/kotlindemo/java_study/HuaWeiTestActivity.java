package com.jlpay.kotlindemo.java_study;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     */
    public static void hj28() {
        //不会，过
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
            Arrays.sort(currentWorldArray);
            Arrays.sort(worldArray);
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
     */
    public static void hj25() {
        //较难，先过
    }

    /**
     * HJ24 合唱队
     */
    public static void hj24() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = sc.nextInt();
            }
            //存储每个数左边小于其的数的个数
            int[] left = new int[n];
            left[0] = 1;//最左边的数设为1
            //存储每个数右边小于其的数的个数
            int[] right = new int[n];
            right[n - 1] = 1;//最右边的数设为1

            //计算每个位置左侧的最长递增
            //不理解  过
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
     * https://blog.csdn.net/weixin_30364147/article/details/95768264?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-5.pc_relevant_paycolumn_v3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-5.pc_relevant_paycolumn_v3&utm_relevant_index=9
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
        //较难，可以先略过，有时间回来看
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
     * HJ16 购物单
     */
    public static void hj16() {
        //不会，过
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
        Arrays.sort(array);
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
