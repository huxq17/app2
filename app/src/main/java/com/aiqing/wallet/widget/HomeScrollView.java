package com.aiqing.wallet.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.huxq17.xprefs.LogUtils;

public class HomeScrollView extends NestedScrollView {
    public HomeScrollView(@NonNull Context context) {
        super(context);
    }

    public HomeScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
        LogUtils.e("onStopNestedScroll");
    }
}
