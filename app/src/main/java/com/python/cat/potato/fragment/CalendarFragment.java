package com.python.cat.potato.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.adapter.CalendarInfoAdapter;
import com.python.cat.potato.base.BaseFragment;
import com.python.cat.potato.base.OnFragmentInteractionListener;
import com.python.cat.potato.utils.ToastHelper;
import com.python.cat.potato.viewmodel.CalDeletePop;
import com.python.cat.potato.viewmodel.CalendarFragmentVM;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONObject;
import org.reactivestreams.Subscription;

import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * calendar
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CalendarFragment extends BaseFragment {


    public static final int REQUEST_ADD_EVENTS = 17;

    static long extraPos = 0;
    private CalendarInfoAdapter adapter;
    private RecyclerView showDataRecyclerView;
    private CalDeletePop deletePop;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.d(view + " , " + savedInstanceState);

        initOperationLayout(view);
        initShowDataLayout(view);
    }

    private void initShowDataLayout(View view) {
        if (getActivity() == null) {
            throw new RuntimeException("activity == null");
        }
        ViewGroup showDataLayout = view.findViewById(R.id.fragment_calendar_show_data_layout);
        showDataLayout.setVisibility(View.VISIBLE);
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.fragment_calendar_swipe_refresh_layout);
        showDataRecyclerView = view.findViewById(R.id.fragment_calendar_recycler_view);
        showDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CalendarInfoAdapter(getActivity());
        showDataRecyclerView.setAdapter(adapter);
        addDisposable(
                CalendarFragmentVM.queryAllEventsSimple(getActivity())
                        .subscribe(infoList -> {
                            ToastHelper.show(getActivity(), getString(R.string.events_count, infoList.size()));
                            adapter.setCalendarInfoList(infoList);
                        }, Throwable::printStackTrace)
        );
        adapter.setOnItemLongClickListener((targetView, info, adapterPosition) -> {
            // 删除事件
            itemLongClick(info, adapterPosition);
        });
        refreshLayout.setOnRefreshListener(() -> addDisposable(
                CalendarFragmentVM.queryAllEventsSimple(getActivity())
                        .doOnError(e -> ToastHelper.show(getActivity(), "刷新失败..."))
                        .subscribe(infoList -> {
                                    adapter.setCalendarInfoList(infoList);
                                    ToastHelper.show(getActivity(), "刷新成功..."
                                            + getString(R.string.events_count, infoList.size()));
                                },
                                e -> {
                                    LogUtils.e(e);
                                    refreshLayout.setRefreshing(false);
                                },
                                () -> refreshLayout.setRefreshing(false))
        ));

        FloatingActionButton fabAdd = view.findViewById(R.id.fragment_calendar_fab_add);
        fabAdd.setOnClickListener(v -> doAddEvent());
        FloatingActionButton fabDelete = view.findViewById(R.id.fragment_calendar_fab_delete);
        fabDelete.setOnClickListener(v -> {
            deletePop = new CalDeletePop(getActivity());
            deletePop.showAsBottomPopupWindow(v);
            LogUtils.d("显示全部带自定义属性的日历事件....");
        });

    }

    @Override
    public void onDestroy() {
        if (deletePop != null) {
            deletePop.hidePopupWindow();
        }
        super.onDestroy();
    }

    private void doAddEvent() {
        // 添加事件
        LogUtils.v("");

//            Intent intent = new Intent(getActivity(), EventEditActivity.class);
//            startActivityForResult(intent, REQUEST_ADD_EVENTS);
            /*
            context, title, desc,
                    allDay, start, end, timezone, location
             */
        Context context = getContext();
        String title = "我是随便插入的一个事件 " + extraPos;
        String desc = "我是随便插入的事件的描述 " + extraPos;
        boolean allDay = false;

        long start = CalendarFragmentVM.createStartTime();
        long end = CalendarFragmentVM.createEndTime(start);
        String timezone = "Asia/Shanghai";
        String location = "我是随便弄的一个地点 " + extraPos;
        //noinspection ConstantConditions
        addDisposable(CalendarFragmentVM.insertEvent(context, title, desc, allDay,
                start, end, timezone, location)
                .flatMap(insert -> CalendarFragmentVM.queryEventByID(context, ContentUris.parseId(insert)))
                .doOnError(e -> extraPos += 1)
                .doOnComplete(() -> extraPos += 1)
                .subscribe(
                        next -> {
                            adapter.addList(next);
                            ToastHelper.show(Objects.requireNonNull(getActivity()),
                                    "添加成功..."+next);
                            LogUtils.w("insert:" + next);
                        },
                        Throwable::printStackTrace)
        );
    }

    private void itemLongClick(String info, int adapterPosition) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setMessage(info)
                .setCancelable(true)
                .setPositiveButton(
                        R.string.positive_button_text,
                        (dialog, which) -> {
                            LogUtils.d("delete....");
                            LogUtils.json(info);
                            doDelete(info, adapterPosition);
                        })
                .setTitle(R.string.delete_event_or_not);
        builder.show();
    }

    private void doDelete(String info, int adapterPosition) {
        addDisposable(
                CalendarFragmentVM.deleteByCustom(getContext(), info)
                        .subscribe(rows -> {
                            LogUtils.d("after delete: " + rows);
                            if (rows > 0) {
                                ToastHelper.show(Objects.requireNonNull(getContext()),
                                        "delete success.." + rows);
                                adapter.notifyItemRemoved(adapterPosition); // 老是不准确
                            }
                        }, Throwable::printStackTrace)
        );
    }

    private void initOperationLayout(View view) {
        View operationLayout = view.findViewById(R.id.fragment_calendar_operation_layout);
        operationLayout.setVisibility(View.GONE);
        view.findViewById(R.id.btn_query_calendar_events)
                .setOnClickListener(v -> {
                    LogUtils.v("");
                    LogUtils.v("query..");
                    AndPermission.with(this)
                            .permission(Manifest.permission.WRITE_CALENDAR,
                                    Manifest.permission.READ_CALENDAR)
                            .onGranted(permissions -> {
                                // Storage permission are allowed.
                                LogUtils.v("success " + permissions);
                                final FragmentActivity activity = getActivity();
                                LogUtils.v("....");
                                addDisposable(
                                        CalendarFragmentVM.queryAllEvents(activity).subscribe(
                                                LogUtils::d, Throwable::printStackTrace)
                                );
                            })
                            .onDenied(permissions -> {
                                // Storage permission are not allowed.
                                LogUtils.e("fail " + permissions);
                            })
                            .start();
                });

        view.findViewById(R.id.btn_add_calendar_events)
                .setOnClickListener(v -> {
                    LogUtils.v("");
                    LogUtils.v("add..");
                });

        view.findViewById(R.id.btn_delete_calendar_events)
                .setOnClickListener(v -> {
                    LogUtils.v("");
                    LogUtils.v("delete..");
                });

        view.findViewById(R.id.btn_update_calendar_events)
                .setOnClickListener(v -> {
                    LogUtils.v("");
                    LogUtils.v("update..");
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EVENTS
                && resultCode == Activity.RESULT_OK) {
            LogUtils.v("return from add events...");
        } else {
            LogUtils.e("error? " + requestCode + " , " + resultCode);
        }
    }

}
