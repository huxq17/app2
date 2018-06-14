package com.aiqing.wallet.my.invitation;

/**
 * 邀请人列表实体类
 */
public class Invitations {
    String Nickname;//昵称
    String Asset;//持币数
    String Activity;//活跃度

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getAsset() {
        return Asset;
    }

    public void setAsset(String asset) {
        Asset = asset;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }
}
