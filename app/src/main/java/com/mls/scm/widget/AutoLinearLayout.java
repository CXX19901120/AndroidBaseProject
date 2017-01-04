package com.mls.scm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class AutoLinearLayout extends LinearLayout {

    protected Paint mPaint;

    protected boolean isPressed = false;

    public AutoLinearLayout(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public AutoLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public AutoLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setWillNotDraw(false);
    }


    public boolean onTouchEvent(MotionEvent event) {
        isPressed = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPaint.setColor(0x60636363);
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(0x00000000);
                break;
            case MotionEvent.ACTION_CANCEL:
                mPaint.setColor(0x00000000);
                break;
            default:
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPressed && isClickable() && isEnabled()) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                    0, 0, mPaint);
        }
    }
}
