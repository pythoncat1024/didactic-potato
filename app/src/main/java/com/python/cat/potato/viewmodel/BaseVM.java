package com.python.cat.potato.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class BaseVM {

    private BaseVM() {
    }

    public static void jump2Target(@NonNull FragmentManager fragmentManager,
                                   @NonNull Fragment targetFragment,
                                   @IdRes int containerID,
                                   boolean backStack,
                                   boolean animate) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animate) {
            transaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out);
        }
        transaction.replace(containerID, targetFragment);
        if (backStack) {
            transaction.addToBackStack(null); // 添加到回退栈
        }
        transaction.commit();
    }

    public static void jump2Target(@NonNull Activity activity, Class<?> cls, boolean animate) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        if (animate) {
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
