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

    private TitleHook hook;
    private CompositeDisposable compositeDisposable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TitleHook) {
            hook = (TitleHook) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " current activity must implement "
                    + TitleHook.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hook = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (hook != null) {
            hook.setFragmentTitle(getClass().getSimpleName());
        }

        LogUtils.d(hook + " , " + getClass().getSimpleName());
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
