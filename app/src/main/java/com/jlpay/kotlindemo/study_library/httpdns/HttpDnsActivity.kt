package com.jlpay.kotlindemo.study_library.httpdns

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.sdk.android.httpdns.HttpDns
import com.alibaba.sdk.android.httpdns.HttpDnsService
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.utils.AppUtils
import okhttp3.Dns
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress

/**
 * 阿里的 HttpDns 框架 （没有开源，商业性sdk）
 * https://www.jianshu.com/p/54a5dbb7b769
 * 源码：https://github.com/milovetingting/Samples/tree/master/HttpDns
 */
class HttpDnsActivity : AppCompatActivity() {

    val TAG: String = HttpDnsActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_httpdns)
    }

    fun httpdnsClick(view: View) {
        netRequest()
//        Toast.makeText(this, "HttpDns", Toast.LENGTH_SHORT).show()
    }

    fun netRequest() {
        val client: OkHttpClient = OkHttpClient.Builder()
            //阿里的HttpDns要收费的，这里没配相关参数
            .dns(AliDns(AppUtils.getContext()))
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://www.wanandroid.com/")
            .client(client)
            .build()
        val api = retrofit.create(Api::class.java)
        val call: Call<Bean> = api.banner
        call.enqueue(object : Callback<Bean> {
            override fun onResponse(call: Call<Bean>, response: Response<Bean>) {
                if (!response.isSuccessful) {
                    Toast.makeText(this@HttpDnsActivity, "请求失败", Toast.LENGTH_SHORT).show()
                    return
                }
                val bean = response.body()
                Toast.makeText(this@HttpDnsActivity, bean.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Bean>, t: Throwable) {
                Toast.makeText(this@HttpDnsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    class AliDns(context: Context) : Dns {

        private var httpDns: HttpDnsService

        init {
            httpDns = HttpDns.getService(context, "account_id")
        }

        override fun lookup(hostname: String): List<InetAddress> {
            //通过异步解析接口获取ip
            val ip = httpDns.getIpByHostAsync(hostname)
            //Android9.0系统及以后版本，https请求无法直接访问，方便起见，
            //直接在AndroidManifest.xml中配置android:usesCleartextTraffic="true"
            if (ip != null) {
                //如果ip不为null，直接使用该ip进行网络请求
                Log.e("AliDns", "ip: $ip")
                //kotlin Array和List互转 https://blog.csdn.net/qq_38527695/article/details/98734750
                //val inetAddresses: List<InetAddress> = Arrays.asList(*InetAddress.getAllByName(ip))
                //参考 Dns.SYSTEM.lookup(hostname) 的写法
                //一般情况下，一个域名会对应多个IP地址，所以这里返回的是一组IP地址
                val inetAddresses: List<InetAddress> = InetAddress.getAllByName(ip).toList()
                return inetAddresses
            }
            //如果返回null，走系统DNS服务解析域名
            return Dns.SYSTEM.lookup(hostname)
        }
    }
}