package com.aiqing.wallet.currency;

import com.aiqing.wallet.bean.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface CurrencyApi {
    @GET("/currency")
    Observable<Bean> getCurrency();

    class Bean extends BaseResponse<CurrencyBean> {
    }
}
