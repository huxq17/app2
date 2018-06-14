package com.aiqing.wallet.home.function;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.aiqing.basiclibrary.utils.DensityUtil;
import com.huxq17.handygridview.scrollrunner.OnItemMovedListener;

import java.util.ArrayList;
import java.util.List;

public class FunctionAdapter extends BaseAdapter implements OnItemMovedListener {
    private Context context;
    private List<FunctionBean> mDatas = new ArrayList<>();

    public FunctionAdapter(Context context, List<FunctionBean> dataList) {
        this.context = context;
        this.mDatas.addAll(dataList);
    }


    private String getString(int drawable) {
        return context.getResources().getString(drawable);
    }

    private GridView mGridView;
    private boolean inEditMode = false;

    public void setData(List<FunctionBean> dataList) {
        this.mDatas.clear();
        this.mDatas.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public FunctionBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mGridView == null) {
            mGridView = (GridView) parent;
        }
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            convertView = textView;
            textView.setMaxLines(1);
//            textView.setHeight(DensityUtil.dip2px(context, 60));
            textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            int padding = DensityUtil.dip2px(context, 18);
            textView.setPadding(0, padding, 0, padding);
//            int id = context.getResources().getIdentifier("s_grid_item", "drawable", context.getPackageName());
//            Drawable drawable = context.getResources().getDrawable(id);
//            textView.setBackgroundDrawable(drawable);
            textView.setGravity(Gravity.CENTER);
            textView.setCompoundDrawablePadding(DensityUtil.dip2px(context, 10));
        } else {
            textView = (TextView) convertView;
        }
        FunctionBean item = getItem(position);
        textView.setText(item.text);
        Drawable top = textView.getContext().getResources().getDrawable(item.drawable);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        return convertView;
    }

    @Override
    public void onItemMoved(int from, int to) {
        mDatas.add(to, mDatas.remove(from));
    }

    @Override
    public boolean isFixed(int position) {
        //When position==0,the item can not be dragged.
        if (position == 0) {
            return true;
        }
        return true;
    }
}