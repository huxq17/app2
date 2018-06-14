package com.aiqing.wallet.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.aiqing.basiclibrary.utils.DensityUtil;
import com.aiqing.wallet.MainActivity;
import com.aiqing.wallet.R;
import com.aiqing.wallet.bean.Code;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserService;
import com.aiqing.wallet.utils.LanguageUtils;
import com.aiqing.wallet.widget.AnimDialog;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class LoginAct extends BaseActivity {
    private EditText etMobile, etPass;
    private CheckBox cbPassVisibility;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int menuWidth = DensityUtil.dip2px(this, 12);
        int menuHeight = DensityUtil.dip2px(this, 6);
//        showMenu(getLanguage(), R.drawable.iv_title_menu, menuWidth, menuHeight);
        showMenu(getString(R.string.s_chinese), R.drawable.iv_title_menu, menuWidth, menuHeight);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        dealClick();
        setTitle(getResources().getString(R.string.s_login_title));
        etMobile = findViewById(R.id.et_mobile);
        etPass = findViewById(R.id.et_pass);
        cbPassVisibility = findViewById(R.id.cb_pass_visiblity);
        cbPassVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etPass.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance()
                        : PasswordTransformationMethod.getInstance());
                etPass.requestFocus();
                etPass.setSelection(getText(etPass).length());
            }
        });
        inflateMobile();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        inflateMobile();
    }

    private String getLanguage() {
        if (LanguageUtils.isChinese()) {
            return getString(R.string.s_chinese);
        } else if (LanguageUtils.isEnglish()) {
            return getString(R.string.s_english);
        }
        Configuration config = getResources().getConfiguration();// 获得设置对象
        String language = config.locale.getLanguage();
        if (language.equals(Locale.CHINESE.getLanguage())) {
            return getString(R.string.s_chinese);
        } else {
            return getString(R.string.s_english);
        }
    }

    @Override
    public void onMenuClick() {
//        int height = DensityUtil.dip2px(this, 40);
//        mLanguageDialog = new AnimDialog(this)
//                .addItem(new AnimDialog.Item(getString(R.string.s_chinese)).height(height))
//                .addItem(new AnimDialog.Item(getString(R.string.s_english)).height(height))
//                .gravity(Gravity.BOTTOM)
//                .build();
//        mLanguageDialog.show();
//        mLanguageDialog.setOnItemClickListener(new AnimDialog.OnItemClickListener() {
//            @Override
//            public void onItemClick(String text) {
//                if (text.equals(getString(R.string.s_chinese))) {
//                    LanguageUtils.setLanguage(LoginAct.this, true);
//                } else if (text.equals(getString(R.string.s_english))) {
//                    LanguageUtils.setLanguage(LoginAct.this, false);
//                }
//                int menuWidth = DensityUtil.dip2px(LoginAct.this, 12);
//                int menuHeight = DensityUtil.dip2px(LoginAct.this, 6);
//                showMenu(getLanguage(), R.drawable.iv_title_menu, menuWidth, menuHeight);
//            }
//        });
    }

    private void inflateMobile() {
        String nickName = UserService.getUserName();
        if (!isEmpty(nickName)) {
            etMobile.setText(nickName);
            etMobile.setSelection(nickName.length());
            etPass.requestFocus();
        }
    }

    private void dealClick() {
        findViewById(R.id.tv_forget_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPassAct.start(LoginAct.this);
            }
        });
        findViewById(R.id.tv_go_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterAct.start(LoginAct.this);
            }
        });
    }

    private AnimDialog mDialog;
    private AnimDialog mLanguageDialog;

    public void login(View v) {
        if (isEmpty(etMobile) || isEmpty(etPass)) {
            toast(getString(R.string.s_blank_info));
            if (isEmpty(etMobile)) {
                etMobile.requestFocus();
            } else if (isEmpty(etPass)) {
                etPass.requestFocus();
            }
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("username", getText(etMobile));
        params.put("password", getText(etPass));
        ApiManager.getApi(LoginApi.class).login(ApiTransform.transform(params))
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<LoginApi.Bean, ObservableSource<LoginApi.Bean>>() {
                    @Override
                    public ObservableSource<LoginApi.Bean> apply(LoginApi.Bean bean) {
                        return loginNetEaseIM(bean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<User>(this, getString(R.string.s_login_ing)) {
                    @Override
                    protected void onSuccess(User user) {
                        if (user.isActive()) {
                            user.Mobile = getText(etMobile);
                            UserService.save(user);
                            MainActivity.start(LoginAct.this);
                        } else {
                            mDialog = new AnimDialog(LoginAct.this);
                            mDialog.contentView(R.layout.layout_user_active)
                                    .gravity(Gravity.CENTER)
                                    .canceledOnTouchOutside(false)
//                    .animator(R.style.ActionSheetDialogAnimation)
                                    .build().show();
//                            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                @Override
//                                public void onDismiss(DialogInterface dialog) {
//                                    User user = new User();
//                                    user.Nickname = UserService.getNickName();
//                                    UserService.save(user);
//                                }
//                            });
                        }
//                        HomeActivity.start(LoginAct.this);
//                        finish();
                    }
                });
    }

    private Observable<LoginApi.Bean> loginNetEaseIM(final LoginApi.Bean bean) {
        return Observable.create(new ObservableOnSubscribe<LoginApi.Bean>() {
            @Override
            public void subscribe(final ObservableEmitter<LoginApi.Bean> e) throws Exception {
                if (bean == null) {
                    e.onNext(bean);
                    return;
                }
                int code = bean.getCode();
                if (code == Code.OK) {
                    final User user = bean.getData();
                    String account = user.Accid;
                    final String token = user.ImToken;
                    NimUIKit.login(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            bean.getData().appKey = param.getAppKey();
                            NimUIKit.loginSuccess(user.Accid);
                            e.onNext(bean);
                        }

                        @Override
                        public void onFailed(int code) {
//                            if (code == 302 || code == 404) {
//                                e.onError(new Exception("帐号或密码错误"));
//                            } else {
                            e.onError(new Exception("login failed: " + code));
//                            }
                        }

                        @Override
                        public void onException(Throwable exception) {
                            e.onError(exception);
                        }
                    });
                } else {
                    e.onNext(bean);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean clearAll) {
        Intent intent = new Intent(context, LoginAct.class);
        if (clearAll) {
            User user = new User();
            user.Username = UserService.getUserName();
            UserService.save(user);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void onActive(View v) {
        onActiveCancel(v);
    }

    public void onActiveCancel(View v) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
