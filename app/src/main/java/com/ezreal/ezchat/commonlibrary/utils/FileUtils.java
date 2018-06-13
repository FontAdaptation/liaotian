package com.ezreal.ezchat.commonlibrary.utils;

import java.io.File;

/**
 * 文件工具类
 * Created by 张静
 */

public class FileUtils {

    /**
     * 递归创建文件夹，从最上层文件夹开始，只要不存在就会新建
     * @param dirPath 文件夹完整路径
     */
    public static void mkDir(String dirPath) {
        String[] dirArray = dirPath.split("/");
        String pathTemp = "";
        for (int i = 1; i < dirArray.length; i++) {
            pathTemp = pathTemp + "/" + dirArray[i];
            File newF = new File(dirArray[0] + pathTemp);
            if (!newF.exists()) {
                newF.mkdir();
            }
        }
    }
}
