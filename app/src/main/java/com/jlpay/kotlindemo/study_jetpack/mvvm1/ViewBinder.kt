package com.jlpay.kotlindemo.study_jetpack.mvvm1

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText

class ViewBinder {
    companion object {
        fun bind(editText: EditText, stringAttr: StringAttr) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.equals(stringAttr.value, s)) {
                        stringAttr.value = s.toString()
                        Log.e("MVVM：", "表现数据通知内存了！！${s}")
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
            stringAttr.onChangeListener = object : StringAttr.OnChangeListener {
                override fun onChange(newValue: String?) {
                    if (!TextUtils.equals(newValue, editText.text)) {
                        editText.setText(newValue)
                        Log.e("MVVM：", "内存通知表现数据了！！${newValue}")
                    }
                }
            }


//            //只需要重新afterTextChanged()方法 Kotlin的KTX方法  TODO 有点问题，待有空解决
//            editText.doAfterTextChanged {
//            }
        }
    }
}