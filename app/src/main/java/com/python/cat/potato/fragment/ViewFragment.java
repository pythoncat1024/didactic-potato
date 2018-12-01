package com.python.cat.potato.fragment;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.DrawerFragment;
import com.python.cat.potato.utils.ToastHelper;
import com.python.cat.potato.view.CustomView;
import com.python.cat.potato.view.MenuView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ViewFragment extends DrawerFragment {

    public ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ViewFragment newInstance() {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CustomView cv = view.findViewById(R.id.view_fragment_custom_view);
        cv.setOnAreaClickListener((v, area) -> {
            ToastHelper.show(v.getContext(), area.name());
            LogUtils.w("area click --" + area.name());
        });

        cv.setOnClickListener(v -> {
            ToastHelper.show(v.getContext(), "click....");
            LogUtils.w("click...");
        });
        MenuView viewById = view.findViewById(R.id.menu_view);

        viewById.setLengthFactor(0.5f);
        viewById.setOnMenuClickListener((index, menu, parent) -> {
            ToastHelper.show(view.getContext(), menu);
            LogUtils.d(menu.toString());
        });


        TextView tvChar = view.findViewById(R.id.char_tv);

        ValueAnimator animator = ValueAnimator.ofObject((fraction, startValue, endValue) -> {
            char start = (Character) startValue;
            char end = (Character) endValue;
            return (int) (start + (end - start) * fraction);
        }, 'A', 'Z');

        animator.addUpdateListener(animation -> {
            int s = (Integer) animation.getAnimatedValue();
            char ss = (char) s;
            com.apkfuns.logutils.LogUtils.w("ss==" + ss);
            tvChar.setText(String.format("%s", ss));
        });

        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
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
