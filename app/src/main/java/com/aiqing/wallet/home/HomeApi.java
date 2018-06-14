package com.aiqing.wallet.home;


import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.home.details.RecordBean;
import com.aiqing.wallet.home.notice.NoticeBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HomeApi {

    @GET("/index")
    Observable<HomeBean> getHome();


    @POST("/home/activeFriend")
    Observable<Bean> activeFriend(@Body RequestBody body);

    @GET("/home/asset/log")
    Observable<RecordBean> queryAccountBook(@Query("month") String month, @Query("format") String format);

    @GET("/home/asset/log")
    Observable<RecordBean> queryAccountBook(@Query("month") String month, @Query("format") String format, @Query("earning") String earning);

    @GET("/notice")
    Observable<NoticeBean> getNotice(@Query("pageSize") int pageSize, @Query("format") int pageNum);
    class Bean extends BaseResponse<Object> {
    }


}
