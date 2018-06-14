package com.aiqing.wallet.home.notice;

import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.home.HomeBean;

import java.util.List;

public class NoticeBean extends BaseResponse<NoticeBean.DataBean> {

    public static class DataBean {

        private List<HomeBean.DataBean.NoticeListBean> list;
        private int pageNum;
        private int pageSize;
        private int total;
        private String url;

        public String getUrl() {
            return url;
        }

        public int getPageNum() {
            return pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotal() {
            return total;
        }


        public List<HomeBean.DataBean.NoticeListBean> getList() {
            return list;
        }

    }
}
