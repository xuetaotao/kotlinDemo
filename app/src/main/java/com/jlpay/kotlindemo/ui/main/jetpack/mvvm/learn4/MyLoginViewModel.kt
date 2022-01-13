package com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn4

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class MyLoginViewModel : ViewModel() {

    private val TAG: String? = this.javaClass.simpleName

    val account: ObservableField<String> by lazy {
        ObservableField<String>("")
    }

    val password: ObservableField<String> by lazy {
        ObservableField<String>("").apply {
            addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    val passwordObservableField = sender as ObservableField<String>
                    Log.e(TAG, "onPropertyChanged: ${passwordObservableField.get()}")
                }
            })
        }
    }
}