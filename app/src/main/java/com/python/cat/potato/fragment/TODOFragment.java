package com.python.cat.potato.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.adapter.ScheduleTaskAdapter;
import com.python.cat.potato.base.BaseApplication;
import com.python.cat.potato.base.DrawerFragment;
import com.python.cat.potato.base.NeedLogin;
import com.python.cat.potato.domain.ScheduleTask;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.net.HttpRequest;
import com.python.cat.potato.utils.SpUtils;
import com.python.cat.potato.utils.ToastHelper;

import java.util.List;

import io.reactivex.Flowable;

/**
 * A simple {@link Fragment} subclass.
 */
public class TODOFragment extends DrawerFragment {


    private NeedLogin needLogin;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView tvHeader;

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

        tvHeader = view.findViewById(R.id.text_schedule_count);
        refreshLayout = view.findViewById(R.id.fragment_todo_swipe_refresh_layout);

        refreshLayout.setOnRefreshListener(() -> {
            ToastHelper.show(BaseApplication.get(), "我真的刷新了...");
        });
        recyclerView = view.findViewById(R.id.fragment_todo_recycler_view);
        String cookie = SpUtils.get(getContext(), GlobalInfo.SP_KEY_COOKIE);
        if (cookie.length() > 0) {
            LogUtils.i("request todo use cookie: " + cookie);
            Flowable<ScheduleTask> todo = HttpRequest.queryTodo(cookie, 1);
            addDisposable(todo.subscribe(info -> {
                LogUtils.d("todo info");
                if (info.errorCode == 0) {
                    loadData2View(info);

                } else {
                    throw new RuntimeException(info.errorMsg);
                }
                LogUtils.d(info);
            }, Throwable::printStackTrace));
        } else {
            LogUtils.e("need sign in first");
            if (needLogin != null) {
                needLogin.needLogin();
            }
        }
    }

    private void loadData2View(ScheduleTask info) {

        tvHeader.setText(getString(R.string.task_count, info.data.datas.size()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScheduleTaskAdapter adapter = new ScheduleTaskAdapter();
        adapter.replaceData(info);
        recyclerView.setAdapter(adapter);

    }

    public void setNeedLogin(NeedLogin needLogin) {
        this.needLogin = needLogin;
    }
}
