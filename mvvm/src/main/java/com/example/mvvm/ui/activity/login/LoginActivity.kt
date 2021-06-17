package com.example.mvvm.ui.activity.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.mvvm.R
import com.example.mvvm.base.BaseViewModelActivity
import com.example.mvvm.constant.Constant
import com.example.mvvm.httpUtils.LoginData
import com.example.mvvm.mvvm.viewModel.LoginActivityViewModel
import com.example.mvvm.utils.MyMMKV
import com.example.mvvm.utils.toast
import com.jeremyliao.liveeventbus.LiveEventBus
import java.util.*

/**
 * TODO unfinished
 */
class LoginActivity : BaseViewModelActivity<LoginActivityViewModel>() {

    override fun providerVMClass(): Class<LoginActivityViewModel> =
        LoginActivityViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_login

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("登录")
        val btn_login: Button = findViewById(R.id.btn_login)
        btn_login.setOnClickListener {
            login()
        }
    }

    override fun initView() {
//        checkNeedPermission(initPermissionList())
    }

    override fun startHttp() {

    }

    private fun login() {
        val et_login_id: EditText = findViewById(R.id.et_login_id)
        val enterpriseName = et_login_id.text.toString().trim()
        val et_login_password: EditText = findViewById(R.id.et_login_password)
        val password = et_login_password.text.toString().trim()
        //when 也可以用来取代 if-else if链。 如果不提供参数，所有的分支条件都是简单的布尔表达式，而当一个分支的条件为真时则执行该分支：
        when {
            enterpriseName == "" -> toast("账号不能为空")
            password == "" -> toast("密码不能为空")
            else -> {
                viewModel.login(enterpriseName, password).observe(this,
                    Observer<LoginData> {
                        MyMMKV.mmkv.encode(Constant.IS_LOGIN, true)
                        LiveEventBus.get(Constant.IS_LOGIN).post(true)
                        toast("登录成功")
//                        finish()
                    })
            }
        }
    }


    /**
     * 1.在AndroidManifest.xml中添加所需权限
     * 2.动态获取权限
     */
    private fun initPermissionList(): List<String> {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        return ArrayList(Arrays.asList(*permissions))
    }

    private fun checkNeedPermission(permissionList: List<String>) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val REQUEST_PERMISSION_CODE = 0x1001
        val noPermissionList: MutableList<String> = ArrayList()
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                noPermissionList.add(permission)
            }
        }
        if (noPermissionList.size > 0) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
            // 如果设备规范禁止应用具有该权限，此方法也会返回 false
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(
                this,
                noPermissionList.toTypedArray(),
                REQUEST_PERMISSION_CODE
            )
        }
    }


    /**
     * 5.请求权限后回调的方法
     *
     * @param requestCode  是我们自己定义的权限请求码
     * @param permissions  是我们请求的权限名称数组
     * @param grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限
     * 名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermissionsDenied = false //是否有权限被拒绝
        if (requestCode == 0x1001) {
            if (grantResults != null && grantResults.size > 0) {
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        hasPermissionsDenied = true
                        break
                    }
                }
            }
            if (hasPermissionsDenied) {
                Toast.makeText(this, "you need allow some permissions", Toast.LENGTH_SHORT).show()
            } else {
                //have get all permissions
            }
        }
    }
}