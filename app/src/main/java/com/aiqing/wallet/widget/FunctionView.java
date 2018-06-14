package com.aiqing.wallet.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.huxq17.handygridview.HandyGridView;

public class FunctionView extends HandyGridView {
    public FunctionView(Context context) {
        super(context);
    }

    public FunctionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
