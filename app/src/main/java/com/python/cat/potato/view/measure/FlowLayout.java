package com.python.cat.potato.view.measure;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private List<View> lineViews = new ArrayList<>();

    private List<int[]> childPosition = new ArrayList<>();

    private int maxWidth;
    private int maxHeight;

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
        LogUtils.d("size w=" + w + " ,,, h=" + h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        this.maxWidth = ws;
        this.maxHeight = ws;
        LogUtils.i("default wh: " + ws + " , " + hs);
        int count = getChildCount();
        int lines = 0;
        int width = 0;
        int height = 0;
        int preLineWidth = 0; // 预先的 行宽
        int preLineHeight = 0; // 预先的 行宽
        int sizeOfViewsInLine = 0;
        boolean endLine; // 结束当前行：此时 进行 height += lineHeight
        boolean needNext;
        for (int i = 0; i < count; i++) {
            if (i == 3) {
                LogUtils.w("333333");
            }
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }
            sizeOfViewsInLine += 1;

            // 测量 child 宽高 --- start
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            // 测量 child 宽高 --- end
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int itemW = measuredWidth + lp.leftMargin + lp.rightMargin;
            int itemH = measuredHeight + lp.topMargin + lp.bottomMargin;

            preLineWidth += itemW;
            needNext = sizeOfViewsInLine > 1 && preLineWidth + getPaddingRight() + getPaddingLeft() > ws;
            if (needNext) {
                endLine = true;
                lines += 1;
                sizeOfViewsInLine = 1;
                preLineWidth = itemW;
                preLineHeight = itemH;
            } else {
                endLine = false;
                preLineHeight = Math.max(preLineHeight, itemH);
            }
            if (i == count - 1) {
                endLine = true;
            }
            width = Math.max(preLineWidth, width);
            if (endLine) {
                // 这时候 lineWidth 等于 新行的 第一个 child 的 宽度
                height += preLineHeight;
                LogUtils.wtf("well=== %s : %s %s", width, preLineWidth, lines);
            }
            LogUtils.wtf("child=%s , line=%s, lw=%s , h=%s ,w=%s", i, lines, preLineWidth, height, width);
        }

        int finalW = wm == MeasureSpec.EXACTLY ? ws : width + getPaddingLeft() + getPaddingRight();
        int finalH = hm == MeasureSpec.EXACTLY ? hs : height + getPaddingTop() + getPaddingBottom();
        com.apkfuns.logutils.LogUtils.i("measure: " + finalW + " , " + finalH);
//        setMeasuredDimension(ws, hs); // 设置成 fill_parent 了
        setMeasuredDimension(finalW, finalH);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
/*
l = width - itemW + lp.leftMargin;
t = height - itemH + lp.topMargin;
r = l + child.getMeasuredWidth();
b = t + child.getMeasuredHeight();
child.layout(l, t, r, b);
 */


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
