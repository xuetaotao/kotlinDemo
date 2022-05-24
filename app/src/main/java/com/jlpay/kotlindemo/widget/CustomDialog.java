package com.jlpay.kotlindemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.jlpay.kotlindemo.R;

public class CustomDialog extends Dialog {

    protected CustomDialog(@NonNull Context context) {
        super(context);
    }

    protected CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        private Context context;
        private String title;
        private String content;
        private String positiveButtonStr;
        private String negativeButtonStr;
        private boolean cancelable;
        private boolean titleVisible;
        private OnClickListener positiveListener;
        private OnClickListener negativeListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(@StringRes int titleStrRes) {
            this.title = context.getString(titleStrRes);
            return this;
        }

        public Builder setTitleVisible(boolean titleVisible) {
            this.titleVisible = titleVisible;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setContent(@StringRes int contentStrRes) {
            this.content = context.getString(contentStrRes);
            return this;
        }

        public Builder setPositiveButton(String positiveButtonStr, OnClickListener listener) {
            this.positiveButtonStr = positiveButtonStr;
            this.positiveListener = listener;
            return this;
        }

        public Builder setPositiveButton(@StringRes int positiveButtonStrRes, OnClickListener listener) {
            this.positiveButtonStr = context.getString(positiveButtonStrRes);
            this.positiveListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonStr, OnClickListener listener) {
            this.negativeButtonStr = negativeButtonStr;
            this.negativeListener = listener;
            return this;
        }

        public Builder setNegativeButton(@StringRes int negativeButtonStrRes, OnClickListener listener) {
            this.negativeButtonStr = context.getString(negativeButtonStrRes);
            this.negativeListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public CustomDialog create() {
            CustomDialog customDialog = new CustomDialog(context, R.style.custom_dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_customer, null);
            //标题
            TextView tv_title = view.findViewById(R.id.tv_title);
            tv_title.setText(title);
            tv_title.setVisibility(titleVisible ? View.VISIBLE : View.GONE);
            //内容
            TextView tv_content = view.findViewById(R.id.tv_content);
            tv_content.setText(content);
            //确定按钮
            Button btn_confirm = view.findViewById(R.id.btn_confirm);
            btn_confirm.setText(positiveButtonStr);
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positiveListener.onClick(customDialog, DialogInterface.BUTTON_POSITIVE);
                }
            });
            //取消按钮
            Button btn_cancel = view.findViewById(R.id.btn_cancel);
            btn_cancel.setText(negativeButtonStr);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    negativeListener.onClick(customDialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });

            customDialog.setContentView(view);
            customDialog.setCancelable(cancelable);
            return customDialog;
        }


    }
}
