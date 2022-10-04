package com.jlpay.kotlindemo.study_java;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DataStructActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_struct);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dataStructDemo(View view) {
//        sparseArrayDemo();
//        priorityQueueDemo();
        priorityQueueDemo2();
//        Toast.makeText(this, "dataStructDemo", Toast.LENGTH_SHORT).show();
    }

    /**
     * SparseArray
     * 本质：两个数组，一个放key，一个放value
     * 注：dataBinding自动生成的类里也有用到这个
     */
    public void sparseArrayDemo() {
        SparseArray<String> sKeys = new SparseArray<>(8);
        sKeys.put(0, "_all");
        sKeys.put(1, "click");
        sKeys.put(2, "imageBean");
        sKeys.put(3, "jetpackDataBindingActivity");
        sKeys.put(4, "onClick");
        sKeys.put(5, "presenter");
        sKeys.put(6, "viewModel");
        sKeys.put(7, "vm");
        String aa = sKeys.get(6);
        Toast.makeText(this, aa, Toast.LENGTH_SHORT).show();
    }

    /**
     * 小根堆，在 java 中就是优先级队列
     * 优先级队列结构，底层就是堆结构。
     */
    public void priorityQueueDemo() {
        //不传比较器默认是小根堆
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        heap.add(8);
        heap.add(4);
        heap.add(5);
        heap.add(9);
        heap.add(10);
        heap.add(3);
        while (!heap.isEmpty()) {
            //依次弹出的话，是按照较小值先弹出，从小到大输出
            Log.e("TAG", "priorityQueueDemo: " + heap.poll());
        }
    }

    /**
     * 大根堆
     * 如果优先级队列里传入了自定义的比较器，就可以变成大根堆
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void priorityQueueDemo2() {
        PriorityQueue<Integer> heap = new PriorityQueue<>(new BigHeap());
        heap.add(8);
        heap.add(4);
        heap.add(5);
        heap.add(9);
        heap.add(10);
        heap.add(3);
        while (!heap.isEmpty()) {
            //依次弹出的话，是按照较大值先弹出，从大到小输出
            Log.e("TAG", "priorityQueueDemo2: " + heap.poll());
        }
    }

    public static class BigHeap implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            // 返回负数的时候，第一个参数排在上面
            // 返回正数的时候，第二个参数排在上面
            // 返回0的时候，谁在上面无所谓
            return o2 - o1;
        }
    }
}
