package com.python.cat.potato.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

public abstract class BaseFragment extends Fragment {

    private TitleHook hook;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TitleHook) {
            hook = (TitleHook) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " current activity must implement "
                    + TitleHook.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hook = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (hook != null) {
            hook.setToolbarTitle(getClass().getSimpleName());
        }

        LogUtils.d(hook + " , " + getClass().getSimpleName());
    }
}
