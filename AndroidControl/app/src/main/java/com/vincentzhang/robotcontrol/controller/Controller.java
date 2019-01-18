package com.vincentzhang.robotcontrol.controller;

import com.vincentzhang.robotcontrol.controller.commands.ControllerStatusChangeListener;

import java.io.IOException;

/**
 * Created by VincentZhang on 1/9/2019.
 */

public interface Controller {
    void setLeftSpeed(int speed);
    void setRightSpeed(int speed);
    boolean connect(String hostname, int port) throws IOException;

    boolean isControllerRunning();

    void setServoDegree(int idx, double degServoTheta);
    void setStatusChangeListener(ControllerStatusChangeListener listener);
}
