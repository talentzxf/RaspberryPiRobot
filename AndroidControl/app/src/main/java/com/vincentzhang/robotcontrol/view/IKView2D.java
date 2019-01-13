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
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);                    //设置画笔颜色
        canvas.drawColor(Color.WHITE);                  //设置背景颜色
        paint.setStrokeWidth((float) 1.0);              //设置线宽
        canvas.drawLine(50, 50, 450, 50, paint);        //绘制直线
        paint.setStrokeWidth((float) 5.0);              //设置线宽
        canvas.drawLine(50, 150, 450, 150, paint);      //绘制直线
        paint.setStrokeWidth((float) 10.0);             //设置线宽
        canvas.drawLine(50, 250, 450, 250, paint);      //绘制直线
        paint.setStrokeWidth((float) 15.0);             //设置线宽
        canvas.drawLine(50, 350, 450, 350, paint);      //绘制直线
        paint.setStrokeWidth((float) 20.0);             //设置线宽
        canvas.drawLine(50, 450, 450, 450, paint);      //绘制直线
    }

}
