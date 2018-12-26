package com.python.cat.potato.apm;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.tencent.matrix.plugin.DefaultPluginListener;
import com.tencent.matrix.report.Issue;
import com.tencent.matrix.util.MatrixLog;

public class TestPluginListener extends DefaultPluginListener {
    public static final String TAG = "Matrix.TestPluginListener";
    public TestPluginListener(Context context) {
        super(context);
        
    }

    @Override
    public void onReportIssue(Issue issue) {
        super.onReportIssue(issue);
        MatrixLog.e(TAG, issue.toString());
        
        //add your code to process data

        LogUtils.e(issue);
    }
}