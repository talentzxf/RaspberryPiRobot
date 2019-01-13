package com.vincentzhang.robotcontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class IKView2D extends android.support.v7.widget.AppCompatImageView {
    public IKView2D(Context context) {
        super(context);
    }

    public IKView2D(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IKView2D(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
