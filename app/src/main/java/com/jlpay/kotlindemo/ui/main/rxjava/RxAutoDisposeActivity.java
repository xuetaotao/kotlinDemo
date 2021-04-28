package com.jlpay.kotlindemo.ui.main.rxjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

public class RxAutoDisposeActivity extends AppCompatActivity {

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, RxAutoDisposeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_auto_dispose);
    }
}
