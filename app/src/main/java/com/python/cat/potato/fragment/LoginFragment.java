package com.python.cat.potato.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.BaseApplication;
import com.python.cat.potato.base.BaseFragment;
import com.python.cat.potato.base.LoginCallback;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.net.HttpRequest;
import com.python.cat.potato.utils.SpUtils;
import com.python.cat.potato.utils.ToastHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {


    private LoginCallback result;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar loginProgressBar = view.findViewById(R.id.login_progress);
        ViewGroup signFormLayout = view.findViewById(R.id.login_form);
        EditText tvUsername = view.findViewById(R.id.et_username_login);
        EditText tvPassword = view.findViewById(R.id.et_password_login);
        View btnSignIn = view.findViewById(R.id.user_sign_in_button);

        btnSignIn.setOnClickListener(v -> {
            tvUsername.setText("pythoncat");
            tvPassword.setText("wanandroid123");
            tvUsername.setSelection(tvUsername.length());
            tvPassword.setSelection(tvPassword.length());
            String username = tvUsername.getText().toString();
            String password = tvPassword.getText().toString();

            com.apkfuns.logutils.LogUtils.d("click sign in...");

            loginProgressBar.setVisibility(View.VISIBLE);
            addDisposable(
                    HttpRequest
                            .userLogin(username, password)
                            .subscribe(info -> {
                                LogUtils.e(info);
                                if (info.errorCode == 0) {
                                    loginProgressBar.setVisibility(View.GONE);
                                    LogUtils.d("login success...");
                                    ToastHelper.show(BaseApplication.get(), "login success");
                                    SpUtils.put(getContext(), GlobalInfo.SP_KEY_USERNAME, username);
                                } else {
                                    throw new Exception(info.errorMsg);
                                }
                            }, e -> {
                                loginProgressBar.setVisibility(View.GONE);
                                LogUtils.e(e);
                                ToastHelper.show(BaseApplication.get(), e.getMessage());
                                if (result != null) {
                                    result.onResult(false);
                                }
                            })
            );
        });
    }

    public void setLoginResult(LoginCallback result) {
        this.result = result;
    }
}
