package com.aiqing.wallet.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.huxq17.xprefs.LogUtils;

public class CustomBehavior extends AppBarLayout.ScrollingViewBehavior {

    public CustomBehavior() {
        LogUtils.e("CustomBehavior");
    }

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        LogUtils.e("CustomBehavior");
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }
}