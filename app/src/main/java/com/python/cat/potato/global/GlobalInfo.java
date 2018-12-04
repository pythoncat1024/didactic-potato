package com.python.cat.potato.global;

public class GlobalInfo {
    private GlobalInfo() {
    }

    public static final boolean ALLOW_LOG = true;
    public static boolean SHOW_LOADING = true;
    public static int LOADING_SECONDS = 5;

    public static boolean LOG2FILE = false; // 日志写进文件


    public static interface IntentKey {

        String KEY_EVENT_ID = "key_event_id";
    }
}
