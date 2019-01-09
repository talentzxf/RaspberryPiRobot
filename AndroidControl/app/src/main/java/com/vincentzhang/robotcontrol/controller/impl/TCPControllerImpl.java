package com.vincentzhang.robotcontrol.controller.impl;

import android.util.Log;

import com.vincentzhang.robotcontrol.controller.Controller;
import com.vincentzhang.robotcontrol.controller.commands.CommandDispatcher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by VincentZhang on 1/9/2019.
 */

public class TCPControllerImpl implements Controller, Runnable {
    private String tag = "TCPControllerImpl";

    private String hostname;
    private int port;

    private Socket clientSocket;
    private volatile DataOutputStream outputStream;
    private volatile BufferedReader inputReader;
    private CommandDispatcher commandDispatcher = new CommandDispatcher();

    private AtomicBoolean threadIsRunning = new AtomicBoolean(false);

    public boolean connect(String hostname, int port) throws IOException {
        clientSocket = new Socket(hostname, port);
        outputStream = new DataOutputStream(clientSocket.getOutputStream());
        inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        return true;
    }

    @Override
    public void setLeftSpeed(int speed) {
        setSpeed("LEFT", speed);
    }

    @Override
    public void setRightSpeed(int speed) {
        setSpeed("RIGHT", speed);
    }

    private void setSpeed(String side, int speed) {
        if (null != outputStream) {
            int realSpeed = speed - 100;
            try {
                outputStream.writeBytes(String.format("SET %sSPEED %d\n", side, realSpeed));
            } catch (IOException e) {
                Log.i(tag, "Write information failed!");
            }
        }
    }

    @Override
    public void run() {
        threadIsRunning.set(true);

        try {
            while (threadIsRunning.get()) {
                String command = inputReader.readLine();
                if (command.equalsIgnoreCase("exit") ||
                        command.equalsIgnoreCase("quit") ||
                        command.equalsIgnoreCase("bye")) {
                    threadIsRunning.set(false);
                } else {
                    commandDispatcher.executeCommand(command);
                }
            }
        } catch (IOException e) {
            Log.e(tag, "Exception happened when trying to read from connection", e);
        } finally {
            try {
                outputStream.close();
                inputReader.close();
                clientSocket.close();

                outputStream = null;
                inputReader = null;
                clientSocket.close();

            } catch (IOException e) {
                Log.e(tag, "Exception happened when trying to close stream", e);
            }
        }
        Log.i(tag, "Command Recving thread exited");
    }

}
