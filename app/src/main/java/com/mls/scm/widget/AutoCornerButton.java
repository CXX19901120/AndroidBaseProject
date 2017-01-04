package com.mls.scm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;


public class AutoCornerButton extends AutoButton {

    public AutoCornerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoCornerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCornerButton(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPressed && isClickable() && isEnabled()) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                    15, 15, mPaint);
        }
    }
}
