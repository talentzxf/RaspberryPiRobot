package com.vincentzhang.robotcontrol.model;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.vincentzhang.robotcontrol.BR;
import com.vincentzhang.robotcontrol.R;
import com.vincentzhang.robotcontrol.controller.Controller;
import com.vincentzhang.robotcontrol.controller.commands.ControllerStatusChangeListener;
import com.vincentzhang.robotcontrol.controller.impl.TCPControllerImpl;
import com.vincentzhang.robotcontrol.view.IKSolver2D;
import com.vincentzhang.robotcontrol.view.SkeletonChangeListener;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by VincentZhang on 1/8/2019.
 */

public class ControllerModel extends BaseObservable implements SkeletonChangeListener, ControllerStatusChangeListener {
    private String tag = "ControllerModel";
    private Controller controller = new TCPControllerImpl();

    // 200 means full speed ahead
    // 100 means full stop
    // 0 means full speed back
    public static int INITSPEED = 100;

    public static int INITANGLE = 28;
    public static int MAXANGLE = 50;
    public static int MINANGLE = 10;

    private String msg;
    private int leftSpeed = INITSPEED;
    private int rightSpeed = INITSPEED;
    private int clampAngle = INITANGLE;

    private String controllerStatus = "unknown";
    private boolean isControllerRunning() {
        return controller.isControllerRunning();
    }

    public ControllerModel(Resources resources) {
        msg = resources.getString(R.string.signature);
        controller.setStatusChangeListener(this);
        try {
            // controller.connect("10.86.48.161", 9999);
            controller.connect("10.0.0.18", 9999);
        } catch (IOException e) {
            Log.e(tag, "Can't connect to remote server", e);
        }
    }

    @Bindable
    public String getMsg() {
        return msg + "Controller Status:" + controllerStatus;
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

    @Bindable
    public int getClampAngle(){
        return clampAngle;
    }

    public void setClampAngle(int clampAngle){
        this.clampAngle = clampAngle;
        notifyPropertyChanged(BR.clampAngle);
        controller.setServoDegree(0, this.clampAngle);
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

    @Override
    public void onStatusChanged(String newStatus) {
        this.controllerStatus = newStatus;
        notifyPropertyChanged(BR.msg);
    }
}
