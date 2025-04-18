package com.heaton.blelibrary.ble.request;

import android.content.Context;

import com.heaton.blelibrary.ble.BleLog;
import com.heaton.blelibrary.ble.annotation.Implement;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 *
 * Created by LiuLei on 2018/1/22.
 */

public class Rproxy {

    private static Map<Class, Object> requestObjs;

    public static void init(Class... clss){
        requestObjs = new HashMap<>();
//        List<Class> requestsClass = getRequestsClass(context, getClass().getPackage().getName());
        for(Class cls : clss){
            if(cls.isAnnotationPresent(Implement.class)){
                for(Annotation ann : cls.getDeclaredAnnotations()){
                    if(ann instanceof Implement){
                        try {
                            requestObjs.put(cls, ((Implement) ann).value().newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static <T>T getRequest(Class cls){
        T t = (T) requestObjs.get(cls);
        if (t != null){
            return t;
        }
        return getRequestByReflect(cls);
    }

    private static <T>T getRequestByReflect(Class cls){
        try {
            Constructor constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            T request = null;
            try {
                request = (T) constructor.newInstance();
                requestObjs.put(cls,request);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return request;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new NoClassDefFoundError("Class not Request Type");
    }

    public static void release(){
        requestObjs.clear();
        BleLog.d("Rproxy", "Request proxy cache is released");
    }

    private List<Class> getRequestsClass(Context context, String packageName){
        List<Class> classRequestsList = new ArrayList<>();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历
                String className = (String) enumeration.nextElement();
                if (className.contains(packageName) && !className.contains("$")) {//在当前所有可执行的类里面查找包含有该包名的所有类
                    try {
                        Class requestCls = Class.forName(className);
                        classRequestsList.add(requestCls);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  classRequestsList;
    }


}
