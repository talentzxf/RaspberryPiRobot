package com.vincentzhang.robotcontrol.model;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vincentzhang.robotcontrol.BR;
import com.vincentzhang.robotcontrol.R;

/**
 * Created by VincentZhang on 1/8/2019.
 */

public class ControllerModel extends BaseObservable {
    private Resources resources;
    private String msg;
    private int leftSpeed = 50;
    private int rightSpeed = 50;

    public ControllerModel(Resources resources) {
        msg = resources.getString(R.string.signature);
    }

    public String getMsg() {
        return msg;
    }

    @Bindable
    public int getLeftSpeed() {
        return leftSpeed;
    }

    public void setLeftSpeed(int leftSpeed) {
        this.leftSpeed = leftSpeed;
        notifyPropertyChanged(BR.leftSpeed);
    }

    @Bindable
    public int getRightSpeed() {
        return rightSpeed;
    }

    public void setRightSpeed(int rightSpeed) {
        this.rightSpeed = rightSpeed;
        notifyPropertyChanged(BR.rightSpeed);
    }
}
