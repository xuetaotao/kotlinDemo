package com.jlpay.kotlindemo.ui.main.chapter10

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.remotecustomservice.IPet
import com.jlpay.remotecustomservice.Person
import com.jlpay.remotecustomservice.Pet

class CustomAidlServiceActivity : AppCompatActivity() {

    private var petService: IPet? = null

    private val conn: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("CustomAidlService", "--ParcelableService Connected--")
            petService = IPet.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("CustomAidlService", "--ParcelableService Disconnected--")
            petService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_aidl_service)

        initView()
    }

    private fun initView() {
        val intent: Intent = Intent()
        intent.setAction("com.jlpay.remotecustomservice.action.ParcelableService")
        intent.setPackage("com.jlpay.remotecustomservice")
        bindService(intent, conn, Service.BIND_AUTO_CREATE)

        val tv_service: TextView = findViewById(R.id.tv_service)
        val et_person: EditText = findViewById(R.id.et_person)
        val name: String = et_person.text.toString().trim()
        val show: ListView = findViewById(R.id.show)
        val btn_get: Button = findViewById(R.id.btn_get)
        btn_get.setOnClickListener {
            try {
                //调用远程Service的方法
                val pets =
                    petService?.getPets(Person(1, "sun", "sun"))
                val pet: Pet = pets!!.get(0)
                tv_service.text = pet.name + ":" + pet.year
                //将程序返回的List包装成ArrayAdapter
                val adapter: ArrayAdapter<Pet> =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, pets)
                show.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(conn)
    }
}