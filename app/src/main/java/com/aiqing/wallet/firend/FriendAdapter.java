package com.aiqing.wallet.firend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiqing.wallet.R;
import com.aiqing.wallet.home.details.OnItemClickListener;
import com.aiqing.wallet.imageloader.ImageLoader;
import com.buyi.huxq17.serviceagency.ServiceAgency;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    protected Context mContext;
    protected List<FriendBean.Friend.DataBean> mDatas;
    protected LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;

    public FriendAdapter(Context mContext, List<FriendBean.Friend.DataBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<FriendBean.Friend.DataBean> getDatas() {
        return mDatas;
    }

    public FriendAdapter setDatas(List<FriendBean.Friend.DataBean> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.friend_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.nickName.setText(mDatas.get(position).getNickname());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
        ServiceAgency.getService(ImageLoader.class).loadImage(mDatas.get(position).getAvatar(), holder.avatar);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickName;
        ImageView avatar;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            nickName = (TextView) itemView.findViewById(R.id.nickName);
            avatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            content = itemView.findViewById(R.id.content);
        }
    }
}
