package com.jlpay.kotlindemo.study_java;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Java代码执行命令行
 */
public class CommandLineActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_line);
    }

    public void commandClick(View view) {
        command1();
    }


    private void command1() {
        try {
            //相当于启动cmd，然后在命令行里输入这个命令
            //TODO 未完成，这里执行有问题，可能是没有把命令加入环境变量的原因，有空查查
            //未配置环境变量的命令可以通过java方法拿到 local.properties 文件下的sdk路径
            Process process = Runtime.getRuntime().exec("java -version");
            process.waitFor();
            //获取输出
            InputStream inputStream = process.getErrorStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            String output = new String(byteArrayOutputStream.toByteArray());
            System.out.println(output);
            Log.e(TAG, "command1: " + output);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
