package com.aiqing.wallet.firend;

import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.wallet.WalletBean;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.netease.nimlib.sdk.friend.model.Friend;

import java.io.Serializable;
import java.util.List;


public class FriendBean extends BaseResponse<FriendBean.Friend> {
    public static class Friend {
        List<DataBean> list;

        public List<DataBean> getList() {
            return list;
        }

        public void setList(List<DataBean> list) {
            this.list = list;
        }

        public static class DataBean extends BaseIndexPinyinBean implements Serializable {
            int ID;
            String Username;
            String Mobile;
            String Nickname;
            String Avatar;
            int Gender;

            @Override
            public String getTarget() {
                return Username;
            }

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getUsername() {
                return Username;
            }

            public void setUsername(String username) {
                Username = username;
            }

            public String getMobile() {
                return Mobile;
            }

            public void setMobile(String mobile) {
                Mobile = mobile;
            }

            public String getNickname() {
                return Nickname;
            }

            public void setNickname(String nickname) {
                Nickname = nickname;
            }

            public String getAvatar() {
                return Avatar;
            }

            public void setAvatar(String avatar) {
                Avatar = avatar;
            }

            public int getGender() {
                return Gender;
            }

            public void setGender(int gender) {
                Gender = gender;
            }
        }


    }
}




