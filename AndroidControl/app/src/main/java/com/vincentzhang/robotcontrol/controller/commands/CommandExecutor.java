package com.vincentzhang.robotcontrol.controller.commands;

/**
 * Created by VincentZhang on 1/9/2019.
 */

public interface CommandExecutor {
    @CommandMeta()
    String execute(String ... params);
}
