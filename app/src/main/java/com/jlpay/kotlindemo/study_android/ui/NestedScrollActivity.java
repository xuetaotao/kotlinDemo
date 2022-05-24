package com.jlpay.kotlindemo.study_android.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.widget.NestedScrollView;

import com.jlpay.kotlindemo.R;

/**
 * 嵌套滑动
 * NestedScrollView
 */
public class NestedScrollActivity extends AppCompatActivity {

    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll);

        nestedScrollView = findViewById(R.id.nestedScrollView);
    }

    private void relatedClass() {
        //实现了这个接口的类，嵌套滑动中可以作为父容器
        NestedScrollingParent nsp;
        //实现了这个接口的类，嵌套滑动中可以作为子View，如RecyclerView
        NestedScrollingChild nsc;
        //实现了NestedScrollingParent 与 NestedScrollingChild，嵌套滑动中既可以作为父容器，又可以作为子View
        NestedScrollView nsv = new NestedScrollView(this);
        NestedScrollingParentHelper nsph = new NestedScrollingParentHelper(nestedScrollView);
        NestedScrollingChildHelper nsch = new NestedScrollingChildHelper(nestedScrollView);
    }
}
