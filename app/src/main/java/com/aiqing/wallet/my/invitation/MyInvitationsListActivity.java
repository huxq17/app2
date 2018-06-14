package com.aiqing.wallet.my.invitation;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.adapter.InvitationsAdapter;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.huxq17.xprefs.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MyInvitationsListActivity extends BaseActivity {
    private TextView my_inviter;
    RecyclerView recyclerView;
    List<Invitations> data = new ArrayList<>();
    InvitationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invitations_list);
        initView();
        initRecyclerView();
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.invitation_list_title));
        showBackButton();
        initData();
    }

    private void initData() {
        HashMap<String, Object> params = new HashMap<>();
        ApiManager.getApi(InvitationsApi.class).getMyInvitationsList(ApiTransform.transform(params))
                .compose(RxSchedulers.<InvitationsApi.Bean>compose())
                .subscribe(new BaseObserver<InvitationsList>() {
                    @Override
                    protected void onSuccess(InvitationsList invitationsList) {
                        my_inviter.setText(getResources().getString(R.string.inviter) + invitationsList.getInviter());
                        data.addAll(invitationsList.getList());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }

    private void initView() {
        my_inviter = findViewById(R.id.my_inviter);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void initRecyclerView() {
        adapter = new InvitationsAdapter(MyInvitationsListActivity.this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
