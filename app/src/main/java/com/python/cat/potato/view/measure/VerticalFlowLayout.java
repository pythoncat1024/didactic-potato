package com.python.cat.potato.view.measure;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * item 竖直排列，一列排满了，去第二列
 */
public class VerticalFlowLayout extends ViewGroup {
    private int maxH;

    public VerticalFlowLayout(Context context) {
        this(context, null);
    }

    public VerticalFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VerticalFlowLayout(Context context, AttributeSet attrs,
                              int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        this.maxH = hs;
        int widthUsed = 0;
        int heightUsed = 0;

        int width = 0;
        int height = 0;

        int columnWidth = 0;
        int columnHeight = 0;
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();

            int itemW = measuredWidth + lp.leftMargin + lp.rightMargin;
            int itemH = measuredHeight + lp.topMargin + lp.bottomMargin;
            if (columnHeight + itemH > hs - getPaddingBottom() - getPaddingTop()) {
                // 大于最大高度，换行
                width += columnWidth; // 记录上一列的数据
                height = Math.max(height, columnHeight); // 记录上一列的数据
                // 换列了
                columnWidth = itemW;
                columnHeight = itemH;
            } else {
                columnWidth = Math.max(columnWidth, itemW);
                columnHeight += itemH;
            }

            if (i == size - 1) {
                // 最后一个 view
                width += columnWidth; // 记录当前列的数据
                height = Math.max(height, columnHeight); // 记录当前列的数据
            }
        }

        width = width + getPaddingLeft() + getPaddingRight();
        height = height + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(wm == MeasureSpec.EXACTLY
                ? ws : width, hm == MeasureSpec.EXACTLY ? hs : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = 0;
        int height = 0;

        int columnWidth = 0;
        int columnHeight = 0;
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();

            int itemW = measuredWidth + lp.leftMargin + lp.rightMargin;
            int itemH = measuredHeight + lp.topMargin + lp.bottomMargin;
            if (columnHeight + itemH > maxH - getPaddingBottom() - getPaddingTop()) {
                // 大于最大高度，换行
                width += columnWidth; // 记录上一列的数据
                height = Math.max(height, columnHeight); // 记录上一列的数据
                // 换列了
                columnWidth = itemW;
                columnHeight = itemH;
            } else {
                columnWidth = Math.max(columnWidth, itemW);
                columnHeight += itemH;
            }

            if (i == size - 1) {
                // 最后一个 view
                width += columnWidth; // 记录当前列的数据
                height = Math.max(height, columnHeight); // 记录当前列的数据
            }

            l = width + lp.leftMargin + getPaddingLeft();
            if (i == size - 1) {
                l = l - columnWidth;
            }
            r = l + measuredWidth;
            t = columnHeight + lp.topMargin - itemH + getPaddingTop();
            b = t + measuredHeight;
            child.layout(l, t, r, b);
        }

    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
