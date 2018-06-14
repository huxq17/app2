package com.aiqing.wallet.currency;

import com.aiqing.wallet.bean.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RateApi {
    @GET("/currency/{id}/rate")
    Observable<Bean> getRate(@Path("id") int id);

    class Bean extends BaseResponse<RateBean> {
    }
}
