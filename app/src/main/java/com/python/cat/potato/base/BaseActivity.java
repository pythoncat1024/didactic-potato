package com.python.cat.potato.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {

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

    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected AppCompatActivity get() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null
                && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(get(), clazz));
    }

}
