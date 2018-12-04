package com.python.cat.potato.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.BaseActivity;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.viewmodel.BaseVM;
import com.python.cat.potato.viewmodel.WelcomeVM;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        View decorView = getWindow().getDecorView();
        Drawable background = decorView.getBackground();
        LogUtils.e(background.toString());

        if (GlobalInfo.SHOW_LOADING) {
            showDecorViewAnimate(decorView);
        } else {
            new WelcomeVM().interval(1).doOnSubscribe(sbs -> jump2content()).subscribe();
        }
//        Animation animation = AnimationUtils.loadAnimation(get(), R.anim.scale_bg_bigger);
//        decorView.startAnimation(animation); // 对 decorView 无效, 对 textView 有效

    }

    private void showDecorViewAnimate(View decorView) {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha",
                1.0f, 1.0f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",
                1.0f, 1.5f, 2.0f, 1.5f, 1.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",
                1.0f, 1.5f, 2.0f, 1.5f, 1.0f);
        ObjectAnimator appear = ObjectAnimator.ofPropertyValuesHolder(decorView,
                scaleX, scaleY)
                .setDuration(GlobalInfo.LOADING_SECONDS * 1000);
        ObjectAnimator disappear = ObjectAnimator.ofFloat(decorView, "alpha",
                1.0f, 1.0f); // 透明度动画对 decorView 显示很奇怪
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(appear);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                new WelcomeVM().interval(1).doOnSubscribe(sbs -> jump2content()).subscribe();
            }
        });
    }

    private void jump2content() {
        BaseVM.jump2Target(get(), DrawerActivity.class, true);
    }

    @Override
    protected void onDestroy() {
        WelcomeVM welcomeVM = null;
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        navigationBarStatusBar(get(), hasFocus);
    }

    /**
     * https://blog.csdn.net/qiyei2009/article/details/74435809
     * 导航栏，状态栏隐藏
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void navigationBarStatusBar(Activity activity, boolean hasFocus) {
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


}
