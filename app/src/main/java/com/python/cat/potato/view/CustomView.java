package com.python.cat.potato.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

public class CustomView extends View {


    private Region centerRegion;
    private Region leftTopRegion;
    private Region leftBottomRegion;
    private Region rightTopRegion;
    private Region rightBottomRegion;
    private Rect tempRect;
    private int mW;
    private int mH;
    private int centerX;
    private int centerY;
    private Rect outer;
    private RectF ovalOuter;
    private RectF ovalInner;
    private Path path;
    private Region clip;
    private int mX;
    private int mY;
    private Paint centerPaint;
    private Paint leftTopPaint;
    private Paint leftBottomPaint;
    private Paint rightTopPaint;
    private Paint rightBottomPaint;

    private int accountColor = Color.RED;
    private OnAreaClickListener mAreaListener;

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
        this.mW = w;
        this.mH = h;
        this.centerX = mW / 2;
        this.centerY = mH / 2;
        setOuter();
        setOvalOuter();
        setOvalInner();

        setCenterRegion();

        setLeftTopRegion();
        setRightTopRegion();
        setLeftBottomRegion();
        setRightBottomRegion();
    }

    private void setRightBottomRegion() {
        path.reset();
        path.rewind();
        RectF rectF = new RectF();
        float left = centerX + mW / 2 * 0.1f;
        float top = centerY + mH / 2 * 0.1f;
        float right = centerX + mW / 2;
        float bottom = centerY + mH / 2;
        rectF.set(left, top, right, bottom);

        path.addRect(rectF, Path.Direction.CW);

        Path another = new Path();
        another.addOval(ovalOuter, Path.Direction.CW);
        path.op(another, Path.Op.DIFFERENCE);
        clip.set(outer);
        rightBottomRegion.setPath(path, clip);

        LogUtils.d(rectF);
    }

    private void setLeftBottomRegion() {
        path.reset();
        path.rewind();
        RectF rectF = new RectF();
        float left = outer.left;
        float top = centerY + mH / 2 * 0.1f;
        float right = centerX - mW / 2 * 0.1f;
        float bottom = centerY + mH / 2;
        rectF.set(left, top, right, bottom);

        path.addRect(rectF, Path.Direction.CW);

        Path another = new Path();
        another.addOval(ovalOuter, Path.Direction.CW);
        path.op(another, Path.Op.DIFFERENCE);
        clip.set(outer);
        leftBottomRegion.setPath(path, clip);

        LogUtils.d(rectF);
    }

    private void setLeftTopRegion() {
        path.reset();
        path.rewind();

        RectF rectF = new RectF();
        float left = outer.left;
        float top = outer.top;
        float right = centerX - mW / 2 * 0.1f;
        float bottom = centerY - mH / 2 * 0.1f;
        rectF.set(left, top, right, bottom);

        path.addRect(rectF, Path.Direction.CW);

        Path another = new Path();
        another.addOval(ovalOuter, Path.Direction.CW);
        path.op(another, Path.Op.DIFFERENCE);
        clip.set(outer);
        leftTopRegion.setPath(path, clip);
    }

    private void setRightTopRegion() {
        path.reset();
        path.rewind();

        RectF rectF = new RectF();
        float left = centerX + mW / 2 * 0.1f;
        float top = outer.top;
        float right = centerX + mW / 2;
        float bottom = centerY - mH / 2 * 0.1f;
        rectF.set(left, top, right, bottom);

        path.addRect(rectF, Path.Direction.CW);
        Path another = new Path();
        another.addOval(ovalOuter, Path.Direction.CW);
        path.op(another, Path.Op.DIFFERENCE);
        clip.set(outer);
        rightTopRegion.setPath(path, clip);
    }

    private void setCenterRegion() {
        path.reset();
        path.rewind();
        path.addOval(ovalInner, Path.Direction.CW);
        clip.set(outer);
        centerRegion.setPath(path, clip);
    }


    private void setOvalInner() {
        float left = centerX - mW / 2 * 0.7f;
        float right = centerX + mW / 2 * 0.7f;
        float top = centerY - mH / 2 * 0.7f;
        float bottom = centerY + mH / 2 * 0.7f;
        ovalInner.set(left, top, right, bottom);
        LogUtils.v(ovalOuter);
    }

    private void setOvalOuter() {
        float left = centerX - mW / 2 * 0.8f;
        float right = centerX + mW / 2 * 0.8f;
        float top = centerY - mH / 2 * 0.8f;
        float bottom = centerY + mH / 2 * 0.8f;
        ovalOuter.set(left, top, right, bottom);
        LogUtils.v(ovalOuter);
    }

    private void setOuter() {
        int left = (int) (centerX - mW / 2 * 0.9f);
        int right = (int) (centerX + mW / 2 * 0.9f);
        int top = (int) (centerY - mH / 2 * 0.9f);
        int bottom = (int) (centerY + mH / 2 * 0.9f);
        outer.set(left, top, right, bottom);
        LogUtils.v(outer);
    }

    private void init() {
        initPaint();

        path = new Path();

        tempRect = new Rect();
        clip = new Region();

        centerRegion = new Region();
        leftTopRegion = new Region();
        leftBottomRegion = new Region();
        rightTopRegion = new Region();
        rightBottomRegion = new Region();
        outer = new Rect();
        ovalOuter = new RectF();
        ovalInner = new RectF();
    }

    private void initPaint() {
        centerPaint = new Paint();
        leftTopPaint = new Paint();
        leftBottomPaint = new Paint();
        rightTopPaint = new Paint();
        rightBottomPaint = new Paint();
    }

    private void updatePaint(Area area) {
        switch (area) {
            case NONE:
                centerPaint.setColor(Color.BLACK);
                leftBottomPaint.setColor(Color.BLACK);
                leftTopPaint.setColor(Color.BLACK);
                rightTopPaint.setColor(Color.BLACK);
                rightBottomPaint.setColor(Color.BLACK);
                break;
            case LEFT_TOP:
                centerPaint.setColor(Color.BLACK);
                leftBottomPaint.setColor(Color.BLACK);
                leftTopPaint.setColor(accountColor);
                rightTopPaint.setColor(Color.BLACK);
                rightBottomPaint.setColor(Color.BLACK);
                break;
            case LEFT_BOTTOM:
                centerPaint.setColor(Color.BLACK);
                leftBottomPaint.setColor(accountColor);
                leftTopPaint.setColor(Color.BLACK);
                rightTopPaint.setColor(Color.BLACK);
                rightBottomPaint.setColor(Color.BLACK);
                break;
            case RIGHT_TOP:
                centerPaint.setColor(Color.BLACK);
                leftBottomPaint.setColor(Color.BLACK);
                leftTopPaint.setColor(Color.BLACK);
                rightTopPaint.setColor(accountColor);
                rightBottomPaint.setColor(Color.BLACK);
                break;
            case RIGHT_BOTTOM:
                centerPaint.setColor(Color.BLACK);
                leftBottomPaint.setColor(Color.BLACK);
                leftTopPaint.setColor(Color.BLACK);
                rightTopPaint.setColor(Color.BLACK);
                rightBottomPaint.setColor(accountColor);
                break;
            case CENTER_OVAL:
                centerPaint.setColor(accountColor);
                leftBottomPaint.setColor(Color.BLACK);
                leftTopPaint.setColor(Color.BLACK);
                rightTopPaint.setColor(Color.BLACK);
                rightBottomPaint.setColor(Color.BLACK);
                break;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRegion(canvas, centerRegion, tempRect, centerPaint);
        drawRegion(canvas, leftTopRegion, tempRect, leftTopPaint);
        drawRegion(canvas, leftBottomRegion, tempRect, leftBottomPaint);
        drawRegion(canvas, rightTopRegion, tempRect, rightTopPaint);
        drawRegion(canvas, rightBottomRegion, tempRect, rightBottomPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mX = (int) event.getX();
                mY = (int) event.getY();
                Area area = witchArea(mX, mY);
                updatePaint(area);
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                mX = -1;
                mY = -1;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    public boolean performClick() {
        Area area = witchArea(mX, mY);
        if (mAreaListener != null) {
            mAreaListener.click(this, area);
        }
//        return super.performClick();
        return false; // 让 view.onClick 失效
    }

    private void drawRegion(Canvas canvas, Region region, Rect temp, Paint paint) {
        RegionIterator iterator = new RegionIterator(region);
        while (iterator.next(temp)) {
            canvas.drawRect(temp, paint);
        }
    }

    public enum Area {
        NONE("none"),
        LEFT_TOP("left_top"),
        LEFT_BOTTOM("left_bottom"),
        RIGHT_TOP("right_top"),
        RIGHT_BOTTOM("right_bottom"),
        CENTER_OVAL("center_oval");
        private final String value;

        Area(String none) {
            this.value = none;
        }
    }

    private Area witchArea(int x, int y) {
        if (centerRegion.contains(x, y)) {
            return Area.CENTER_OVAL;
        } else if (leftTopRegion.contains(x, y)) {
            return Area.LEFT_TOP;
        } else if (leftBottomRegion.contains(x, y)) {
            return Area.LEFT_BOTTOM;
        } else if (rightTopRegion.contains(x, y)) {
            return Area.RIGHT_TOP;
        } else if (rightBottomRegion.contains(x, y)) {
            return Area.RIGHT_BOTTOM;
        } else {
            return Area.NONE;
        }
    }


    public interface OnAreaClickListener {
        void click(View v, Area area);
    }

    public void setOnAreaClickListener(OnAreaClickListener listener) {
        this.mAreaListener = listener;
    }
}
