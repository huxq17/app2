package com.aiqing.wallet.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.home.notice.NoticeBean;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;

import java.util.LinkedHashMap;

public class HomeRepository {
    public static HomeRepository get() {
        return new HomeRepository();
    }

    public LiveData<HomeBean.DataBean> getHome() {
        final MutableLiveData<HomeBean.DataBean> data = new MutableLiveData<>();
        ApiManager.getApi(HomeApi.class).getHome()
                .compose(RxSchedulers.<HomeBean>compose())
                .subscribe(new BaseObserver<HomeBean.DataBean>() {
                    @Override
                    protected void onSuccess(HomeBean.DataBean dataBean) {
                        data.setValue(dataBean);
                    }
                });
        return data;
    }

    public LiveData<Boolean> activeFriend(String username) {
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("username", username);
        ApiManager.getApi(HomeApi.class).activeFriend(ApiTransform.transform(params))
                .compose(RxSchedulers.<HomeApi.Bean>compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccess(Object o) {
                        data.setValue(true);
                    }
                });
        return data;
    }

    public LiveData<NoticeBean.DataBean> getNotice() {
        final MutableLiveData<NoticeBean.DataBean> data = new MutableLiveData<>();
        ApiManager.getApi(HomeApi.class).getNotice(1000, 1)
                .compose(RxSchedulers.<NoticeBean>compose())
                .subscribe(new BaseObserver<NoticeBean.DataBean>() {
                    @Override
                    protected void onSuccess(NoticeBean.DataBean dataBean) {
                        if (dataBean != null )
                            data.setValue(dataBean);
                    }

                });
        return data;
    }
}
