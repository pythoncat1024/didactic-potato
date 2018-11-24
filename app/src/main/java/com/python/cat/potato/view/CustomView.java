package com.python.cat.potato.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

import java.util.Locale;

public class CustomView extends View {

    private Paint linePaint;
    private Path path;
    private int centerX;
    private int centerY;

    private int count = 6; // 6层
    private int slideSize = 6; // 6边形
    private int maxRadius; // 最大半径

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs,
                      int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        path.moveTo(centerX, centerY);
        maxRadius = Math.round(Math.min(centerX, centerY) * 0.9f);

        String format = "center:(%d,%d) ### maxRadius = %d";
        LogUtils.i(String.format(Locale.ENGLISH, format, centerX, centerY, maxRadius));
        path.reset();
        path.rewind();

        postInvalidate();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeWidth(1);
        linePaint.setStyle(Paint.Style.STROKE);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(4);

        canvas.drawPoint(centerX, centerY, linePaint); // ok
        int radius = maxRadius; // 每层的半径

        for (float angle = 0; angle < 360; angle += 360 / count) {
            float x = centerX + (float) (radius * cos(angle));
            float y = centerY + (float) (radius * sin(angle));
//            canvas.drawPoint(x, y, linePaint);
            if (angle == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path,linePaint);
    }

    /**
     * @param angle 0-360
     * @return [-1,1]
     */
    private double sin(double angle) {
        angle = angle * Math.PI / 180;
        return Math.sin(angle);
    }

    /**
     * @param angle 0-360
     * @return [-1,1]
     */
    private double cos(double angle) {
        angle = angle * Math.PI / 180;
        return Math.cos(angle);
    }
}
