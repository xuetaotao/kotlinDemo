package com.jlpay.kotlindemo.java_study;

public class JavaListNode {

    /**
     * JZ6 从尾到头打印链表
     */
//    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
//
//    }

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

    //创建链表及遍历链表
    static class Test {

        public static void main(String[] args) {
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

        static void print(ListNode listNode) {
            //创建链表节点
            while (listNode != null) {
                System.out.println("节点：" + listNode.val);
//                Log.e("TAG", "print: 节点：" + listNode.val);
                listNode = listNode.next;
            }
            System.out.println();
        }
    }
}
