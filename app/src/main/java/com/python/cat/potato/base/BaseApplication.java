package com.python.cat.potato.base;

import android.app.Application;
import android.os.Environment;

import com.apkfuns.log2file.LogFileEngineFactory;
import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.BuildConfig;
import com.python.cat.potato.apm.DynamicConfigImplDemo;
import com.python.cat.potato.apm.TestPluginListener;
import com.python.cat.potato.global.GlobalInfo;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.matrix.Matrix;
import com.tencent.matrix.iocanary.IOCanaryPlugin;
import com.tencent.matrix.iocanary.config.IOConfig;

import java.io.File;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Holder.app = this;
        initLogUtils();
        initLeakCanary();
        initAPM();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    private void initLogUtils() {
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
                .configAllowLog(GlobalInfo.ALLOW_LOG);
    }

    private void initAPM() {
        Matrix.Builder builder = new Matrix.Builder(get()); // build matrix
        builder.patchListener(new TestPluginListener(this)); // add general pluginListener
        DynamicConfigImplDemo dynamicConfig = new DynamicConfigImplDemo(); // dynamic config

        // init plugin
        IOCanaryPlugin ioCanaryPlugin = new IOCanaryPlugin(new IOConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .build());
        //add to matrix
        builder.plugin(ioCanaryPlugin);

        //init matrix
        Matrix.init(builder.build());

        // start plugin
        ioCanaryPlugin.start();
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
