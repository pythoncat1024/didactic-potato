package com.python.cat.potato.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class PathView extends View {

    private Path srcPath;
    private Path dstPath;
    private Paint paint;
    private int mCenterX;
    private int mCenterY;
    private PathMeasure pathMeasure;
    private float mCurrentValue;
    private float[] pos;
    private float[] tan;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs,
                    int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mCenterX = w / 2;
        this.mCenterY = h / 2;
        srcPath.addOval(-100, -100, 100, 100, Path.Direction.CW);
        pathMeasure.setPath(srcPath, false);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(animation -> {
            this.mCurrentValue = (Float) animation.getAnimatedValue();
        });
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        canvas.save();

        canvas.translate(mCenterX, mCenterY);
        dstPath.reset();
        dstPath.rewind();
        float stopD = pathMeasure.getLength() * mCurrentValue;
        pathMeasure.getSegment(0, stopD, dstPath, true);
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(dstPath, paint);
        canvas.restore();
    }

    private void init() {
        srcPath = new Path();
        dstPath = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        pathMeasure = new PathMeasure();
        pos = new float[2];
        tan = new float[2];
    }
}
