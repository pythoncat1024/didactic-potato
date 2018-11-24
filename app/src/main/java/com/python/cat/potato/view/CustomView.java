package com.python.cat.potato.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

    private int mX;
    private int mY;
    private Paint paint;
    private Rect rect;
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
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        rect = new Rect();
        rect.set(100, 100, 400, 400);
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rect.contains(mX, mY)) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.BLUE);
        }
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(49, 49);
        // forceMoveTo: 强制不连接之前的路径
        path.arcTo(new RectF(rect), 45, 90, true);

        path.addArc(500, 500, 600, 600, -90, -180);
        canvas.drawPath(path, paint);

        RectF oval = new RectF(100, 700, 700, 1000);
        path.reset();
        path.addOval(oval, Path.Direction.CW);
        String text = "我是比亚迪车主，你是比亚迪";
        canvas.drawTextOnPath(text,path,0,0,paint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = (int) event.getX();
                mY = (int) event.getY();
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                mX = (int) event.getX();
                mY = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                performClick();
            case MotionEvent.ACTION_CANCEL:
                mX = -1;
                mY = -1;
                postInvalidate();
                break;

        }
        return false;
    }
}
