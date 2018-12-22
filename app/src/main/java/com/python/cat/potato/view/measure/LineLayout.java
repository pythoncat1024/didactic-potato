package com.python.cat.potato.view.measure;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;

public class LineLayout extends ViewGroup {


    public LineLayout(Context context) {
        this(context, null);
    }

    public LineLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LineLayout(Context context, AttributeSet attrs,
                      int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtils.d("size w=" + w + " ,,, h=" + h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();
        int width = 0;
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            height += measuredHeight + lp.topMargin + lp.bottomMargin;
            width = measuredWidth + lp.leftMargin + lp.rightMargin;
            width = Math.min(width, ws);
        }
        int finalW = wm == MeasureSpec.EXACTLY ? ws : width + getPaddingLeft() + getPaddingRight();
        int finalH = hm == MeasureSpec.EXACTLY ? hs : height + getPaddingTop() + getPaddingBottom();
        com.apkfuns.logutils.LogUtils.i("measure: " + finalW + " , " + finalH);
        setMeasuredDimension(finalW, finalH);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        com.apkfuns.logutils.LogUtils.d(String.format("%s -- %s , %s , %s , %s", changed, l, t, r, b));
        t = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

//            LogUtils.d(lp);
            l = lp.leftMargin + getPaddingLeft();
            t = t + lp.topMargin;
            r = l + child.getMeasuredWidth();
            b = t + child.getMeasuredHeight();
            child.layout(l, t, r, b);
            t += child.getMeasuredHeight() + lp.bottomMargin;
//            break;
            com.apkfuns.logutils.LogUtils.d("item width:" + (lp.rightMargin + lp.leftMargin + child.getMeasuredWidth()));
        }
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
