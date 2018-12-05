package com.python.cat.potato.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
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
    private Paint rectPaint;
    private Paint midYPaint;

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
        rectPaint = new Paint();
        rectPaint.setColor(Color.MAGENTA);
        rectPaint.setStyle(Paint.Style.STROKE);

        midYPaint = new Paint();
        midYPaint.setColor(Color.CYAN);
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
        // 跟 baseLine 无关！


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

        //        ################################
        //        ################################ 下面才是真实场景
        //        ################################ 下面才是真实场景
        //        ################################
        int left = 100;
        int top = 500;
        int right = left + 837;
        int bottom = top + 200;
        canvas.drawRect(left, top, right, bottom, rectPaint);
        // : 在矩形中间绘制文字！！！
        int midY = top + (bottom - top) / 2;
        LogUtils.d("midY===" + midY);
        textPaint.setTextSize(100);
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int baseLineY = getBaseY(midY, fontMetricsInt);
        canvas.drawText("我在矩形中间了吗？", left, baseLineY, textPaint);
        canvas.drawLine(-1000, midY, 100 * 100, midY, midYPaint);

    }

    /**
     * <div style="color:blue">以指定的 y 坐标为中线，绘制文字 </div>
     * <p>计算基线y坐标的方式如下：</p>
     * <ul>
     * <b>a.基线与四线格对应转换公式:</b>
     * <li>int topY = baseY + fontMetrics.top;</li>
     * <li>int ascentY = baseY + fontMetrics.ascent;</li>
     * <li>int descentY = baseY + fontMetrics.descent;</li>
     * <li>int bottomY = baseY + fontMetrics.bottom;</li>
     * </ul>
     * <ul>
     * <b>b.文字所在区域高度计算公式：</b><br/>
     * <i>高度实际计算公式是：int deltaH = bottomY - topY;
     * <br/>由a(基线与四线格对应转换公式)可以将公式化简为：</i>
     * <li>int deltaH = fontMetricsInt.bottom - fontMetricsInt.top; // ΔH</li>
     * </ul>
     *
     * @param midY           指定的基准中线 y 坐标值
     * @param fontMetricsInt Paint.FontMetrics
     * @return baseY 对应的坐标值
     */
    private int getBaseY(int midY, @NonNull Paint.FontMetricsInt fontMetricsInt) {
        int deltaH = fontMetricsInt.bottom - fontMetricsInt.top; // ΔH
        int botY = midY + deltaH / 2;
        int topY = midY - deltaH / 2;
        // botY = baseY + bot;
//        int baseY = botY - fontMetricsInt.bottom; // ok 效果相同
        return topY - fontMetricsInt.top;
    }

    private void drawSpecialLine(Canvas canvas, Paint paint, int startX, int startY) {
        canvas.drawLine(startX, startY, startX <= 0 ? 100 : startX * 100, startY, paint);
    }
}
