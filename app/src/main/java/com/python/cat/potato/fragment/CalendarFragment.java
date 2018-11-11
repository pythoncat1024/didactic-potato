package com.python.cat.potato.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.BaseFragment;
import com.python.cat.potato.base.OnFragmentInteractionListener;

/**
 * calendar
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CalendarFragment extends BaseFragment {


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

        view.findViewById(R.id.btn_query_calendar_events)
                .setOnClickListener(v -> {
                    LogUtils.v("");
                    LogUtils.v("query..");
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
}
