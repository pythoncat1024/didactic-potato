package com.python.cat.potato.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.apkfuns.logutils.LogUtils;

/**
 * 叫 windowsView 并没有扫码具体含义，只是表示这个类是在 Windows 环境下创建的
 */
public class WindowsView extends View {

    private Paint paint;
    private float animatedValue;
    private float animatedValue2;
    private int mW;
    private int mH;
    private Path srcPath;
    private Path srcPath2;
    private Path dstPath;
    private Path dstPath2;
    private PathMeasure pathMeasure;
    private PathMeasure pathMeasure2;

    public WindowsView(Context context) {
        this(context, null);
    }

    public WindowsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WindowsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mW = w;
        this.mH = h;
        srcPath.addCircle(0, 0, 100, Path.Direction.CCW);

        srcPath2.moveTo(-50, 0);
        srcPath2.lineTo(0, 50);
        srcPath2.lineTo(50, -50);

        pathMeasure.setPath(srcPath, false);
        pathMeasure2.setPath(srcPath2, false);
        ValueAnimator va = ValueAnimator.ofFloat(0, 1);

        va.addUpdateListener(animation -> {
            animatedValue = (Float) animation.getAnimatedValue();
            //canvas.drawPoint(0, animatedValue, paint);

            LogUtils.d("animatedValue===" + animatedValue);
        });
        va.setDuration(2000);
        va.start();
        ValueAnimator va2 = ValueAnimator.ofFloat(0, 1);
        va2.addUpdateListener(animation -> {
            animatedValue2 = (float) animation.getAnimatedValue();
            LogUtils.i("animatedValue2===" + animatedValue2);
        });
        va2.setDuration(2000);
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                va2.start();
            }
        });
    }

    private void init() {
        pathMeasure = new PathMeasure();
        pathMeasure2 = new PathMeasure();
        srcPath = new Path();
        srcPath2 = new Path();
        dstPath = new Path();
        dstPath2 = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null); // todo: 禁用硬件加速
        canvas.translate(mW / 2, mH / 2);
        dstPath.rewind();
        boolean segment =
            pathMeasure.getSegment(0, animatedValue * pathMeasure.getLength(), dstPath, true);
        if (segment) {
            canvas.drawPath(dstPath, paint);
        }
        dstPath2.rewind();
        boolean ss =
            pathMeasure2.getSegment(0, animatedValue2 * pathMeasure2.getLength(), dstPath2, true);
        if (ss) {
            canvas.drawPath(dstPath2, paint);
        }
    }
}
