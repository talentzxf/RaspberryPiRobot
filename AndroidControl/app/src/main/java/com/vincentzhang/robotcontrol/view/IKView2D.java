package com.vincentzhang.robotcontrol.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class IKView2D extends android.support.v7.widget.AppCompatImageView {
    final static double length1 = 280.0;
    final static double length2 = 280.0;

    private Skeleton2D skeleton2D = new Skeleton2D(length1, length2);

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

        this.skeleton2D.draw(canvas);
    }

}
