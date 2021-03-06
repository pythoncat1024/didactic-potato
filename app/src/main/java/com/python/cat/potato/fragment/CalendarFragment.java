package com.python.cat.potato.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.python.cat.potato.activity.EventInfoActivity;
import com.python.cat.potato.adapter.CalendarInfoAdapter;
import com.python.cat.potato.base.BaseApplication;
import com.python.cat.potato.base.DrawerFragment;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.utils.ToastHelper;
import com.python.cat.potato.viewmodel.BaseVM;
import com.python.cat.potato.viewmodel.CalDeletePop;
import com.python.cat.potato.viewmodel.CalendarVM;
import com.yanzhenjie.permission.AndPermission;

import java.util.Arrays;
import java.util.Objects;

/**
 * calendar
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
public class CalendarFragment extends DrawerFragment {

    public static final int REQUEST_ADD_EVENTS = 17;

    static long extraPos = 0; // 给系统日历的
    private CalendarInfoAdapter adapter;
    private CalDeletePop deletePop;
    private ContentObserver calendarObserver;
    private ContentResolver resolver;
    private TextView tvHeader;

    public CalendarFragment() {
        // Required empty public constructor
    }

    private Handler mMainHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtils.d(msg);
        }
    };

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.w("..." + isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (calendarObserver != null && resolver != null) {
            resolver.unregisterContentObserver(calendarObserver);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AndPermission.with(this)
                .permission(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
                .onGranted(grant -> {
                    LogUtils.v("permissions granted! :" + grant);
                })
                .onDenied(permissions -> {
                    LogUtils.e("permission denied:" + permissions);
                    throw new RuntimeException(Arrays.toString(permissions.toArray()));
                })
                .start();
        setEventsObserver();
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }


    private void setEventsObserver() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            resolver = activity.getContentResolver();
            calendarObserver = new ContentObserver(mMainHandler) {
                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    LogUtils.i(selfChange + " ,,, " + uri);
                    // onChange 每次都会被回调两次，甚至多次。
                    // 每次增删之前注册，收到立即反注册即可
                    resolver.unregisterContentObserver(calendarObserver);
                    refreshUI();
                }
            };
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.d(view + " , " + savedInstanceState + " ### ");
        initOperationLayout(view);
        initShowDataLayout(view);
    }


    private void initShowDataLayout(View view) {
        if (getActivity() == null) {
            throw new RuntimeException("activity == null");
        }
        ViewGroup showDataLayout = view.findViewById(R.id.fragment_calendar_show_data_layout);
        showDataLayout.setVisibility(View.VISIBLE);
        // list view header
        tvHeader = view.findViewById(R.id.text_event_count);
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.fragment_calendar_swipe_refresh_layout);
        RecyclerView showDataRecyclerView = view.findViewById(R.id.fragment_calendar_recycler_view);
        showDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CalendarInfoAdapter(getActivity());
        showDataRecyclerView.setAdapter(adapter);
        refreshUI();
        adapter.setOnItemLongClickListener(
                (CalendarInfoAdapter.OnItemLongClickListener<String>)
                        (targetView, info, adapterPosition) -> {
                            // 删除事件
                            itemLongClick(info, adapterPosition);
                        });
        adapter.setOnItemClickListener((targetView, info, adapterPosition) -> {
            itemClick(info);
        });
        refreshLayout.setOnRefreshListener(() -> addDisposable(
                CalendarVM.queryAllEventsSimple(getActivity())
                        .doOnError(e -> ToastHelper.show(getActivity(), "刷新失败..."))
                        .subscribe(infoList -> {
                                    adapter.setCalendarInfoList(infoList);
                                    tvHeader.setText(getString(R.string.events_count, infoList.size()));
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

    private void itemClick(String info) {
        jump2EventDetail(info);
    }

    private void jump2EventDetail(String info) {
        if (getActivity() == null) {
            ToastHelper.show(BaseApplication.Holder.app, "activity == null!");
            return;
        }
        long eventID = CalendarVM.parseEventIDFromInfo(info);
        Bundle bundle = new Bundle();
        bundle.putLong(GlobalInfo.IntentKey.KEY_EVENT_ID, eventID);
        BaseVM.jump2Target(getActivity(), EventInfoActivity.class, bundle, true);
    }

    private void refreshUI() {
        addDisposable(
                CalendarVM.queryAllEventsSimple(getActivity())
                        .subscribe(infoList -> {
                            tvHeader.setText(getString(R.string.events_count, infoList.size()));
                            adapter.setCalendarInfoList(infoList);
                        }, Throwable::printStackTrace)
        );
    }

    @Override
    public void onDestroy() {
        if (deletePop != null) {
            deletePop.hidePopupWindow();
        }
        super.onDestroy();
    }

    private void jump2AddEvent() {
        if (getActivity() == null) {
            LogUtils.w("attached activity disappeared ...");
            return;
        }
        BaseVM.jump2Target(getActivity(), EventInfoActivity.class, true);
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

        long start = CalendarVM.createStartTime();
        long end = CalendarVM.createEndTime(start);
        String timezone = "Asia/Shanghai";
        String location = "我是随便弄的一个地点 " + extraPos;
        //noinspection ConstantConditions
        addDisposable(CalendarVM.insertEvent(context, title, desc, allDay,
                start, end, timezone, location)
                .flatMap(insert -> CalendarVM.queryEventByID(context, ContentUris.parseId(insert)))
                .doOnError(e -> extraPos += 1)
                .doOnComplete(() -> extraPos += 1)
                .doOnSubscribe(t -> resolver
                        .registerContentObserver(Events.CONTENT_URI,
                                false, calendarObserver))
                .subscribe(
                        next -> {
                            ToastHelper.show(Objects.requireNonNull(getActivity()),
                                    "添加成功..." + next);
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
                CalendarVM.deleteByCustom(getContext(), info)
                        .doOnSubscribe(t -> resolver
                                .registerContentObserver(Events.CONTENT_URI,
                                        false, calendarObserver))
                        .subscribe(rows -> {
                            LogUtils.d("after delete: " + rows);
                            if (rows > 0) {
                                ToastHelper.show(Objects.requireNonNull(getContext()),
                                        "delete success.." + rows);
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
                                        CalendarVM.queryAllEvents(activity).subscribe(
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
