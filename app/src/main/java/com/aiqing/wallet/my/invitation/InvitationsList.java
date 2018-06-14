package com.aiqing.wallet.my.invitation;

import java.util.List;

/**
 * 好友邀请，邀请人列表实体类
 */
public class InvitationsList {
    String InviteCode;//邀请列表
    String Url;//二维码
    String Content;

    String inviter;
    List<Invitations> list;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getInviteCode() {
        return InviteCode;
    }

    public void setInviteCode(String inviteCode) {
        InviteCode = inviteCode;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public List<Invitations> getList() {
        return list;
    }

    public void setList(List<Invitations> list) {
        this.list = list;
    }
}
