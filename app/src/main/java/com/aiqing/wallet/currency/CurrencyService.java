package com.aiqing.wallet.currency;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.home.HomeBean;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.alibaba.fastjson.JSONObject;
import com.huxq17.xprefs.XPrefs;

public class CurrencyService extends AndroidViewModel {
    private MutableLiveData<String> currencyData;

    public CurrencyService(@NonNull Application application) {
        super(application);
    }

    public String getCurrencyFromLocal() {
        return XPrefs.get(CurrencyBean.class).currency;
    }

    public void save(CurrencyBean currencyBean) {
        XPrefs.save(currencyBean, "currency");
        if(currencyData!=null)
        currencyData.setValue(currencyBean.currency);
    }

    public LiveData<String> getCurrency() {
        if (currencyData == null) {
            currencyData = new MutableLiveData<>();
            String currency = getCurrencyFromLocal();
            if (TextUtils.isEmpty(currency)) {
                ApiManager.getApi(CurrencyApi.class).getCurrency()
                        .compose(RxSchedulers.<CurrencyApi.Bean>compose())
                        .subscribe(new BaseObserver<CurrencyBean>() {
                            @Override
                            protected void onSuccess(CurrencyBean dataBean) {
                                String data = JSONObject.toJSONString(dataBean.getList());
                                dataBean.currency = data;
                                save(dataBean);
                            }

                            @Override
                            protected void onFailed(String msg) {
                                super.onFailed(msg);
                            }
                        });

            } else {
                currencyData.setValue(currency);
            }
        }
        return currencyData;
    }

}
