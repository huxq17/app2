package com.aiqing.wallet.firend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.home.details.OnItemClickListener;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.huxq17.xprefs.LogUtils;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends BaseActivity {
    private RecyclerView mRv;
    private FriendAdapter mAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;
    private List<FriendBean.Friend.DataBean> mDatas = new ArrayList<>();//列表数据
    private List<FriendBean.Friend.DataBean> searchDatas = new ArrayList<>();//搜索数据
    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    private SearchView searchView;
    private TextView no_search_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.choose_friend));
        showBackButton();
        initView();
        initList();
        getFriendData();
        searchFriend();
    }

    private void initView() {
        searchView = findViewById(R.id.searchView);
        no_search_data = findViewById(R.id.no_search_data);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar
    }

    private void initList() {
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mAdapter = new FriendAdapter(this, mDatas);
        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("friend", mDatas.get(position));
                setResult(0, intent);
                finish();
            }
        });
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
            }
        };
        mRv.setAdapter(mHeaderAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mDatas));
        mRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
    }

    private void getFriendData() {
        ApiManager.getApi(FriendApi.class).getFriendList()
                .compose(RxSchedulers.<FriendApi.Bean>compose())
                .subscribe(new BaseObserver<FriendBean.Friend>() {
                    @Override
                    protected void onSuccess(FriendBean.Friend dataBean) {
                        mDatas.clear();
                        mDatas.addAll(dataBean.list);
                        if (mDatas.size() > 0) {
                            no_search_data.setVisibility(View.GONE);
                            mIndexBar.setmSourceDatas(mDatas)//设置数据
                                    .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount())//设置HeaderView数量
                                    .invalidate();
                            mAdapter.setDatas(mDatas);
                            mHeaderAdapter.notifyDataSetChanged();
                            mDecoration.setmDatas(mDatas);
                        } else {
                            no_search_data.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }

    private void searchFriend() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEmpty(s.toString())) {
                    getFriendData();
                } else {
                    getSearchData(s.toString());
                }
            }
        });
    }

    private void getSearchData(String s) {
        searchDatas.clear();
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).Nickname.indexOf(s) != -1) {
                searchDatas.add(mDatas.get(i));
            }
        }
        if (searchDatas.size() > 0) {
            mDatas.clear();
            no_search_data.setVisibility(View.GONE);
            mDatas.addAll(searchDatas);
            mIndexBar.setmSourceDatas(mDatas)
                    .invalidate();
            mHeaderAdapter.notifyDataSetChanged();
        } else {
            no_search_data.setVisibility(View.VISIBLE);
        }
    }


}
