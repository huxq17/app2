package com.aiqing.wallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.home.details.OnItemClickListener;
import com.aiqing.wallet.utils.Utils;
import com.aiqing.wallet.wallet.WalletBean;
import com.huxq17.xprefs.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.InvitationsViewHolder> {
    private Context mContext;
    List<WalletBean.Wallet.DataBean> data;
    private OnItemClickListener mItemClickListener;

    public WalletAdapter(Context mContext, List<WalletBean.Wallet.DataBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    String mCurrency;

    public void setCurrency(String currency) {
        mCurrency = currency;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InvitationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wallet_item, parent, false);
        InvitationsViewHolder holder = new InvitationsViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationsViewHolder holder, int position) {
        String id = data.get(position).getCurrency();
        holder.vdo.setText(getName(id));
        holder.vdo_num.setText(Utils.keepFourBits(data.get(position).getBalance()) + mContext.getString(R.string.vdo));
        holder.usd.setText(mContext.getString(R.string.approximately) + Utils.keepTwoBits(Double.valueOf(data.get(position).getExchangeRate())) + " " + mContext.getString(R.string.usd));
        holder.usd_num.setText(mContext.getString(R.string.approximately) + Utils.keepTwoBits(data.get(position).getValuation()) + " " + mContext.getString(R.string.usd));
    }

    private String getName(String id) {
        try {
            JSONArray jsonArray = new JSONArray(mCurrency);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                if (jsonObject.optString("iD").equals(id)) {
                    return jsonObject.optString("name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class InvitationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vdo;
        TextView vdo_num;
        TextView usd;
        TextView usd_num;
        OnItemClickListener mItemClickListener;

        public InvitationsViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            vdo = (TextView) itemView.findViewById(R.id.vdo);
            vdo_num = (TextView) itemView.findViewById(R.id.vdo_num);
            usd = (TextView) itemView.findViewById(R.id.usd);
            usd_num = (TextView) itemView.findViewById(R.id.usd_num);
            this.mItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getPosition());
            }
        }
    }
}