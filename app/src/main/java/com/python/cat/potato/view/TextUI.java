package com.python.cat.potato.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

public class TextUI extends View {

    private Paint textPaint;
    private Paint ascentPaint;
    private Paint descentPaint;
    private Paint topPaint;
    private Paint bottomPaint;
    private Paint basePaint;

    public TextUI(Context context) {
        this(context, null);
    }

    public TextUI(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TextUI(Context context, @Nullable AttributeSet attrs,
                  int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(200); // px
        basePaint = new Paint();
        basePaint.setColor(Color.GRAY);
        ascentPaint = new Paint();
        ascentPaint.setColor(Color.RED);
        descentPaint = new Paint();
        descentPaint.setColor(Color.BLUE);
        topPaint = new Paint();
        topPaint.setColor(Color.YELLOW);
        bottomPaint = new Paint();
        bottomPaint.setColor(Color.GREEN);

        textPaint.setStrokeWidth(4);
        basePaint.setStrokeWidth(4);
        ascentPaint.setStrokeWidth(4);
        descentPaint.setStrokeWidth(4);
        topPaint.setStrokeWidth(4);
        bottomPaint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int baseX = 100;
        int baseY = 300;
//        LogUtils.w("before:==" + textPaint.getFontMetricsInt());
        canvas.drawText("FfGgIiJj", baseX, baseY, textPaint);
//        LogUtils.w("after:===" + textPaint.getFontMetricsInt());

        // before 和 after 相同。说明这个值只跟 textPaint.setTextSize(size) 有关。

        drawSpecialLine(canvas, basePaint, baseX, baseY); // base line
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();


        int topX = baseX;
        int topY = baseY + fontMetrics.top;
        drawSpecialLine(canvas, topPaint, topX, topY); // top line

        int ascentX = baseX;
        int ascentY = baseY + fontMetrics.ascent;
        drawSpecialLine(canvas, ascentPaint, ascentX, ascentY); // ascent line

        int descentX = baseX;
        int descentY = baseY + fontMetrics.descent;
        drawSpecialLine(canvas, descentPaint, descentX, descentY); // descent line

        int bottomX = baseX;
        int bottomY = baseY + fontMetrics.bottom;
        drawSpecialLine(canvas, bottomPaint, bottomX, bottomY); // bottom line

    }


    private void drawSpecialLine(Canvas canvas, Paint paint, int startX, int startY) {
        canvas.drawLine(startX, startY, startX <= 0 ? 100 : startX * 100, startY, paint);
    }
}
