package com.jlpay.kotlindemo.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.widget.CustomDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 主要用于存放一些合伙人重复调用的公共View方法(与业务相关)
 */
public class DialogUtils {

    public static int str2Int(String str) {
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 格式化小数位数
     *
     * @param editText   输入文本框
     * @param str        输入字符串
     * @param decimalNum 保留几位小数
     */
    public static void formatDecimal(EditText editText, String str, int decimalNum) {
        String result = "";
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.startsWith(".")) {
            result = "";
            editText.setText(result);
            return;
        }
        if (str.startsWith("00")) {
            result = "0";
            editText.setText(result);
            editText.setSelection(result.length());
            return;
        }
        if (str.contains(".")) {
            if (str.length() - 1 - str.indexOf(".") > decimalNum) {
                result = str.substring(0, str.indexOf(".") + decimalNum + 1);
                editText.setText(result);
                editText.setSelection(result.length());
            }
        }
    }


    /**
     * 时间选择器
     *
     * @param context
     * @param textView 显示所选时间的文本框
     */
    public static void showTimeSelect(Context context, TextView textView) {
        Calendar selectedDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy,MM,dd");
        String format = simpleDateFormat.format(System.currentTimeMillis());
        String[] split = format.split(",");
        Calendar startDate = Calendar.getInstance();
        startDate.set(2001, 1, 1);//设置起始年份
        Calendar endDate = Calendar.getInstance();
        endDate.set(str2Int(split[0]) + 20, 11, 31);//设置结束年份

        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat1.format(date);
                textView.setText(format);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})//只显示年月日
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
//                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.GRAY)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
        //设置在底部显示
        Dialog dialog = pvTime.getDialog();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        //设置背景View
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        params.leftMargin = 0;
        params.rightMargin = 0;
        ViewGroup viewGroup = pvTime.getDialogContainerLayout();
        viewGroup.setLayoutParams(params);
        //显示
        pvTime.show();
    }


    public interface OptionListener {
        void onSelected(String selected);
    }

    /**
     * 条件选择器
     *
     * @param context
     * @param optionList 可供选择的列表
     * @param listener   选择完成后的监听
     */
    @Deprecated
    public static void showOptionSelect(Context context, List<String> optionList, OptionListener listener) {
        showOptionSelect(context, optionList, new SelectedOptionsListener() {
            @Override
            public void onSelectedOption(int options1) {
                listener.onSelected(optionList.get(options1));
            }
        });
    }

    public interface SelectedOptionsListener {
        void onSelectedOption(int options1);
    }

    /**
     * 条件选择器
     *
     * @param context
     * @param optionList 可供选择的列表
     * @param listener   选择完成后的监听
     */
    public static void showOptionSelect(Context context, List<String> optionList, SelectedOptionsListener listener) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.onSelectedOption(options1);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("")//标题
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setSubCalSize(18)//确定和取消文字大小
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.GRAY)//取消按钮文字颜色
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setContentTextSize(16)//滚轮文字大小
                .setLabels("", "", "")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项
                .build();
        pvOptions.setPicker(optionList, null, null);
        pvOptions.show();
    }


    /**
     * 格式化EditText输入
     *
     * @param editText   格式化对象
     * @param decimalNum 限制小数位数
     */
    public static void formatEditInput(EditText editText, int decimalNum) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                formatDecimal(editText, s.toString(), decimalNum);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public interface WxShareHandle {
        void wxShare();

        void wxFriendShare();
    }


    /**
     * 温馨提示对话框
     *
     * @param activity
     * @param message
     */
    public static void warmTipDialog(Activity activity, int message, WarmTipDialogListener listener) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setContent(activity.getResources().getString(message))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                }).setCancelable(false).create().show();
    }

    public static void warmTipDialog(Activity activity, String message, WarmTipDialogListener listener) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setContent(message)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                }).setCancelable(false).create().show();
    }

    public static void warmTipDialog(Activity activity, @StringRes int title, @StringRes int message, @StringRes int positiveStr, WarmTipDialogListener listener) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setTitle(title)
                .setTitleVisible(true)
                .setContent(message)
                .setPositiveButton(activity.getResources().getString(positiveStr), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                }).setCancelable(false).create().show();
    }

    public static void warmTipDialog(Activity activity, String title, String message, String positiveStr, WarmTipDialogListener listener) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setTitle(title)
                .setTitleVisible(true)
                .setContent(message)
                .setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                }).setCancelable(false).create().show();
    }

    public interface WarmTipDialogListener {
        void confirm();
    }

    public interface WarmTipDialogListener2 {
        void confirm();

        void cancel();
    }

    public static void warmTipDialog2(Activity activity, String message, WarmTipDialogListener2 listener) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setContent(message)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.cancel();
                    }
                })
                .setCancelable(false).create().show();
    }

    public static void warmTipDialog2(Activity activity, int message, WarmTipDialogListener2 listener) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setContent(activity.getResources().getString(message))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.cancel();
                    }
                })
                .setCancelable(false).create().show();
    }

    public static void warmTipDialog2(Activity activity, String message, WarmTipDialogListener2 listener, String positiveStr, String negativeStr) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setContent(message)
                .setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                })
                .setNegativeButton(negativeStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.cancel();
                    }
                })
                .setCancelable(false).create().show();
    }

    public static void warmTipDialog2(Activity activity, @StringRes int message, WarmTipDialogListener2 listener, @StringRes int positiveStr, @StringRes int negativeStr) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setContent(activity.getResources().getString(message))
                .setPositiveButton(activity.getResources().getString(positiveStr), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                })
                .setNegativeButton(activity.getResources().getString(negativeStr), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.cancel();
                    }
                })
                .setCancelable(false).create().show();
    }

    public static void warmTipDialog2(Activity activity, @StringRes int title, @StringRes int message, @StringRes int positiveStr, @StringRes int negativeStr, WarmTipDialogListener2 listener) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setTitle(title)
                .setTitleVisible(true)
                .setContent(message)
                .setPositiveButton(activity.getResources().getString(positiveStr), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                })
                .setNegativeButton(activity.getResources().getString(negativeStr), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.cancel();
                    }
                })
                .setCancelable(false).create().show();
    }

    public static void warmTipDialog2(Activity activity, String title, String message, String positiveStr, String negativeStr, WarmTipDialogListener2 listener) {
        if (activity.isFinishing()) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setTitle(title)
                .setTitleVisible(true)
                .setContent(message)
                .setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.confirm();
                    }
                })
                .setNegativeButton(negativeStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.cancel();
                    }
                })
                .setCancelable(false).create().show();
    }


    public static class ImageGetterUtils {
        public static MyImageGetter getImageGetter(Context context, TextView textView) {
            MyImageGetter myImageGetter = new MyImageGetter(context, textView);
            return myImageGetter;
        }

        public static class MyImageGetter implements Html.ImageGetter {

            private URLDrawable urlDrawable = null;
            private TextView textView;
            private Context context;

            public MyImageGetter(Context context, TextView textView) {
                this.textView = textView;
                this.context = context;
            }

            @Override
            public Drawable getDrawable(final String source) {
                Log.e("source", "source:" + source);
                urlDrawable = new URLDrawable();
                Glide.with(context).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        urlDrawable.bitmap = changeBitmapSize(resource);
                        urlDrawable.setBounds(0, 0, changeBitmapSize(resource).getWidth(), changeBitmapSize(resource).getHeight());
                        textView.invalidate();
                        textView.setText(textView.getText());//不加这句显示不出来图片，原因不详
                    }
                });

                return urlDrawable;
            }

            public class URLDrawable extends BitmapDrawable {
                public Bitmap bitmap;

                @Override
                public void draw(Canvas canvas) {
                    super.draw(canvas);
                    if (bitmap != null) {
                        canvas.drawBitmap(bitmap, 0, 0, getPaint());
                    }
                }
            }

            private Bitmap changeBitmapSize(Bitmap bitmap) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Log.e("width", "width:" + width);

                Log.e("height", "height:" + height);
                //设置想要的大小
                int newWidth = width;
                int newHeight = height;
                //计算压缩的比率
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                //获取想要缩放的matrix
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                //获取新的bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

                bitmap.getWidth();

                bitmap.getHeight();

                Log.e("newWidth", "newWidth" + bitmap.getWidth());

                Log.e("newHeight", "newHeight" + bitmap.getHeight());

                return bitmap;
            }
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity activity
     * @param view     触发隐藏的view
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null && manager.isActive()) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // 两次点击按钮之间的点击间隔不能少于400毫秒
    private static final int MIN_CLICK_DELAY_TIME = 400;
    private static long lastClickTime;

    /**
     * 防止按钮快速重复点击
     *
     * @return true:是快速点击 flase:不是
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
