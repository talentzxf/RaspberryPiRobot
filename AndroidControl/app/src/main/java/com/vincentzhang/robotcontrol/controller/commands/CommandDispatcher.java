package com.vincentzhang.robotcontrol.controller.commands;

import android.util.Log;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by VincentZhang on 1/9/2019.
 */

public class CommandDispatcher {
    private String tag = "CommandDispatcher";
    private Map<String, List<Method>> tag2CommandMapping = new HashMap<>();
    private Map<Method, String> command2PatternMapping = new HashMap<>();
    private Map<Class, Object> classObjectMap = new HashMap();

    private Object getObjectForClass(Class clz) {
        Object targetObject = classObjectMap.get(clz);
        if (targetObject == null) {
            try {
                targetObject = clz.newInstance();
                classObjectMap.put(clz, targetObject);
            } catch (IllegalAccessException e) {
                Log.e(tag, "Can't new instance for class", e);
                return null;
            } catch (InstantiationException e) {
                Log.e(tag, "Can't new instance for class", e);
                return null;
            }
        }
        return targetObject;
    }

    private void registerMethod(String tag, String pattern, Method method) {
        if (null == tag2CommandMapping.get(tag)) {
            tag2CommandMapping.put(tag, new ArrayList<Method>());
        }
        tag2CommandMapping.get(tag).add(method);
        command2PatternMapping.put(method, pattern);
    }

    public CommandDispatcher() {
        // Scan all commands in controller/commands/impl folder
        Reflections reflections = new Reflections("com.vincentzhang.robotcontrol.controller.commands.impl");
        Set<Method> executionMethods = reflections.getMethodsAnnotatedWith(CommandMeta.class);
        for (Method method : executionMethods) {
            CommandMeta executionMeta = method.getAnnotation(CommandMeta.class);
            if (!"undef".equals(executionMeta.tag()) &&
                    !"undef".equals(executionMeta.pattern())) {
                registerMethod(executionMeta.tag(), executionMeta.pattern(), method);
            }
        }
    }

    public String executeCommand(String commandLine) {
        String retValue = null;
        String[] commandParams = commandLine.split(" ");

        List<Method> methodCandidates = tag2CommandMapping.get(commandParams[0]);
        if (null != methodCandidates || !methodCandidates.isEmpty()) {
            if (methodCandidates.size() == 1) { // Only 1 candidate, just run it.
                Method targetMethod = methodCandidates.get(0);
                System.arraycopy(commandParams, 1, commandParams, 0, commandParams.length - 1);
                try {
                    targetMethod.invoke(getObjectForClass(targetMethod.getDeclaringClass()), commandParams, commandParams);
                } catch (IllegalAccessException e) {
                    Log.e(tag, "Error while try to exeucte command!", e);
                } catch (InvocationTargetException e) {
                    Log.e(tag, "Error while try to exeucte command!", e);
                }
            }
        } else {
            Log.i(tag, "Can't find command:" + commandLine);
        }

        return retValue;
    }
}
