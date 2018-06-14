package com.aiqing.wallet.home.node;

import com.aiqing.wallet.bean.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NodePlanApi {
    @GET("/home/asset/earning")
    Observable<Bean> getList();


    @POST("/home/asset/earning")
    Observable<Bean> incomeExit(@Body RequestBody body);

    @GET("/home/asset/log")
    Observable<Bean> assetRecords(@Body RequestBody body);

    class Bean extends BaseResponse<NodePlanBean> {
    }
}
