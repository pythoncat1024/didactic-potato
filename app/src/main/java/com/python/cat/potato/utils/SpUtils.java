package com.python.cat.potato.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    private SpUtils() {
    }

    private static final String DEFAULT_SP_FILE = "default_sp";
    private static final String FUCK_SP_FILE = "fuck_sp";

    private static SharedPreferences defaultSP(Context context) {
        return context.getSharedPreferences(DEFAULT_SP_FILE, Context.MODE_PRIVATE);
    }

    public static void put(Context context, String key, String value) {
        defaultSP(context).edit().putString(key, value).apply();
    }

    public static String get(Context context, String key, String defaultStr) {
        return defaultSP(context).getString(key, defaultStr);
    }

    public static String get(Context context, String key) {
        return get(context, key, "");
    }
}
