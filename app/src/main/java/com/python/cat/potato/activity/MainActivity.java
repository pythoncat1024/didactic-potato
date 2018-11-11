package com.python.cat.potato.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.BaseActivity;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.viewmodel.MainVM;

import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {

    private MainVM mainVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        mainVM = new MainVM();
        TextView tv = findViewById(R.id.tv_text);
        if (GlobalInfo.SHOW_LOADING) {

            Disposable subscribe = mainVM.interval(GlobalInfo.LOADING_SECONDS)
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
        Intent intent = new Intent(get(), DrawerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mainVM = null;
        super.onDestroy();
    }
}
