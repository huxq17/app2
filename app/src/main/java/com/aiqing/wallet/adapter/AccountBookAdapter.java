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
import com.aiqing.wallet.modle.AccountBook;

import java.util.List;

public class AccountBookAdapter extends RecyclerView.Adapter<AccountBookAdapter.InvitationsViewHolder> {
    private Context mContext;
    private List<AccountBook> data;
    private OnItemClickListener mItemClickListener;

    public AccountBookAdapter(Context mContext, List<AccountBook> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public InvitationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.invitations_item, parent, false);
        InvitationsViewHolder holder = new InvitationsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationsViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.vdo.setText(data.get(position).getVdo());
        holder.time.setText(data.get(position).getTime());
        holder.rmb.setText(data.get(position).getRmb());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class InvitationsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView vdo;
        TextView time;
        TextView rmb;

        public InvitationsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            vdo = (TextView) itemView.findViewById(R.id.vdo);
            time = (TextView) itemView.findViewById(R.id.time);
            rmb = (TextView) itemView.findViewById(R.id.rmb);
        }
    }
}