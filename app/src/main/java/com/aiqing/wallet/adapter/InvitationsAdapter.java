package com.aiqing.wallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.my.invitation.Invitations;
import com.aiqing.wallet.utils.Utils;

import java.util.List;

public class InvitationsAdapter extends RecyclerView.Adapter<InvitationsAdapter.InvitationsViewHolder> {
    private Context mContext;
    private List<Invitations> data;


    public InvitationsAdapter(Context mContext, List<Invitations> data) {
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
        holder.nickname.setText(data.get(position).getNickname());
        holder.amountMoney.setText(Utils.keepTwoBits(Double.valueOf(data.get(position).getAsset())));
        holder.active.setText(data.get(position).getActivity());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class InvitationsViewHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        TextView amountMoney;
        TextView active;

        public InvitationsViewHolder(View itemView) {
            super(itemView);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            amountMoney = (TextView) itemView.findViewById(R.id.amountMoney);
            active = (TextView) itemView.findViewById(R.id.active);
        }
    }
}