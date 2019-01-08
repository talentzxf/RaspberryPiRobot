package com.vincentzhang.robotcontrol.model;

import android.content.res.Resources;

import com.vincentzhang.robotcontrol.R;

/**
 * Created by VincentZhang on 1/8/2019.
 */

public class ControllerModel {
    private Resources resources;
    private String msg;

    public ControllerModel(Resources resources){
         msg = resources.getString(R.string.signature);
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
