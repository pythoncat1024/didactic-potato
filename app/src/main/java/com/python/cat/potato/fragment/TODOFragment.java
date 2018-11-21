package com.python.cat.potato.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.TitleHook;

/**
 * A simple {@link Fragment} subclass.
 */
public class TODOFragment extends Fragment {

    private TitleHook hook;

    public TODOFragment() {
        // Required empty public constructor
    }

    public static TODOFragment newInstance() {
        Bundle args = new Bundle();
        TODOFragment fragment = new TODOFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TitleHook) {
            hook = (TitleHook) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hook = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.w("..."+isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        showCurrentTitle();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.d(view + " , " + savedInstanceState +" ### "+hook);
    }

    private void showCurrentTitle() {
        if (hook != null) {
            hook.setFragmentTitle(getClass().getSimpleName());
            LogUtils.d("setFragmentTitle"+getClass().getSimpleName());
        }
    }
}
