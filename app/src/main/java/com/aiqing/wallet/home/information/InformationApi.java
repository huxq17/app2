package com.aiqing.wallet.home.information;

import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InformationApi {
    @GET("/news")
    Observable<Bean> getList();

    class Bean extends BaseResponse<InformationBean.Information> {
    }
}
