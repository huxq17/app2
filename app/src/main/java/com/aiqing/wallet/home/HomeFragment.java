package com.aiqing.wallet.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DiscreteScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScaleTransformer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.base.BaseFragment;
import com.aiqing.wallet.home.adapter.GalleryAdapter;
import com.aiqing.wallet.home.details.TradeRecordAct;
import com.aiqing.wallet.home.function.FunctionAdapter;
import com.aiqing.wallet.home.function.FunctionBean;
import com.aiqing.wallet.home.information.InformationActivity;
import com.aiqing.wallet.home.node.NodePlanActivity;
import com.aiqing.wallet.home.notice.NoticeListAct;
import com.aiqing.wallet.home.transferaccounts.TransferAccountsActivity;
import com.aiqing.wallet.widget.coordinatescroll.CollapsingTitleView;
import com.huxq17.handygridview.HandyGridView;
import com.xiaosu.view.text.DataSetAdapter;
import com.xiaosu.view.text.VerticalRollingTextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements DiscreteScrollView.CurrentItemChangeListener, FunctionBean.OnItemClickListener, View.OnClickListener {
    private View mView;
    private DiscreteScrollView banner;
    private GalleryAdapter bannerAdapter;
    private HandyGridView gridView;
    private VerticalRollingTextView tvNotice;
    private static final long BANNER_SCROLL_INTERVAL = 5000;
    private HomeViewModel homeViewModel;
    private View tvScan, tvScan2, tvCode, tvCode2, tvDetails, tvDetails2;
    private TextView tvRate;
    private String rate;
    private NestedScrollView nestedScrollView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getHomeData().observe(this, new Observer<HomeBean.DataBean>() {
            @Override
            public void onChanged(@Nullable HomeBean.DataBean dataBean) {
                if (dataBean == null) return;
                List<HomeBean.DataBean.BannerListBean> bannerList = dataBean.getBannerList();
                if (bannerList != null && bannerList.size() > 0) {
                    bannerAdapter.setData(bannerList);
                }

                List<HomeBean.DataBean.NoticeListBean> noticeList = dataBean.getNoticeList();
                if (noticeList != null && noticeList.size() > 0) {
                    final UnderlineSpan underlineSpan = new UnderlineSpan();
                    tvNotice.setDataSetAdapter(new DataSetAdapter<HomeBean.DataBean.NoticeListBean>(noticeList) {

                        @Override
                        protected CharSequence text(HomeBean.DataBean.NoticeListBean noticeItem) {
                            String title = noticeItem.getTitle();
                            if (TextUtils.isEmpty(title)) return "";
                            SpannableString span = new SpannableString(title);
                            span.setSpan(underlineSpan, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            return span;
                        }
                    });
                    tvNotice.run();
                    tvNotice.setOnClickListener(HomeFragment.this);
                }

                rate = dataBean.getUsdRate();
                tvRate.setText(getString(R.string.s_home_rate, "-"));
//                tvRate.setText(getString(R.string.s_home_rate, rate));
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
        setWindowStatusBarColor(Color.parseColor("#1DD07A"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frg_home_layout, container, false);
        tvDetails = mView.findViewById(R.id.tv_home_title_detail);
        tvScan = mView.findViewById(R.id.tv_home_title_scan);
        tvCode = mView.findViewById(R.id.tv_home_title_receivables);
        tvScan2 = mView.findViewById(R.id.iv_home_title_scan2);
        tvCode2 = mView.findViewById(R.id.iv_home_title_receivables2);
        tvDetails2 = mView.findViewById(R.id.iv_home_title_detail2);
        tvDetails.setOnClickListener(this);
        tvDetails2.setOnClickListener(this);
        tvScan.setOnClickListener(this);
        tvScan2.setOnClickListener(this);
        tvCode.setOnClickListener(this);
        tvCode2.setOnClickListener(this);
        tvRate = mView.findViewById(R.id.tv_home_rate);
        banner = mView.findViewById(R.id.item_picker);
        bannerAdapter = new GalleryAdapter();
        banner.setAdapter(bannerAdapter);
        banner.setItemTransitionTimeMillis(100);
        banner.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        banner.setCurrentItem(Integer.MAX_VALUE / 2);
        banner.setCurrentItemChangeListener(this);
        initFunction();

        tvNotice = mView.findViewById(R.id.verticalRollingView);
        final ViewGroup toolbar = mView.findViewById(R.id.toolbar);
        final View header = mView.findViewById(R.id.home_head);
        final CollapsingTitleView collapsingTitleView = mView.findViewById(R.id.home_app_bar);
        collapsingTitleView.setOnOffsetChangedListener(new CollapsingTitleView.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(CollapsingTitleView collapsingTitleView1, int verticalOffset) {
                int totalScrollRange = collapsingTitleView.getTotalScrollRange();
                int absOffset = Math.abs(verticalOffset);
                float alpha = absOffset * 1f / totalScrollRange;
                alpha = alpha > 1 ? 1 : alpha;
                toolbar.setAlpha(alpha);
                header.setAlpha(1 - alpha);
                toolbar.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);
                if (alpha == 0) {
                    setWindowStatusBarColor(Color.parseColor("#1DD07A"));
                } else if (alpha == 1) {
                    setWindowStatusBarColor(Color.parseColor("#1dbabf"));
                }
            }
        });
        nestedScrollView = mView.findViewById(R.id.nest_scrollView);
//        nestedScrollView.set();
        return mView;
    }

    private List<FunctionBean> mDatas = new ArrayList<>();

    private void initFunction() {
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_transfer), R.drawable.iv_home_transfer, this));
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_trade), R.drawable.iv_home_trade, this));
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_news), R.drawable.iv_home_news, this));
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_exp), R.drawable.iv_home_exp, this));
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_active_friend), R.drawable.iv_home_active_friend, this));
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_c2c), R.drawable.iv_home_c2c, this));
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_benefit), R.drawable.iv_home_benefit, this));
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_bonous), R.drawable.iv_home_bonus, this));
        this.mDatas.add(new FunctionBean(getString(R.string.s_home_more), R.drawable.iv_home_more, this));
        gridView = mView.findViewById(R.id.grid_fuction);
        gridView.setAdapter(new FunctionAdapter(getContext(), mDatas));
        gridView.setSelectorEnabled(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDatas.get(position).click();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        setWindowStatusBarColor(getContext().getResources().getColor(R.color.base_bg));
        banner.startLooper(BANNER_SCROLL_INTERVAL);
    }

    @Override
    public void onStop() {
        banner.stopLooper();
        super.onStop();
    }

    @Override
    public void onCurrentItemChanged(RecyclerView.ViewHolder viewHolder, int adapterPosition) {
    }

    @Override
    public void onItemClick(String text) {
        if (text.equals(getString(R.string.s_home_active_friend))) {
            ActiveFriendAct.start(getActivity());
        } else if (text.equals(getString(R.string.s_home_transfer))) {
            TransferAccountsActivity.start(getActivity(), rate);
        } else if (text.equals(getString(R.string.s_home_news))) {
            InformationActivity.start(getActivity());
        } else if (text.equals(getString(R.string.s_home_bonous))) {
            BonusAct.start(getActivity(), ApiManager.getStaticBaseUrl() + "activeList.html", getString(R.string.s_home_bonous));
        } /*else if(text.equals(getString(R.string.s_home_exp))){
            BonusAct.start(getActivity(), "http://101.132.186.109:3389/",getString(R.string.s_home_exp));
        }*/ else if (text.equals(getString(R.string.s_home_benefit))) {
            NodePlanActivity.start(getActivity(), rate);
        } else {
            toast(R.string.s_undeveloped);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvDetails || v == tvDetails2) {
            TradeRecordAct.start(getActivity(), rate, 1);
        } else if (v == tvNotice) {
            NoticeListAct.start(getActivity());
        } else {
            toast(R.string.s_undeveloped);
        }
    }
}