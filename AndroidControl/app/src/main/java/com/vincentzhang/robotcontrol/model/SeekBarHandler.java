package com.vincentzhang.robotcontrol.model;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by VincentZhang on 1/8/2019.
 */

public class SeekBarHandler implements View.OnTouchListener {
    public boolean onTouch(View v, MotionEvent event) {
        SeekBar thisBar = (SeekBar) v;
        if(event.getAction() == MotionEvent.ACTION_UP){
            thisBar.setProgress(ControllerModel.INITSPEED);
            return true;
        }

        return v.onTouchEvent(event);
    }
}
