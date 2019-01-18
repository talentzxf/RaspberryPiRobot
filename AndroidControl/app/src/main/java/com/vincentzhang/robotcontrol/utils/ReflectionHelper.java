package com.vincentzhang.robotcontrol.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * Created by VincentZhang on 1/9/2019.
 */

public class ReflectionHelper {
    private static Context applicationContext;
    private static Activity mainActivity;

    public static List<Method> getAllMethodsWithAnnotation(String packageName, final Class<? extends Annotation> annotation) throws IOException, ClassNotFoundException {
        Class[] classes = getClasses(packageName);

        List<Method> retMethodList = new ArrayList<>();

        for (Class clz : classes) {
            List<Method> clzMethods = getMethodsAnnotatedWith(clz, annotation);
            retMethodList.addAll(clzMethods);
        }
        return retMethodList;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        PathClassLoader classLoader = (PathClassLoader) applicationContext.getClassLoader();
        ArrayList<Class> classes = new ArrayList<Class>();
        try {
            String packageCodePath = applicationContext.getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                String className = iter.nextElement();
                if (className.contains(packageName)) {
                    classes.add(classLoader.loadClass(className));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;
        while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
            final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(annotation)) {
                    Annotation annotInstance = method.getAnnotation(annotation);
                    // TODO process annotInstance
                    methods.add(method);
                }
            }
            // move to the upper class in the hierarchy in search for more methods
            klass = klass.getSuperclass();
        }
        return methods;
    }

    public static void setContext(Context applicationContext) {
        ReflectionHelper.applicationContext = applicationContext;
    }

    public static Context getContext() {
        return applicationContext;
    }

    public static void setActivity(Activity activity) {
        mainActivity = activity;
    }

    public static Activity getActivity() {
        return mainActivity;
    }

    public static void toastMessage(final String msg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
