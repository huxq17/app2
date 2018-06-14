package com.aiqing.wallet;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.aiqing.im.DemoCache;
import com.aiqing.im.NimDemoLocationProvider;
import com.aiqing.im.mixpush.DemoPushContentProvider;
import com.aiqing.im.session.SessionHelper;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.user.UserService;
import com.aiqing.wallet.utils.LanguageUtils;
import com.huxq17.xprefs.XPrefs;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.io.IOException;
import java.util.Locale;


public class App extends Application {
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        XPrefs.bind(this);
        Configuration localeConfig = getResources().getConfiguration();// 获得设置对象
        String language = localeConfig.locale.getLanguage();
        if (LanguageUtils.isChinese()) {
            if (!language.equals("zh")) {
                localeConfig.locale = Locale.CHINESE;
                DisplayMetrics dm = getResources().getDisplayMetrics();
                getResources().updateConfiguration(localeConfig, dm);
            }
        } else if (LanguageUtils.isEnglish()) {
            if (!language.equals("en")) {
                localeConfig.locale = Locale.ENGLISH;
                DisplayMetrics dm = getResources().getDisplayMetrics();
                getResources().updateConfiguration(localeConfig, dm);
            }
        }
        new ApiManager.Creator()
                .initialize();
        SDKOptions options = new SDKOptions();
        options.sdkStorageRootPath = getAppCacheDir(this) + "/im";
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = MainActivity.class;// 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
//        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录
        NIMClient.init(this, loginInfo(), options);
        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
            initUiKit();
//            initAVChatKit();
        }
    }

    public static Context getContext() {
        return context;
    }

    private void initUiKit() {
        NimUIKit.init(this);
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());
        SessionHelper.init();
        DemoCache.setContext(this);

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());
    }

//    private void initAVChatKit() {
//        AVChatOptions avChatOptions = new AVChatOptions() {
//            @Override
//            public void logout(Context context) {
//                LoginAct.start(context, true);
//            }
//        };
//        avChatOptions.entranceActivity = MainActivity.class;
//        avChatOptions.notificationIconRes = R.drawable.ic_stat_notify_msg;
//        AVChatKit.init(avChatOptions);
//        AVChatKit.setContext(context);
//        // 初始化日志系统
//        AVChatKit.setiLogUtil(new ILogUtil() {
//            @Override
//            public void ui(String msg) {
//                LogUtil.ui(msg);
//            }
//
//            @Override
//            public void e(String tag, String msg) {
//                LogUtil.e(tag, msg);
//            }
//
//            @Override
//            public void i(String tag, String msg) {
//                LogUtil.i(tag, msg);
//            }
//
//            @Override
//            public void d(String tag, String msg) {
//                LogUtil.d(tag, msg);
//            }
//        });
//        // 设置用户相关资料提供者
//        AVChatKit.setUserInfoProvider(new IUserInfoProvider() {
//            @Override
//            public UserInfo getUserInfo(String account) {
//                return NimUIKit.getUserInfoProvider().getUserInfo(account);
//            }
//
//            @Override
//            public String getUserDisplayName(String account) {
//                return UserInfoHelper.getUserDisplayName(account);
//            }
//        });
//        // 设置群组数据提供者
//        AVChatKit.setTeamDataProvider(new ITeamDataProvider() {
//            @Override
//            public String getDisplayNameWithoutMe(String teamId, String account) {
//                return TeamHelper.getDisplayNameWithoutMe(teamId, account);
//            }
//
//            @Override
//            public String getTeamMemberDisplayName(String teamId, String account) {
//                return TeamHelper.getTeamMemberDisplayName(teamId, account);
//            }
//        });
//    }

    private LoginInfo loginInfo() {
        String account = UserService.getAccount();
        String token = UserService.getIMToken();
        String appKey = UserService.getAppKey();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            NimUIKit.setAccount(account);
            return new LoginInfo(account, token, appKey);
        } else {
            return null;
        }
    }

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + DemoCache.getContext().getPackageName();
        }

        return storageRootPath;
    }
}
