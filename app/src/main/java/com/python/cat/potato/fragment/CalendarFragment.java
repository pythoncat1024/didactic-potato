package com.python.cat.potato.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.python.cat.potato.viewmodel.CalendarFragmentVM;
import com.yanzhenjie.permission.AndPermission;

/**
 * calendar
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CalendarFragment extends BaseFragment {


    private CalendarFragmentVM mCalendarVM;

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
}
