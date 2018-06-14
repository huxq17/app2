package com.aiqing.wallet.home;

import android.text.TextUtils;

import com.aiqing.wallet.bean.BaseResponse;

import java.util.List;

public class HomeBean extends BaseResponse<HomeBean.DataBean> {

    public static class DataBean {
        private int bannerTotal;
        private int noticeTotal;
        private String usdRate;
        private List<BannerListBean> bannerList;
        private List<NoticeListBean> noticeList;

        public int getBannerTotal() {
            return bannerTotal;
        }

        public int getNoticeTotal() {
            return noticeTotal;
        }

        public String getUsdRate() {
            return usdRate;
        }

        public List<BannerListBean> getBannerList() {
            return bannerList;
        }

        public List<NoticeListBean> getNoticeList() {
            return noticeList;
        }

        public static class BannerListBean {
            private int ID;
            private int BannerPositionID;
            private String Path;
            private String URL;
            private String CreatedAt;

            public int getID() {
                return ID;
            }

            public int getBannerPositionID() {
                return BannerPositionID;
            }

            public String getPath() {
                return Path;
            }

            public String getURL() {
                return URL;
            }

            public String getCreatedAt() {
                return CreatedAt;
            }

        }

        public static class NoticeListBean {
            /**
             * ID : 2
             * Title : vdo
             * Content : hahahhahahahahhah
             * CreatedAt : 2018-05-24T13:39:39+08:00
             */

            private int ID;
            private String Title;
            private String Content;
            private String CreatedAt;
            private String ReleasedAt;

            public int getID() {
                return ID;
            }

            public String getTitle() {
                return Title;
            }

            public String getContent() {
                return Content;
            }

            public String getCreatedAt() {
                if (!TextUtils.isEmpty(ReleasedAt)) {
                    return ReleasedAt.substring(0, 10);
                }
                return ReleasedAt;
            }
        }
    }
}
