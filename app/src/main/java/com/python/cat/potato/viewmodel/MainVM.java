package com.python.cat.potato.viewmodel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @see com.python.cat.potato.MainActivity
 */
public class MainVM {


    public Flowable<Long> interval(long init) {

        return Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(init)
                .map(t -> init - t - 1)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
