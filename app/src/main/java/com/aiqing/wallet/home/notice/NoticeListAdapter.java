package com.aiqing.wallet.home.notice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseRecyclerViewAdapter;
import com.aiqing.wallet.home.HomeBean;

public class NoticeListAdapter extends BaseRecyclerViewAdapter<HomeBean.DataBean.NoticeListBean, NoticeListAdapter.ViewHolder> {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public static NoticeListAdapter get() {
        return new NoticeListAdapter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, null);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeBean.DataBean.NoticeListBean data = getItem(position);
        holder.tvNoticeTitle.setText(data.getTitle());
        holder.tvNoticeDate.setText(data.getCreatedAt());
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvNoticeTitle, tvNoticeDate;
        private NoticeListAdapter adapter;

        public ViewHolder(View itemView, NoticeListAdapter adapter) {
            super(itemView);
            tvNoticeTitle = itemView.findViewById(R.id.v_notice_title);
            tvNoticeDate = itemView.findViewById(R.id.v_notice_date);
            itemView.setOnClickListener(this);
            this.adapter = adapter;
        }

        @Override
        public void onClick(View v) {
            int id = adapter.getItem(getAdapterPosition()).getID();
            String url = new StringBuilder(adapter.url).append("&id=").append(id).toString();
            DetailsAct.start(itemView.getContext(), url, 3);
        }
    }
}
