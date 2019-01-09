package com.vincentzhang.robotcontrol.controller.commands.impl;

import android.util.Log;

import com.vincentzhang.robotcontrol.controller.commands.CommandMeta;
import com.vincentzhang.robotcontrol.controller.commands.CommandExecutor;

/**
 * Created by VincentZhang on 1/9/2019.
 */

public class EchoCommandExecutor implements CommandExecutor {

    private static String tag = "EchoCommandExecutor";

    @CommandMeta(tag = "echo", pattern = "echo")
    @Override
    public String execute(String... params) {
        String msg = "";
        for (String msgEntry : params) {
            msg += " " + msgEntry;
        }
        Log.i(tag, "Received:" + msg);
        return msg;
    }
}
