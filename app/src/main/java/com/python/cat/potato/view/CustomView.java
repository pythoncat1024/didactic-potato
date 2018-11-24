package com.python.cat.potato.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

    private Paint paint;
    private Path path;

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

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        path = new Path();
        RectF rectF = new RectF(100, 100, 700, 700);
        path.addRect(rectF,Path.Direction.CW);
        RectF oval = new RectF(400, 400, 1000, 1000);
//        path.addArc(oval,0,360); // 强制 CW，不能指定！
        path.addCircle(700,700,300,Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        //  [前提，方向相同]都是 CW,或都是 CCW
//        path.setFillType(Path.FillType.WINDING); // 理论上是并集
        path.setFillType(Path.FillType.EVEN_ODD); // XOR
        canvas.drawPath(path,paint);

    }
}
