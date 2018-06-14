package com.aiqing.wallet.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.utils.SoftInputUtils;

/**
 * 交易密码输入对话框
 */
public class PasswordDialog extends Dialog {
    public PasswordDialog(Context context) {
        super(context);
    }

    public PasswordDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        PasswordDialog dialog;
        private Activity activity;
        Callback callback;
        EditText pd_ed;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setCallback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public PasswordDialog create() {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = new PasswordDialog(activity, R.style.dialog);
            View layout = inflater.inflate(R.layout.password_keypad, null);
            ((ImageView) layout.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onCancel();
                }
            });
            pd_ed = (EditText) layout.findViewById(R.id.password);
            pd_ed.setFocusable(true);
            pd_ed.setFocusableInTouchMode(true);
            pd_ed.requestFocus();
            pd_ed.post(new Runnable() {
                @Override
                public void run() {
                    SoftInputUtils.showSoftInput(activity);
                }
            });
            ((TextView) layout.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onConfirm(pd_ed.getText().toString());
                }
            });
            ((TextView) layout.findViewById(R.id.forget_pass)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onForgetPassword();
                }
            });
            dialog.setContentView(layout);
            dialog.setCancelable(false);
            return dialog;
        }

    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }



    /**
     * 交易密码回调接口
     */
    public interface Callback {
        void onForgetPassword();

        void onConfirm(String password);

        void onCancel();
    }

}
