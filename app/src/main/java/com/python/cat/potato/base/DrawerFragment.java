package com.python.cat.potato.base;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;

/**
 * fragment base on {@link com.python.cat.potato.activity.DrawerActivity}
 */
public class DrawerFragment extends BaseFragment {
    private TitleHook hook;

    private void showCurrentTitle() {
        if (hook != null) {
            hook.setFragmentTitle(getClass().getSimpleName());
            LogUtils.d("setFragmentTitle" + getClass().getSimpleName());
        }
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


}
