package com.aiqing.wallet.utils;


import com.aiqing.wallet.bean.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface VersionApi {
    @GET("/version")
    Observable<Bean> getAppVersion();

    class Bean extends BaseResponse<Bean.DataBean> {
        public static class DataBean {
            /**
             * android : {"url":"http://download.17kaiheiba.com/app/kaiheiba.apk","version":"1.0.0"}
             * ios : {"url":"http://itunes.apple.com/cn/lookup?id=1366109559","version":"1.0.0"}
             */

            private AndroidBean Android;

            public AndroidBean getAndroid() {
                return Android;
            }

            public static class AndroidBean {
                /**
                 * url : http://download.17kaiheiba.com/app/kaiheiba.apk
                 * version : 1.0.0
                 */

                private String Url;
                private String Version;

                public String getUrl() {
                    return Url;
                }


                public String getVersion() {
                    return Version;
                }

            }

        }
    }
}
