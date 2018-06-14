package com.aiqing.wallet.home.notice;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aiqing.wallet.home.HomeRepository;

public class NoticeViewModel extends AndroidViewModel {
    private LiveData<NoticeBean.DataBean> noticeListData;

    public NoticeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<NoticeBean.DataBean> getNoticeListData() {
        if (noticeListData == null) {
            noticeListData = HomeRepository.get().getNotice();
        }
        return noticeListData;
    }
}
