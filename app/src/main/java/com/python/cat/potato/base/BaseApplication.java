package com.python.cat.potato.base;

import android.app.Application;

import com.apkfuns.log2file.LogFileEngineFactory;
import com.apkfuns.logutils.LogUtils;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogFileEngineFactory engine = new LogFileEngineFactory(getApplicationContext());
        LogUtils.getLog2FileConfig().configLogFileEngine(engine);
    }
}
