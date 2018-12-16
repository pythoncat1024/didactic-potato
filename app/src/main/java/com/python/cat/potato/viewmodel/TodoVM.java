package com.python.cat.potato.viewmodel;

import android.content.Context;

import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.utils.SpUtils;

public class TodoVM {
    public static boolean checkLogin(Context context) {
        return (SpUtils.get(context, GlobalInfo.SP_KEY_COOKIE).length() > 0);
    }
}
