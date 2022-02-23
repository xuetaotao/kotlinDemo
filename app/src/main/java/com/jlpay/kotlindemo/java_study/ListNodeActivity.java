package com.jlpay.kotlindemo.java_study;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListNodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        listNodeCreateAndLoop();//创建链表及遍历链表
//        listNodeInsert();//插入节点
//        listNodeReplace();//替换节点
//        listNodeDel();//删除节点
//        printNodeList();//从尾到头打印链表 非递归
        printNodeList2();//从尾到头打印链表  递归
    }

    /**
     * 从尾到头打印链表
     * 输入一个链表的头节点，按链表从尾到头的顺序返回每个节点的值（用数组返回）。
     * 非递归
     * ArrayList 中有个方法是 add(index,value)，可以指定 index 位置插入 value 值
     * 所以我们在遍历 listNode 的同时将每个遇到的值插入到 list 的 0 位置，最后输出 listNode 即可得到逆序链表
     */
    private void printNodeList() {
        ListNode nodeSta = new ListNode(0);
        ListNode nextNode;
        nextNode = nodeSta;

        for (int i = 1; i < 10; i++) {
            ListNode node = new ListNode(i);
            nextNode.next = node;
            nextNode = nextNode.next;
        }
        nextNode = nodeSta;
        print(nextNode);
        Log.e("TAG", "下面是倒序后的打印输出：\n");

        ArrayList<Integer> list = new ArrayList<>();
        ListNode tmp = nodeSta;
        while (tmp != null) {
            list.add(0, tmp.val);
            tmp = tmp.next;
        }
        for (Integer i : list) {
            Log.e("TAG", "print: 节点：" + i);
        }
    }

    /**
     * 从尾到头打印链表
     * 输入一个链表的头节点，按链表从尾到头的顺序返回每个节点的值（用数组返回）。
     * 递归
     */
    private void printNodeList2() {
        ListNode nodeSta = new ListNode(0);
        ListNode nextNode;
        nextNode = nodeSta;

        for (int i = 1; i < 10; i++) {
            ListNode node = new ListNode(i);
            nextNode.next = node;
            nextNode = nextNode.next;
        }
        nextNode = nodeSta;
        print(nextNode);
        Log.e("TAG", "下面是倒序后的打印输出：\n");

        ArrayList<Integer> list = printListFromTailToHead(nodeSta);
        for (Integer i : list) {
            Log.e("TAG", "print: 节点：" + i);
        }
    }

    private ArrayList<Integer> list = new ArrayList<>();

    private ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        if (listNode != null) {
            printListFromTailToHead(listNode.next);
            list.add(listNode.val);
        }
        return list;
    }

    /**
     * 删除节点
     */
    private void listNodeDel() {
        ListNode nodeSta = new ListNode(0);
        ListNode nextNode;
        nextNode = nodeSta;

        for (int i = 1; i < 10; i++) {
            ListNode node = new ListNode(i);
            nextNode.next = node;
            nextNode = nextNode.next;
        }
        nextNode = nodeSta;
        print(nextNode);
        Log.e("TAG", "下面是删除节点后的打印输出：\n");

        //删除节点
        while (nextNode != null) {
            if (nextNode.val == 5) {
                ListNode listNode = nextNode.next.next;
                nextNode.next.next = null;
                nextNode.next = listNode;
            }
            nextNode = nextNode.next;
        }
        nextNode = nodeSta;
        print(nextNode);
    }

    /**
     * 替换节点
     */
    private void listNodeReplace() {
        ListNode nodeSta = new ListNode(0);
        ListNode nextNode;
        nextNode = nodeSta;

        for (int i = 1; i < 10; i++) {
            ListNode listNode = new ListNode(i);
            nextNode.next = listNode;
            nextNode = nextNode.next;
            System.out.println();
        }

        nextNode = nodeSta;
        print(nextNode);
        Log.e("TAG", "下面是替换节点后的打印输出：\n");

        //替换节点
        while (nextNode != null) {
            if (nextNode.val == 4) {
                ListNode newNode = new ListNode(99);
                ListNode node = nextNode.next.next;//先保存要替换节点的下一个节点
                nextNode.next.next = null;//被替换节点 指向为空 ，等待java垃圾回收
                nextNode.next = newNode;//插入新节点
                newNode.next = node;//新节点的下一个节点指向 之前保存的节点
            }
            nextNode = nextNode.next;
        }//循环完成之后 nextNode指向最后一个节点
        nextNode = nodeSta;
        print(nextNode);
    }

    /**
     * 插入节点
     */
    private void listNodeInsert() {
        ListNode nodeSta = new ListNode(0);//创建首节点
        ListNode nextNode;//声明一个变量用来在移动过程中指向当前节点
        nextNode = nodeSta;//指向首节点

        //创建链表
        for (int i = 1; i < 10; i++) {
            ListNode listNode = new ListNode(i);//生成新的节点
            nextNode.next = listNode;//把心节点连起来
            nextNode = nextNode.next;//当前节点往后移动
        }//当for循环完成之后 nextNode指向最后一个节点，

        nextNode = nodeSta;//重新赋值让它指向首节点
        print(nextNode);

        Log.e("TAG", "下面是插入节点后的打印输出：\n");

        //插入节点
        while (nextNode != null) {
            if (nextNode.val == 5) {
                ListNode newListNode = new ListNode(99);//生成新的节点
                ListNode next = nextNode.next;//先保存下一个节点
                nextNode.next = newListNode;//插入新节点
                newListNode.next = next;//新节点的下一个节点指向 之前保存的节点
            }
            nextNode = nextNode.next;
        }//循环完成之后 nextNode指向最后一个节点
        nextNode = nodeSta;//重新赋值让它指向首节点
        print(nextNode);
    }

    /**
     * 创建链表及遍历链表
     */
    private void listNodeCreateAndLoop() {
        ListNode nodeSta = new ListNode(0);//创建首节点
        ListNode nextNode;//声明一个变量用来在移动过程中指向当前节点
        nextNode = nodeSta;//指向首节点

        //创建链表
        for (int i = 1; i < 10; i++) {
            ListNode listNode = new ListNode(i);//生成新的节点
            nextNode.next = listNode;//把新节点连起来
            nextNode = nextNode.next;//当前节点往后移动
        }//当for循环完成之后 nextNode指向最后一个节点

        nextNode = nodeSta;//重新赋值让它指向首节点
        print(nextNode);//打印输出
    }

    /**
     * java ListNode 链表
     * https://www.cnblogs.com/easyidea/p/13371863.html
     */
    //类名 ：Java类就是一种自定义的数据结构
    static class ListNode {
        int val;//数据 ：节点数据
        ListNode next;//对象 ：引用下一个节点对象。在Java中没有指针的概念，Java中的引用和C语言的指针类似

        //构造方法 ：构造方法和类名相同
        ListNode(int val) {
            this.val = val;//把接收的参数赋值给当前类的val变量
        }
    }

    //范型写法：使用范型可以兼容不同的数据类型
    static class ListNodeX<E> {
        E val;
        ListNodeX<E> next;

        ListNodeX(E val) {
            this.val = val;
        }
    }

    void print(ListNode listNode) {
        //创建链表节点
        while (listNode != null) {
//            System.out.println("节点：" + listNode.val);
            Log.e("TAG", "print: 节点：" + listNode.val);
            listNode = listNode.next;
        }
        System.out.println();
    }
}
