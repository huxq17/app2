package com.aiqing.wallet.home.details;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.utils.Utils;
import com.huxq17.xprefs.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TradeRecordAdapter extends RecyclerView.Adapter<TradeRecordAdapter.ViewHolder> {
    private List<RecordBean.DataBean.Details> mDatas = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private Context context;
    private int type;

    public TradeRecordAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void setData(List<RecordBean.DataBean.Details> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<RecordBean.DataBean.Details> getDatas() {
        return mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_trade_item, parent, false);
        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(TradeRecordAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        RecordBean.DataBean.Details resultBean = mDatas.get(position);
        switch (type) {
            case 1:
                if (resultBean.getType().equals("fee")) {
                    holder.tradeName.setText(context.getString(R.string.service_charge));
                } else if (resultBean.getType().equals("credit")) {
                    holder.tradeName.setText(context.getString(R.string.transfer_account));
                } else if (resultBean.getType().equals("debit")) {
                    holder.tradeName.setText(context.getString(R.string.transfer));
                }
                break;
            case 2:
                holder.tradeName.setText(resultBean.getTitle());
                break;
        }
        holder.tvDate.setText(resultBean.getCreatedAt().substring(0, 10));
        holder.tvRmb.setText("≈ $ " + Utils.keepTwoBits(Double.valueOf(resultBean.getRmb())));
        if (resultBean.getAmount().indexOf("-") == -1) {
            String m = getAmount(resultBean.getAmount(), resultBean.getFee());
            holder.tvMoney.setText("+" + Utils.keepFourBits(Double.valueOf(m)) + " " + context.getString(R.string.vdo));
            holder.tvMoney.setTextColor(Color.parseColor("#ff8106"));
        } else {
            String m = getAmount(resultBean.getAmount(), resultBean.getFee());
            holder.tvMoney.setText(Utils.keepFourBits(Double.valueOf(m)) + " " + context.getString(R.string.vdo));
            holder.tvMoney.setTextColor(Color.parseColor("#4D4D4D"));
        }
    }

    private String getAmount(String a, String b) {
        return String.valueOf(Utils.adddouble(Double.valueOf(a), Double.valueOf(b)));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tradeName;
        TextView tvDate;
        TextView tvMoney;
        TextView tvRmb;
        OnItemClickListener mItemClickListener;

        public ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            tradeName = view.findViewById(R.id.v_trade_name);
            tvDate = view.findViewById(R.id.v_trade_time);
            tvMoney = view.findViewById(R.id.v_trade_money);
            tvRmb = view.findViewById(R.id.v_trade_rmb);
            this.mItemClickListener = onItemClickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getPosition());
            }
        }
    }

}