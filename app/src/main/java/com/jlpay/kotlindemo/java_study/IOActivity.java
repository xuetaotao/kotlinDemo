package com.jlpay.kotlindemo.java_study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.base.Constants;
import com.jlpay.kotlindemo.utils.DataUtils;
import com.jlpay.lib_annotations.MyBindView;
import com.jlpay.lib_mybinding.MyBinding;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * IO操作
 * 该部分文件相关操作都在APP私有目录下进行，因此不需要进行文件存储权限访问
 */
public class IOActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    //    @MyBindView(id = R.id.textView2, name = "rengwuxian")
//    TextView textView5;
//    @MyBindView(id = R.id.textView2, name = "rengwuxian", age = "haha")//有默认值时可以不填
//    TextView textView3;
    @MyBindView(R.id.textView2)
    TextView textView2;
    @MyBindView(R.id.linearLayout)
    LinearLayout linearLayout;

    private final String TAG = IOActivity.class.getSimpleName();
    private Unbinder unbinder;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, IOActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);

        unbinder = ButterKnife.bind(this);
        MyBinding.bind(this);
//        MyInnerBinding.bind(this);
        //TODO MyBinding有点问题，没有生成 IOActivityBinding 的文件
//        textView2.setText("Rengwuxian");
//        linearLayout.setBackgroundColor(Color.GRAY);

//        io1();
//        io2();
//        io3();
//        io4();
//        io5();
//        io6();
        io7();

        testDataUtils();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void testDataUtils() {
        double aa = Double.parseDouble("11");
        Log.e(TAG, "测试：" + aa);
        double bb = DataUtils.stringToDouble("aa");
        Log.e(TAG, "测试：" + bb);
    }


    /**
     * OutputStream
     */
    private void io1() {
        OutputStream outputStream = null;
        try {
//            File file = new File(Constants.FILE_SAVE_DIR + "text.txt");
            //目录无法自动创建，文件可以
            String filePath = getExternalFilesDir(null) + File.separator + "test";
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            outputStream = new FileOutputStream(filePath + File.separator + "text1.txt");
//            outputStream = new FileOutputStream(Constants.FILE_SAVE_DIR + "text1.txt");
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

    private void io3() {
        InputStream inputStream = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {
            //测试一下目录是否会自动创建
            String filePath = getExternalFilesDir(null) + File.separator + "test" + File.separator + "text1.txt";
//            reader = new FileReader(filePath);
            inputStream = new FileInputStream(filePath);
            reader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(reader);
            String s = bufferedReader.readLine();
            Log.e(TAG, "io3()：" + s);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(inputStream).close();//为空会抛出空指针异常
                Objects.requireNonNull(reader).close();
                Objects.requireNonNull(bufferedReader).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void io4() {
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            outputStream = new FileOutputStream(Constants.FILE_SAVE_DIR + File.separator + "text2.txt");
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write('c');
            bufferedOutputStream.write('d');
            bufferedOutputStream.flush();//带缓冲区的管道必须要flush一下才能写进去

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();//关闭的时候可以自动执行flush操作
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件复制
     */
    private void io5() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(Constants.FILE_SAVE_DIR + File.separator + "text2.txt");
            outputStream = new FileOutputStream(Constants.FILE_SAVE_DIR + File.separator + "newText.txt");

            byte[] data = new byte[1024];
            int read;
            while ((read = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * Socket网络请求
     */
    private void io6() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bufferedWriter = null;
                BufferedReader bufferedReader = null;
                try {
                    Socket socket = new Socket("hencoder.com", 80);//80端口是HTTP端口
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    bufferedWriter.write("GET / HTTP/1.1\n" + "Host: www.example.com\n\n");
                    bufferedWriter.flush();
                    StringBuilder stringBuilder = new StringBuilder();
                    String readline;
                    while ((readline = bufferedReader.readLine()) != null) {
                        stringBuilder.append(readline);
                        Log.e(TAG, readline + "\n");
                    }
                    //可以在这里进行UI操作
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    /**
     * 服务器，没法测试
     */
    private void io7() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bufferedWriter = null;
                BufferedReader bufferedReader = null;
                try {
                    ServerSocket serverSocket = new ServerSocket(80);
                    Socket socket = serverSocket.accept();//此处会一直阻塞等待请求到来
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String readLine;
                    while ((readLine = bufferedReader.readLine()) != null) {//先读请求内容
                        Log.e(TAG, readLine + "\n");
                    }
                    bufferedWriter.write("HTTP/1.1 200 OK\n" + "<html>\n" +
                            "\n" +
                            "<head>\n" +
                            "<title>我的第一个 HTML 页面</title>\n" +
                            "</head>\n" +
                            "\n" +
                            "<body>\n" +
                            "<p>Hello world</p>\n" +
                            "<p>永远相信</p>\n" +
                            "</body>\n" +
                            "\n" +
                            "</html>\n\n");
                    bufferedWriter.flush();//一次性返回响应，然后结束

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //io8() NIO和Okio暂时略过了，以后有需求了再学习
}
