package com.aiqing.wallet.wallet;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.adapter.WalletAdapter;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.base.BaseFragment;
import com.aiqing.wallet.currency.CurrencyService;
import com.aiqing.wallet.home.details.OnItemClickListener;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WalletFragment extends BaseFragment implements OnItemClickListener {
    TextView navTitle;
    TextView asset;
    private View mView;
    RecyclerView recyclerView;
    List<WalletBean.Wallet.DataBean> data = new ArrayList<>();
    WalletAdapter adapter;
    TextView currency;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frg_wallet_layout, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initRecyclerView();
    }

    @Override
    protected void onShow() {
        super.onShow();
        getAsset();
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
    }

    private void getAsset() {
        ViewModelProviders.of(this).get(CurrencyService.class).getCurrency().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable String currency) {
                adapter.setCurrency(currency);
            }
        });
        ApiManager.getApi(WalletApi.class).getAsset()
                .compose(RxSchedulers.<WalletApi.Bean>compose())
                .subscribe(new BaseObserver<WalletBean.Wallet>() {
                    @Override
                    protected void onSuccess(WalletBean.Wallet wallet) {
                        asset.setText(getString(R.string.total_assets) + Utils.keepTwoBits(wallet.getSum()));
                        data.addAll(wallet.getList());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }

    private void initView() {
        navTitle = mView.findViewById(R.id.nav_title);
        navTitle.setText(getString(R.string.s_bottom_wallet));
        asset = mView.findViewById(R.id.asset);
        recyclerView = getActivity().findViewById(R.id.recyclerView);
        currency = getActivity().findViewById(R.id.currency);
    }

    private void initRecyclerView() {
        adapter = new WalletAdapter(getActivity(), data);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onItemClick(int position) {
//        startActivity(new Intent(getActivity(), MoneyDetailsActivity.class));
    }
}
