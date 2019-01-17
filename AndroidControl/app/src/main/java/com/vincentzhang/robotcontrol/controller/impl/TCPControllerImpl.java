package com.vincentzhang.robotcontrol.controller.impl;

import android.util.Log;

import com.vincentzhang.robotcontrol.controller.Controller;
import com.vincentzhang.robotcontrol.controller.commands.CommandDispatcher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by VincentZhang on 1/9/2019.
 */

public class TCPControllerImpl implements Controller, Runnable {
    private String tag = "TCPControllerImpl";
    private static final int ARRAYCAPACITY = 1000;

    private String hostname;
    private int port;

    private Socket clientSocket;
    private volatile DataOutputStream outputStream;
    private volatile BufferedReader inputReader;
    private CommandDispatcher commandDispatcher = new CommandDispatcher();

    private AtomicBoolean threadIsRunning = new AtomicBoolean(false);

    private BlockingQueue<String> pendingCommands = new ArrayBlockingQueue<String>(ARRAYCAPACITY);

    private Thread receiverThread;
    private Thread senderThread;
    private Thread uiThread;

    private volatile AtomicBoolean isServerOnline = new AtomicBoolean(false);

    @Override
    public boolean isControllerRunning() {
        return threadIsRunning.get();
    }

    @Override
    public boolean connect(String hostname, int port) throws IOException {
        this.uiThread = Thread.currentThread();
        this.hostname = hostname;
        this.port = port;
        receiverThread = new Thread(this);
        receiverThread.start();
        return true;
    }

    @Override
    public void setLeftSpeed(int speed) {
        setSpeed("left", speed);
    }

    @Override
    public void setRightSpeed(int speed) {
        setSpeed("right", speed);
    }

    private void setSpeed(String side, int speed) {
        if (null != outputStream) {
            int realSpeed = speed - 100;

            pendingCommands.offer(String.format("setspeed %s %d\n", side, realSpeed));
        }
    }

    @Override
    public void setServoDegree(int idx, double degServoTheta) {
        pendingCommands.offer(String.format("setservodegree %d %f\n", idx, degServoTheta));
    }

    public AtomicBoolean getIsServerOnline() {
        return isServerOnline;
    }

    @Override
    public void run() {
        threadIsRunning.set(true);

        try {
            clientSocket = new Socket(hostname, port);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            isServerOnline.set(true);
            // Start sender
            senderThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (threadIsRunning.get()) {
                        try {
                            String newCommand = pendingCommands.take();
                            outputStream.writeBytes(newCommand);
                        } catch (InterruptedException e) {
                            Log.e(tag, "Interrupted while trying to fetch pending command", e);
                            isServerOnline.set(false);
                        } catch (IOException e) {
                            Log.e(tag, "Error happened while trying to write command", e);
                            isServerOnline.set(false);
                        }
                    }
                }
            });

            senderThread.start();

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
        } catch (java.net.ConnectException e) {
            Log.e(tag, "Exception happened when trying to connect server", e);
            isServerOnline.set(false);
        } catch (IOException e) {
            Log.e(tag, "Exception happened when trying to read from connection", e);
            isServerOnline.set(false);
        } finally {
            try {
                isServerOnline.set(false);

                threadIsRunning.set(false);

                if (outputStream != null)
                    outputStream.close();
                if (inputReader != null)
                    inputReader.close();

                if (clientSocket != null)
                    clientSocket.close();

                outputStream = null;
                inputReader = null;
                clientSocket = null;

            } catch (IOException e) {
                Log.e(tag, "Exception happened when trying to close stream", e);
            }
        }
        Log.i(tag, "Command Recving thread exited");
    }

}
