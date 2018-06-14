package com.aiqing.wallet.home.information;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.home.details.OnItemClickListener;
import com.aiqing.wallet.home.notice.DetailsAct;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;

import java.util.ArrayList;

/**
 * 资讯
 */
public class InformationActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener {
    RecyclerView recyclerView;
    ArrayList<InformationBean.Information.DataBean> data = new ArrayList<>();
    InformationAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, InformationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        initView();
        initRecyclerView();
        getInformationList();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initRecyclerView() {
        adapter = new InformationAdapter(this, data);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        int id = data.get(position).ID;
        String url = new StringBuilder(adapter.getUrl()).append("&id=").append(id).toString();
        DetailsAct.start(this, url, 2);
    }

    private void getInformationList() {
        ApiManager.getApi(InformationApi.class).getList()
                .compose(RxSchedulers.<InformationApi.Bean>compose())
                .subscribe(new BaseObserver<InformationBean.Information>() {
                    @Override
                    protected void onSuccess(InformationBean.Information information) {
                        data.addAll(information.list);
                        adapter.setUrl(information.url);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }
}
