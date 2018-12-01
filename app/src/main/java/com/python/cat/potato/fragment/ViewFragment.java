package com.python.cat.potato.fragment;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.ViewAnimator;

import com.python.cat.potato.R;
import com.python.cat.potato.base.DrawerFragment;
import com.python.cat.potato.view.CharTextView;

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
        View callRing = view.findViewById(R.id.iv_call_ring);
        PropertyValuesHolder rotationH = PropertyValuesHolder.ofFloat("rotation",
                -60f, -10f, 0, 60f, 0f);
        PropertyValuesHolder scaleXH = PropertyValuesHolder.ofFloat("scaleX",
                1.0f, 0.7f, 1.2f, 1, 0.3f, 1f);
        scaleXH.setKeyframes(Keyframe.ofFloat(0, 1f),
                Keyframe.ofFloat(0.3f, 0.4f),
                Keyframe.ofFloat(0.5f, 2f),
                Keyframe.ofFloat(0.8f, 0.8f),
                Keyframe.ofFloat(1f, 1f)
        ); // 无效 --> setKeyframes 估计只对 ofObject 有效果
        scaleXH.setEvaluator(new FloatEvaluator());
        PropertyValuesHolder scaleYH = PropertyValuesHolder.ofFloat("scaleY",
                1.0f, 0.7f, 1.2f, 1, 0.3f, 1f);
        scaleYH.setKeyframes(Keyframe.ofFloat(0, 1f),
                Keyframe.ofFloat(0.3f, 0.4f),
                Keyframe.ofFloat(0.5f, 2f),
                Keyframe.ofFloat(0.8f, 0.8f),
                Keyframe.ofFloat(1f, 1f)
        );
        scaleYH.setEvaluator(new FloatEvaluator());
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(callRing,
                rotationH, scaleXH, scaleYH);
        view.findViewById(R.id.btn_start)
                .setOnClickListener(v -> {
                    animator.setDuration(2000);
//                    animator.setRepeatCount(Animation.INFINITE);
                    animator.setRepeatMode(ValueAnimator.REVERSE);
                    animator.start();
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
