package com.jlpay.kotlindemo.study_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Java 集合
 * https://ke.qq.com/course/3323489?taid=10786582733960801
 * 简述Java中满足线程安全的数据结构
 * https://blog.csdn.net/qq_29229567/article/details/87799838
 * 常见数据结构：
 * 栈：先入后出
 * 队列：先入先出
 * 数组：查询效率高，添加删除效率低
 * 链表：添加删除效率高，查询效率低（对比数组）
 * 哈希表：数组+链表，哈希值是JDK根据对象的地址或者字符串或者数字算出来的int类型的数值;Object.hashCode();
 */
public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_map);

    }

    public void onClick(View view) {
        Student student = new Student();
        student.hashCode();
        Student[] students = new Student[3];
        Student[] students2 = new Student[]{student, student, student};
        Student[] students3 = {};//空数组

        Toast.makeText(this, "OnClick", Toast.LENGTH_SHORT).show();
    }

    /**
     * Map
     * 双列集合
     */
    private void mapDemo() {
        Map<String, String> map = new HashMap<>();
        Set<String> keySet = map.keySet();//获取所有键的集合
        Collection<String> values = map.values();//获取所有值的集合
        //方式二
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
        }

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("aa", "bb");

        Map<String, String> linkedHashMap = new LinkedHashMap<>();

        //根据其元素(这里是键，即Key)的自然排序进行排序(从小到大)
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();

        //线程安全,使用synchronized来修饰方法函数来保证线程安全,但是在多线程运行环境下效率表现非常低下
        Map<String, String> hashtable = new Hashtable<>();

        //线程安全,允许多个修改操作并发运行，其原因在于使用了锁分段技术：
        // 首先将Map存放的数据分成一段一段的存储方式，然后给每一段数据分配一把锁，当一个线程占用锁访问其中一个段的数据时，
        // 其他段的数据也能被其他线程访问。这样就保证了每一把锁只是用于锁住一部分数据，那么当多线程访问Map里的不同数据段的
        // 数据时，线程间就不会存在锁竞争，从而可以有效提高并发访问效率
        //上述的处理机制明显区别于HashTable是给整体数据分配了一把锁的处理方法
        //Key和Value都不能为null，否则会抛出异常，详见put方法
        Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("aa", "bb");
    }

    /**
     * HashMap
     * 不是线程安全的，如果要保证线程安全，需要用 ConcurrentHashMap 这个类
     * 底层数据结构是 ：数组+链表的组合；JDK1.8以后，达到一定条件后(链表长度超过8)，链表会被转化为红黑树
     * 1<<4 = 2的4次方 = 16
     * 1<<n = 2的n次方
     * 当HashMap中的元素超过预置的时候，HashMap中的数组是会动态扩容的
     * 常见数据结构操作复杂度查询：https://www.bigocheatsheet.com
     * <p>
     * 二叉树与平衡二叉树：https://ke.qq.com/course/3323489?taid=10929446231127649
     * 2-3树：https://ke.qq.com/course/3323489?taid=10929450526094945
     * 红黑树：https://ke.qq.com/course/3323489?taid=10929454821062241
     */
    private void hashMapDemo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("aa", "hhh");
        String aa = map.get("aa");
    }


    /**
     * Iterable
     * Iterator 迭代器，集合的专用遍历方式
     */
    private void iterableDemo() {
        Collection<String> collection = new ArrayList<>();
        Iterable<String> iterable = collection;

        Iterator<String> it = iterable.iterator();
        while (it.hasNext()) {//如果迭代具有更多元素，则返回true
            it.next();//返回迭代中的下一个元素
        }
    }

    /**
     * Collection
     * 单列集合
     * List:元素可重复
     * Set:元素不可重复
     * Queue:
     */
    private void collectionDemo() {
        Collection<String> collection = new ArrayList<>();
        collection.add("aa");

        List<String> list = (List<String>) collection;
        //将指定的列表按升序排序
        Collections.sort(list);
        //将指定的列表按降序排序
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                //顺序不一定对
                return o1.compareTo(o2);
            }
        });
        //反转指定列表中元素的顺序
        Collections.reverse(list);
        //使用默认的随机源随机排序指定的列表
        Collections.shuffle(list);
    }


    /**
     * Queue
     * ConcurrentLinkedQueue可以被看作是一个线程安全的LinkedList，使用了非阻塞算法实现的一个高效、线程安全的并发队列
     */
    private void queueDemo() {
        Queue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * List
     * 有序
     * 可重复
     */
    private void listDemo() {
        List<String> arrayList = new ArrayList<>();//底层数据结构是数组
//        List.<String>of("hello","world");
        List<String> list = Arrays.asList("hello", "world");
        //返回的list不能做增删操作，可以做修改操作

        arrayList.add("aa");
        //arrayList使用iterator的next方法进行遍历后，然后调用add方法会产生并发修改异常(ConcurrentModificationException)
        //原因：迭代器遍历的过程中，通过集合对象修改了集合中元素的长度，造成了迭代器获取元素中判断预期修改值和实际修改值不一致
        //解决：用for循环遍历，然后用集合对象做对应的操作即可
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals("aa")) {
                arrayList.add("bb");//ConcurrentModificationException
            }
        }
        //上面这种情况使用ListIterator的add方法可以解决
        ListIterator<String> listIterator = arrayList.listIterator();
        //增强for语句，内部原理是一个Iterator迭代器
        for (String s : arrayList) {
            System.out.println(s);
            if (s.equals("aa")) {
                arrayList.add("bb");//ConcurrentModificationException
            }
        }

        LinkedList<String> linkedList = new LinkedList<>();//底层数据结构是链表
        linkedList.add("hello");
        linkedList.add("world");
        linkedList.add("java");
        linkedList.addFirst("javase");//在该列表开头插入指定的元素
        linkedList.addLast("javaee");//将指定的元素追加到此列表的末尾
        String linkedListFirst = linkedList.getFirst();//返回此列表的第一个元素
        String linkedListLast = linkedList.getLast();//返回此列表中的最后一个元素
        String removeFirst = linkedList.removeFirst();//从此列表删除并返回第一个元素
        String removeLast = linkedList.removeLast();//从此列表删除并返回最后一个元素


        //线程安全,通过数组保存数据,其本质上是一个队列
        //利用synchronized同步锁机制进行实现，其实现方式与HashTable类似
        List<String> vector = new Vector<>();

        //线程安全,提供的数据更新操作都使用了ReentrantLock的lock()方法来加锁，unlock()方法来解锁
        List<String> a = new CopyOnWriteArrayList<>();
    }


    /**
     * Set
     */
    private void setDemo() {
        Set<String> stringSet = new HashSet<>();
        //对集合的迭代顺序不做任何保证，底层数据结构是哈希表
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("hello");

        //底层数据结构是哈希表(保证元素唯一)和链表(保证元素有序)，具有可预测的迭代顺序
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();

        //根据其元素的自然排序进行排序(从小到大)
        //自然排序就是让元素所属的类实现 Comparable 接口，重写 compareTo 方法
        TreeSet<String> treeSet = new TreeSet<>();
        //根据指定的比较器进行排序
        TreeSet<Integer> treeSet2 = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        //线程安全,是对CopyOnWriteArrayList使用了装饰模式后的具体实现
        Set<String> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
    }

    /**
     * ArrayList:内部是数组
     * 数组：在内存中是一块连续的区域；支持随机访问数组元素；查找效率高，插入删除效率比较差；
     */
    private void arrayListDemo() {
        ArrayList<String> arrayList = new ArrayList<>();
    }

    /**
     * LinkedList:内部是链表
     * 链表：不需要一块连续的内存区域；插入删除效率高，查找效率低；只支持顺序访问，访问某个数据都必须从第一个节点开始，一个个寻找；
     */
    private void linkedListDemo() {
        LinkedList<String> linkedList = new LinkedList<>();
    }

    /**
     * StringBuffer与StringBuilder
     * 前者线程安全，后者不是线程安全的
     * StringBuffer是通过对方法函数进行synchronized修饰实现其线程安全特性，实现方式与HashTable、Vector类似
     */
    private void stringDemo() {
        StringBuilder sb = new StringBuilder();
        StringBuffer sbf = new StringBuffer();
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

        //排序方法一(直接排序)，将指定的列表按升序排序
        Collections.sort(integerList);

        //排序方法二  实现Comparable接口  该方法缺点就是只能对单一属性添加进行排序，而且写死在User类中

        //排序方法三
        //使用比较器来进行排序，优点可以自己定义排序规则，可以对多属性进行排序
        //创建比较器类
        //return 0; //认为是同一个元素; return 1; //升序  return -1; //降序
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
    static class User implements Comparable<HuaWeiTestActivity.User> {

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(HuaWeiTestActivity.User user) {
//            return this.getName().compareTo(user.getName());//升序
            return user.getName().compareTo(this.getName());//降序

            //return 0; //认为是同一个元素;
            //return 1; //升序
            //return -1; //降序
        }
    }

    /**
     * Random 学习
     */
    private void randomDemo() {
        Random random = new Random();
        int i = random.nextInt(100);//通过Random对象获取,[0, 100)之间的int整数
    }
}