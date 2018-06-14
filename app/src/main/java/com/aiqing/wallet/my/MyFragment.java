package com.aiqing.wallet.my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseFragment;
import com.aiqing.wallet.imageloader.ImageLoader;
import com.aiqing.wallet.login.LoginAct;
import com.aiqing.wallet.my.certification.CertificationActivity;
import com.aiqing.wallet.my.invitation.FriendInvitationActivity;
import com.aiqing.wallet.my.securitycenter.SecurityCenterActivity;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.aiqing.wallet.user.UserService;
import com.aiqing.wallet.utils.VersionUpgrade;
import com.buyi.huxq17.serviceagency.ServiceAgency;

import java.util.HashMap;


public class MyFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private TextView nickName, id, inviteCode, signOut;
    private LinearLayout personalData, friendInvitation, securityCenter, certification, clean_cach, update;
    private ImageView avatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frg_my_layout, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    @Override
    protected void onShow() {
        super.onShow();
        setWindowStatusBarColor(Color.parseColor("#1DCB8A"));
        getMyHome();
    }

    private void initView() {
        avatar = getView().findViewById(R.id.avatar);
        nickName = getView().findViewById(R.id.nickName);
        id = getView().findViewById(R.id.id);
        inviteCode = getView().findViewById(R.id.inviteCode);
        personalData = getView().findViewById(R.id.personalData);
        friendInvitation = getView().findViewById(R.id.friendInvitation);
        securityCenter = getView().findViewById(R.id.securityCenter);
        certification = getView().findViewById(R.id.certification);
        clean_cach = getView().findViewById(R.id.clean_cach);
        update = getView().findViewById(R.id.update);
        signOut = getView().findViewById(R.id.signOut);
    }

    private void initListener() {
        personalData.setOnClickListener(this);
        friendInvitation.setOnClickListener(this);
        securityCenter.setOnClickListener(this);
        certification.setOnClickListener(this);
        clean_cach.setOnClickListener(this);
        update.setOnClickListener(this);
        signOut.setOnClickListener(this);
        getView().findViewById(R.id.my_aboutus).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personalData:
                startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                break;
            case R.id.friendInvitation:
                startActivity(new Intent(getActivity(), FriendInvitationActivity.class));
                break;
            case R.id.securityCenter:
                startActivity(new Intent(getActivity(), SecurityCenterActivity.class));
                break;
            case R.id.certification:
                startActivity(new Intent(getActivity(), CertificationActivity.class));
                break;
            case R.id.clean_cach:
                Toast.makeText(getActivity(), getString(R.string.clean_success), Toast.LENGTH_SHORT).show();
                break;
            case R.id.update:
                new VersionUpgrade(getActivity())
                        .setToast()
                        .check();
                break;
            case R.id.signOut:
                LoginAct.start(getActivity(), true);
                break;
            case R.id.my_aboutus:
                AboutUsAct.start(getContext());
                break;
        }
    }

    private void getMyHome() {
        HashMap<String, String> params = new HashMap<>();
        ApiManager.getApi(UserApi.class).getMyHome(ApiTransform.addUdid(params))
                .compose(RxSchedulers.<UserApi.Bean>compose())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccess(User dataBean) {
                        nickName.setText(dataBean.Nickname);
                        id.setText(getString(R.string.ID) + ":" + dataBean.ID);
                        inviteCode.setText(getString(R.string.invitation_code) + ":" + dataBean.InviteCode);
                        if (!TextUtils.isEmpty(dataBean.Avatar)) {
                            ServiceAgency.getService(ImageLoader.class).loadImage(dataBean.Avatar, avatar);
                        }
                        UserService.setID(dataBean.ID);
                        UserService.setInviteCode(dataBean.InviteCode);
                        UserService.setUserName(dataBean.Username);
                        UserService.setAvatar(dataBean.Avatar);
                        UserService.setMobile(dataBean.Mobile);
                        UserService.setIsRealAuth(dataBean.Auth);
                    }
                });
    }
}


