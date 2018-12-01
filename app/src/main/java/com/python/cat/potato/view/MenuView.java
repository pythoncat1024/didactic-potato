package com.python.cat.potato.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;

public class MenuView extends FrameLayout {
    private float length;
    private OnMenuClickListener mListener;
    private int maxLen;
    private float lengthFactor;

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(getContext());
    }

    private View get() {
        return this;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxLen = Math.min(w, h);
        if (lengthFactor <= 0 || lengthFactor >= 1) {
            lengthFactor = 0.8f;
        }
        this.length = maxLen * lengthFactor;
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        LogUtils.v("...");
        View view = from.inflate(R.layout.menu_view_layout, this, false);
        this.addView(view);
        initMenuViews(view);
        setClickable(false);
        setFocusable(false);
    }

    boolean isMenuOpen = false;

    private void initMenuViews(View view) {
        View menu = view.findViewById(R.id.view_menu);
        View sa1 = view.findViewById(R.id.view_sa1);
        View sa2 = view.findViewById(R.id.view_sa2);
        View sa3 = view.findViewById(R.id.view_sa3);
        View sa4 = view.findViewById(R.id.view_sa4);
        View sa5 = view.findViewById(R.id.view_sa5);

        menu.setOnClickListener(v -> {
            LogUtils.d("sin90:" + Math.sin(Math.toRadians(90))); // ok
            LogUtils.d("sin30:" + Math.sin(Math.toRadians(30))); // ok
            if (!isMenuOpen) {
                menuOpen(sa1, sa2, sa3, sa4, sa5);
            } else {
                menuClose(sa1, sa2, sa3, sa4, sa5);

            }
            isMenuOpen = !isMenuOpen;
        });

        innerListener(sa1, sa2, sa3, sa4, sa5);

    }

    private void innerListener(View sa1, View sa2, View sa3, View sa4, View sa5) {
        sa1.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.click(0, sa1, get());
            }
        });
        sa2.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.click(1, sa2, get());
            }
        });
        sa3.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.click(2, sa3, get());
            }
        });
        sa4.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.click(3, sa4, get());
            }
        });
        sa5.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.click(4, sa5, get());
            }
        });
    }

    private void menuClose(View sa1, View sa2, View sa3, View sa4, View sa5) {
        AnimatorSet set0 = buildCloseAnimate(sa1, 0, 5);
        AnimatorSet set1 = buildCloseAnimate(sa2, 1, 5);
        AnimatorSet set2 = buildCloseAnimate(sa3, 2, 5);
        AnimatorSet set3 = buildCloseAnimate(sa4, 3, 5);
        AnimatorSet set4 = buildCloseAnimate(sa5, 4, 5);
        set0.start();
        set1.start();
        set2.start();
        set3.start();
        set4.start();
    }

    private void menuOpen(View sa1, View sa2, View sa3, View sa4, View sa5) {
        AnimatorSet set0 = buildOpenAnimate(sa1, 0, 5);
        AnimatorSet set1 = buildOpenAnimate(sa2, 1, 5);
        AnimatorSet set2 = buildOpenAnimate(sa3, 2, 5);
        AnimatorSet set3 = buildOpenAnimate(sa4, 3, 5);
        AnimatorSet set4 = buildOpenAnimate(sa5, 4, 5);
        set0.start();
        set1.start();
        set2.start();
        set3.start();
        set4.start();
    }


    private AnimatorSet buildOpenAnimate(View target, int index, int count) {
        AnimatorSet set = new AnimatorSet();
//        sa1.setScaleX();
        ObjectAnimator scaleXAn = ObjectAnimator.ofFloat(target, "scaleX", 0.1f, 1.0f);
        ObjectAnimator scaleYAn = ObjectAnimator.ofFloat(target, "scaleY", 0.1f, 1.0f);
//        sa1.setAlpha();
        ObjectAnimator alphaAn = ObjectAnimator.ofFloat(target, "alpha", 0.1f, 1.0f);
//        sa1.setTranslationX();
        float radians = (float) Math.toRadians(-90 + (-90) * index / (count - 1));
        float x = (float) (Math.sin(radians) * length);
        float y = (float) (Math.cos(radians) * length);

        LogUtils.w("length == " + length);
        LogUtils.d("x == " + x + " , y=" + y);
        ObjectAnimator translationXAn = ObjectAnimator.ofFloat(target, "TranslationX", 0f, x);
        ObjectAnimator translationYAn = ObjectAnimator.ofFloat(target, "TranslationY", 0f, y);
        set.playTogether(scaleXAn, scaleYAn, alphaAn, translationXAn, translationYAn);
        set.setInterpolator(new OvershootInterpolator());
        set.setDuration(500);
        return set;
    }

    private AnimatorSet buildCloseAnimate(View target, int index, int count) {
        AnimatorSet set = new AnimatorSet();
//        sa1.setScaleX();
        ObjectAnimator scaleXAn = ObjectAnimator.ofFloat(target, "scaleX", 1f, 0.1f);
        ObjectAnimator scaleYAn = ObjectAnimator.ofFloat(target, "scaleY", 1f, 0.1f);
//        sa1.setAlpha();
        ObjectAnimator alphaAn = ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.1f);
//        sa1.setTranslationX();
        float radians = (float) Math.toRadians(-90 + (-90) * index / (count - 1));
        float x = (float) (Math.sin(radians) * length);
        float y = (float) (Math.cos(radians) * length);
        LogUtils.d("x == " + x + " , y=" + y);
        ObjectAnimator translationXAn = ObjectAnimator.ofFloat(target, "TranslationX", x, 0);
        ObjectAnimator translationYAn = ObjectAnimator.ofFloat(target, "TranslationY", y, 0);
        set.playTogether(scaleXAn, scaleYAn, alphaAn, translationXAn, translationYAn);
        set.setDuration(500);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public interface OnMenuClickListener {
        void click(int index, View menu, View parent);
    }

    public void setOnMenuClickListener(OnMenuClickListener length) {
        this.mListener = length;
    }

    public void setLengthFactor(float factor) {
        this.lengthFactor = factor;
    }
}
