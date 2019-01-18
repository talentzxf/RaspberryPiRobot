package com.vincentzhang.robotcontrol.model;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.vincentzhang.robotcontrol.BR;
import com.vincentzhang.robotcontrol.R;
import com.vincentzhang.robotcontrol.controller.Controller;
import com.vincentzhang.robotcontrol.controller.impl.TCPControllerImpl;
import com.vincentzhang.robotcontrol.view.IKSolver2D;
import com.vincentzhang.robotcontrol.view.SkeletonChangeListener;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by VincentZhang on 1/8/2019.
 */

public class ControllerModel extends BaseObservable implements SkeletonChangeListener {
    private String tag = "ControllerModel";
    private Controller controller = new TCPControllerImpl();

    // 200 means full speed ahead
    // 100 means full stop
    // 0 means full speed back
    public static int INITSPEED = 100;

    private String msg;
    private int leftSpeed = INITSPEED;
    private int rightSpeed = INITSPEED;

    private boolean isControllerRunning() {
        return controller.isControllerRunning();
    }

    public ControllerModel(Resources resources) {
        msg = resources.getString(R.string.signature);
        try {
            // controller.connect("10.86.48.161", 9999);
            controller.connect("10.0.0.18", 9999);
        } catch (IOException e) {
            Log.e(tag, "Can't connect to remote server", e);
        }
    }

    public String getMsg() {
        return msg + "Controller Status:" + controller.getStatus();
    }

    @Bindable
    public int getLeftSpeed() {
        return leftSpeed;
    }

    public void setLeftSpeed(int leftSpeed) {
        this.leftSpeed = leftSpeed;
        notifyPropertyChanged(BR.leftSpeed);
        controller.setLeftSpeed(this.leftSpeed);
    }

    @Bindable
    public int getRightSpeed() {
        return rightSpeed;
    }

    public void setRightSpeed(int rightSpeed) {
        this.rightSpeed = rightSpeed;
        notifyPropertyChanged(BR.rightSpeed);
        controller.setRightSpeed(this.rightSpeed);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof IKSolver2D){
            IKSolver2D solver2D = (IKSolver2D) arg;

            Log.i("ControllerModel", "Skeleton change! New Servo1 Degree:" + solver2D.getDegServoTheta1() + " theta2:" + solver2D.getDegServoTheta2());
            controller.setServoDegree(1, solver2D.getDegServoTheta1());
            controller.setServoDegree(2, solver2D.getDegServoTheta2());
        }
    }
}
