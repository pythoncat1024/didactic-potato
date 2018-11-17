package com.python.cat.potato.viewmodel;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class BaseVM {

    public static void jump2Target(FragmentManager fragmentManager,
                                   Fragment targetFragment,
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
}
