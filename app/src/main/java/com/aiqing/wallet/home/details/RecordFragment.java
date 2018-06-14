package com.aiqing.wallet.home.details;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiqing.wallet.R;
import com.aiqing.wallet.home.node.NodeDetailsActivity;
import com.aiqing.wallet.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends android.support.v4.app.Fragment implements OnItemClickListener {
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "";
    private RecyclerView recyclerView;
    private TradeRecordAdapter mAdapter;
    private int type;//1:首页，2节点

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_TITLE);
            type = arguments.getInt("type");
        }
        recyclerView = new RecyclerView(getActivity());
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutmanager);
        mAdapter = new TradeRecordAdapter(getActivity(), type);
        mAdapter.setItemClickListener(this);
        recyclerView.setBackgroundColor(Color.WHITE);
        recyclerView.setAdapter(mAdapter);
        isCreatedView = true;
        refreshData(mDataList);
        return recyclerView;
    }

    @Override
    public void onItemClick(int position) {
        List<RecordBean.DataBean.Details> datas = mAdapter.getDatas();
        if (type == 1) {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Type", datas.get(position).getType());
            bundle.putString("Amount", datas.get(position).getAmount());
            bundle.putInt("AccountID", datas.get(position).getAccountID());
            bundle.putString("Remark2", datas.get(position).getRemark2());
            bundle.putString("Fee", datas.get(position).getFee());
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), NodeDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Title", datas.get(position).getTitle());
            String m = String.valueOf(Utils.adddouble(Double.valueOf(datas.get(position).getAmount()), Double.valueOf(datas.get(position).getFee())));
            bundle.putString("Amount", m);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private boolean isCreatedView = false;
    List<RecordBean.DataBean.Details> mDataList = new ArrayList<>();

    public void refreshData(List<RecordBean.DataBean.Details> dataList) {
        mDataList = dataList;
        if (!isCreatedView || dataList == null) {
            return;
        }
        List<RecordBean.DataBean.Details> list = new ArrayList<>();
        for (RecordBean.DataBean.Details resultBean : dataList) {
            if (mTitle.equals(getString(R.string.s_details_all))) {
                resultBean = getRmb(resultBean);
                list.add(resultBean);
            } else if (mTitle.equals(getString(R.string.s_details_in))) {
                if (type == 1) {
//                    if (resultBean.isIncome()) {
//                        resultBean = getRmb(resultBean);
//                        list.add(resultBean);
//                    }
                    if (resultBean.getType().equals(getString(R.string.debit))) {
                        resultBean = getRmb(resultBean);
                        list.add(resultBean);
                    }
                } else {
                    if (resultBean.getAmount().indexOf("-") == -1) {
                        list.add(resultBean);
                    }
                }
            } else if (mTitle.equals(getString(R.string.s_details_out))) {
                if (type == 1) {
//                    if (!resultBean.isIncome()) {
//                        resultBean = getRmb(resultBean);
//                        list.add(resultBean);
//                    }
                    if (resultBean.getType().equals(getString(R.string.credit))) {
                        resultBean = getRmb(resultBean);
                        list.add(resultBean);
                    }
                } else {
                    if (resultBean.getAmount().indexOf("-") >= 0) {
                        list.add(resultBean);
                    }
                }

            }
        }
        mAdapter.setData(list);
    }

    private RecordBean.DataBean.Details getRmb(RecordBean.DataBean.Details resultBean) {
        double amount = Double.valueOf(resultBean.getAmount());
        double rate = Double.valueOf(((TradeRecordAct) getActivity()).getRate());
        resultBean.setRmb(String.valueOf(Utils.Multiply(amount, rate)));
        return resultBean;
    }


    public static RecordFragment newInstance(String title, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        bundle.putInt("type", type);
        RecordFragment fragment = new RecordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
