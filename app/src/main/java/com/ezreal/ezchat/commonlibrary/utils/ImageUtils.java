package com.ezreal.ezchat.commonlibrary.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Image utils
 * Created by 仝心
 */


public class ImageUtils {


    /**
     * 从文件获取 bitmap ，并根据给定的显示宽高对 bitmap 进行缩放
     *
     * @param filePath 文件路径
     * @param height   需要显示的高度
     * @param width    需要显示的宽度
     * @return 缩放后的 bitmap，若获取失败，返回 null
     */
    public static Bitmap getBitmapFromFile(String filePath, int height, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        if ((srcWidth == -1) || (srcHeight == -1))
            return null;
        int inSampleSize = 1;
        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(filePath, options);
    }



    /**
     * 获取图片旋转角度
     *
     * @param path 图片路径
     * @return 旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
          /*
            TAG_DATETIME时间日期
            TAG_FLASH闪光灯
            TAG_GPS_LATITUDE纬度
            TAG_GPS_LATITUDE_REF纬度参考
            TAG_GPS_LONGITUDE经度
            TAG_GPS_LONGITUDE_REF经度参考
            TAG_IMAGE_LENGTH图片长
            TAG_IMAGE_WIDTH图片宽
            TAG_MAKE设备制造商
            TAG_MODEL设备型号
            TAG_ORIENTATION方向
            TAG_WHITE_BALANCE白平衡 */

            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 根据给定的角度，对 bitmap 进行旋转
     *
     * @param bitmap 原始 bitmap
     * @param degree 旋转角度
     * @return 旋转之后的 bitmap
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        Bitmap returnBm;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            Log.e("ImageUtils", e.getMessage());
            return bitmap;
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }




    public static String getFilePathFromUri(Context context,Uri uri) {
        String filePath = "";
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    /**
     * 将 bitmap 保存到本地 jpeg
     *
     * @param bitmap 图片 bitmap
     * @param path   保存路径（全路径）
     * @throws IOException
     */
    public static void saveBitmap2Jpg(Bitmap bitmap, String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
    }

    /**
     * load bitmap by url into imageView with diskCache
     */
    public static void setImageByUrl(Context context, ImageView imageView, String url,int default_img) {
        setImageByString(context, imageView, url, default_img);
    }

    /**
     * load bitmap by file path into imageView with diskCache
     */
    public static void setImageByFile(Context context, ImageView imageView, String filePath,int default_img) {
        setImageByString(context, imageView, filePath, default_img);
    }



    private static void setImageByString(Context context, ImageView imageView, String path,int default_img) {
        Glide.with(context)
                .load(path)
                .asBitmap()
                .error(default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }



}
