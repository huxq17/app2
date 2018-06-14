package com.aiqing.wallet.user;


public class User {
    public String ID = "";
    public String Mobile = "";
    public String Nickname = "";
    public String born = "";
    public String Token = "";
    public String Avatar = "";
    public String ImToken = "";
    public String Accid = "";
    public String appKey = "";
    public String IsRealAuth = "";
    public int ActiveStatus;
    public String openId;
    public int TradePasswordStatus; // 是否有交易密码 0没有 1有
    public String InviteCode = "";
    public String Username = "";
    public String Auth = "";

    public boolean isActive() {
        return ActiveStatus == 1;
    }

}
