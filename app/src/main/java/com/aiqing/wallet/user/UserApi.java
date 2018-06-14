package com.aiqing.wallet.user;

import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.login.LoginApi;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserApi {
    @POST("/home/mobile/update")
    Observable<Bean> resetMobile(@Body RequestBody body);

    @POST("/home/password/reset")
    Observable<Bean> resetLoginPass(@Body RequestBody body);

    @POST("/home/tradePassword/set")
    Observable<Bean> setTradePass(@Body RequestBody body);

    @POST("/home/tradePassword/reset")
    Observable<Bean> resetTradePass(@Body RequestBody body);

    @POST("/home/tradePassword/forgot")
    Observable<Bean> findTradePass(@Body RequestBody body);

    @GET("/home/home")
    Observable<Bean> getMyHome(@QueryMap Map<String, String> params);

    @Multipart
    @POST("/home/avatar/update")
    Observable<Bean> uploadFile(@Part MultipartBody.Part MultipartFile);

    @POST("/home/realAuth")
    Observable<Bean> Authentication(@QueryMap Map<String, String> params, @Body MultipartBody imgs);


    @GET("/account/{id}")
    Observable<Bean> getUser(@Path("id") int id, @QueryMap Map<String, String> params);

    @GET("/account/find")
    Observable<Bean> findUser(@QueryMap Map<String, String> params);

    @POST("/home/info/update")
    Observable<Bean> updataMsg(@Body RequestBody body);


    @POST("/home/asset")
    Observable<Bean> asset(@Body RequestBody body);

    class Bean extends BaseResponse<User> {
    }
}
