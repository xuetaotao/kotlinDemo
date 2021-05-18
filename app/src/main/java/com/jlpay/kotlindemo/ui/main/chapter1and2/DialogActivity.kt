package com.jlpay.kotlindemo.ui.main.chapter1and2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import java.util.*

class DialogActivity : AppCompatActivity() {

    lateinit var ll_parent: LinearLayout
    lateinit var popupWindow2: PopupWindow

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, DialogActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        initView()
    }

    fun initView() {
        ll_parent = findViewById(R.id.ll_parent)
        val btnSimpledialog: Button = findViewById(R.id.btn_simpleDialog)
        btnSimpledialog.setOnClickListener {
            simple()
        }
        val btnItemdialog: Button = findViewById(R.id.btn_itemDialog)
        btnItemdialog.setOnClickListener {
            itemDialog()
        }
        val btnSinglechoicedialog: Button = findViewById(R.id.btn_singleChoiceDialog)
        btnSinglechoicedialog.setOnClickListener {
            singlechoicedialog()
        }
        val btnMultichoicedialog: Button = findViewById(R.id.btn_multiChoiceDialog)
        btnMultichoicedialog.setOnClickListener {
            multichoicedialog()
        }
        val btnCustomitemsdialog: Button = findViewById(R.id.btn_customItemsDialog)
        btnCustomitemsdialog.setOnClickListener {
            customitemsdialog()
        }
        val btnCustomviewalertdialog: Button = findViewById(R.id.btn_customViewAlertDialog)
        btnCustomviewalertdialog.setOnClickListener {
            customviewalertdialog()
        }
        val btnCustomdialog: Button = findViewById(R.id.btn_customDialog)
        btnCustomdialog.setOnClickListener {
            customdialog()
        }
        val btnFinish: Button = findViewById(R.id.btn_finish)
        btnFinish.setOnClickListener {
            finish()
        }

        popUpWindowTest()
        popUpWindowTest2()

        val btnDatepickerdialog: Button = findViewById(R.id.btn_datePickerDialog)
        btnDatepickerdialog.setOnClickListener {
            datepickerdialogTest()
        }
        val btnTimepickerdialog: Button = findViewById(R.id.btn_timePickerDialog)
        btnTimepickerdialog.setOnClickListener {
            timepickerdialogTest()
        }
    }

    fun timepickerdialogTest() {
        val calendar: Calendar = Calendar.getInstance()
        val timePickerDialog: TimePickerDialog =
            TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    Toast.makeText(
                        this@DialogActivity,
                        "您选择了：" + hourOfDay + "时" + minute + "分",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    fun datepickerdialogTest() {
        val calendar: Calendar = Calendar.getInstance()
        val datePickerDialog: DatePickerDialog = DatePickerDialog(
            this,
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker?,
                    year: Int,
                    month: Int,
                    dayOfMonth: Int
                ) {
                    Toast.makeText(
                        this@DialogActivity,
                        "您选择了：" + year + "年" + (month + 1) + "月" + dayOfMonth + "日",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun popUpWindowTest2() {
        val view: View = LayoutInflater.from(this).inflate(R.layout.item_pop_select, null)
        val tv_open_xiangji: TextView = view.findViewById(R.id.tv_open_xiangji)
        tv_open_xiangji.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@DialogActivity, "相机", Toast.LENGTH_SHORT).show()
            }
        })
        val tv_open_xiangce: TextView = view.findViewById(R.id.tv_open_xiangce)
        tv_open_xiangce.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@DialogActivity, "打开相册", Toast.LENGTH_SHORT).show()
            }
        })
        val cancel: TextView = view.findViewById(R.id.cancel)
        cancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (popupWindow2.isShowing) {
                    popupWindow2.dismiss()
                }
            }
        })
        //1.创建
        popupWindow2 = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val btnPopupwindow2: Button = findViewById(R.id.btn_popupWindow2)
        btnPopupwindow2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //2.显示
                popupWindow2.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0)
            }
        })
    }


    fun popUpWindowTest() {
        val view: View = LayoutInflater.from(this).inflate(R.layout.item_popup, null)
        val btn_cancel: Button = view.findViewById(R.id.btn_cancel)
        val popupWindow: PopupWindow = PopupWindow(view, 560, 720)
        val btnPopupwindow: Button = findViewById(R.id.btn_popupWindow)
        btnPopupwindow.setOnClickListener { v ->
            popupWindow.showAsDropDown(v)//以下拉方式显示，将PopupWindow作为contentView组件的下拉组件显示出来，v作为显示的锚，显示在它下面
//            popupWindow.showAtLocation(ll_parent, Gravity.CENTER, 20, 20)//将PopupWindow作为contentView组件的下拉组件在指定位置显示出来
        }
        btn_cancel.setOnClickListener { popupWindow.dismiss() }//获取PopupWindow中的关闭按钮
    }


    /**
     * 自定义布局文件Dialog
     */
    fun customdialog() {
        val dialog: Dialog = Dialog(this, R.style.custom_dialog)

        //设置Dialog的宽高，但是已在布局文件中设置过了，这里就不用了
//        val window: Window? = dialog.window
//        if (window != null) {
//            val attributes = window.attributes
//            attributes.width = ViewGroup.LayoutParams.WRAP_CONTENT
//            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            attributes.gravity = Gravity.CENTER
//            window.attributes = attributes
//        }

        val view: View = LayoutInflater.from(this).inflate(R.layout.item_privacy, null)
        val tv_content: TextView = view.findViewById(R.id.tv_content)
        val btn_cancel: Button = view.findViewById(R.id.btn_cancel)
        btn_cancel.setOnClickListener {
            Toast.makeText(this, "拒绝了", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        val btn_agree: Button = view.findViewById(R.id.btn_agree)
        btn_agree.setOnClickListener {
            Toast.makeText(this, "同意了", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
    }

    fun customviewalertdialog() {
        val view: View = LayoutInflater.from(this).inflate(R.layout.item_login, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("multichoicedialog")
            .setView(view)
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击确定了", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击取消了", Toast.LENGTH_SHORT).show()
                }
            })
            .setCancelable(false)
            .create()
            .show()
    }


    fun customitemsdialog() {
        val items = arrayOf("韩非", "张良", "弄玉")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("customitemsdialog")
//            .setIcon(R.mipmap.nongyu)
            .setAdapter(
                ArrayAdapter(this, R.layout.item_array, items),
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(
                            this@DialogActivity,
                            "你点击了：" + items[which],
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
//            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    Toast.makeText(this@DialogActivity, "点击确定了", Toast.LENGTH_SHORT).show()
//                }
//            })
//            .setNegativeButton("取消", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    Toast.makeText(this@DialogActivity, "点击取消了", Toast.LENGTH_SHORT).show()
//                }
//            })
            .setCancelable(false)
            .create()
            .show()
    }

    fun multichoicedialog() {
        val items = arrayOf("韩非", "张良", "弄玉")
        val originStaus = booleanArrayOf(false, false, true)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("multichoicedialog")
            .setIcon(R.mipmap.hanfei)
            .setMultiChoiceItems(
                items,
                originStaus,
                object : DialogInterface.OnMultiChoiceClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
                        Toast.makeText(
                            this@DialogActivity,
                            "你点击了：" + items[which],
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击确定了", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击取消了", Toast.LENGTH_SHORT).show()
                }
            })
            .setCancelable(false)
            .create()
            .show()
    }


    fun singlechoicedialog() {
        val items = arrayOf("韩非", "张良", "弄玉");
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("singlechoicedialog")
            .setIcon(R.mipmap.hanfei)
            .setSingleChoiceItems(items, -1, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "你选中了：" + items[which], Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击确定了", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击取消了", Toast.LENGTH_SHORT).show()
                }
            })
            .setCancelable(false)
            .create()
            .show()
    }

    fun itemDialog() {
        val items = arrayOf("韩非", "张良", "弄玉");
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("itemDialog")
            .setIcon(R.mipmap.hanfei)
            .setItems(items, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "你选中了：" + items[which], Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击确定了", Toast.LENGTH_SHORT).show()
//                    dialog?.dismiss()//不需要这个，会自动取消
                }
            })
            .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击取消了", Toast.LENGTH_SHORT).show()
                }
            })
            .setCancelable(false)
            .create()
            .show()
    }


    fun simple() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("simpleDialog")
            .setIcon(R.mipmap.hanfei)
            .setMessage("对话框的测试内容\n第二行内容")
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击确定了", Toast.LENGTH_SHORT).show()
//                    dialog?.dismiss()//不需要这个，会自动取消
                }
            })
            .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@DialogActivity, "点击取消了", Toast.LENGTH_SHORT).show()
                }
            })
            .setCancelable(false)
            .create()
            .show()
    }
}