package com.python.cat.potato.view.measure;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

public class ItemView extends View {

    private int MIN_WIDTH = dp2px(10);
    private int MIN_HEIGHT = dp2px(10);

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs,
                    int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 1. 获取父控件的大小
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        LogUtils.d("default www=" + width + " , height=" + height);
        int fw = resolveMeasure(widthMeasureSpec, MIN_WIDTH);
        int fh = resolveMeasure(heightMeasureSpec, MIN_HEIGHT);
        LogUtils.d("fw=" + fw + " , " + fh);
        setMeasuredDimension(fw, fh);
    }

    private int resolveMeasure(int lenMeasureSpec, int minLength) {
        int len;
        int mode = MeasureSpec.getMode(lenMeasureSpec);
        int size = MeasureSpec.getSize(lenMeasureSpec);
        switch (mode) {
            case MeasureSpec.AT_MOST:
                len = minLength;
                break;
            default:
                len = size;
                break;
        }
        return len;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtils.i("size== " + w + " ,,, " + h);
        LogUtils.d("300dp== " + dp2px(300));
        LogUtils.d("400dp== " + dp2px(400));
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }
}

