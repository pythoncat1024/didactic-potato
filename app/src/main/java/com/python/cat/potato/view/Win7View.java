package com.python.cat.potato.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
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
import com.apkfuns.logutils.LogUtils;

public class Win7View extends View {
    private int mW;
    private int mH;
    private PathMeasure pathMeasure;
    private Path srcPath;

    private float maxValue = 1;
    private Path dstPath;
    private Paint paint;
    private float va1AnimatedValue;
    private float va2AnimateValue;
    private boolean va1End;

    public Win7View(Context context) {
        this(context, null);
    }

    public Win7View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Win7View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Win7View(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        pathMeasure = new PathMeasure();
        srcPath = new Path();
        dstPath = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        srcPath.addCircle(0, 0, 100, Path.Direction.CCW);
        srcPath.moveTo(-50, 0);
        srcPath.lineTo(0, 50);
        srcPath.lineTo(50, -50);
        pathMeasure.setPath(srcPath, false);
        maxValue = 1; // todo:
        AnimatorSet set = new AnimatorSet();
        ValueAnimator va1 = ValueAnimator.ofFloat(0, maxValue);
        va1.setDuration(2000);
        va1.addUpdateListener(animation -> {
            va1AnimatedValue = (Float) animation.getAnimatedValue();
            LogUtils.d("val===" + va1AnimatedValue);
        });
        va1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                va1End = true;
            }
        });
        ValueAnimator va2 = ValueAnimator.ofFloat(0, maxValue);
        va2.addUpdateListener(animation -> {
            va2AnimateValue = (Float) animation.getAnimatedValue();
            LogUtils.i("va2===" + va2AnimateValue);
        });
        va2.setDuration(2000);
        set.playSequentially(va1, va2);
        set.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mW = w;
        this.mH = h;
    }

    boolean haDNext = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.save();
        canvas.drawColor(Color.argb(0x66, 0x99, 0x22, 0x11));
        canvas.translate(mW / 2, mH / 2);
        if (!va1End) {
            pathMeasure.getSegment(0, va1AnimatedValue * pathMeasure.getLength(), dstPath, true);
            canvas.drawPath(dstPath, paint);
        } else {
            //todo: 这一步也很重要， onDraw 会被调用多次，如果不做判断，会一直 next ，导致 src 没有正确赋给 dst
            if (!haDNext) {
                boolean nextContour = pathMeasure.nextContour();
                LogUtils.w("nextContour=======" + nextContour);
                haDNext = true;
            }
            //dstPath.rewind()  // todo: 不能要！！！！;这样 就会保留之前的路径了
            //dstPath.reset();
            pathMeasure.getSegment(0, va2AnimateValue * pathMeasure.getLength(), dstPath, true);
            canvas.drawPath(dstPath, paint);
        }

        //canvas.drawPath(srcPath, paint);
        canvas.restore();
    }
}
