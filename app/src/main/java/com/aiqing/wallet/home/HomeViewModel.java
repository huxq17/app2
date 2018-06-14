package com.aiqing.wallet.home;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;


public class HomeViewModel extends AndroidViewModel {
    private LiveData<HomeBean.DataBean> homeData;
    private HomeRepository homeRepository = HomeRepository.get();
    private MutableLiveData<String> userNameInput = new MutableLiveData();
    public final LiveData<Boolean> activeResult =
            Transformations.switchMap(userNameInput, new Function<String, LiveData<Boolean>>() {
                @Override
                public LiveData<Boolean> apply(String input) {
                    return homeRepository.activeFriend(input);
                }
            });


    public LiveData<HomeBean.DataBean> getHomeData() {
        if (homeData == null) {
            homeData = homeRepository.getHome();
        }
        return homeData;
    }

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void activeFriend(String username) {
        userNameInput.setValue(username);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
