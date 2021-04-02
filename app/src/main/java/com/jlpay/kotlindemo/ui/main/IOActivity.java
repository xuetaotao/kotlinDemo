package com.jlpay.kotlindemo.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.ui.base.Constants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 该部分文件相关操作都在APP私有目录下进行，因此不需要进行文件存储权限访问
 */
public class IOActivity extends AppCompatActivity {

    private final String TAG = IOActivity.class.getSimpleName();

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, IOActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);

//        io1();
        io2();
    }


    /**
     * OutputStream
     */
    private void io1() {
        OutputStream outputStream = null;
        try {
//            File file = new File(Constants.FILE_SAVE_DIR + "text.txt");
            outputStream = new FileOutputStream(Constants.FILE_SAVE_DIR + "text1.txt");
            outputStream.write('a');
            outputStream.write('b');
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //放在try里面的流会自动关闭，等效上面的io1()
    private void io1test() {
        try (OutputStream outputStream = new FileOutputStream(Constants.FILE_SAVE_DIR + "text1.txt")) {
            outputStream.write('a');
            outputStream.write('b');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * InputStream
     */
    private void io2() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Constants.FILE_SAVE_DIR + "text1.txt");
            int read = inputStream.read();
            int read2 = inputStream.read();
            Log.e(TAG, (char) read + "");
            Log.e(TAG, read2 + "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
