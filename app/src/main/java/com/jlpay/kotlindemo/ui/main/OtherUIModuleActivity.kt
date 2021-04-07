package com.jlpay.kotlindemo.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class OtherUIModuleActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, OtherUIModuleActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_ui)

        initView()
    }

    fun initView() {
        toastPractice()
    }


    fun toastPractice() {
        val btnSimpleToast: Button = findViewById(R.id.btn_simple_toast)
        btnSimpleToast.setOnClickListener {
            Toast.makeText(this, "简单的信息提示", Toast.LENGTH_SHORT).show()
        }
        val btnImgToast: Button = findViewById(R.id.btn_img_toast)
        btnImgToast.setOnClickListener {
            val toast: Toast = Toast(this@OtherUIModuleActivity)
            val ll: LinearLayout = LinearLayout(this@OtherUIModuleActivity)
            ll.orientation = LinearLayout.VERTICAL
            val imageView = ImageView(this@OtherUIModuleActivity)
            imageView.setImageResource(R.mipmap.hanfei)
            ll.addView(imageView)
            val textView = TextView(this@OtherUIModuleActivity)
            textView.text = "带图片的提示信息"
            textView.setTextColor(Color.BLUE)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            ll.addView(textView)
            toast.view = ll//Android11上过时
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}