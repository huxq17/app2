package com.aiqing.wallet.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.aiqing.wallet.utils.StatusBarUtils;


public class BaseFragment extends Fragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    public static final String ID = "id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onHide();
        } else {
            onShow();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            onShow();
        }
    }

    protected void onShow() {
    }

    protected void onHide() {
    }

    public void postDelayed(Runnable runnable, long delay) {
        Activity activity = getActivity();
        if (activity == null) return;
        activity.getWindow().getDecorView().postDelayed(runnable, delay);
    }

    public void removeCallback(Runnable runnable) {
        Activity activity = getActivity();
        if (activity == null) return;
        activity.getWindow().getDecorView().removeCallbacks(runnable);
    }

    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(int msgId) {
        Toast.makeText(getActivity(), getString(msgId), Toast.LENGTH_SHORT).show();
    }

    public void post(Runnable runnable) {
        Activity activity = getActivity();
        if (activity == null) return;
        activity.getWindow().getDecorView().post(runnable);
    }

    public void setWindowStatusBarColor(int color) {
        StatusBarUtils.setWindowStatusBarColor(getActivity(), color);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

}
