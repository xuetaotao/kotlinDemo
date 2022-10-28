package com.jlpay.kotlindemo.study_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.util.Stack;

/**
 * 牛客网算法100题：
 * https://www.nowcoder.com/exam/oj
 */
public class AlgorithmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm);
    }

    public void algorDemo(View view) {
        Toast.makeText(this, "牛客网算法100题", Toast.LENGTH_SHORT).show();
    }


    /**
     * BM2 链表内指定区间反转
     * 解法一：双指针(两次遍历)
     * 说明：方便理解，以下注释中将用left，right分别代替m,n节点
     */
    public static ListNode bm2(ListNode head, int m, int n) {
        //设置虚拟头节点
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;
        ListNode pre = dummyNode;
        //1.走left-1步到left的前一个节点
        for (int i = 0; i < m - 1; i++) {
            pre = pre.next;
        }
        //2.走right-left+1步到right节点
        ListNode rightNode = pre;
        for (int i = 0; i < n - m + 1; i++) {
            rightNode = rightNode.next;
        }
        //3.截取出一个子链表
        ListNode leftNode = pre.next;
        ListNode cur = rightNode.next;
        //4.切断链接
        pre.next = null;
        rightNode.next = null;
        //5.反转局部链表
        reverseLinkedList2(leftNode);
        //6.接回原来的链表
        pre.next = rightNode;
        leftNode.next = cur;
        return dummyNode.next;
    }

    //反转局部链表
    public static void reverseLinkedList2(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            //Cur_next 指向cur节点的下一个节点
            ListNode Cur_next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = Cur_next;
        }
    }

    //一次遍历（对解法一的优化）
    public static ListNode bm2Two(ListNode head, int m, int n) {
        //设置虚拟头节点
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;
        ListNode pre = dummyNode;
        for (int i = 0; i < m - 1; i++) {
            pre = pre.next;
        }
        ListNode cur = pre.next;
        ListNode Cur_next;
        for (int i = 0; i < n - m; i++) {
            Cur_next = cur.next;
            cur.next = Cur_next.next;
            Cur_next.next = pre.next;
            pre.next = Cur_next;
        }
        return dummyNode.next;
    }

    /**
     * BM1 反转链表
     * 1，使用栈解决。栈是先进后出的。实现原理就是把链表节点一个个入栈，当全部入栈完之后再一个个出栈，
     * 出栈的时候在把出栈的结点串成一个新的链表。
     */
    public static ListNode bm1(ListNode head) {
        Stack<ListNode> stack = new Stack<>();
        //把链表节点全部摘掉放到栈中
        while (head != null) {
            stack.push(head);//入栈
            head = head.next;
        }
        if (stack.isEmpty()) {
            return null;
        }
        ListNode node = stack.pop();//出栈
        ListNode dummy = node;
        //栈中的结点全部出栈，然后重新连成一个新的链表
        while (!stack.isEmpty()) {
            ListNode tempNode = stack.pop();
            node.next = tempNode;
            node = node.next;
        }
        //最后一个结点就是反转前的头结点，一定要让他的next
        //等于空，否则会构成环
        node.next = null;
        return dummy;
    }

    //双链表求解，把原链表的结点一个个摘掉，每次摘掉的链表都让他成为新的链表的头结点，然后更新新链表。
    //他每次访问的原链表节点都会成为新链表的头结点
    public static ListNode bm1Two(ListNode head) {
        //新链表
        ListNode newHead = null;
        while (head != null) {
            //先保存访问的节点的下一个节点，保存起来
            //留着下一步访问的
            ListNode temp = head.next;
            //每次访问的原链表节点都会成为新链表的头结点，
            //其实就是把新链表挂到访问的原链表节点的
            //后面就行了
            head.next = newHead;
            //更新新链表
            newHead = head;
            //重新赋值，继续访问
            head = temp;
        }
        //返回新链表
        return newHead;
    }

    public static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
