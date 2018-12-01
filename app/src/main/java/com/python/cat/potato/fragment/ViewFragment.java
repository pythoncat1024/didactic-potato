package com.python.cat.potato.fragment;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import com.apkfuns.logutils.LogUtils;
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

        CharTextView tvChar = view.findViewById(R.id.char_tv);
        tvChar.setCharText('X');
        PropertyValuesHolder charHolder = PropertyValuesHolder.ofObject("CharText",
                new CharEvaluator(), 'A', 'Z');
        charHolder.setKeyframes(Keyframe.ofObject(0, 'A'),
                Keyframe.ofObject(0.1f, 'M'),
                Keyframe.ofObject(0.5f, 'G'),
                Keyframe.ofObject(0.8f, 'M'),
                Keyframe.ofObject(1f, 'Z')
        );
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("TranslationY", 0, 1000, 700, 1400, 0);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(tvChar, charHolder, translationY);
        view.findViewById(R.id.btn_start)
                .setOnClickListener(v -> {
                    animator.setDuration(6000);
                    animator.setInterpolator(new BounceInterpolator());
                    animator.start();
                });

    }

    private class CharEvaluator implements TypeEvaluator<Character> {

        @Override
        public Character evaluate(float fraction, Character startValue, Character endValue) {
            int sta = startValue;
            int end = endValue;
            char c = (char) (sta + (end - sta) * fraction);
//            LogUtils.e("char=== " + c);
            return c;
        }
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
