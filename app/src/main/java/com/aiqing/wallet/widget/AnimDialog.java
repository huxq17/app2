package com.aiqing.wallet.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiqing.basiclibrary.utils.IDFactory;
import com.aiqing.wallet.R;

import java.util.ArrayList;
import java.util.List;

public class AnimDialog extends Dialog implements View.OnClickListener {
    private final Activity activity;
    private final List<Item> items;
    int gravity;
    View contentView;
    @StyleRes
    int animatorId = -1;
    private boolean cancelable = true;
    private boolean canceledOnTouchOutside = true;

    public AnimDialog(Activity activity) {

        super(activity, R.style.dialog);
        this.activity = activity;
        items = new ArrayList<>();
    }

    public AnimDialog addItem(Item item) {
        items.add(item);
        return this;
    }

    public AnimDialog gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public AnimDialog cancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public AnimDialog canceledOnTouchOutside(boolean cancel) {
        this.canceledOnTouchOutside = cancel;
        return this;
    }

    public AnimDialog contentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    public AnimDialog animator(@StyleRes int id) {
        animatorId = id;
        return this;
    }

    public AnimDialog contentView(@LayoutRes int contentId) {
        this.contentView = View.inflate(activity, contentId, null);
        return this;
    }

    private OnItemClickListener onItemClickListener;

    public AnimDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public AnimDialog build() {
        if (contentView == null) {
            LinearLayout rootView = new LinearLayout(activity);
            rootView.setOrientation(LinearLayout.VERTICAL);
            rootView.setBackgroundColor(Color.WHITE);
            parseItem(rootView);
            configWindow();
            super.setContentView(rootView);
        } else {
            configWindow();
            super.setContentView(contentView);
        }
        return this;
    }

    private void parseItem(LinearLayout rootView) {
        for (Item item : items) {
            String text = item.text;
            TextView textView = new TextView(activity);
            textView.setText(text);
            textView.setGravity(item.gravity);
            textView.setTextSize(item.textSize);
            textView.setId(item.id);
            textView.setOnClickListener(this);
            textView.setTag(item);
            rootView.addView(textView, new LinearLayout.LayoutParams(item.width, item.height));
        }
    }

    private void configWindow() {
        Window window = this.getWindow();
        if (window != null) {
            window.setGravity(gravity);
            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.y = DensityUtil.dip2px(mContext, 10);
            window.getDecorView().setPadding(0, 0, 0, 0);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            if (animatorId != -1) {
                window.setWindowAnimations(animatorId);
            }
            setCancelable(cancelable);
            setCanceledOnTouchOutside(canceledOnTouchOutside);
            window.setAttributes(lp);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String text);
    }

    public static class Item {
        int id;
        String text;
        int gravity;
        int textSize;
        int height;
        int width;

        public Item(String text) {
            this.text = text;
            gravity = Gravity.CENTER;
            id = IDFactory.generateId();
            this.textSize = 16;
            width = ViewGroup.LayoutParams.MATCH_PARENT;
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        public Item gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Item textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Item width(int width) {
            this.width = width;
            return this;
        }

        public Item height(int height) {
            this.height = height;
            return this;
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        Object tag = v.getTag();
        if (onItemClickListener == null || tag == null || !(tag instanceof Item)) return;
        Item item = (Item) tag;
        if (item != null) {
            onItemClickListener.onItemClick(item.text);
        }
    }

    @Override
    public void show() {
        if (activity.isFinishing()) {
            return;
        }
        super.show();
    }
}