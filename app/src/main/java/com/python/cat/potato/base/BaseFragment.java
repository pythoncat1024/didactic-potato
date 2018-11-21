package com.python.cat.potato.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment implements HandleDisposable {

    private CompositeDisposable compositeDisposable;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        super.onDestroy();
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable != null && disposable != null) {
            compositeDisposable.add(disposable);
        } else {
            throw new RuntimeException("error: "
                    + compositeDisposable + " ### " + disposable);
        }
    }
}
