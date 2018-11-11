package com.python.cat.potato.base;

import android.content.Context;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.file.LogFileEngine;
import com.apkfuns.logutils.file.LogFileParam;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.pqpo.librarylog4a.LogBuffer;

public class LogFactory implements LogFileEngine {
    private static final String FORMAT = "[%s][%s][%s:%s]%s\n";
    private DateFormat dateFormat;
    private LogBuffer buffer;
    private Context context;

    public LogFactory(Context context) {
        this.context = context;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS",
                Locale.getDefault());
    }

    @Override
    public void writeToFile(File logFile, String logContent, LogFileParam params) {
        if (buffer == null) {
            synchronized (LogFileEngine.class) {
                if (buffer == null) {
                    if (context == null) {
                        throw new NullPointerException("Context must not null!");
                    }
                    File bufferFile = new File(context.getFilesDir(), ".log4aCache");
                    buffer = new LogBuffer(bufferFile.getAbsolutePath(), 4096,
                            logFile.getAbsolutePath(), false);
                }
            }
        }
        buffer.write(logContent);
    }

    /**
     * 写入文件的内容
     *
     * @param logContent log value
     * @param params     LogFileParam
     * @return file log content
     */
    private String getWriteString(String logContent, LogFileParam params) {
        String time = dateFormat.format(new Date(params.getTime()));
        return String.format(FORMAT, time, getLogLevelString(params.getLogLevel()),
                params.getThreadName(), params.getTagName(), logContent);
    }

    /**
     * 日志等级
     *
     * @param level level
     * @return level string
     */
    private String getLogLevelString(int level) {
        switch (level) {
            case LogLevel.TYPE_VERBOSE:
                return "V";
            case LogLevel.TYPE_ERROR:
                return "E";
            case LogLevel.TYPE_INFO:
                return "I";
            case LogLevel.TYPE_WARM:
                return "W";
            case LogLevel.TYPE_WTF:
                return "Wtf";
        }
        return "D";
    }

    @Override
    public void flushAsync() {
        if (buffer != null) {
            buffer.flushAsync();
        }
    }

    @Override
    public void release() {
        if (buffer != null) {
            buffer.release();
            buffer = null;
        }
    }

}
