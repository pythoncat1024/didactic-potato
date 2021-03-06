package com.python.cat.potato.viewmodel;

import com.python.cat.potato.activity.WelcomeActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @see WelcomeActivity
 */
public class WelcomeVM {


    public Flowable<Long> interval(long init) {

        return Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(init)
                .map(t -> init - t - 1)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
