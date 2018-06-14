package com.aiqing.wallet.firend;

import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.netease.nimlib.sdk.friend.model.Friend;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface FriendApi {
    @GET("/friend/list")
    Observable<Bean> getFriendList();


    class Bean extends BaseResponse<FriendBean.Friend> {
    }
}
