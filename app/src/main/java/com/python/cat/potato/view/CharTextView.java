package com.python.cat.potato.view;

import android.content.Context;
import android.util.AttributeSet;

public class CharTextView extends android.support.v7.widget.AppCompatTextView {
    public CharTextView(Context context) {
        super(context);
    }

    public CharTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * @param c char 一定要是包装类型，不能是基本类型
     *          否则 object 动画不生效
     */
    public void setCharText(Character c) {
        setText(String.valueOf(c));
    }
}
