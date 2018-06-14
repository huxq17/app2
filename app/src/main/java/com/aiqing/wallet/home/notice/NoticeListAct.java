package com.aiqing.wallet.home.notice;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.home.HomeBean;

import java.util.List;

public class NoticeListAct extends BaseActivity {
    private RecyclerView rvNoticeList;
    private NoticeListAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, NoticeListAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        setTitle(R.string.s_notice);
        showBackButton();
        rvNoticeList = findViewById(R.id.rv_notice_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvNoticeList.setLayoutManager(layoutManager);
        adapter = NoticeListAdapter.get();
        rvNoticeList.setAdapter(adapter);
        NoticeViewModel viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);
        viewModel.getNoticeListData().observe(this, new Observer<NoticeBean.DataBean>() {
            @Override
            public void onChanged(@Nullable NoticeBean.DataBean noticeBean) {
                List<HomeBean.DataBean.NoticeListBean> list = noticeBean.getList();
                if (list != null) {
                    adapter.setData(list);
                    adapter.setUrl(noticeBean.getUrl());
                }
            }
        });
    }
}
