package com.hcjcch.sqlbritetest.sql;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import lombok.Getter;
import rx.schedulers.Schedulers;

/**
 * sql lite data source
 *
 * @author huangchen
 * @version 1.0
 * @time 16/9/8 17:42
 */

public class SqlLiteDataSource {
    @Getter private TodoSqlDataSource todoSqlDataSource;
    private BriteDatabase briteDatabase;

    public SqlLiteDataSource(Context context) {
        if (briteDatabase == null) {
            synchronized (SqlLiteDataSource.class) {
                if (briteDatabase == null) {
                    briteDatabase =
                        SqlBrite.create().wrapDatabaseHelper(new SqlLiteOpenHelper(context), Schedulers.io());
                }
            }
        }
        this.todoSqlDataSource = new TodoSqlDataSource(briteDatabase);
    }
}
