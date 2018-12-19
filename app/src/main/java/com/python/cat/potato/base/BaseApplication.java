package com.python.cat.potato.base;

import android.app.Application;
import android.os.Environment;

import com.apkfuns.log2file.LogFileEngineFactory;
import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.BuildConfig;
import com.python.cat.potato.global.GlobalInfo;

import java.io.File;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogFileEngineFactory engine = new LogFileEngineFactory(getApplicationContext());
        LogUtils.getLog2FileConfig().configLogFileEngine(engine);

        if (GlobalInfo.LOG2FILE) {
            LogUtils.getLog2FileConfig().configLog2FileEnable(true)
                    // targetSdkVersion >= 23 需要确保有写sdcard权限
//                .configLog2FilePath("/sdcard/项目文件夹/logs/")
                    .configLog2FilePath(logPath())
                    .configLog2FileNameFormat("%d{yyyyMMdd}.logUtils.txt")
                    .configLogFileEngine(new LogFileEngineFactory(getApplicationContext()));
        }
        LogUtils.getLogConfig()
                .configShowBorders(false)
                .configAllowLog(GlobalInfo.ALLOW_LOG)
        ;

        Holder.app = this;
    }


    private String logPath() {
        String type = Environment.DIRECTORY_DOCUMENTS;
        File parent = Environment.getExternalStoragePublicDirectory(type);
        File selfDir = new File(parent, BuildConfig.APPLICATION_ID);
        return selfDir.getAbsolutePath();
    }

    public static class Holder {
        public static Application app;
    }

    public static Application get() {
        return Holder.app;
    }
}