package com.aiqing.wallet.user;


import android.text.TextUtils;

import com.huxq17.xprefs.XPrefs;

public class UserService {
    public static String getUserId() {
        User user = XPrefs.get(User.class);
        return user.ID;
    }

    public static String getAccount() {
        User user = XPrefs.get(User.class);
        return user.Accid;
    }

    public static String getIMToken() {
        User user = XPrefs.get(User.class);
        return user.ImToken;
    }

    public static String isRealAuth() {
        User user = XPrefs.get(User.class);
        return user.IsRealAuth;
    }

    public static String getAppKey() {
        User user = XPrefs.get(User.class);
        return user.appKey;
    }

    public static boolean isLogin() {
        User user = XPrefs.get(User.class);
        return !TextUtils.isEmpty(user.Token);
    }

    public static String getMobile() {
        User user = XPrefs.get(User.class);
        return user.Mobile;
    }

    public static void setMobile(String mobile) {
        User user = XPrefs.get(User.class);
        user.Mobile = mobile;
        XPrefs.saveAll(user);
    }

    public static String getUserToken() {
        User user = XPrefs.get(User.class);
        return user.Token;
    }

    public static void setUserName(String Username) {
        User user = XPrefs.get(User.class);
        user.Username = Username;
        XPrefs.saveAll(user);
    }

    public static String getUserName() {
        User user = XPrefs.get(User.class);
        return user.Username;
    }

    public static String getNickName() {
        User user = XPrefs.get(User.class);
        return user.Nickname;
    }

    public static String getIsRealAuth() {
        User user = XPrefs.get(User.class);
        return user.IsRealAuth;
    }

    public static void setNickName(String Nickname) {
        User user = XPrefs.get(User.class);
        user.Nickname = Nickname;
        XPrefs.saveAll(user);
    }


    public static void setAvatar(String avatar) {
        User user = XPrefs.get(User.class);
        user.Avatar = avatar;
        XPrefs.saveAll(user);
    }

    public static void setIsRealAuth(String auth) {
        User user = XPrefs.get(User.class);
        user.IsRealAuth = auth;
        XPrefs.saveAll(user);
    }

    public static String getAvatar() {
        User user = XPrefs.get(User.class);
        return user.Avatar;
    }

    public static int getTradePasswordStatus() {
        User user = XPrefs.get(User.class);
        return user.TradePasswordStatus;
    }

    public static void setTradePasswordStatus(int state) {
        User user = XPrefs.get(User.class);
        user.TradePasswordStatus = state;
        XPrefs.saveAll(user);
    }

    public static String getID() {
        User user = XPrefs.get(User.class);
        return user.ID;
    }

    public static void setID(String ID) {
        User user = XPrefs.get(User.class);
        user.ID = ID;
        XPrefs.saveAll(user);
    }

    public static String getInviteCode() {
        User user = XPrefs.get(User.class);
        return user.InviteCode;
    }

    public static void setInviteCode(String inviteCode) {
        User user = XPrefs.get(User.class);
        user.InviteCode = inviteCode;
        XPrefs.saveAll(user);
    }

    public static void save(User user) {
        XPrefs.saveAll(user);
    }
}
