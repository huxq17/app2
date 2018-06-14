package com.aiqing.wallet.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.huxq17.xprefs.XPrefs;

import java.util.Locale;

public class LanguageUtils {
    private static final int CHINESE = 0;
    private static final int ENGLISH = 1;
    private int language;

    public static boolean isChinese() {
        LanguageUtils languageUtils = get();
        return languageUtils.language == CHINESE;
    }

    public static boolean isEnglish() {
        LanguageUtils languageUtils = get();
        return languageUtils.language == ENGLISH;
    }

    public static void setLanguage(Activity activity, boolean isChinese) {
        LanguageUtils languageUtils = get();
        if (isChinese == isChinese()) return;
        if (isChinese) {
            languageUtils.language = CHINESE;
        } else {
            languageUtils.language = ENGLISH;
        }
        Configuration config = activity.getResources().getConfiguration();
        if (isChinese) {
            config.locale = Locale.CHINESE;
        } else {
            config.locale = Locale.ENGLISH;
        }
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        activity.getResources().updateConfiguration(config, dm);
        activity.recreate();
        XPrefs.saveAll(languageUtils);
    }

    private static LanguageUtils get() {
        return XPrefs.get(LanguageUtils.class);
    }
}
