package com.python.cat.potato.viewmodel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.apkfuns.logutils.LogUtils;

public class CalendarFragmentVM {

    /**
     * from: https://blog.csdn.net/wenzhi20102321/article/details/80644833
     */
    public void queryCalendarEvents( Context context) {

        if(context==null){
            throw new RuntimeException("Context == null");
        }

        LogUtils.v("query calendar events...");
        String CALENDER_EVENT_URL = "content://com.android.calendar/events";
        Uri uri = Uri.parse(CALENDER_EVENT_URL);
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver==null");
        }
        Cursor cursor = resolver
                .query(uri, null, null, null, null);
        if (cursor == null) {
            throw new RuntimeException("Cursor == null");
        }
        int columnCount = cursor.getColumnCount();
        LogUtils.d("columnCount :" + columnCount);// 多少个属性
        while (cursor.moveToNext()) {
            for (int i = 0; i < columnCount; i++) {
                //获取到属性的名称
                String columnName = cursor.getColumnName(i);
                //获取到属性对应的值
                String message = cursor.getString(cursor.getColumnIndex(columnName));
                //打印属性和对应的值
                LogUtils.d(columnName + " : " + message);

            }
        }
        cursor.close();
    }
}
