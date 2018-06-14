package com.aiqing.wallet.my.invitation;


import com.aiqing.wallet.bean.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InvitationsApi {
    @POST("/home/invitation/list")
    Observable<Bean> getMyInvitationsList(@Body RequestBody body);

    @POST("/home/invitation")
    Observable<Bean> friendInvitations(@Body RequestBody body);

    class Bean extends BaseResponse<InvitationsList> {
    }
}
