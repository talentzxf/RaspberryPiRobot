package com.vincentzhang.robotcontrol.controller.commands;

/**
 * Created by VincentZhang on 1/18/2019.
 */

public interface ControllerStatusChangeListener {
    void onStatusChanged(String newStatus);
}
