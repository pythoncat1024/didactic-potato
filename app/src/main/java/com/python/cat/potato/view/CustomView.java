package com.python.cat.potato.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {

    private Paint linePaint;
    private Path path;
    private int centerX;
    private int centerY;

    private int count = 6; // 6层
    private int slideSize = 6; // 6边形
    private int maxRadius; // 最大半径
    private int[] data; // 势力分布 每个势力区间[1,6], 共6个势力
    private Paint dataPaint;
    private Paint pointPaint;

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
//        LogUtils.i(String.format(Locale.ENGLISH, format, centerX, centerY, maxRadius));
        postInvalidate();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeWidth(1);
        linePaint.setStyle(Paint.Style.STROKE);

        dataPaint = new Paint();
        dataPaint.setColor(Color.BLUE);
        dataPaint.setStyle(Paint.Style.FILL);
        dataPaint.setAlpha(100); // [0,255]

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.FILL);
        data = new int[]{2, 5, 1, 3, 4, 6}; // 势力分布
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0x33 << 24);
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(2);
//        canvas.drawPoint(centerX, centerY, linePaint); // ok
        drawSpider(canvas);
        drawLines(canvas);

        drawData(canvas);
    }

    /**
     * 画势力图
     * <pre>
     *     0. 由于势力图是覆盖的，所以要设置 Paint.Style.FILL
     *     1. 根据势力(比例)值，计算出每个势力对应的顶点
     *     2. 利用 path.moveTo + lineTo
     * </pre>
     */
    private void drawData(Canvas canvas) {
        path.rewind();
        path.reset();

        int radius;
        for (int index = 0; index < count; index++) {
            float angle = 1.0f * 360 / count * index;
            radius = maxRadius * data[index] / count;
            float x = centerX + (float) (radius * cos(angle));
            float y = centerY + (float) (radius * sin(angle));
            if (index == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            canvas.drawCircle(x, y, 10, pointPaint); // 势力顶点强调
        }
        path.close();
        canvas.drawPath(path, dataPaint);
    }

    /**
     * 画对角线
     * <pre>
     *     1. 找到顶点
     *     2. 找到对角顶点
     *     3. 利用 path.moveTo + lineTo
     *     > 注意，只要 180°即可，画的是直径，不是半径
     * </pre>
     */
    private void drawLines(Canvas canvas) {
        path.reset();
        path.rewind();
        float radius = maxRadius;
        for (float angle = 0; angle < 360 / 2; angle += 360 / count) {
            float x = centerX + (float) (radius * cos(angle));
            float y = centerY + (float) (radius * sin(angle));
            float x2 = x - (float) (radius * cos(angle)) * 2;
            float y2 = y - (float) (radius * sin(angle)) * 2;
//            canvas.drawLine(x, y, x2, y2, linePaint);
            path.moveTo(x, y);
            path.lineTo(x2, y2);
        }
        canvas.drawPath(path, linePaint);
    }

    /**
     * 画蜘蛛网
     * <pre>
     *     1. 找到中心点
     *     2. 根据中心点找到六边形的顶点坐标(6个顶点)
     *     3. 利用 path.moveTo + lineTo
     * </pre>
     */
    private void drawSpider(Canvas canvas) {
        path.reset();
        path.rewind();
        int radius; // 每层的半径
        for (int i = 1; i <= count; i++) {
            radius = maxRadius * i / count;
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
        }
        canvas.drawPath(path, linePaint);
    }

    /**
     * <a href="https://blog.csdn.net/forDreamforYou/article/details/81299710">Android 三角函数</a>
     *
     * @param angle 0-360
     * @return [-1,1]
     */
    private double sin(double angle) {
        angle = angle * Math.PI / 180;
        return Math.sin(angle);
    }

    /**
     * <a href="https://blog.csdn.net/forDreamforYou/article/details/81299710">Android 三角函数</a>
     *
     * @param angle 0-360
     * @return [-1,1]
     */
    private double cos(double angle) {
        angle = angle * Math.PI / 180;
        return Math.cos(angle);
    }
}
