package com.aiqing.wallet.home.information;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.home.details.OnItemClickListener;
import com.huxq17.xprefs.LogUtils;

import java.util.ArrayList;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<InformationBean.Information.DataBean> data = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private String url;

    public InformationAdapter(Context mContext, ArrayList<InformationBean.Information.DataBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.information_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.num.setText(String.valueOf(position + 1));
        holder.title.setText(data.get(position).getTitle());
        holder.time.setText(data.get(position).getReleasedAt().substring(0, 10));
        if (position < 3) {
            holder.num.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.num.setBackgroundResource(R.drawable.blue_circle);
        } else {
            holder.num.setTextColor(mContext.getResources().getColor((R.color.blue)));
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView num;
        TextView title;
        TextView time;
        OnItemClickListener mItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            num = (TextView) itemView.findViewById(R.id.num);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
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
