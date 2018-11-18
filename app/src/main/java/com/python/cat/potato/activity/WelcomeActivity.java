package com.python.cat.potato.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.BaseActivity;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.viewmodel.BaseVM;
import com.python.cat.potato.viewmodel.WelcomeVM;

import io.reactivex.disposables.Disposable;

public class WelcomeActivity extends BaseActivity {

    private WelcomeVM welcomeVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        welcomeVM = new WelcomeVM();
        TextView tv = findViewById(R.id.tv_text);
        if (GlobalInfo.SHOW_LOADING) {

            Disposable subscribe = welcomeVM.interval(GlobalInfo.LOADING_SECONDS)
                    .doOnComplete(this::jump2content)
                    .subscribe(t -> {
                                LogUtils.d("t==" + t);
                                tv.setText(getString(R.string.welcome_text, t));
                            }, Throwable::printStackTrace
                    );
            addDisposable(subscribe);
        } else {
            jump2content();
        }
    }

    private void jump2content() {
        BaseVM.jump2Target(get(), DrawerActivity.class, true);
    }

    @Override
    protected void onDestroy() {
        welcomeVM = null;
        super.onDestroy();
    }
}
