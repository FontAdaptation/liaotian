package com.ezreal.ezchat.utils;

import android.os.Environment;

import java.io.File;

public class Constant {

    public static final String APP_SECURY = "78d9451926b2";
    /**
     * SharePreference 相关
     */
    public static final String LOCAL_LOGIN_TABLE = "LOGIN_INFO";
    public static final String LOCAL_USER_ACCOUNT = "USER_ACOUNT";
    public static final String LOCAL_USER_TOKEN = "USER_TOKEN";

    public static final String OPTION_TABLE = "OPTION_TABLE";
    public static final String OPTION_KEYBOARD_HEIGHT = "OPTION_KEYBOARD_HEIGHT";

    /**
     * APP 缓存文件夹根目录
     */
    public static final String APP_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator+"EzChat";

    public static final String APP_CACHE_AUDIO = Constant.APP_CACHE_PATH + File.separator + "audio";

    public static final String APP_CACHE_IMAGE = Constant.APP_CACHE_PATH + File.separator + "image";

}
