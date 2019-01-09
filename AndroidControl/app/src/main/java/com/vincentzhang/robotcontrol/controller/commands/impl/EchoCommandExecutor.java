package com.vincentzhang.robotcontrol.controller.commands.impl;

import android.util.Log;

import com.vincentzhang.robotcontrol.controller.commands.CommandExecutor;
import com.vincentzhang.robotcontrol.controller.commands.CommandMeta;

/**
 * Created by VincentZhang on 1/9/2019.
 */

public class EchoCommandExecutor implements CommandExecutor {

    private static String tag = "EchoCommandExecutor";

    @Override
    @CommandMeta(tag = "echo", pattern = "echo")
    public String execute(String[] params) {
        String msg = "";
        for (String msgEntry : params) {
            msg += " " + msgEntry;
        }
        Log.i(tag, "Received:" + msg);
        return msg;
    }
}
