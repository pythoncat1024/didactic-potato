package com.python.cat.potato.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.DrawerFragment;

import java.util.Random;
import java.util.UUID;

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
        ListView lv = view.findViewById(R.id.list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(lv.getContext(),
                android.R.layout.simple_expandable_list_item_1);
        adapter.addAll(getResources().getStringArray(android.R.array.postalAddressTypes));
        lv.setAdapter(adapter);

        view.findViewById(R.id.btn_add)
                .setOnClickListener(v -> {
                    int bound = adapter.getCount() - 1;
                    bound = bound <= 1 ? 1 : bound;
                    int index = new Random().nextInt(bound);
                    index = index <= 0 ? 0 : index;
                    adapter.insert(UUID.randomUUID().toString(), index);
                });
        view.findViewById(R.id.btn_remove).setOnClickListener(v -> {
            int bound = adapter.getCount() - 1;
            if (bound <= 0) return;
            int index = new Random().nextInt(bound);
            View childAt = lv.getChildAt(index);
            PropertyValuesHolder removeX = PropertyValuesHolder.ofFloat("translationX",
                    0, childAt.getWidth(), 0); // 向右滚粗
            removeX = PropertyValuesHolder.ofFloat("translationY",
                    0, -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), -childAt.getHeight(), 0);
            LogUtils.d(childAt);
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 1f);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(childAt, removeX,alpha);
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    adapter.remove(adapter.getItem(index));
                }
            });
            animator.start();

        });

        LayoutTransition transition = new LayoutTransition();
        ObjectAnimator animator = ObjectAnimator.ofFloat((View) null, "translationY", -100f, 80f, 0);
        transition.setAnimator(LayoutTransition.DISAPPEARING, animator); // 无效
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, animator); // 无效
        transition.setAnimator(LayoutTransition.APPEARING, animator);
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING, animator);
        lv.setLayoutTransition(transition);
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
