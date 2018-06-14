package com.aiqing.wallet.home.details;

import com.aiqing.wallet.bean.BaseResponse;

import java.util.List;

public class RecordBean extends BaseResponse<RecordBean.DataBean> {

    public static class DataBean {
        private List<Details> list;

        public List<Details> getList() {
            return list;
        }

        public static class Details {

            private int AccountID;
            private String Amount;
            private String Remark2;
            private String Type;
            private String CreatedAt;
            private String Rmb;
            private String Fee;

            private String Title;

            public boolean isIncome() {
                if (Type.equalsIgnoreCase("fee") || Type.equalsIgnoreCase("credit")) {
                    return true;
                }
                return false;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String title) {
                Title = title;
            }

            public int getAccountID() {
                return AccountID;
            }

            public String getAmount() {
                return Amount;
            }

            public String getType() {
                return Type;
            }

            public String getCreatedAt() {
                return CreatedAt;
            }

            public String getRmb() {
                return Rmb;
            }

            public void setRmb(String rmb) {
                Rmb = rmb;
            }

            public String getRemark2() {
                return Remark2;
            }

            public String getFee() {
                return Fee;
            }
        }
    }
}
