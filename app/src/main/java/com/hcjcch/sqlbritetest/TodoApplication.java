package com.hcjcch.sqlbritetest;

import android.app.Application;

import com.hcjcch.sqlbritetest.sql.SqlLiteDataSource;

import lombok.Getter;

/**
 * TodoApplication
 *
 * @author huangchen
 * @version 1.0
 * @time 16/9/10 13:44
 */

public class TodoApplication extends Application {
    @Getter private static SqlLiteDataSource sqlLiteDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        sqlLiteDataSource = new SqlLiteDataSource(this);
    }
}