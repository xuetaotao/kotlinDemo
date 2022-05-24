package com.jlpay.kotlindemo.study_java;

import android.os.Bundle;
import android.util.LruCache;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * TODO
 */
public class LruCacheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LruCacheDemo1();
    }

    private void LruCacheDemo1() {
        LruCache<String, String> pathLruCache = new LruCache<>(100);
    }


}
