package com.hcjcch.sqlbritetest.sql;

import android.database.Cursor;

/**
 * 请添加至少一句话描述
 *
 * @author huangchen
 * @version 1.0
 * @time 16/9/8 18:13
 */

public class DbHelper {
    private DbHelper() {
        throw new AssertionError("No instances.");
    }

    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        return getInt(cursor, columnName) == BOOLEAN_TRUE;
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static float getFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndexOrThrow(columnName));
    }
}
