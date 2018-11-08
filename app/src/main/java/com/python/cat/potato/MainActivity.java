package com.python.cat.potato;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.base.BaseActivity;
import com.python.cat.potato.viewmodel.MainVM;

import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {

    private MainVM mainVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainVM = new MainVM();
        TextView tv = findViewById(R.id.tv_text);
        Disposable subscribe = mainVM.interval(5)
                .doOnComplete(() -> {
                    Intent intent = new Intent(get(), DrawerActivity.class);
                    startActivity(intent);
                })
                .subscribe(t -> {
                            LogUtils.d("t==" + t);
                            tv.setText(getString(R.string.welcome_text, t));
                        }, Throwable::printStackTrace
                );
        addDisposable(subscribe);
    }

    @Override
    protected void onDestroy() {
        mainVM = null;
        super.onDestroy();
    }
}
