package com.python.cat.potato.view.measure;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;

/**
 * support scroller layout
 */
public class ItemLayout extends LinearLayout {


    private int downX, downY;

    public ItemLayout(Context context) {
        this(context, null);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ItemLayout(Context context, AttributeSet attrs,
                      int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LogUtils.getLogConfig().configTagPrefix("ItemLayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtils.w("size== " + w + " , " + h);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean b = super.dispatchTouchEvent(ev);
        LogUtils.d("dispatch-- " + b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean b = super.onInterceptHoverEvent(event);
        LogUtils.d("intercept-- " + b);
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN: {
                // 收到点击事件，未必拦截了
                LogUtils.d("down");
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                // 收到滑动事件，未必拦截了
                LogUtils.d("move");
            }
            break;
            case MotionEvent.ACTION_UP: {
                // 收到抬起事件，未必拦截了
                LogUtils.d("up");
            }
            break;
            case MotionEvent.ACTION_CANCEL: {
                // 被 parent 拦截了，在自己事件处理还没结束的时候
                LogUtils.d("cancel");
            }
            break;
        }
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            performClick();
        }
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN: {
                // 收到点击事件，如果要处理，就必须返回 true
                LogUtils.i("down # down");
                downX = Math.round(event.getX());
                downY = Math.round(event.getY());
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                // 收到滑动事件，如果 down 的时候返回了 true
                LogUtils.i("move # move");
                // 3步走 ，1 获取 diff;2 执行逻辑 3 重置 down

                // 01 获取diff
                int moveX = Math.round(event.getX());
                int moveY = Math.round(event.getY());
                int[] diff = getTouchDiff(downX, downY, moveX, moveY);
                int diffX = diff[0];
                int diffY = diff[1];
                // 02 处理逻辑
                handlerLayoutByDiff(diffX, diffY);

                // 03 end. 处理逻辑之后，重置 down
                downX = moveX;
                downY = moveY;
            }
            break;
            case MotionEvent.ACTION_UP: {
                // 收到抬起事件，如果 down 的时候返回了 true
                LogUtils.i("up # up");
            }
            break;
            case MotionEvent.ACTION_CANCEL: {
                // 收到 parent 拦截事件，如果 down 的时候返回了 true
                LogUtils.i("cancel # cancel");
            }
            break;
        }
        boolean b = super.onTouchEvent(event);
        LogUtils.d("touch-- " + b);
        return true;
    }

    private void handlerLayoutByDiff(int diffX, int diffY) {
        // moveChild 获取 scrollBy 都可以 ！
        //  scrollBy(-diffX, 0);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }
            moveChild(child, diffX, diffY);
        }

    }

    @Override
    public boolean performClick() {
        boolean b = super.performClick();
        LogUtils.d("perform click --- " + b);
        return b;
    }


    private int[] getTouchDiff(int startX, int startY, int endX, int endY) {
        return new int[]{endX - startX, endY - startY};
    }

    private void moveChild(View child, int diffX, int diffY) {
        // layout(
        // getLeft() + diffX,
        // getTop() + diffY,
        // getRight() + diffX,
        // getBottom() + diffY
        // );
        int l = child.getLeft() + diffX;
        int t = child.getTop() + 0;
        int r = child.getRight() + diffX;
        int b = child.getBottom() + 0;
        child.layout(l, t, r, b);
    }
}
