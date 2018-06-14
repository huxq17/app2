package com.aiqing.wallet.common;


import android.text.TextUtils;

import com.aiqing.basiclibrary.utils.Device;
import com.aiqing.basiclibrary.utils.MD5;
import com.aiqing.basiclibrary.utils.Utils;
import com.aiqing.wallet.App;
import com.aiqing.wallet.user.UserService;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
//        HttpUrl url = chain.request().url().newBuilder()
//                .addQueryParameter("udid", Device.getDeviceId(App.getContext()))
//                .build();

        Request.Builder newBuilder = originalRequest.newBuilder();

        String userToken = UserService.getUserToken();
        if (!TextUtils.isEmpty(userToken)) {
            newBuilder.header("X-Auth-Token", userToken);
            newBuilder.header("X-Auth-Udid", Device.getDeviceId(App.getContext()));
        }
        return chain.proceed(newBuilder.build());
    }

    private String sign(LinkedHashMap<String, Object> params) {
        String str = Utils.sortHashMap(params);
        return MD5.MD5WithAiyou(str);
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
