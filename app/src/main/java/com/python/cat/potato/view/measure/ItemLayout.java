package com.python.cat.potato.view.measure;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.apkfuns.logutils.LogUtils;

/**
 * support scroller layout
 */
public class ItemLayout extends LinearLayout {

    public static final String TAG = "ItemLayout#scroll";

    private int downX, downY;
    private ViewConfiguration viewConfig;

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
        viewConfig = ViewConfiguration.get(getContext());
        // LogUtils.e(viewConfig);
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
        // 注意这个方法，无论返回值是什么，move up 都是会走的
        boolean b = super.onInterceptHoverEvent(event);
        LogUtils.d("intercept-- " + b);
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN: {
                // 收到点击事件，未必拦截了
                LogUtils.d("down");
                downX = Math.round(event.getX());
                downY = Math.round(event.getY());
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                // 收到滑动事件，未必拦截了
                LogUtils.d("move");
                // 01 获取diff
                int moveX = Math.round(event.getX());
                int moveY = Math.round(event.getY());
                int[] diff = getTouchDiff(downX, downY, moveX, moveY);
                int diffX = diff[0];
                int diffY = diff[1];
                int touchSlop = viewConfig.getScaledTouchSlop();
                if (Math.abs(diffX) > touchSlop) {
                    // 一定要取绝对值，否则，向左滑动永远不满足条件
                    LogUtils.w("需要处理滑动了：" + diffX);
                    return true;
                }
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
                // 01-1 滑动边界值检查

                boolean canScroll = checkScrollEdge(diffX, diffY);
                // 02 处理逻辑 （包含滑动边界检测）
                if (canScroll) {
                    handlerLayoutByDiff(diffX, diffY);
                }
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

    @Override
    public boolean performClick() {
        boolean b = super.performClick();
        LogUtils.d("perform click --- " + b);
        return b;
    }


    private boolean checkScrollEdge(int diffX, int diffY) {
        if (Math.abs(diffX) < viewConfig.getScaledTouchSlop()) {
            // 滑动距离过小，就不滑动
            Log.v("scroll", "too small deltaX to scroll! ## " + diffX + " , " + diffY);
            return false;
        }
        boolean canScroll;
        // 如果当前 view 停留在初始值左边了， scrollX > 0 ;
        // 如果 view 停留在初始位置右边了， scrollX < 0 ;
        int scrollX = getScrollX();
        // scrollX --> 表示当前位置(滑动后)，比初始位置 的 x 的距离
        int menuWidths = 0;
        for (int i = 1; i < getChildCount(); i++) {
            View temp = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) temp.getLayoutParams();
            int width = temp.getWidth();
            menuWidths += width + lp.leftMargin + lp.rightMargin;
        }
        if (scrollX - diffX < 0) {
            // 不能向左滑动了，直接移动到初始位置
            // scrollTo(-scrollX, 0); // 调用的话，画面抖动频繁
            Log.v(TAG, "too left , can not scroll");
            canScroll = false;
        } else if (scrollX - diffX > menuWidths) {
            // 不能再向右滑动了
            // scrollTo(-scrollX, 0); // 调用的话，画面抖动频繁
            Log.v(TAG, "too right , can not scroll");
            canScroll = false;
        } else {
            canScroll = true;
        }
        return canScroll;
    }

    private void handlerLayoutByDiff(int diffX, int diffY) {

        // moveChild / scrollBy 都可以 ！

        scrollBy(-diffX, 0);

//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            if (child == null || child.getVisibility() == GONE) {
//                continue;
//            }
//            moveChild(child, diffX, 0);
//
//        }
    }


    private int[] getTouchDiff(int startX, int startY, int endX, int endY) {
        return new int[]{endX - startX, endY - startY};
    }

    private void moveChild(View child, int diffX, int diffY) {
        //  diffY = 0; // 永远不处理上下滑动
        int l = child.getLeft() + diffX;
        int t = child.getTop() + diffY;
        int r = child.getRight() + diffX;
        int b = child.getBottom() + diffY;
        child.layout(l, t, r, b);
    }
}
