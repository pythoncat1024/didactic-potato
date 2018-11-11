package com.python.cat.potato.viewmodel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CalendarFragmentVM {

    public static final String SORT_ORDER = "_id DESC";

    /**
     * from: https://blog.csdn.net/wenzhi20102321/article/details/80644833
     * <br/>
     * 查询全部的日历事件，包含<b>全部的</b>事件字段
     */
    private List<String> _queryCalendarEvents(Context context) {
        ArrayList<String> infoList = new ArrayList<>();
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        LogUtils.i("query calendar events...");
        String CALENDER_EVENT_URL = "content://com.android.calendar/events";
        Uri uri = Uri.parse(CALENDER_EVENT_URL);
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver==null");
        }
        Cursor cursor = resolver
                .query(uri, null, null, null, SORT_ORDER);
        if (cursor == null) {
            throw new RuntimeException("Cursor == null");
        }
        int columnCount = cursor.getColumnCount();
        LogUtils.d("columnCount :" + columnCount);// 多少个属性
        StringBuilder line = new StringBuilder();
        int row = 0; // 第几行
//        line.append("all info: [\n");
        while (cursor.moveToNext()) {
            line.delete(0, line.length()); // 每次进来清空
            line.append("row ").append(row).append(" : ")
                    .append("{");
            for (int i = 0; i < columnCount; i++) {
                //获取到属性的名称
                String columnName = cursor.getColumnName(i);
                //获取到属性对应的值
                String message = cursor.getString(cursor.getColumnIndex(columnName));
                //打印属性和对应的值
//                LogUtils.d(columnName + " : " + message);
                String format = String.format(Locale.getDefault()
                        , "\n\"%s\" -> \"%s\"", columnName, message);
                line.append(format);
            }
            line.append("\n").append("}").append("\n");
            infoList.add(line.toString());
            row += 1;
        }
        line.append("\n] end...");
        LogUtils.i(line);
        LogUtils.getLog2FileConfig().flushAsync();
        cursor.close();
        return infoList;
    }

    /**
     * from: https://blog.csdn.net/wenzhi20102321/article/details/80644833
     * <br/>
     * 查询全部的日历事件，包含<b>主要的</b>事件字段
     */
    private List<String> _queryCalendarEventsSimple(Context context) {
        ArrayList<String> infoList = new ArrayList<>();
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        LogUtils.i("query calendar events...");
        String CALENDER_EVENT_URL = "content://com.android.calendar/events";
        Uri uri = Uri.parse(CALENDER_EVENT_URL);
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver==null");
        }
        String[] projection = new String[]{
                "calendar_id",
                "account_type",
                "account_name",
                "calendar_displayName",
                "_id",
                "title",
                "description",
                "eventLocation",
                "eventTimezone",
                "dtstart",
                "dtend",
                "lastDate",
                "allDay",
                "rrule",
                "duration",
        };
        Cursor cursor = resolver
                .query(uri, projection, null, null, SORT_ORDER);
        if (cursor == null) {
            throw new RuntimeException("Cursor == null");
        }
        int columnCount = cursor.getColumnCount();
        LogUtils.d("columnCount :" + columnCount);// 多少个属性
        StringBuilder line = new StringBuilder();
        int row = 0; // 第几行
//        line.append("all info: [\n");
        while (cursor.moveToNext()) {
            line.delete(0, line.length()); // 每次进来清空
            line.append("row ").append(row).append(" : ")
                    .append("{");
            for (int i = 0; i < columnCount; i++) {
                //获取到属性的名称
                String columnName = cursor.getColumnName(i);
                //获取到属性对应的值
                String message = cursor.getString(cursor.getColumnIndex(columnName));
                //打印属性和对应的值
//                LogUtils.d(columnName + " : " + message);
                String format = String.format(Locale.getDefault()
                        , "\n\"%s\" -> \"%s\"", columnName, message);
                line.append(format);
            }
            line.append("\n").append("}").append("\n");
            infoList.add(line.toString());
            row += 1;
        }
        line.append("\n] end...");
        LogUtils.i(line);
        LogUtils.getLog2FileConfig().flushAsync();
        cursor.close();
        return infoList;
    }


    public Flowable<List<String>> queryAllEventsSimple(Context context) {
        return Flowable.create(
                (FlowableOnSubscribe<List<String>>) emitter ->
                {
                    List<String> stringList = _queryCalendarEventsSimple(context);
                    emitter.onNext(stringList);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<String>> queryAllEvents(Context context) {
        return Flowable.create(
                (FlowableOnSubscribe<List<String>>) emitter ->
                {
                    List<String> stringList = _queryCalendarEvents(context);
                    emitter.onNext(stringList);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
