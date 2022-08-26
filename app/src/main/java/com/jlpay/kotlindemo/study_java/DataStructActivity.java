package com.jlpay.kotlindemo.study_java;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

public class DataStructActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_struct);
    }

    public void dataStructDemo(View view) {
        sparseArrayDemo();
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
}
