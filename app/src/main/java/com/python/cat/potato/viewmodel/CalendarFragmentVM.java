package com.python.cat.potato.viewmodel;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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

//        String CALENDER_EVENT_URL = "content://com.android.calendar/events";
//        Uri uri = Uri.parse(CALENDER_EVENT_URL);
        Uri uri = CalendarContract.Events.CONTENT_URI;
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
                if (columnName.equals(Events.DTSTART)
                        || columnName.equals(Events.DTEND)
                        || columnName.equals(Events.LAST_DATE)) {
                    if (!TextUtils.isEmpty(message)) {
                        message += " # " + formatTime(Long.parseLong(message));
                    }
                }
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
//        LogUtils.i(line);
//        LogUtils.getLog2FileConfig().flushAsync();
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


    /**
     * from: https://blog.csdn.net/wenzhi20102321/article/details/80644833
     */
    private void blogInsert(Context context) {

        String calID = "1";
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 9, 14, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 9, 14, 8, 45);
        endMillis = endTime.getTimeInMillis();


        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, "Jazzercise");
        values.put(Events.DESCRIPTION, "Group workout");
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri uri = cr.insert(Events.CONTENT_URI, values);          //插入数据的实际操作

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
//
// ... do something with event ID，比如添加提醒事件（往提醒表添加数据），或者其他事件
    }


    private Uri _insertEvent(Context context, String title, String desc, boolean allDay,
                             long start, long end, String timezone, String location) {
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        Uri uri = CalendarContract.Events.CONTENT_URI;

        ContentValues info = new ContentValues();
        // todo
        // todo
        // todo 这一块是硬编码，实际上应该是从其他地方获取的
//        info.put(Events.ACCOUNT_NAME, "upmail@exlab.com");
//        info.put(Events.ACCOUNT_TYPE, "Local");
        String brand = android.os.Build.BRAND;
        LogUtils.e(String.format(Locale.getDefault(), "brand=[%s]", brand));
        if ("Xiaomi".equals(brand)) {
            info.put(Events.CALENDAR_ID, 4); // 小米金融
            LogUtils.d("insert calendar_id: " + 4);
        } else {
            info.put(Events.CALENDAR_ID, 15); // upmail
            LogUtils.d("insert calendar_id: " + 15);
        }
        // todo
        // todo
        info.put(Events.TITLE, title);
        info.put(Events.DESCRIPTION, desc);
        info.put(Events.DTSTART, start);
        info.put(Events.DTEND, end);
        info.put(Events.ALL_DAY, allDay ? 1 : 0);
        info.put(Events.EVENT_TIMEZONE, timezone);
        info.put(Events.EVENT_LOCATION, location);
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver == null");
        }
        Uri insert = resolver.insert(uri, info);
        LogUtils.d("insert=== " + insert);
        return insert;
    }

    public Flowable<Uri> insertEvent(Context context, String title, String desc,
                                     boolean allDay, long start, long end,
                                     String timezone, String location) {

        return Flowable.create((FlowableOnSubscribe<Uri>) emitter -> {
            Uri uri = _insertEvent(context, title, desc,
                    allDay, start, end, timezone, location);
            emitter.onNext(uri);
            emitter.onComplete();
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }


//    private int _deleteEvent(Context context){
//        long ID = 20; //这个id一定是要表中_id的值，不能是calendar_id的值。
//        ContentResolver cr = getContentResolver();
//        ContentValues values = new ContentValues();
//        Uri deleteUri = null;
//        deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
//        int rows = getContentResolver().delete(deleteUri, null, null);
//        Log.i(DEBUG_TAG, "Rows deleted: " + rows);
//    }
//

    public long createStartTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, instance.get(Calendar.YEAR));
        instance.set(Calendar.MONTH, instance.get(Calendar.MONTH));
        instance.set(Calendar.DAY_OF_MONTH, new Random().nextInt(27) + 1);
        instance.set(Calendar.HOUR_OF_DAY, new Random().nextInt(23));
        instance.set(Calendar.MINUTE, new Random().nextInt(59));
        instance.set(Calendar.SECOND, new Random().nextInt(59));
        instance.set(Calendar.MILLISECOND, new Random().nextInt(1000));
        return instance.getTimeInMillis();
    }

    public long createEndTime(long timeInMillis) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        instance.add(Calendar.MINUTE, new Random().nextInt(110) + 10);
        return instance.getTimeInMillis();
    }

    public String formatTime(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        String formatTime = String.format(Locale.getDefault(),
                "%04d%02d%02d %02d:%02d:%02d.%03d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND)
        );
        return formatTime;
    }
}
