package com.aiqing.wallet.wallet;

import com.aiqing.wallet.bean.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WalletApi {
    @GET("home/asset?format=1")
    Observable<Bean> getAsset();


    class Bean extends BaseResponse<WalletBean.Wallet> {
    }
}
