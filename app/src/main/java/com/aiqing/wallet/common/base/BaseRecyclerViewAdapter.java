package com.aiqing.wallet.common.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by renpingqing on 21/01/2017.
 *
 * @param <D>  data type
 * @param <VH> ViewHolder type
 */
public abstract class BaseRecyclerViewAdapter<D, VH extends ViewHolder> extends
        RecyclerView.Adapter<VH> {

    protected OnItemClickListener onItemClickListener;
    private List<D> data = new ArrayList<>();
    protected boolean isClick = false;

    public BaseRecyclerViewAdapter() {
    }

    public BaseRecyclerViewAdapter(Context context) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public D getItem(int position) {
        return data.get(position);
    }

    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {
        if (isClick) return;
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void appendData(List<D> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void deleteData(int position) {
        this.data.remove(position);
        notifyItemRemoved(position);
//        if(position != data.size()){ // 如果移除的是最后一个，忽略
//            notifyItemRangeChanged(position, data.size() - position);
//        }
    }

    public void modifyData(int position, D data) {
        this.data.remove(position);
        this.data.add(position, data);
        notifyItemChanged(position);
    }


    /**
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Item click listener.
     */
    public interface OnItemClickListener {

        void onItemClick(int position);
    }


}
