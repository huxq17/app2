package com.aiqing.im.session.viewholder;

import com.aiqing.im.session.extension.DefaultCustomAttachment;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderText;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderDefCustom extends MsgViewHolderText {

    public MsgViewHolderDefCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected String getDisplayText() {
        String s = "";
        try {
            DefaultCustomAttachment attachment = (DefaultCustomAttachment) message.getAttachment();
            s = "type: " + attachment.getType() + ", data: " + attachment.getContent();
        } catch (Exception e) {
        }
        return s;
    }
}
