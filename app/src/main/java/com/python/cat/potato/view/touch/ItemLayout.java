package com.python.cat.potato.view.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * support scroller layout
 */
public class ItemLayout extends LinearLayout {


    public static final int MENU_CLOSE = 0;
    public static final int MENU_OPEN = 1;
    public static final int MENU_SCROLLING = 2;
    public static final int MENU_MOVING = 3;
    private ValueAnimator openMenuAnimator;
    private ValueAnimator closeMenuAnimator;

    @IntDef(value = {MENU_CLOSE, MENU_OPEN, MENU_SCROLLING, MENU_MOVING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MenuState {
    }

    @MenuState
    private int mCurrentMenuState = MENU_CLOSE;

    public void setCurrentMenuState(int mCurrentMenuState) {
        this.mCurrentMenuState = mCurrentMenuState;
    }

    public int getCurrentMenuState() {
        return mCurrentMenuState;
    }

    public static final String TAG = "ItemLayout#scroll";

    private int downX, downY;
    private ViewConfiguration viewConfig;
    private VelocityTracker vTracker; // 速度追踪器

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
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean b = super.dispatchTouchEvent(event);
        LogUtils.d("dispatch-- " + b);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.d("dispatch down");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.d("dispatch move");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.d("dispatch up");
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtils.d("dispatch cancel");
                break;
        }
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
                LogUtils.d("intercept down");
                downX = Math.round(event.getX());
                downY = Math.round(event.getY());
                if (this.getCurrentMenuState() == MENU_SCROLLING) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (this.getCurrentMenuState() == MENU_OPEN) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (openMenuAnimator != null && openMenuAnimator.isRunning()) {
                        openMenuAnimator.cancel();
                    }
                    if (closeMenuAnimator == null) {
                        closeMenu();
                    } else if (!closeMenuAnimator.isRunning()) {
                        // 已经是在关闭过程中，就不用管
                        closeMenu();
                    }
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                // 收到滑动事件，未必拦截了
                LogUtils.d("intercept move");
                // 01 获取diff
                int moveX = Math.round(event.getX());
                int moveY = Math.round(event.getY());
                int[] diff = getTouchDiff(downX, downY, moveX, moveY);
                int diffX = diff[0];
                int diffY = diff[1];
                int touchSlop = viewConfig.getScaledTouchSlop();
                if (Math.abs(diffX) > touchSlop
                        && Math.abs(diffX) > Math.abs(diffY)
                        && this.getCurrentMenuState() != MENU_SCROLLING) {
                    // 一定要取绝对值，否则，向左滑动永远不满足条件
                    LogUtils.w("需要处理滑动了：" + diffX);
                    // 如果 parent 也会处理滑动事件，此时让 parent 不要处理
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                // 收到抬起事件，未必拦截了
                LogUtils.d("intercept up");
            }
            break;
            case MotionEvent.ACTION_CANCEL: {
                // 被 parent 拦截了，在自己事件处理还没结束的时候
                LogUtils.d("intercept cancel");
            }
            break;
        }
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        vTracker.addMovement(event);
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            performClick();
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                // 收到点击事件，如果要处理，就必须返回 true
                LogUtils.i("touch down # down");
                if (this.getCurrentMenuState() == MENU_SCROLLING) {
                    return false; // 不处理本次的 move,up, 等后续事件
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                // 收到滑动事件，如果 down 的时候返回了 true
                LogUtils.i("touch move # move");
                // 3步走 ，1 获取 diff;2 执行逻辑 3 重置 down

                // 01 获取diff
                int moveX = Math.round(event.getX());
                int moveY = Math.round(event.getY());
                int[] diff = getTouchDiff(downX, downY, moveX, moveY);
                int diffX = diff[0];
                int diffY = diff[1];
                // 01-1 滑动边界值检查

                boolean canScroll = checkScrollEdge(diffX, diffY)
                        && this.getCurrentMenuState() != MENU_SCROLLING;

                LogUtils.e("touch move canScroll ? " + canScroll + " , " + mCurrentMenuState);
                // 02 处理逻辑 （包含滑动边界检测）
                if (canScroll) {
                    LogUtils.e("touch move scrollTo ? ");
                    getParent().requestDisallowInterceptTouchEvent(true);
                    handlerLayoutByDiff(diffX, diffY);
                }
                // 03 end. 处理逻辑之后，重置 down
                downX = moveX;
                downY = moveY;

            }
            break;
            case MotionEvent.ACTION_UP: {
                // 收到抬起事件，如果 down 的时候返回了 true
                LogUtils.i("touch up # up");
                boolean canAutoScroll = checkScroll()  // 在滑动边界内
                        && this.getCurrentMenuState() != MENU_SCROLLING; // 上次的滑动已经结束

                LogUtils.e("touch up autoScroll: " + canAutoScroll);
                if (!canAutoScroll)
                    return false;
                int minVelocity = viewConfig.getScaledMinimumFlingVelocity();
                vTracker.computeCurrentVelocity(1000); // 先计算
                float xVelocity = vTracker.getXVelocity(); // 再求值
                float yVelocity = vTracker.getYVelocity();
                LogUtils.e("velocity#UP=(%s,%s) ### min=%s",
                        Math.round(xVelocity), Math.round(yVelocity), minVelocity);
                // xV>0, 说明从左往右滑动了；
                // xV<0, 说明从右往左滑动了；
                int upX = Math.round(event.getX());
                int upY = Math.round(event.getY());
                int[] diff = getTouchDiff(downX, downY, upX, upY);
                int diffX = diff[0];
                int diffY = diff[1];

                int menuWidths = getMenuWidths();
                // 03 end. 处理逻辑之后，重置 down
                boolean fastSpeed = Math.abs(xVelocity) > minVelocity;
                if (xVelocity > 0 && fastSpeed) {
                    // xV>0 , 说明从左往右滑动了 ==> 回到初始值 【关闭menu】
                    closeMenu();
                } else if (xVelocity < 0 && fastSpeed) {
                    openMenu(menuWidths);
                } else {
                    // 速度 == 0 ，说明是滑动之后停止，然后再抬起手指的，这时候，就判断距离吧
                    if (Math.abs(getScaleX()) > menuWidths / 2) {
                        // 滑动距离超过 1/2 , openMenu
                        openMenu(menuWidths);
                    } else {
                        // closeMenu
                        closeMenu();
                    }
                }
                //
                downX = upX;
                downY = upY;
                vTracker.clear();
            }
            break;
            case MotionEvent.ACTION_CANCEL: {
                // 收到 parent 拦截事件，如果 down 的时候返回了 true
                LogUtils.i("touch cancel # cancel");
                int currentMenuState = getCurrentMenuState();
                switch (currentMenuState) {
                    case MENU_CLOSE:
                        closeMenu();
                        break;
                    case MENU_OPEN:
                        openMenu();
                        break;
                }
            }
            break;
        }
        return true;
    }

    private int getMenuWidths() {
        int menuWidths = 0;
        for (int i = 1; i < getChildCount(); i++) {
            View temp = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) temp.getLayoutParams();
            int width = temp.getWidth();
            menuWidths += width + lp.leftMargin + lp.rightMargin;
        }
        return menuWidths;
    }

    public void closeMenu() {

        LogUtils.e("touch up menu close: ▶️ -->");
        //                    scrollBy(-getScrollX(), 0);
        //                    scrollTo(0, 0);
        final int lastState = getCurrentMenuState();
        closeMenuAnimator = ValueAnimator.ofInt(getScrollX(), 0);
        closeMenuAnimator.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            scrollTo(value, 0);
        });
        closeMenuAnimator.setDuration(3000);
        closeMenuAnimator.setInterpolator(new DecelerateInterpolator());
        closeMenuAnimator.start();
        this.setCurrentMenuState(MENU_SCROLLING);
        closeMenuAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setCurrentMenuState(MENU_CLOSE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                setCurrentMenuState(lastState);
            }
        });
    }

    private void openMenu(int menuWidths) {
        LogUtils.e("touch up menu open: ◀️️ <--");
        //                    scrollTo(menuWidths, 0);
        final int lastState = getCurrentMenuState();
        this.setCurrentMenuState(MENU_SCROLLING);
        openMenuAnimator = ValueAnimator.ofInt(getScrollX(), menuWidths);
        openMenuAnimator.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            scrollTo(value, 0);
        });
        openMenuAnimator.setInterpolator(new DecelerateInterpolator());
        openMenuAnimator.setDuration(3000);
        openMenuAnimator.start();
        openMenuAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setCurrentMenuState(MENU_OPEN);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                setCurrentMenuState(lastState);
            }
        });
    }

    public void openMenu() {
        openMenu(getMenuWidths());
    }

    @Override
    public boolean performClick() {
        boolean b = super.performClick();
        LogUtils.d("perform click --- " + b);
        return b;
    }


    private boolean checkScrollEdge(int diffX, int diffY) {

        if (Math.abs(diffX) < viewConfig.getScaledTouchSlop()) {
            // 滑动距离过小，就不滑动 == 这个实际效果不好，去掉
            Log.v("scroll", "too small deltaX to scroll! ## " + diffX + " , " + diffY);
            // 实际现象是感觉触摸不灵敏，反应迟钝的样子
            //            return false;
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
            canScroll = checkScroll();
        }


        return canScroll;
    }

    private boolean checkScroll() {
        boolean canScroll;
        canScroll = true;
        // 添加判断，如果当前有 menu 是展开的情况，并且滑动的不是这个 menu #### start
        // 添加判断，如果当前有 menu 是展开的情况，并且滑动的不是这个 menu #### end

        // add condition for scrolling
        if (this.getCurrentMenuState() == MENU_SCROLLING) {
            canScroll = false;
        }
        return canScroll;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (vTracker == null) {
            vTracker = VelocityTracker.obtain();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (vTracker != null) {
            // vTracker.recycle();
        }
    }

    private void handlerLayoutByDiff(int diffX, int diffY) {
        // moveChild / scrollBy 都可以 ！
        scrollTo(getScrollX() - diffX, 0);
    }


    private int[] getTouchDiff(int startX, int startY, int endX, int endY) {
        return new int[]{endX - startX, endY - startY};
    }

}
