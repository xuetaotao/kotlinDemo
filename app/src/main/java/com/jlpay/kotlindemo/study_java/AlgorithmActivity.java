package com.jlpay.kotlindemo.study_java;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
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
     * BM16 删除有序链表中重复的元素-II
     */
    public static ListNode bm16(ListNode head) {
        //空链表
        if (head == null) {
            return null;
        }
        ListNode res = new ListNode(0);
        //在链表前加一个表头
        res.next = head;
        ListNode cur = res;
        while (cur.next != null && cur.next.next != null) {
            //遇到相邻两个节点值相同
            if (cur.next.val == cur.next.next.val) {
                int temp = cur.next.val;
                //将所有相同的都跳过
                while (cur.next != null && cur.next.val == temp) {
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }
        //返回时去掉表头
        return res.next;
    }

    //哈希表（扩展思路）
    public static ListNode bm16Two(ListNode head) {
        //空链表
        if (head == null) {
            return null;
        }
        Map<Integer, Integer> map = new HashMap<>();
        ListNode cur = head;
        //遍历链表统计每个节点值出现的次数
        while (cur != null) {
            if (map.containsKey(cur.val)) {
                map.put(cur.val, (int) map.get(cur.val) + 1);
            } else {
                map.put(cur.val, 1);
            }
            cur = cur.next;
        }
        ListNode res = new ListNode(0);
        //在链表前加一个表头
        res.next = head;
        cur = res;
        //再次遍历链表
        while (cur.next != null) {
            //如果节点值计数不为1
            if (map.get(cur.next.val) != 1) {
                //删去该节点
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        //去掉表头
        return res.next;
    }

    /**
     * BM15 删除有序链表中重复的元素-I
     * 方法：遍历删除（推荐使用）
     */
    public static ListNode bm15(ListNode head) {
        //空链表
        if (head == null) {
            return null;
        }
        //遍历指针
        ListNode cur = head;
        //指针当前和下一位不为空
        while (cur != null && cur.next != null) {
            //如果当前与下一位相等则忽略下一位
            if (cur.val == cur.next.val) {
                cur.next = cur.next.next;
            } else {
                //否则指针正常遍历
                cur = cur.next;
            }
        }
        return head;
    }

    /**
     * BM14 链表的奇偶重排
     * 双指针（推荐使用）
     */
    public static ListNode bm14(ListNode head) {
        //如果链表为空，不用重排
        if (head == null) {
            return head;
        }
        //even开头指向第二个节点(偶数节点)，可能为空
        ListNode even = head.next;
        //odd开头指向第一个节点
        ListNode odd = head;
        //指向even开头
        ListNode evenHead = even;
        while (even != null && even.next != null) {
            //odd连接even的后一个，即奇数位
            odd.next = even.next;
            //odd进入后一个奇数位
            odd = odd.next;
            //even连接后一个奇数的后一位，即偶数位
            even.next = odd.next;
            //even进入后一个偶数位
            even = even.next;
        }
        //even整体接在odd后面
        odd.next = evenHead;
        return head;
    }


    /**
     * BM13 判断一个链表是否为回文结构
     * 反转后半部分链表
     */
    public static boolean bm13(ListNode head) {
        ListNode fast = head, slow = head;
        //通过快慢指针找到中点
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        //如果fast不为空，说明链表的长度是奇数个
        if (fast != null) {
            slow = slow.next;
        }
        //反转后半部分链表
        slow = reverse13(slow);
        fast = head;
        while (slow != null) {
            //然后比较，判断节点值是否相等
            if (fast.val != slow.val) {
                return false;
            }
            fast = fast.next;
            slow = slow.next;
        }
        return true;
    }

    //反转链表
    public static ListNode reverse13(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }

    //使用栈解决
    //栈是先进后出的一种数据结构，这里还可以使用栈先把链表的节点全部存放到栈中，
    // 然后再一个个出栈，这样就相当于链表从后往前访问了
    public static boolean bm13Two(ListNode head) {
        if (head == null) {
            return true;
        }
        ListNode temp = head;
        Stack<Integer> stack = new Stack<>();
        //链表的长度
        int len = 0;
        //把链表节点的值存放到栈中
        while (temp != null) {
            stack.push(temp.val);
            temp = temp.next;
            len++;
        }
        //len长度除以2
        len >>= 1;
        //然后再出栈
        for (int i = len; i >= 0; i--) {
            if (head.val != stack.pop()) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * BM12 单链表的排序
     * 方法一：归并排序（推荐使用）
     */
    public static ListNode bm12(ListNode head) {
        //链表为空或者只有一个元素，直接就是有序的
        if (head == null || head.next == null) {
            return head;
        }
        ListNode left = head;
        ListNode mid = head.next;
        ListNode right = head.next.next;
        //右边的指针到达末尾时，中间的指针指向该段链表的中间
        while (right != null && right.next != null) {
            left = left.next;
            mid = mid.next;
            right = right.next.next;
        }
        //左边指针指向左段的左右一个节点，从这里断开
        left.next = null;
        //分成两段排序，合并排好序的两段
        return merge12(bm12(head), bm12(mid));
    }

    //合并两段有序链表
    public static ListNode merge12(ListNode head1, ListNode head2) {
        //一个已经为空了，直接返回另一个
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        //加一个表头
        ListNode head = new ListNode(0);
        ListNode cur = head;
        //两个链表都要不为空
        while (head1 != null && head2 != null) {
            //取较小值的节点
            if (head1.val <= head2.val) {
                cur.next = head1;
                //只移动取值的指针
                head1 = head1.next;

            } else {
                cur.next = head2;
                //只移动取值的指针
                head2 = head2.next;
            }
            //指针后移
            cur = cur.next;
        }
        //哪个链表还有剩，直接连在后面
        if (head1 != null) {
            cur.next = head1;
        } else {
            cur.next = head2;
        }
        //返回值去掉表头
        return head.next;
    }

    //方法二：转化为数组排序（扩展思路）
    public static ListNode bm12Two(ListNode head) {
        ArrayList<Integer> numList = new ArrayList<>();
        ListNode p = head;
        //遍历链表，将节点值加入数组
        while (p != null) {
            numList.add(p.val);
            p = p.next;
        }
        p = head;
        //对数组元素排序
        Collections.sort(numList);
        //遍历数组
        for (int i = 0; i < numList.size(); i++) {
            //将数组元素依次加入链表
            p.val = numList.get(i);
            p = p.next;
        }
        return head;
    }


    /**
     * BM11 链表相加
     * 链表的问题大多借助栈和队列的结构思想
     * 申请两个栈空间和一个标记位，然后将两个栈中内容依次相加，将结果倒插入新节点中。
     */
    public static ListNode bm11(ListNode head1, ListNode head2) {
        LinkedList<Integer> list1 = new LinkedList<>();
        LinkedList<Integer> list2 = new LinkedList<>();
        putData11(list1, head1);//入栈
        putData11(list2, head2);
        ListNode newNode = null;
        ListNode head = null;
        int carry = 0;//标记进位
        while (!list1.isEmpty() || !list2.isEmpty() || carry != 0) {
            int x = (list1.isEmpty()) ? 0 : list1.pop();//依次从栈中取出
            int y = (list2.isEmpty()) ? 0 : list2.poll();
            int sum = x + y + carry;//与进位一起相加
            carry = sum / 10;//更新进位
            //将计算值放入节点
            newNode = new ListNode(sum % 10);
            //更新下一个节点的指向
            newNode.next = head;
            head = newNode;
        }
        return head;
    }

    public static void putData11(LinkedList<Integer> s1, ListNode head1) {
        if (s1 == null) {
            s1 = new LinkedList<>();
        }
        //遍历节点将其插入栈中
        while (head1 != null) {
            s1.push(head1.val);
            head1 = head1.next;
        }
    }


    /**
     * BM10 两个链表的第一个公共结点
     * 一个朴素的解法自然是两层枚举，逐个检查哪个节点相同。
     */
    public static ListNode bm10(ListNode head1, ListNode head2) {
        for (ListNode h1 = head1; h1 != null; h1 = h1.next) {
            for (ListNode h2 = head2; h2 != null; h2 = h2.next) {
                if (h1 == h2) {
                    return h1;
                }
            }
        }
        return null;
    }

    //栈解法，这是一种「从后往前」找的方式
    //将两条链表分别压入两个栈中，然后循环比较两个栈的栈顶元素，同时记录上一位栈顶元素。
    //当遇到第一个不同的节点时，结束循环，上一位栈顶元素即是答案。
    public static ListNode bm10Two(ListNode head1, ListNode head2) {
        Deque<ListNode> deque1 = new ArrayDeque<>();
        Deque<ListNode> deque2 = new ArrayDeque<>();
        while (head1 != null) {
            deque1.add(head1);
            head1 = head1.next;
        }
        while (head2 != null) {
            deque2.add(head2);
            head2 = head2.next;
        }
        ListNode ans = null;
        //peekLast：查看尾部元素
        //pollLast：从尾部出队
        while (!deque1.isEmpty() && !deque2.isEmpty() &&
                deque1.peekLast() == deque2.peekLast()) {
            ans = deque1.pollLast();
            deque2.pollLast();
        }
        return ans;
    }

    //Set 解法：这是一种「从前往后」找的方式。
    //使用 Set 数据结构，先对某一条链表进行遍历，同时记录下来所有的节点。
    //然后在对第二链条进行遍历时，检查当前节点是否在 Set 中出现过，第一个在 Set 出现过的节点即是交点。
    public static ListNode bm10Three(ListNode head1, ListNode head2) {
        Set<ListNode> set = new HashSet<>();
        while (head1 != null) {
            set.add(head1);
            head1 = head1.next;
        }
        while (head2 != null && !set.contains(head2)) {
            head2 = head2.next;
        }
        return head2;
    }

    //差值法
    //由于两条链表在相交节点后面的部分完全相同，因此我们可以先对两条链表进行遍历，分别得到两条链表的长度，
    // 并计算差值 d。
    // 让长度较长的链表先走 d 步，然后两条链表同时走，第一个相同的节点即是节点。
    public static ListNode bm10Four(ListNode head1, ListNode head2) {
        int c1 = 0, c2 = 0;
        ListNode ta = head1, tb = head2;
        while (ta != null && c1++ >= 0) {
            ta = ta.next;
        }
        while (tb != null && c2++ >= 0) {
            tb = tb.next;
        }
        int d = c1 - c2;
        if (d > 0) {
            while (d-- > 0) {
                head1 = head1.next;
            }

        } else if (d < 0) {
            d = -d;
            while (d-- > 0) {
                head2 = head2.next;
            }
        }
        while (head1 != head2) {
            head1 = head1.next;
            head2 = head2.next;
        }
        return head1;
    }

    /**
     * BM9 删除链表的倒数第n个节点
     * 方法一：双指针（推荐使用）
     * 具体做法：
     * step 1：给链表添加一个表头，处理删掉第一个元素时比较方便。
     * step 2：准备一个快指针，在链表上先走nnn步。
     * step 3：准备慢指针指向原始链表头，代表当前元素，前序节点指向添加的表头，这样两个指针之间相距就是一直都是nnn。
     * step 4：快慢指针同步移动，当快指针到达链表尾部的时候，慢指针正好到了倒数nnn个元素的位置。
     * step 5：最后将该节点前序节点的指针指向该节点后一个节点，删掉这个节点。
     */
    public static ListNode bm9(ListNode head, int n) {
        //添加表头
        ListNode res = new ListNode(-1);
        res.next = head;
        //当前节点
        ListNode cur = head;
        //前序节点
        ListNode pre = res;
        ListNode fast = head;
        //快指针先行n步
        while (n != 0) {
            fast = fast.next;
            n--;
        }
        //快慢指针同步，快指针到达末尾，慢指针就到了倒数第n个位置
        while (fast != null) {
            fast = fast.next;
            pre = cur;
            cur = cur.next;
        }
        //删除该位置的节点
        pre.next = cur.next;
        //返回去掉头
        return res.next;
    }


    /**
     * 方法二：长度统计法（思路扩展）
     * 具体做法：
     * step 1：给链表添加一个表头，处理删掉第一个元素时比较方便。
     * step 2：遍历整个链表，统计链表长度。
     * step 3：准备两个指针，一个指向原始链表头，表示当前节点，一个指向我们添加的表头，表示前序节点。两个指针同步遍历L−nL-nL−n次找到倒数第nnn个位置。
     * step 4：前序指针的next指向当前节点的next，代表越过当前节点连接，即删去该节点。
     * step 5：返回去掉添加的表头。
     */
    public static ListNode bm9Two(ListNode head, int n) {
        //记录链表长度
        int length = 0;
        //添加表头
        ListNode res = new ListNode(-1);
        res.next = head;
        //当前节点
        ListNode cur = head;
        //前序节点
        ListNode pre = res;
        //找到链表长度
        while (cur != null) {
            cur = cur.next;
            length++;
        }
        //回到头部
        cur = head;
        //从头遍历找到倒数第n个位置
        for (int i = 0; i < length - n; i++) {
            pre = cur;
            cur = cur.next;
        }
        //删去倒数第n个节点
        pre.next = cur.next;
        //返回去掉头节点
        return res.next;
    }

    /**
     * BM8 链表中倒数最后k个结点
     * 方法一：快慢双指针（推荐使用）
     * 具体做法：
     * step 1：准备一个快指针，从链表头开始，在链表上先走k步。
     * step 2：准备慢指针指向原始链表头，代表当前元素，则慢指针与快指针之间的距离一直都是k。
     * step 3：快慢指针同步移动，当快指针到达链表尾部的时候，慢指针正好到了倒数k个元素的位置。
     */
    public static ListNode bm8(ListNode head, int k) {
        int n = 0;
        ListNode fast = head;
        ListNode slow = head;
        //快指针先行k步
        for (int i = 0; i < k; i++) {
            if (fast != null) {
                fast = fast.next;
            } else {
                //达不到k步说明链表过短，没有倒数k
                slow = null;
                return slow;
            }
        }
        //快慢指针同步，快指针先到底，慢指针指向倒数第k个
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    //先找长度再找最后k（扩展思路）
    //思路：链表不能逆向遍历，也不能直接访问。但是对于倒数第kkk个位置，
    // 我们只需要知道是正数多少位还是可以直接遍历得到的。
//    具体做法：
//    step 1：可以先遍历一次链表找到链表的长度。
//    step 2：然后比较链表长度是否比k小，如果比k小返回一个空节点。
//    step 3：如果链表足够长，则我们从头节点往后遍历n−k次即可找到所求。
    public static ListNode bm8Two(ListNode head, int k) {
        int n = 0;
        ListNode p = head;
        //遍历链表，统计链表长度
        while (p != null) {
            n++;
            p = p.next;
        }
        //长度过小，返回空链表
        if (n < k) {
            return null;
        }
        p = head;
        //遍历n-k次
        for (int i = 0; i < n - k; i++) {
            p = p.next;
        }
        return p;
    }

    /**
     * BM7 链表中环的入口结点
     * 方法：双指针（推荐使用）
     * 具体做法：
     * step 1：使用BM6中的方法判断链表是否有环，并找到相遇的节点。
     * step 2：慢指针继续在相遇节点，快指针回到链表头，两个指针同步逐个元素逐个元素开始遍历链表。
     * step 3：再次相遇的地方就是环的入口。
     */
    public static ListNode bm7(ListNode head) {
        ListNode slow = hasCycle7(head);
        //没有环
        if (slow == null) {
            return null;
        }
        //快指针回到表头
        ListNode fast = head;
        //再次相遇即是环入口
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    //判断有没有环，返回相遇的地方
    public static ListNode hasCycle7(ListNode head) {
        //先判断链表为空的情况
        if (head == null) {
            return null;
        }
        //快慢双指针
        ListNode fast = head;
        ListNode slow = head;
        //如果没环快指针会先到链表尾
        while (fast != null && fast.next != null) {
            //快指针移动两步
            fast = fast.next.next;
            //慢指针移动一步
            slow = slow.next;
            //相遇则有环，返回相遇的位置
            if (slow == fast) {
                return slow;
            }
        }
        //到末尾说明没有环，返回null
        return null;
    }


    /**
     * BM6 判断链表中是否有环
     * 方法：双指针（推荐使用）
     * 双指针指的是在遍历对象的过程中，不是普通的使用单个指针进行访问，而是使用两个指针（特殊情况甚至可以多个），
     * 两个指针或是同方向访问两个链表、或是同方向访问一个链表（快慢指针）、或是相反方向扫描（对撞指针），
     * 从而达到我们需要的目的。
     * <p>
     * 具体做法：
     * step 1：设置快慢两个指针，初始都指向链表头。
     * step 2：遍历链表，快指针每次走两步，慢指针每次走一步。
     * step 3：如果快指针到了链表末尾，说明没有环，因为它每次走两步，所以要验证连续两步是否为NULL。
     * step 4：如果链表有环，那快慢双指针会在环内循环，因为快指针每次走两步，因此快指针会在环内追到慢指针，
     * 二者相遇就代表有环。
     */
    public static boolean bm6(ListNode head) {
        //先判断链表为空的情况
        if (head == null) {
            return false;
        }
        //快慢双指针
        ListNode fast = head;
        ListNode slow = head;
        //如果没环快指针会先到链表尾
        while (fast != null && fast.next != null) {
            //快指针移动两步
            fast = fast.next.next;
            //慢指针移动一步
            slow = slow.next;
            //相遇则有环
            if (fast == slow) {
                return true;
            }
        }
        //到末尾则没有环
        return false;
    }

    /**
     * BM5 合并k个已排序的链表
     * 方法一：归并排序思想（推荐使用）
     */
    public static ListNode bm5(ArrayList<ListNode> lists) {
        //k个链表归并排序
        return divideMerge5(lists, 0, lists.size() - 1);
    }

    //划分合并区间函数
    public static ListNode divideMerge5(ArrayList<ListNode> lists, int left, int right) {
        if (left > right) {
            return null;
        } else if (left == right) {
            //中间一个的情况
            return lists.get(left);
        }
        //从中间分成两段，再将合并好的两段合并
        int mid = (left + right) / 2;
        return merge5(divideMerge5(lists, left, mid), divideMerge5(lists, mid + 1, right));
    }

    //两个链表合并函数
    public static ListNode merge5(ListNode list1, ListNode list2) {
        //一个已经为空了，直接返回另一个
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        //加一个表头
        ListNode head = new ListNode(0);
        ListNode cur = head;
        //两个链表都要不为空
        while (list1 != null && list2 != null) {
            //取较小值的节点
            if (list1.val <= list2.val) {
                cur.next = list1;
                //只移动取值的指针
                list1 = list1.next;
            } else {
                cur.next = list2;
                //只移动取值的指针
                list2 = list2.next;
            }
            //指针后移
            cur = cur.next;
        }
        //哪个链表还有剩，直接连在后面
        if (list1 != null) {
            cur.next = list1;
        } else {
            cur.next = list2;
        }
        //返回值去掉表头
        return head.next;
    }

    //方法二：优先队列（扩展思路）
    //优先级队列结构，底层就是堆结构。不传比较器默认是小根堆，依次弹出的话，是按照较小值先弹出，从小到大输出。
    //优先队列即PriorityQueue，是一种内置的机遇堆排序的容器，分为大顶堆与小顶堆，大顶堆的堆顶为最大元素，
    // 其余更小的元素在堆下方，小顶堆与其刚好相反。且因为容器内部的次序基于堆排序，因此每次插入元素时间
    // 复杂度都是O(log2n)，而每次取出堆顶元素都是直接取出。
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ListNode bm5Two(ArrayList<ListNode> lists) {
        //小顶堆
        Queue<ListNode> queue = new PriorityQueue<>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val - o2.val;//负数, o1 o2，升序
            }
        });
        //遍历所有链表第一个元素
        for (int i = 0; i < lists.size(); i++) {
            //不为空则加入小顶堆
            if (lists.get(i) != null) {
                queue.add(lists.get(i));
            }
        }
        //加一个表头
        ListNode res = new ListNode(-1);
        ListNode head = res;
        //直到小顶堆为空
        while (!queue.isEmpty()) {
            //取出最小的元素
            ListNode temp = queue.poll();
            //连接
            head.next = temp;
            head = head.next;
            //每次取出链表的后一个元素加入小顶堆
            if (temp.next != null) {
                queue.add(temp.next);
            }
        }
        //去掉表头
        return res.next;
    }


    /**
     * BM4 合并两个排序的链表
     * 算法思想一：迭代，类似于归并排序法（答案的代码好像不是Java，略过）
     * 算法思想二：递归
     */
    public static ListNode bm4(ListNode list1, ListNode list2) {
        // list1 list2为空的情况
        if (list1 == null || list2 == null) {
            return list1 != null ? list1 : list2;
        }
        // 两个链表元素依次对比
        if (list1.val <= list2.val) {
            // 递归计算 list1.next, list2
            list1.next = bm4(list1.next, list2);
            return list1;

        } else {
            // 递归计算 list1, list2.next
//            list2.next = bm4(list1, list2.next);//与下面这句都可以
            list2.next = bm4(list2.next, list1);
            return list2;
        }
    }

    //算法思路三：借助额外数组
    public static ListNode bm4Two(ListNode list1, ListNode list2) {
        // list1 list2为空的情况
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        //将两个链表存放在list中
        ArrayList<Integer> list = new ArrayList<>();
        // 遍历存储list1
        while (list1 != null) {
            list.add(list1.val);
            list1 = list1.next;
        }
        // 遍历存储list2
        while (list2 != null) {
            list.add(list2.val);
            list2 = list2.next;
        }
        // 对 list 排序
        Collections.sort(list);
        // 将list转换为 链表
        ListNode newHead = new ListNode(list.get(0));
        ListNode cur = newHead;
        for (int i = 1; i < list.size(); i++) {
            cur.next = new ListNode(list.get(i));
            cur = cur.next;
        }
        // 输出合并链表
        return newHead;
    }


    /**
     * BM3 链表中的节点每k个一组翻转
     * 递归的三段式模版：
     * 终止条件： 当进行到最后一个分组，即不足k次遍历到链表尾（0次也算），就将剩余的部分直接返回。
     * 返回值： 每一级要返回的就是翻转后的这一分组的头，以及连接好它后面所有翻转好的分组链表。
     * 本级任务： 对于每个子问题，先遍历k次，找到该组结尾在哪里，然后从这一组开头遍历到结尾，依次翻转，结尾就可以作为下一个分组的开头，而先前指向开头的元素已经跑到了这一分组的最后，可以用它来连接它后面的子问题，即后面分组的头。
     */
    public static ListNode bm3(ListNode head, int k) {
        //找到每次翻转的尾部
        ListNode tail = head;
        //遍历k次到尾部
        for (int i = 0; i < k; i++) {
            //如果不足k到了链表尾，直接返回，不翻转
            if (tail == null) {
                return head;
            }
            tail = tail.next;
        }
        //翻转时需要的前序和当前节点
        ListNode pre = null;
        ListNode cur = head;
        //在到达当前段尾节点前
        while (cur != tail) {
            //翻转
            ListNode temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        //当前尾指向下一段要翻转的链表
        head.next = bm3(tail, k);
        return pre;
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
