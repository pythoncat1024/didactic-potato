package com.python.cat.potato.view.measure;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.utils.SizeUtils;

import java.util.Locale;

public class LineLayout extends ViewGroup {

    static {
        LogUtils.getLogConfig().configAllowLog(true);
    }

    // default values
    final int VERTICAL = LinearLayout.VERTICAL;
    final int HORIZONTAL = LinearLayout.HORIZONTAL;
    final float defDimension = SizeUtils.dp2px(0);
    final int defOrientation = LinearLayout.HORIZONTAL;
    final int defShowDividers = LinearLayout.SHOW_DIVIDER_NONE;
    final float defWeightSum = 0;
    final int defGravity = Gravity.NO_GRAVITY;

    // fields
    private int mGravity;
    private float mWeightSum;
    private int mShowDividers;
    private int mOrientation;
    private float mDividerPadding;
    private Drawable mDivider /*= new ColorDrawable(Color.TRANSPARENT)*/; // 默认给透明颜色值

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

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.LineLayout, defStyleAttr, defStyleRes);
        // int length = a.length();  // 获取定义的自定义属性的个数
        // int indexCount = a.getIndexCount(); // 获取使用的自定义属性的个数

        this.mDivider = a.getDrawable(R.styleable.LineLayout_android_divider);
        this.mDividerPadding = a.getDimension(R.styleable.LineLayout_android_dividerPadding, defDimension);
        this.mShowDividers = a.getInt(R.styleable.LineLayout_android_showDividers, defShowDividers);
        this.mOrientation = a.getInt(R.styleable.LineLayout_android_orientation, defOrientation);
        this.mWeightSum = a.getFloat(R.styleable.LineLayout_android_weightSum, defWeightSum);
        this.mGravity = a.getInt(R.styleable.LineLayout_android_gravity, defGravity);
        LogUtils.e(mDivider); // 如果设置为 R.color.black --> 就获取到一个 ColorDrawable
        LogUtils.e(String.format(Locale.CHINA,
                "padding=%s,orientation=%s,showDividers=%s,weightSum=%s,gravity=%s",
                mDividerPadding, mOrientation, mShowDividers, mWeightSum, mGravity));
        a.recycle();
        LogUtils.e("showDividers:begin=%s,middle=%s,end=%s", showBegin(), showMiddle(), showEnd());
        setWillNotDraw(!drawDivider());

    }

    private boolean drawDivider() {
        return mDividerPadding > 0 && (showBegin() || showMiddle() || showEnd());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtils.d("size w=" + w + " ,,, h=" + h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtils.i("## default measure: w=%s,h=%s",
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        if (mOrientation == HORIZONTAL) {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();
        int width = 0;
        int height = 0;
        int visibleCount = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.itemGone) {
                // 示例一下 LayoutParams attr 的使用方式
                continue;
            }
            visibleCount += 1;
            // 这一句非常重要，不调用就不会测量 child
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            int itemW = measuredWidth + lp.leftMargin + lp.rightMargin;
            int itemH = measuredHeight + lp.topMargin + lp.bottomMargin;
            LogUtils.v("ITEMS:mw=%s,mh=%s, ## itemW=%s,itemH=%s",
                    measuredWidth, measuredHeight, itemW, itemH);
            height = Math.max(height, itemH);
            width += itemW;
        }
        if (visibleCount > 0) {
            if (showBegin()) {
                width += mDividerPadding;
            }
            if (showMiddle()) {
                width += (visibleCount - 1) * mDividerPadding;
            }
            if (showEnd()) {
                width += mDividerPadding;
            }
        }
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        width = Math.min(ws, width);
        height = Math.min(hs, height);
        int finalW = MeasureSpec.EXACTLY == wm ? ws : width;
        int finalH = MeasureSpec.EXACTLY == hm ? hs : height;
        LogUtils.w("measureH: w=%s,h=%s", finalW, finalH);
        setMeasuredDimension(finalW, finalH);
    }

    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();
        int width = 0;
        int height = 0;
        int visibleCount = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.itemGone) {
                // 示例一下 LayoutParams attr 的使用方式
                continue;
            }
            visibleCount += 1;
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            int itemW = measuredWidth + lp.leftMargin + lp.rightMargin;
            int itemH = measuredHeight + lp.topMargin + lp.bottomMargin;

            LogUtils.v("ITEMS:mw=%s,mh=%s, ## itemW=%s,itemH=%s",
                    measuredWidth, measuredHeight, itemW, itemH);
            width = Math.max(width, itemW);
            height += itemH;
        }
        if (visibleCount > 0) {
            if (showBegin()) {
                height += mDividerPadding;
            }
            if (showMiddle()) {
                height += (visibleCount - 1) * mDividerPadding;
            }
            if (showEnd()) {
                height += mDividerPadding;
            }
        }
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        width = Math.min(ws, width);
        height = Math.min(hs, height);
        int finalW = MeasureSpec.EXACTLY == wm ? ws : width;
        int finalH = MeasureSpec.EXACTLY == hm ? hs : height;
        LogUtils.w("measureV: w=%s,h=%s", finalW, finalH);
        setMeasuredDimension(finalW, finalH);
    }


    private boolean showBegin() {
        return mDividerPadding > 0 &&
                (mShowDividers & LinearLayout.SHOW_DIVIDER_BEGINNING) != 0;
    }

    private boolean showMiddle() {
        return mDividerPadding > 0 &&
                (mShowDividers & LinearLayout.SHOW_DIVIDER_MIDDLE) != 0;
    }

    private boolean showEnd() {
        return mDividerPadding > 0 &&
                (mShowDividers & LinearLayout.SHOW_DIVIDER_END) != 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        com.apkfuns.logutils.LogUtils.d(String.format("%s -- %s , %s , %s , %s", changed, l, t, r, b));
        if (mOrientation == HORIZONTAL) {
            layoutHorizontal(changed, l, t, r, b);
        } else {
            layoutVertical(changed, l, t, r, b);
        }
    }

    private void layoutVertical(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        if (showBegin()) {
            t = (int) mDividerPadding;
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.itemGone) {
                // 示例一下 LayoutParams attr 的使用方式
                continue;
            }
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            l = getPaddingLeft() + lp.leftMargin;
            t += getPaddingTop() + lp.topMargin;
            r = l + measuredWidth;
            b = t + measuredHeight;
            child.layout(l, t, r, b);

            LogUtils.d("layout===%s,%s,%s,%s", l, t, r, b);
            t = b + lp.bottomMargin;

            LogUtils.d("t==" + t);
            if (showMiddle()) {
                t = (int) (t + mDividerPadding);
            }
            LogUtils.d("t==eee===" + t);
        }

    }

    private void layoutHorizontal(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        if (showBegin()) {
            l = (int) mDividerPadding;
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.itemGone) {
                // 示例一下 LayoutParams attr 的使用方式
                continue;
            }
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            l += getPaddingLeft() + lp.leftMargin;
            t = getPaddingTop() + lp.topMargin;
            r = l + measuredWidth;
            b = t + measuredHeight;
            child.layout(l, t, r, b);

            LogUtils.d("layout===%s,%s,%s,%s", l, t, r, b);
            l = r + lp.bottomMargin;
            LogUtils.d("l==" + t);
            if (showMiddle()) {
                l = (int) (l + mDividerPadding);
            }
            LogUtils.d("l==eee===" + t);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        LogUtils.e("onDraw ####");
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LineLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        if (mOrientation == HORIZONTAL) {
            return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        } else if (mOrientation == VERTICAL) {
            return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        throw new RuntimeException("i  need a legal Orientation");
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof LineLayout.LayoutParams) {
            return new LayoutParams((LayoutParams) lp);
        }
        return new MarginLayoutParams(lp);
    }

    class LayoutParams extends ViewGroup.MarginLayoutParams {
        final float defLayoutWeight = 0;
        final int defLayoutGravity = Gravity.NO_GRAVITY;
        private boolean itemGone = Boolean.FALSE;
        private int layoutGravity = defLayoutGravity;
        private float layoutWeight = defLayoutWeight;

        LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            // 从 xml 加载的时候，一般都是走这个构造方法
            LogUtils.d("use this constructor ###1 ");
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.LineLayout_Layout);

            this.layoutWeight = a.getFloat(R.styleable.LineLayout_Layout_android_layout_weight, defLayoutWeight);
            this.layoutGravity = a.getInt(R.styleable.LineLayout_android_layout_gravity, defLayoutGravity);
            this.itemGone = a.getBoolean(R.styleable.LineLayout_Layout_layout_itemGone, Boolean.FALSE);
            LogUtils.e(String.format(Locale.CHINA, "layoutWeight=%s,layoutGravity=%s,itemGone=%s",
                    layoutWeight, layoutGravity, itemGone));
            a.recycle();
        }

        LayoutParams(int width, int height) {
            super(width, height);
            LogUtils.d("use this constructor ###2 ");
        }

        LayoutParams(LineLayout.LayoutParams source) {
            super(source);
            this.layoutGravity = source.layoutGravity;
            this.layoutWeight = source.layoutWeight;
            this.itemGone = source.itemGone;
            LogUtils.d("use this constructor ###3 ");
        }
    }
}
