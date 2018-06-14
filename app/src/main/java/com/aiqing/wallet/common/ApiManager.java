package com.aiqing.wallet.common;


import android.text.TextUtils;

import com.aiqing.wallet.App;
import com.aiqing.wallet.BuildConfig;
import com.aiqing.wallet.bean.Code;
import com.aiqing.wallet.login.LoginAct;
import com.huxq17.xprefs.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory2;

public class ApiManager {
    private final HashMap<Class, Object> mCachedApi = new LinkedHashMap<>();
    private OkHttpClient okHttpClient;
    private static ApiManager INSTANCE;

    private ApiManager(Creator creator) {
        if (TextUtils.isEmpty(BuildConfig.BASE_URL)) {
            new AssertionError("config base url first.");
        }
        INSTANCE = this;
    }

    public static final class Creator {
        public void initialize() {
            new ApiManager(this);
        }
    }

    public static OkHttpClient getOkHttpClient() {
        if (INSTANCE == null) {
            new AssertionError("please build ApiManger first.");
        }
        if (INSTANCE.okHttpClient == null) {
            HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtils.d(message);
                }
            });
            loggingInterceptor.setLevel(level);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(7, TimeUnit.SECONDS)
                    .readTimeout(7, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response response = chain.proceed(chain.request());
//                            //存入Session
//                            if (response.header("Set-Cookie") != null) {
//                                SessionManager.setSession(response.header("Set-Cookie"));
//                            }
//                            SessionManager.setLastApiCallTime(System.currentTimeMillis());
                            if (response.code() == Code.ERROR_HTTP_CODE_UNAUTHORIZED) {
                                LoginAct.start(App.getContext(), true);
                            }
                            return response;
                        }

                    })
                    .addInterceptor(new NetworkInterceptor());
            if (BuildConfig.DEBUG) {
            }
            INSTANCE.okHttpClient = builder.build();
        }
        return INSTANCE.okHttpClient;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getApi(Class<T> tClass, String baseUrl) {
        if (INSTANCE == null) {
            new AssertionError("please build ApiManger first.");
        }
        Object cacheApi = INSTANCE.mCachedApi.get(tClass);
        T api = null;
        if (cacheApi != null && cacheApi.getClass() == tClass) {
            api = (T) cacheApi;
        }
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getOkHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory2.create())
                    .build();
            api = retrofit.create(tClass);
            INSTANCE.mCachedApi.put(tClass, api);
        }
        return api;
    }


    public static <T> T getApi(Class<T> apiClass) {
        if (INSTANCE == null) {
            new AssertionError("please build ApiManger first.");
        }
        String baseUrl = INSTANCE.getBaseUrl();
        return getApi(apiClass, baseUrl);
    }

    public static String getBaseUrl() {
        if (INSTANCE == null) {
            new AssertionError("please build ApiManger first.");
        }
        if (BuildConfig.IS_TEST) {
            return BuildConfig.TEST_BASE_URL;
        } else {
            return BuildConfig.BASE_URL;
        }
    }

    public static String getStaticBaseUrl() {
        return getBaseUrl() + "static/";
    }

    public void clear() {
        mCachedApi.clear();
    }
}
