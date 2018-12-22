package com.python.cat.potato.view.measure;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

    private int maxWidth;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        LogUtils.d("size w=" + w + " ,,, h=" + h);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        this.maxWidth = ws;
        //        LogUtils.i("default wh: " + ws + " , " + hs);
        int count = getChildCount();
        int width = 0;
        int height = 0;
        int lineWidth = 0; // 预先的 行宽
        int lineHeight = 0; // 预先的 行宽
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }

            // 测量 child 宽高 --- start
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            // 测量 child 宽高 --- end
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int itemW = measuredWidth + lp.leftMargin + lp.rightMargin;
            int itemH = measuredHeight + lp.topMargin + lp.bottomMargin;

            if (lineWidth + itemW > ws - getPaddingLeft() - getPaddingRight()) {
                // 换行 ,先记录换行之前的数据
                width = Math.max(width, lineWidth); // 记录的是上一行的数据
                height += lineHeight; // 记录的是上一行的数据

                // 换行了
                lineHeight = itemH;
                lineWidth = itemW;
            } else {
                // 不换行
                lineHeight = Math.max(lineHeight, itemH);
                lineWidth += itemW;
            }

            if (i == count - 1) {
                // 最后一个 view ， 记录这一行的数据
                width = Math.max(width, lineWidth); // 记录的是这一行的数据
                height += lineHeight; // 记录这一行的数据

            }

        }

        int finalW = wm == MeasureSpec.EXACTLY ? ws : width + getPaddingLeft() + getPaddingRight();
        int finalH = hm == MeasureSpec.EXACTLY ? hs : height + getPaddingTop() + getPaddingBottom();
//        setMeasuredDimension(ws, hs); // 设置成 fill_parent 了
        setMeasuredDimension(finalW, finalH);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();
        int width = 0;
        int height = 0;
        int lineWidth = 0; // 预先的 行宽
        int lineHeight = 0; // 预先的 行宽
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }

            // 测量 child 宽高 --- start
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            // 测量 child 宽高 --- end
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int itemW = measuredWidth + lp.leftMargin + lp.rightMargin;
            int itemH = measuredHeight + lp.topMargin + lp.bottomMargin;

            if (lineWidth + itemW > maxWidth - getPaddingLeft() - getPaddingRight()) {
                // 换行 ,先记录换行之前的数据
                width = Math.max(width, lineWidth); // 记录的是上一行的数据
                height += lineHeight; // 记录的是上一行的数据

                // 换行了
                lineHeight = itemH;
                lineWidth = itemW;
            } else {
                // 不换行
                lineHeight = Math.max(lineHeight, itemH);
                lineWidth += itemW;
            }

            if (i == count - 1) {
                // 最后一个 view ， 记录这一行的数据
                width = Math.max(width, lineWidth); // 记录的是这一行的数据
                height += lineHeight; // 记录这一行的数据

            }

            l = lineWidth - itemW + lp.leftMargin + getPaddingLeft();
            r = l + measuredWidth;
            t = height + lp.topMargin + getPaddingTop();
            if (i == count - 1) {
                t = height - lineHeight + lp.topMargin;
            }
            b = t + measuredHeight;
            child.layout(l, t, r, b);
        }
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
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
