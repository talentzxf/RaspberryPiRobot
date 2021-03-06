package com.vincentzhang.robotcontrol.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.vincentzhang.robotcontrol.utils.MathHelper;

public class IKView2D extends android.support.v7.widget.AppCompatImageView {
    final static double length1 = 280.0;
    final static double length2 = 280.0;

    private Skeleton2D skeleton2D = new Skeleton2D(length1, length2);
    private int scrWidth = 0;
    private int scrHeight = 0;
    private SkeletonChangeListener skeletonChangeListener;

    public IKView2D(Context context) {
        super(context);
    }

    public IKView2D(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IKView2D(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSkeletonChangeListener(SkeletonChangeListener listener){
        Log.i("IKView2D", "setOnSkeletonChangeListener");
        skeletonChangeListener = listener;
        this.skeleton2D.addObserver(listener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        scrWidth = w;
        scrHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double x = MathHelper.getWorldX(scrWidth, event.getX());
        double y = MathHelper.getWorldY(scrHeight, event.getY());

        Log.i("IKView2D", "Touched screen, position:" + x + "," + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.skeleton2D.insideObject(x, y)) {
                    this.skeleton2D.setClampMoving(true);
                    Log.i("IKView2D", "Clamp begin to move!");
                }
                break;
            case MotionEvent.ACTION_UP:
                this.skeleton2D.setClampMoving(false);
                Log.i("IKView2D", "Clamp stop moving!");
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.skeleton2D.isClampMoving()) {
                    Log.i("IKView2D", "Clamp new position:" + x + "," + y + "!");
                    this.skeleton2D.setClampPosition(x, y);
                }
                break;
        }

        this.invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.skeleton2D.draw(canvas);
    }

}
