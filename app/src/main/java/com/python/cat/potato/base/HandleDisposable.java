package com.python.cat.potato.base;

import io.reactivex.disposables.Disposable;

public interface HandleDisposable {

    void addDisposable(Disposable disposable);
}
