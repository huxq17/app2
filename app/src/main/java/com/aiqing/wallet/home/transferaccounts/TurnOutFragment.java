package com.aiqing.wallet.home.transferaccounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiqing.wallet.R;
import com.aiqing.wallet.adapter.AccountBookAdapter;
import com.aiqing.wallet.modle.AccountBook;

import java.util.ArrayList;
import java.util.List;

public class TurnOutFragment extends Fragment {
    List<AccountBook> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private AccountBookAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_turn_out, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initRecyclerView();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
    }

    private void initRecyclerView() {
        adapter = new AccountBookAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
