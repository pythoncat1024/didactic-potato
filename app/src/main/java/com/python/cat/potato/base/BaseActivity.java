package com.python.cat.potato.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.apkfuns.logutils.LogUtils;
import com.yanzhenjie.permission.AndPermission;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity
        implements OnFragmentInteractionListener, HandleDisposable {

    static {
        LogUtils.getLogConfig().configShowBorders(false);
    }

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
////批量添加
//        compositeDisposable.add(observer1);
//        compositeDisposable.add(observer2);
//        compositeDisposable.add(observer2);
////最后一次性全部取消订阅
//        compositeDisposable.dispose();

        AndPermission.with(getApplicationContext())
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .onDenied(permissions -> {
                    LogUtils.e(permissions);
                    throw new RuntimeException("denied: " + permissions);
                })
                .start();
    }

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable != null && disposable != null) {
            compositeDisposable.add(disposable);
        } else {
            throw new RuntimeException("error: "
                    + compositeDisposable + " ### " + disposable);
        }
    }

    protected AppCompatActivity get() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null
                && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
