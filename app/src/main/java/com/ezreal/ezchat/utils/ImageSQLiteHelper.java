package com.ezreal.ezchat.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 李晨晨
 */

public class ImageSQLiteHelper extends SQLiteOpenHelper {

    public  static final int VERSION = 1;

    public ImageSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public ImageSQLiteHelper(Context context, String name, int version){
        this(context,name,null,version);
    }

    public ImageSQLiteHelper(Context context, String name){
        this(context,name,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table array(id varchar(20), content text, name int)");
        System.out.println("create a tableBase!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
