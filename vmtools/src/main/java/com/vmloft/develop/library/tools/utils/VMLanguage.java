package com.vmloft.develop.library.tools.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by lzan13 on 2017/12/21.
 * 语言管理工具
 */
public class VMLanguage {

    public static final String KEY_LANGUAGE = "language";
    // 简体中文
    public static final String LANG_SIMPLIFIED_CHINESE = "zh";
    // 英文
    public static final String LANG_ENGLISH = "en";
    // 繁体中文
    public static final String LANG_TRADITIONAL_CHINESE = "zh-hant";
    // 法语
    public static final String LANG_FRANCE = "fr";
    // 德语
    public static final String LANG_GERMAN = "de";
    // 印地语
    public static final String LANG_HINDI = "hi";
    // 意大利语
    public static final String LANG_ITALIAN = "it";

    // 预定义语言列表
    private static HashMap<String, Locale> languages = new HashMap<String, Locale>(7) {{
        put(LANG_ENGLISH, Locale.ENGLISH);
        put(LANG_SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
        put(LANG_TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
        put(LANG_FRANCE, Locale.FRANCE);
        put(LANG_GERMAN, Locale.GERMANY);
        put(LANG_HINDI, new Locale(LANG_HINDI, "IN"));
        put(LANG_ITALIAN, Locale.ITALY);
    }};

    /**
     * 获取系统语言
     */
    public static String getDefaultLang() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前语言
     */
    public static String getCurrentLang(Context context) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0).getLanguage();
        } else {
            return config.locale.getLanguage();
        }
    }

    /**
     * 修改语言
     */
    @SuppressWarnings("deprecation")
    public static void changeAppLanguage(Context context, String language) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        Locale locale = getLocaleByLanguage(language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        res.updateConfiguration(config, dm);
        VMSPUtil.put(VMLanguage.KEY_LANGUAGE, language);
    }

    /**
     * 修改语言
     */
    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateLanguageResources(context, language);
        } else {
            return context;
        }
    }

    /**
     * 7.x 以上修改语言方法
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static Context updateLanguageResources(Context context, String language) {
        Resources res = context.getResources();
        Locale locale = getLocaleByLanguage(language);
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        config.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(config);
    }

    /**
     * 获取指定语言的locale信息，如果指定语言不存在{@link #languages}，返回本机语言，如果本机语言不是语言集合中的一种{@link #languages}，返回英语
     *
     * @param language language
     * @return
     */
    public static Locale getLocaleByLanguage(String language) {
        if (isSupportLanguage(language)) {
            return languages.get(language);
        } else {
            Locale locale = Locale.getDefault();
            for (String key : languages.keySet()) {
                if (TextUtils.equals(languages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
        }
        return Locale.ENGLISH;
    }

    /**
     * 获取是否支持当前语言，
     *
     * @param language 查询的语言
     * @return 如果支持直接返回，否则返回默认语言 english
     */
    public static String getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return language;
        }
        return LANG_ENGLISH;
    }

    /**
     * 是否支持当前语言
     */
    private static boolean isSupportLanguage(String language) {
        return languages.containsKey(language);
    }

    public static String localLanguage(Context context) {
        return (String) VMSPUtil.get(context, KEY_LANGUAGE, VMLanguage.getDefaultLang());
    }

}
