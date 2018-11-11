package com.python.cat.potato.viewmodel;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CalendarFragmentVM {
    private CalendarFragmentVM() {
    }

    private static final String SORT_ORDER = "_id DESC";

    /**
     * from: https://blog.csdn.net/wenzhi20102321/article/details/80644833
     * <br/>
     * 查询全部的日历事件，包含<b>主要的</b>事件字段
     */
    private static List<String> _queryCalendarEventsSimple(Context context) throws JSONException {
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
                Events.CALENDAR_ID,
                Events.ACCOUNT_TYPE,
                Events.ACCOUNT_NAME,
                Events.CALENDAR_DISPLAY_NAME,
                Events._ID,
                Events.CUSTOM_APP_PACKAGE,
                Events.TITLE,
                Events.DESCRIPTION,
                Events.EVENT_LOCATION,
                Events.EVENT_TIMEZONE,
                Events.DTSTART,
                Events.DTEND,
                Events.LAST_DATE,
                Events.ALL_DAY,
                Events.RRULE,
                Events.DURATION,
        };
        Cursor cursor = resolver
                .query(uri, projection, null, null, SORT_ORDER);
        if (cursor == null) {
            throw new RuntimeException("Cursor == null");
        }
        int columnCount = cursor.getColumnCount();
        LogUtils.d("columnCount :" + columnCount);// 多少个属性
//        line.append("all info: [\n");
        while (cursor.moveToNext()) {
            JSONObject obj = new JSONObject();
            for (int i = 0; i < columnCount; i++) {
                //获取到属性的名称
                String columnName = cursor.getColumnName(i);
                //获取到属性对应的值
                String message = cursor.getString(cursor.getColumnIndex(columnName));
                //打印属性和对应的值
//                LogUtils.d(columnName + " : " + message);
                obj.put(columnName, message);
            }
            infoList.add(formatJson(obj));
        }
//        LogUtils.i(line);
//        LogUtils.getLog2FileConfig().flushAsync();
        cursor.close();
        return infoList;
    }


    /**
     * from: https://blog.csdn.net/wenzhi20102321/article/details/80644833
     * <br/>
     * 查询全部的日历事件，包含<b>全部的</b>事件字段
     */
    private static List<String> _queryCalendarEvents(Context context) throws JSONException {
        ArrayList<String> infoList = new ArrayList<>();
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        LogUtils.i("query calendar events...");
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver==null");
        }
        Cursor cursor = resolver
                .query(Events.CONTENT_URI, null, null,
                        null, SORT_ORDER);
        if (cursor == null) {
            throw new RuntimeException("Cursor == null");
        }
        int columnCount = cursor.getColumnCount();
        LogUtils.d("columnCount :" + columnCount);// 多少个属性
        int row = 0; // 第几行
//        line.append("all info: [\n");
        while (cursor.moveToNext()) {
            JSONObject jObj = new JSONObject();
            for (int i = 0; i < columnCount; i++) {
                //获取到属性的名称
                String columnName = cursor.getColumnName(i);
                //获取到属性对应的值
                String message = cursor.getString(cursor.getColumnIndex(columnName));
                //打印属性和对应的值
//                LogUtils.d(columnName + " : " + message);
                jObj.put(columnName, message);
            }
            infoList.add(formatJson(jObj));
            row += 1;
        }
        cursor.close();
        return infoList;
    }


    private static String _queryEventById(Context context, long eventID) throws JSONException {
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        LogUtils.i("query calendar events...");
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver==null");
        }
        String selection = String.format(Locale.ENGLISH, "%s = ?", Events._ID);
        String[] selectionArgs = new String[]{String.valueOf(eventID)};
        Cursor cursor = resolver
                .query(Events.CONTENT_URI, null, selection,
                        selectionArgs, SORT_ORDER);
        if (cursor == null) {
            throw new RuntimeException("Cursor == null");
        }
        int columnCount = cursor.getColumnCount();
        LogUtils.d("columnCount :" + columnCount); // 多少个属性
        JSONObject obj = new JSONObject();
        if (cursor.moveToNext()) {
            for (int i = 0; i < columnCount; i++) {
                // 获取到属性的名称
                String columnName = cursor.getColumnName(i);
                // 获取到属性对应的值
                String message = cursor.getString(cursor.getColumnIndex(columnName));
                // 打印属性和对应的值
                obj.put(columnName, message);
            }
        }
        LogUtils.json(obj.toString());
        cursor.close();
        return obj.toString();
    }

    private static List<String> _queryEventsByMessageID(Context context, long messageID)
            throws JSONException {
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        LogUtils.i("query calendar events...");
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver==null");
        }
        ArrayList<String> infoList = new ArrayList<>();
        String selection = String.format(Locale.ENGLISH, "%s = ?",
                Events.CUSTOM_APP_PACKAGE);
        String[] selectionArgs = new String[]{String.valueOf(messageID)};
        Cursor cursor = resolver
                .query(Events.CONTENT_URI, null, selection,
                        selectionArgs, SORT_ORDER);
        if (cursor == null) {
            throw new RuntimeException("Cursor == null");
        }
        int columnCount = cursor.getColumnCount();
        LogUtils.d("columnCount :" + columnCount); // 多少个属性
        StringBuilder line = new StringBuilder();
        while (cursor.moveToNext()) {
            JSONObject obj = new JSONObject();
            for (int i = 0; i < columnCount; i++) {
                // 获取到属性的名称
                String columnName = cursor.getColumnName(i);
                // 获取到属性对应的值
                String message = cursor.getString(cursor.getColumnIndex(columnName));
                // 打印属性和对应的值
                obj.put(columnName, message);
            }
            line.append(obj.toString());
            line.append("\n");
            infoList.add(formatJson(obj));
        }
        LogUtils.d(line);
        cursor.close();
        return infoList;
    }

    private static List<String> _queryEventsHadMessageID(Context context)
            throws JSONException {
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        LogUtils.i("query calendar events...");
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver==null");
        }
        ArrayList<String> infoList = new ArrayList<>();

        String[] projection = new String[]{
                Events.CALENDAR_ID,
                Events.ACCOUNT_TYPE,
                Events.ACCOUNT_NAME,
                Events.CALENDAR_DISPLAY_NAME,
                Events._ID,
                Events.CUSTOM_APP_PACKAGE,
                Events.TITLE,
                Events.DESCRIPTION,
                Events.DTSTART,
        };
        Cursor cursor = resolver
                .query(Events.CONTENT_URI, projection, null,
                        null, SORT_ORDER);
        if (cursor == null) {
            throw new RuntimeException("Cursor == null");
        }
        int columnCount = cursor.getColumnCount();
        LogUtils.d("columnCount :" + columnCount); // 多少个属性
        while (cursor.moveToNext()) {
            JSONObject obj = new JSONObject();
            for (int i = 0; i < columnCount; i++) {
                String columnName = cursor.getColumnName(i);
                String message = cursor.getString(cursor.getColumnIndex(columnName));
                obj.put(columnName, message);
            }
            if (obj.has(Events.CUSTOM_APP_PACKAGE)) {
                String messageID = obj.getString(Events.CUSTOM_APP_PACKAGE);
                try {
                    long message = Long.parseLong(messageID);
                    if (message != Long.MAX_VALUE) {
                        // 无意义的判断 ~
                        infoList.add(formatJson(obj));
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        cursor.close();
        return infoList;
    }

    public static Flowable<List<String>> queryAllEventsHadMessageID(Context context) {
        return Flowable.create(
                (FlowableOnSubscribe<List<String>>) emitter ->
                {
                    List<String> stringList = _queryEventsHadMessageID(context);
                    emitter.onNext(stringList);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<List<String>> queryAllEventsSimple(Context context) {
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

    public static Flowable<List<String>> queryAllEvents(Context context) {
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


    public static Flowable<List<String>> queryEventsByMessageID(Context context, long messageID) {
        return Flowable.create((FlowableOnSubscribe<List<String>>) emitter ->
                {
                    List<String> stringList = _queryEventsByMessageID(context, messageID);
                    emitter.onNext(stringList);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<String> queryEventByID(Context context, long eventID) {
        return Flowable.create((FlowableOnSubscribe<String>) emitter ->
                {

                    String info = _queryEventById(context, eventID);
                    emitter.onNext(info);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * from: https://blog.csdn.net/mysimplelove/article/details/81018641
     */
    private static long checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Calendars.CONTENT_URI,
                null, null, null, null);
        try {
            if (userCursor == null) { // 查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { // 存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * from: https://blog.csdn.net/mysimplelove/article/details/81018641
     * 添加日历账户
     */
    private static Uri addCalendarAccount(Context context) {
        String calendarName = "custom";
        String accountName = "custom@test.com";
        String accountType = "com.android.custom";
        String displayName = "自定义日历账户";
        TimeZone timeZone = TimeZone.getDefault();
        LogUtils.d(timeZone);
        LogUtils.d(timeZone.getID());
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, calendarName);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, accountType);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, displayName);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, accountName);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,
                        accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        accountType)
                .build();
        Uri result = context.getContentResolver().insert(calendarUri, value);
        return result;
    }


    /**
     * <pre>
     * 添加日历事件:
     * 1. 插入到事件表 Events (必选项)
     * 2. 插入到参加者表 Reminders （可选，如果有参加者的话）
     * 3. 插入到提醒表 Attendees（可选，如果有提醒的话）
     * </pre>
     */
    private static Uri _insertEvent(Context context, String title, String desc, boolean allDay,
                                    long start, long end, String timezone, String location) {
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        // 1. 插入到事件表
        ContentValues eventInfo = new ContentValues();
        // todo
        // todo 这一块是硬编码，实际上应该是从其他地方获取的
//        eventInfo.put(Events.ACCOUNT_NAME, "upmail@exlab.com");
//        eventInfo.put(Events.ACCOUNT_TYPE, "Local");
        String brand = android.os.Build.BRAND;
        LogUtils.e(String.format(Locale.getDefault(), "brand=[%s]", brand));
        if ("Xiaomi".equals(brand)) {
            eventInfo.put(Events.CALENDAR_ID, 4); // 小米金融
            LogUtils.d("insert calendar_id: " + 4);
        } else {
            eventInfo.put(Events.CALENDAR_ID, 15); // upmail
            LogUtils.d("insert calendar_id: " + 15);
        }
        String customField = String.valueOf(new Random().nextInt(1024) + 111); // 自定义字段
        eventInfo.put(Events.CUSTOM_APP_PACKAGE, customField); // ok
        eventInfo.put(Events.TITLE, title);
        eventInfo.put(Events.DESCRIPTION, desc);
        eventInfo.put(Events.DTSTART, start);
        eventInfo.put(Events.DTEND, end);
        eventInfo.put(Events.ALL_DAY, allDay ? 1 : 0);
        eventInfo.put(Events.EVENT_TIMEZONE, timezone);
        eventInfo.put(Events.EVENT_LOCATION, location);
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            throw new RuntimeException("ContentResolver == null");
        }
        Uri insert = resolver.insert(Events.CONTENT_URI, eventInfo);
        if (insert != null) {
            String lastPathSegment = insert.getLastPathSegment();
            // 2. 插入到参加者表
            long eventID = ContentUris.parseId(insert);
            LogUtils.v("eventID: " + lastPathSegment + " ## " + eventID);
            ContentValues attendeeInfo = new ContentValues();
            attendeeInfo.put(Attendees.ATTENDEE_NAME, "Tom猫");
            attendeeInfo.put(Attendees.ATTENDEE_EMAIL, "tomcat@example.com");
            attendeeInfo.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ATTENDEE);
            attendeeInfo.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_OPTIONAL);
            attendeeInfo.put(Attendees.ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_INVITED);
            attendeeInfo.put(Attendees.EVENT_ID, eventID);
            Uri attendee = resolver.insert(Attendees.CONTENT_URI, attendeeInfo);
            LogUtils.d("after attendee : " + attendee);
            // 3. 插入到提醒表
            ContentValues reminderInfo = new ContentValues();
            reminderInfo.put(Reminders.MINUTES, 60 * 24 * 7); // 一周前
            reminderInfo.put(Reminders.EVENT_ID, eventID);
            reminderInfo.put(Reminders.METHOD, Reminders.METHOD_ALERT);
            Uri reminder = resolver.insert(Reminders.CONTENT_URI, reminderInfo);
            LogUtils.d("after reminders : " + reminder);
        }
        LogUtils.d("after insert : " + insert);
        return insert;
    }

    public static Flowable<Uri> insertEvent(Context context, String title, String desc,
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


    private static int _deleteEventByMessageID(Context context, int messageID) {
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        ContentResolver cr = context.getContentResolver();
        if (cr == null) {
            throw new RuntimeException("ContentResolver == null");
        }
        String where = String.format(Locale.ENGLISH, "%s = ?", Events.CUSTOM_APP_PACKAGE);
        String[] selectionArgs = new String[]{String.valueOf(messageID)};
        int rows = cr.delete(Events.CONTENT_URI, where, selectionArgs);
        return rows;
    }

    private static int _deleteEventByID(Context context, int eventID) {
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        ContentResolver cr = context.getContentResolver();
        if (cr == null) {
            throw new RuntimeException("ContentResolver == null");
        }
//      Uri deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
        String where = String.format(Locale.ENGLISH, "%s = ?", Events._ID);
        String[] selectionArgs = new String[]{String.valueOf(eventID)};
        int rows = cr.delete(Events.CONTENT_URI, where, selectionArgs);
        return rows;
    }

    private static int _deleteByCustom(Context context, String where, String[] selectionArgs) {
        if (context == null) {
            throw new RuntimeException("Context == null");
        }
        ContentResolver cr = context.getContentResolver();
        if (cr == null) {
            throw new RuntimeException("ContentResolver == null");
        }
        int rows = cr.delete(Events.CONTENT_URI, where, selectionArgs);
        return rows;
    }

    public static Flowable<Integer> deleteByCustom(Context context, String where,
                                                   String[] selectionArgs) {
        return Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {
                    int delete = _deleteByCustom(context, where, selectionArgs);
                    emitter.onNext(delete);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<Integer> deleteByCustom(Context context, String info) {
        return Flowable.create((FlowableOnSubscribe<JSONObject>) emitter -> {
                    JSONObject object = new JSONObject(info);
                    emitter.onNext(object);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .flatMap((Function<JSONObject, Publisher<Integer>>) json ->
                {
                    String selection = null;
                    String[] selectionArgs;
                    Iterator<String> keys = json.keys();
                    StringBuilder keyBuff = new StringBuilder();
                    ArrayList<String> values = new ArrayList<>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = json.getString(key);
                        String keyItem = String.format(Locale.ENGLISH,
                                "%s = ? and ", key);
                        keyBuff.append(keyItem);
                        values.add(value);
                    }
                    String temp = keyBuff.toString();
                    if (temp.endsWith(" and ")) {
                        selection = temp.substring(0, temp.length() - " and ".length());
                    }
                    selectionArgs = values.toArray(new String[0]);
                    LogUtils.d("custom delete...");
                    LogUtils.d(selection);
                    LogUtils.d(selectionArgs);
                    return deleteByCustom(context, selection, selectionArgs);
                });
    }


    public static Flowable<Integer> deleteEventByMessageID(Context context, int messageID) {
        return Flowable.create((FlowableOnSubscribe<Integer>) emitter ->
                {
                    int delete = _deleteEventByMessageID(context, messageID);
                    emitter.onNext(delete);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<Integer> deleteEventByID(Context context, int eventID) {
        return Flowable.create((FlowableOnSubscribe<Integer>) emitter ->
                {
                    int delete = _deleteEventByID(context, eventID);
                    emitter.onNext(delete);
                    emitter.onComplete();
                },
                BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static long createStartTime() {
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

    public static long createEndTime(long timeInMillis) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        instance.add(Calendar.MINUTE, new Random().nextInt(110) + 10);
        return instance.getTimeInMillis();
    }

    public static String formatTime(long timeInMillis) {
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

    /**
     * 将 json 换行显示
     */
    private static String formatJson(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        return formatJson(object);
    }

    /**
     * 将 json 换行显示
     */
    public static String formatJsonWithTime(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        return formatJsonWithTime(object);
    }

    private static String formatJson(JSONObject json) throws JSONException {
        Iterator<String> keys = json.keys();
        StringBuilder line = new StringBuilder();
        line.append("{");
        while (keys.hasNext()) {
            String key = keys.next();
            String value = json.getString(key);
            String format = String.format(Locale.getDefault()
                    , "\n\t\"%s\":\"%s\",", key, value);
            line.append(format);
        }
        if (line.charAt(line.length() - 1) == ',') {
            line.deleteCharAt(line.length() - 1);
        }
        line.append("\n").append("}");
        return line.toString();
    }

    private static String formatJsonWithTime(JSONObject json) throws JSONException {
        Iterator<String> keys = json.keys();
        StringBuilder line = new StringBuilder();
        line.append("{");
        while (keys.hasNext()) {
            String key = keys.next();
            String value = json.getString(key);
            if (key.equals(Events.DTSTART)
                    || key.equals(Events.DTEND)
                    || key.equals(Events.LAST_DATE)) {
                if (!TextUtils.isEmpty(value)) {
                    value += " # " + formatTime(Long.parseLong(value));
                }
            }
            String format = String.format(Locale.getDefault()
                    , "\n\t\"%s\":\"%s\",", key, value);
            line.append(format);
        }
        if (line.charAt(line.length() - 1) == ',') {
            line.deleteCharAt(line.length() - 1);
        }
        line.append("\n").append("}");
        return line.toString();
    }
}
