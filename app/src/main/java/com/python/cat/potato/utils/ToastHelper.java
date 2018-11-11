package com.python.cat.potato.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

public class ToastHelper {

    private ToastHelper() {
    }

    private static Toast tn;


    public static void show(@NonNull Context context, CharSequence text) {
        cancel();
        tn = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        tn.show();
    }

    public static void cancel() {
        if (tn != null) {
            tn.cancel();
            tn = null;
        }
    }


}
