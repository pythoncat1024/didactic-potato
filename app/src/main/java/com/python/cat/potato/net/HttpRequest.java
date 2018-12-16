package com.python.cat.potato.net;

import android.support.annotation.NonNull;

import com.python.cat.potato.domain.LoginResult;
import com.python.cat.potato.domain.TODO;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {

    @NonNull
    private static Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(HttpService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(HttpClient.client())
                .build();
    }

    private static HttpService getHttpService() {
        Retrofit retrofit = retrofit();
        return retrofit.create(HttpService.class);
    }

    public static Flowable<LoginResult> userLogin(String username, String password) {
        HttpService service = getHttpService();
        return service.userLogin(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<TODO> queryTodo(String cookie, int currentPage) {
        HttpService service = getHttpService();
        return service.queryTodo(cookie, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
