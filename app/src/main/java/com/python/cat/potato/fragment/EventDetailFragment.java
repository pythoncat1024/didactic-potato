package com.python.cat.potato.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.python.cat.potato.R;
import com.python.cat.potato.base.BaseFragment;
import com.python.cat.potato.viewmodel.CalendarVM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends BaseFragment {
    private static final String KEY_EVENT_INFO = "key_event_info";

    private long eventInfo;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    public static EventDetailFragment newInstance(long eventInfo) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_EVENT_INFO, eventInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventInfo = getArguments().getLong(KEY_EVENT_INFO);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitle(view);
        TextView tvContent = view.findViewById(R.id.fragment_event_detail_text_tv);
//        tvContent.setText(eventInfo);
        addDisposable(CalendarVM.queryEventByID(getContext(), eventInfo)
                .subscribe(info -> {
                    String jsonWithTime = CalendarVM.formatJsonWithTime(info);
                    tvContent.setText(jsonWithTime);
                }, Throwable::printStackTrace));
    }

    private void initTitle(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle(getClass().getSimpleName());

    }
}
