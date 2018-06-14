package com.aiqing.wallet.android.support.design.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.huxq17.xprefs.LogUtils;

public class myBehavior extends AppBarLayout.Behavior {
    void onFlingFinished(CoordinatorLayout parent, AppBarLayout layout) {
        // no-op
        LogUtils.e("onFlingFinished");
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        if (type == ViewCompat.TYPE_TOUCH) {
            // If we haven't been flung then let's see if the current view has been set to snap
            int verticalOffset = getTopAndBottomOffset();
            int totalScrollRange = abl.getTotalScrollRange();
            int absOffset = Math.abs(verticalOffset);
            if (absOffset > totalScrollRange / 2) {
                abl.setExpanded(true);
            } else {
                abl.setExpanded(false);
            }
        }
    }
}
