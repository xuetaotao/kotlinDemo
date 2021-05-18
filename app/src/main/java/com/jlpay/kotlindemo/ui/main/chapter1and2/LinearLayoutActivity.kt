package com.jlpay.kotlindemo.ui.main.chapter1and2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class LinearLayoutActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, LinearLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_layout)

        radioButtonAndCheckBox()
        toggleButtonAndSwitch()
        chronometer()
    }


    /**
     * 单选按钮(RadioButton)和复选框(CheckBox)
     */
    private fun radioButtonAndCheckBox() {

        val stringBuilder: StringBuilder = StringBuilder()
        val radioGroup: RadioGroup = findViewById(R.id.radio_group)
        val tvResult: TextView = findViewById(R.id.tv_result)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_male -> {
                    stringBuilder.append("male")
                    tvResult.text = stringBuilder
                }
                R.id.rb_female -> {
                    stringBuilder.append("female")
                    tvResult.text = stringBuilder
                }
            }
        }


        val cbRed: CheckBox = findViewById(R.id.cb_red)
        cbRed.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                stringBuilder.append(", like red")
                tvResult.text = stringBuilder
            }
        }
        val cbBlue: CheckBox = findViewById(R.id.cb_blue)
        cbBlue.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                stringBuilder.append(", like blue")
                tvResult.text = stringBuilder
            }
        }
        val cbGreen: CheckBox = findViewById(R.id.cb_green)
        cbGreen.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                stringBuilder.append(", like green")
                tvResult.text = stringBuilder
            }
        }
    }

    /**
     * 状态开关按钮(ToggleButton)和开关(Switch)
     */
    private fun toggleButtonAndSwitch() {
        val btnToggle: ToggleButton = findViewById(R.id.btn_toggle)
        val btnSwitch: Switch = findViewById(R.id.btn_switch)
        val llTest: LinearLayout = findViewById(R.id.ll_test)

        val checkListener: CompoundButton.OnCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    llTest.orientation = LinearLayout.VERTICAL
                    btnToggle.isChecked = true
                    btnSwitch.isChecked = true
                } else {
                    llTest.orientation = LinearLayout.HORIZONTAL
                    btnToggle.isChecked = false
                    btnSwitch.isChecked = false
                }
            }

        btnToggle.setOnCheckedChangeListener(checkListener)
        btnSwitch.setOnCheckedChangeListener(checkListener)
    }


    /**
     * 计时器
     */
    private fun chronometer() {
        val chronometer: Chronometer = findViewById(R.id.chronometer)
        val btnChronometer: Button = findViewById(R.id.btn_chronometer)
        btnChronometer.setOnClickListener {
            //设置开始计时的时间
            chronometer.base = SystemClock.elapsedRealtime()
            //启动计时器
            chronometer.start()
        }
        chronometer.setOnChronometerTickListener {
            if (SystemClock.elapsedRealtime() - chronometer.base > 60 * 1000) {
                chronometer.stop()
                btnChronometer.isEnabled = true
            }
        }
    }

}