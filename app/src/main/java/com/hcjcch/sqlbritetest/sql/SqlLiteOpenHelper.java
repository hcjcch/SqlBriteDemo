package com.hcjcch.sqlbritetest.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * SqlLiteOpenHelper
 *
 * @author huangchen
 * @version 1.0
 * @time 16/9/8 19:06
 */

public class SqlLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public SqlLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoSqlDataSource.CREATE_LOG_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
