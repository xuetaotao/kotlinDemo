package com.jlpay.kotlindemo.java_study;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Java 集合
 * https://ke.qq.com/course/3323489?taid=10786582733960801
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
     */
    private void mapDemo() {
        Map<String, String> map = new HashMap<>();
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
     * Iterator
     */
    private void iterableDemo() {
        Iterable<String> iterable = new ArrayList<>();
        Iterator<String> iterator = iterable.iterator();
    }

    /**
     * Collection
     */
    private void collectionDemo() {
        Collection<String> collection = new ArrayList<>();
    }


    /**
     * List
     */
    private void listDemo() {
        List<String> list = new ArrayList<>();
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
}