package com.python.cat.potato.viewmodel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.adapter.CalendarInfoAdapter;
import com.python.cat.potato.utils.ToastHelper;

import org.json.JSONObject;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CalDeletePop {

    private Context mContext;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mDataRecyclerView;
    private CompositeDisposable compositeDisposable;
    private CalendarInfoAdapter adapter;

    public CalDeletePop(Context context) {
        this.mContext = context;
    }

    private PopupWindow mPopupWindow;

    public PopupWindow showAsBottomPopupWindow(View button) {
        compositeDisposable = new CompositeDisposable();

        final ViewGroup va = getPopupWindowLayout();
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mContext);
        }
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(va);
        // 设置PopupWindow的背景
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // 设置PopupWindow是否能响应外部点击事件
        mPopupWindow.setOutsideTouchable(true); // 外部点击，window 消息

        // 添加动画
        mPopupWindow.setAnimationStyle(R.style.BottomPopupWindow);
        // window.showAsDropDown(v);
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        mPopupWindow.showAtLocation(button.getRootView(), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopupWindow.setOnDismissListener(this::releaseAll);
        return mPopupWindow;
    }

    private ViewGroup getPopupWindowLayout() {
        View rootView = LayoutInflater.from(mContext)
                .inflate(R.layout.calendar_delete_popup_window_layout, null);
        refreshLayout = rootView
                .findViewById(R.id.popup_calendar_swipe_refresh_layout);
        mDataRecyclerView = rootView.findViewById(R.id.popup_calendar_recycler_view);
        mDataRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new CalendarInfoAdapter(mContext);
        mDataRecyclerView.setAdapter(adapter);
        addDisposable(
                CalendarFragmentVM.queryAllEventsHadMessageID(mContext)
                        .subscribe(infoList -> {
                            ToastHelper.show(mContext, mContext.getString(R.string.events_count, infoList.size()));
                            adapter.setCalendarInfoList(infoList);
                        }, Throwable::printStackTrace)
        );
        adapter.setOnItemLongClickListener((targetView, info, adapterPosition) -> {
            // 删除事件
            itemLongClick(info, adapterPosition);
        });

        refreshLayout.setOnRefreshListener(() -> addDisposable(
                CalendarFragmentVM.queryAllEventsHadMessageID(mContext)
                        .doOnError(e -> ToastHelper.show(mContext, "刷新失败..."))
                        .subscribe(infoList -> {
                                    adapter.setCalendarInfoList(infoList);
                                    ToastHelper.show(mContext, "刷新成功..."
                                            + mContext.getString(R.string.events_count, infoList.size()));
                                },
                                e -> {
                                    LogUtils.e(e);
                                    refreshLayout.setRefreshing(false);
                                },
                                () -> refreshLayout.setRefreshing(false))
        ));

        return (ViewGroup) rootView;
    }

    private void itemLongClick(String info, int adapterPosition) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(Objects.requireNonNull(mContext));
        builder.setMessage(info)
                .setCancelable(true)
                .setPositiveButton(
                        R.string.positive_button_text,
                        (dialog, which) -> {
                            LogUtils.d("delete....");
                            LogUtils.json(info);
                            try {
                                JSONObject object = new JSONObject(info);
                                String customAppPackage = Events.CUSTOM_APP_PACKAGE;
                                int messageID = object.getInt(customAppPackage);
                                doDelete(messageID,adapterPosition);
                            } catch (Exception e) {
                                LogUtils.e(e);
                                throw new RuntimeException("info has no _id");
                            }
                        })
                .setTitle(R.string.delete_event_or_not);
        builder.show();
    }


    private void doDelete(int messageID, int adapterPosition) {
        addDisposable(
                CalendarFragmentVM.deleteEventByMessageID(
                        mContext, messageID)
                        .subscribe(rows -> {
                            LogUtils.d("after delete: " + rows);
                            if (rows > 0) {
                                ToastHelper.show(Objects.requireNonNull(mContext),
                                        "delete success.." + rows);
                                adapter.notifyItemRemoved(adapterPosition); // 老是不准确
                                adapter.notifyDataSetChanged();
                            }
                        }, Throwable::printStackTrace)
        );
    }


    public void hidePopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    private void releaseAll() {
        if (compositeDisposable != null
                && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        adapter = null;
        mPopupWindow = null;
        mContext = null;
        refreshLayout = null;
        mDataRecyclerView = null;

    }


    private void addDisposable(Disposable disposable) {
        if (compositeDisposable != null && disposable != null) {
            compositeDisposable.add(disposable);
        } else {
            throw new RuntimeException("error: "
                    + compositeDisposable + " ### " + disposable);
        }
    }

}
