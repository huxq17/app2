package com.aiqing.wallet.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseRecyclerViewAdapter;
import com.aiqing.wallet.home.HomeBean;
import com.aiqing.wallet.home.notice.DetailsAct;
import com.aiqing.wallet.imageloader.ImageLoader;
import com.buyi.huxq17.serviceagency.ServiceAgency;

public class GalleryAdapter extends BaseRecyclerViewAdapter<HomeBean.DataBean.BannerListBean, GalleryAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int size = getData().size();
        if (size == 0) return;
        int index = position % size;
        ServiceAgency.getService(ImageLoader.class).loadImage(getItem(index).getPath(), holder.image, 0, 0);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;


        public ViewHolder(View itemView, final GalleryAdapter adapter) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int size = adapter.getData().size();
                    if (size == 0) return;
                    int position = getAdapterPosition();
                    String url = adapter.getItem(position % size).getURL();
                    DetailsAct.start(ViewHolder.this.itemView.getContext(), url, 1);
                }
            });
        }
    }
}
