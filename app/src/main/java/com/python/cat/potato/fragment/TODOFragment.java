package com.python.cat.potato.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.DrawerFragment;
import com.python.cat.potato.domain.TODO;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.net.HttpRequest;
import com.python.cat.potato.utils.SpUtils;

import io.reactivex.Flowable;

/**
 * A simple {@link Fragment} subclass.
 */
public class TODOFragment extends DrawerFragment {


    public TODOFragment() {
        // Required empty public constructor
    }

    public static TODOFragment newInstance() {
        Bundle args = new Bundle();
        TODOFragment fragment = new TODOFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.w("..." + isVisibleToUser);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.d(view + " , " + savedInstanceState + " ### ");

        String cookie = SpUtils.get(getContext(), GlobalInfo.SP_KEY_COOKIE);
        if (cookie.length() > 0) {
            LogUtils.i("request todo use cookie: " + cookie);
            Flowable<TODO> todo = HttpRequest.queryTodo(cookie, 1);
            addDisposable(todo.subscribe(info -> {
                LogUtils.d("todo info");
                LogUtils.d(info);
            }, Throwable::printStackTrace));
        } else {
            LogUtils.e("need sign in first");
        }

    }

}
