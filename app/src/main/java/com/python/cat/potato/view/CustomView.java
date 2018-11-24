package com.python.cat.potato.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {


    private Paint textPaint;
    private String text;
    private Paint linePaint;
    private Path path;
    private int mH;
    private int mW;

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
        this.mH = h;
        this.mW = w;
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setAlpha(100);
        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true); // 抗锯齿
        textPaint.setTextSize(80);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(2);
        text = "明明如月，何时可掇？";

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setFakeBoldText(true); // 字体加粗
        textPaint.setUnderlineText(true); // 在 paint.drawTextOnPath 时无效
        textPaint.setStrikeThruText(true); // 在 paint.drawTextOnPath 时无效
        textPaint.setTextSkewX(-0.25f); // 错切
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 先画线
//        canvas.drawLine(100, 0, 100, 10000, linePaint); // y
//        canvas.drawLine(0, 100, 10000, 100, linePaint); // x

        path.moveTo(500, mH);
        path.lineTo(500, 100);
        canvas.drawPath(path, linePaint);
        canvas.drawTextOnPath(text, path, 100, 100, textPaint);
        canvas.drawText(text, 200, 200, textPaint);
    }
}
