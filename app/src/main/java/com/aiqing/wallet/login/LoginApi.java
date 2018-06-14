package com.aiqing.wallet.login;


import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserService;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("/verifyCode")
    Observable<Bean> obtainMobileCode(@Body RequestBody body);

    @POST("/register")
    Observable<Bean> register(@Body RequestBody body);

    @POST("/login")
    Observable<Bean> login(@Body RequestBody body);

    @POST("/member/password/reset")
    Observable<Bean> updatePass(@Body RequestBody body);

    @POST("/password/forgot")
    Observable<Bean> resetPass(@Body RequestBody body);

    class Bean extends BaseResponse<User> {
    }
}
