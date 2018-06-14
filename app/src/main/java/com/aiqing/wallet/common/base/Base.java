package com.aiqing.wallet.common.base;

import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;


public class Base {
    private static WeakReference<Context> mContextRef;
    private Toast mToast;
    private String mMsg;

    private Base() {
    }

    private static class BaseHolder {
        private static Base instance = new Base();
    }

    public static Base getInstance(Context context) {
        mContextRef = new WeakReference<>(context.getApplicationContext());
        return BaseHolder.instance;
    }

    /**
     * 格式化字符串
     *
     * @param format
     * @param args
     */
    public String format(String format, Object... args) {
        return String.format(format, args);
    }

    // 判定是否需要隐藏
    public boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            Rect bounds = new Rect();
            v.getGlobalVisibleRect(bounds);
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            if (bounds.contains(x, y)) {
                return false;
            } else {
                return true;
            }

        }
        return false;
    }

    private Context getContext() {
        return mContextRef.get();
    }

    // 隐藏软键盘
    public void HideSoftInput(IBinder token) {
        Context context = getContext();
        if (token != null && context != null) {
            InputMethodManager manager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setText(Object obj, String str) {
        if (!isEmpty(str) && (obj != null)) {
            if (obj instanceof EditText) {
                ((EditText) obj).setText(str);
                return;
            }
            if (obj instanceof TextView) {
                ((TextView) obj).setText(str);
                return;
            }
            if (obj instanceof Button) {
                ((Button) obj).setText(str);
                return;
            }
        }
    }

    /**
     * 获取edittext，textView,checkbox和button的文字
     *
     * @param obj
     * @return
     */
    public String getText(Object obj) {
        if (obj instanceof TextView) {
            return ((TextView) obj).getText().toString();
        }
        if (obj instanceof EditText) {
            return ((EditText) obj).getText().toString();
        }
        if (obj instanceof Button) {
            return ((Button) obj).getText().toString();
        }
        if (obj instanceof CheckBox) {
            return ((CheckBox) obj).getText().toString();
        }
        return "";
    }

    public boolean isEmpty(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return TextUtils.isEmpty(getText(obj));
    }

    public boolean isEmpty(String str) {
        return TextUtils.isEmpty(str) || str.equals("null");
    }

    public void toast(String msg) {
        _toast(msg, Toast.LENGTH_SHORT, false);
    }

    private void _toast(String msg, int duration, boolean all) {
        Context context = getContext();
        if (TextUtils.isEmpty(msg) || context == null) {
            return;
        }
        if (mToast == null || !msg.equals(mMsg)) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, duration);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        mMsg = msg;
        if (all) {
            Toast.makeText(context, msg, duration).show();
        } else {
            mToast.show();
        }

    }

    public void toastAll(String msg) {
        _toast(msg, Toast.LENGTH_SHORT, true);
    }

    public void toastAllL(String msg) {
        _toast(msg, Toast.LENGTH_SHORT, true);
    }

    public void toastL(String msg) {
        _toast(msg, Toast.LENGTH_LONG, false);
    }
}
