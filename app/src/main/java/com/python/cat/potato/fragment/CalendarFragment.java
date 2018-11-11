package com.python.cat.potato.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.python.cat.potato.viewmodel.CalendarFragmentVM;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * calendar
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CalendarFragment extends BaseFragment {


    public static final int REQUEST_ADD_EVENTS = 17;
    private CalendarFragmentVM mCalendarVM;

    static long extraPos = 0;

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
        TextView tvHeader = view.findViewById(R.id.text_event_count);
        RecyclerView showDataRecyclerView = view.findViewById(R.id.fragment_calendar_recycler_view);
        showDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CalendarInfoAdapter adapter = new CalendarInfoAdapter(getActivity());
        showDataRecyclerView.setAdapter(adapter);
        addDisposable(
                mCalendarVM.queryAllEventsSimple(getActivity())
                        .subscribe(infoList -> {
                            tvHeader.setText(getString(R.string.events_count, infoList.size()));
                            adapter.setCalendarInfoList(infoList);
                        }, Throwable::printStackTrace)
        );

        adapter.setOnItemLongClickListener((targetView, info) -> {
            // 删除事件
            itemLongClick(info);
        });
        FloatingActionButton fabAdd = view.findViewById(R.id.fragment_calendar_fab_add);
        fabAdd.setOnClickListener(v -> {
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

            long start = mCalendarVM.createStartTime();
            long end = mCalendarVM.createEndTime(start);
            String timezone = "Asia/Shanghai";
            String location = "我是随便弄的一个地点 " + extraPos;
            //noinspection ConstantConditions
            addDisposable(mCalendarVM.insertEvent(context, title, desc, allDay,
                    start, end, timezone, location)
                    .doOnComplete(() -> {
                        extraPos += 1;
                        JSONObject info = new JSONObject();
                        info.put(Events.TITLE, title);
                        info.put(Events.DESCRIPTION, desc);
                        info.put(Events.DTSTART, mCalendarVM.formatTime(start));
                        info.put(Events.DTEND, mCalendarVM.formatTime(end));
                        //noinspection ConstantConditions
                        info.put(Events.ALL_DAY, allDay);
                        info.put(Events.CALENDAR_TIME_ZONE, timezone);
                        info.put(Events.EVENT_LOCATION, location);
                        LogUtils.d("insert...info ###:->");
                        LogUtils.json(info.toString());
                        ToastHelper.show(context, info.toString());
                    })
                    .subscribe());
        });

    }

    private void itemLongClick(String info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(info)
                .setCancelable(true)
                .setPositiveButton(
                        R.string.positive_button_text,
                        (dialog, which) -> {
                            LogUtils.d("delete....");
                            LogUtils.json(info);
                            doDelete(info);
                        })
                .setTitle(R.string.delete_event_or_not);
        builder.show();
    }

    private void doDelete(String info) {
        addDisposable(
                mCalendarVM.deleteByCustom(getContext(), info)
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
                                        mCalendarVM.queryAllEvents(activity).subscribe(
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
        mCalendarVM = new CalendarFragmentVM();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCalendarVM = null;
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
