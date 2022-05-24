package com.jlpay.kotlindemo.study_android.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.utils.NotificationUtils;

/**
 * 通知 Notification 的使用
 */
public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    public void sendNotification(View view) {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationManager notificationManager2 = notificationUtils.getNotificationManager(this);
        Notification notification = notificationUtils.getNotification(notificationManager2
                , notificationUtils.getCHANNEL_ID(), notificationUtils.getCHANNEL_NAME());
        notificationManager2.notify(notificationUtils.getNOTIFICATION_ID(), notification);
    }
}
