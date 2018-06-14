package com.aiqing.wallet.android.support.design.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
@CoordinatorLayout.DefaultBehavior(myBehavior.class)
public class CAppBarLayout extends AppBarLayout {
    public CAppBarLayout(Context context) {
        super(context);
    }

    public CAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
