package com.aiqing.wallet.home.information;

import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.wallet.WalletBean;

import java.util.ArrayList;
import java.util.List;

public class InformationBean extends BaseResponse<InformationBean.Information> {
    public static class Information {
        int pageNum;
        int pageSize;
        int total;
        String url;
        ArrayList<DataBean> list;

        public static class DataBean {
            int ID;
            String Title;
            String Content;
            int Status;
            String CreatedAt;
            String ReleasedAt;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String title) {
                Title = title;
            }

            public String getContent() {
                return Content;
            }

            public void setContent(String content) {
                Content = content;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int status) {
                Status = status;
            }

            public String getCreatedAt() {
                return CreatedAt;
            }

            public void setCreatedAt(String createdAt) {
                CreatedAt = createdAt;
            }

            public String getReleasedAt() {
                return ReleasedAt;
            }

            public void setReleasedAt(String releasedAt) {
                ReleasedAt = releasedAt;
            }
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ArrayList<DataBean> getList() {
            return list;
        }

        public void setList(ArrayList<DataBean> list) {
            this.list = list;
        }
    }


}
