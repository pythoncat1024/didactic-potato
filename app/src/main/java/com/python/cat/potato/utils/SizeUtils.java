package com.python.cat.potato.utils;

import android.util.TypedValue;

import com.python.cat.potato.base.BaseApplication;

public class SizeUtils {

    private SizeUtils() {
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                BaseApplication.get().getResources().getDisplayMetrics());
    }

    public static int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                BaseApplication.get().getResources().getDisplayMetrics());
    }
}
