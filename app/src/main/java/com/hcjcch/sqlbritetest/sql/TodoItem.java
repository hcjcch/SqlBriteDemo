package com.hcjcch.sqlbritetest.sql;

import android.content.ContentValues;
import android.database.Cursor;

import lombok.Getter;
import rx.functions.Func1;

/**
 * @author huangchen
 * @version 1.0
 * @time 16/9/10 00:40
 */

@Getter
public class TodoItem {

    public static final String _ID = "_id";
    public static final String TODO_NAME = "todoName";
    public static final String IS_FINISH = "isFinish";

    private int id;
    private String todoName;
    private boolean isFinish;

    public TodoItem(int id, String todoName, boolean isFinish) {
        this.id = id;
        this.todoName = todoName;
        this.isFinish = isFinish;
    }

    public TodoItem(boolean isFinish, String todoName) {
        this.isFinish = isFinish;
        this.todoName = todoName;
    }

    public static final Func1<Cursor, TodoItem> MAPPER = new Func1<Cursor, TodoItem>() {
        @Override
        public TodoItem call(Cursor cursor) {
            int id = DbHelper.getInt(cursor, _ID);
            String todoName = DbHelper.getString(cursor, TODO_NAME);
            boolean isFinish = DbHelper.getBoolean(cursor, IS_FINISH);
            return new TodoItem(id, todoName, isFinish);
        }
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder todoName(String todoName) {
            values.put(TODO_NAME, todoName);
            return this;
        }

        public Builder isFinish(boolean isFinish) {
            values.put(IS_FINISH, isFinish);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }

    @Override
    public String toString() {
        return "TodoItem{" +
            "id=" + id +
            ", todoName='" + todoName + '\'' +
            ", isFinish=" + isFinish +
            '}';
    }
}
