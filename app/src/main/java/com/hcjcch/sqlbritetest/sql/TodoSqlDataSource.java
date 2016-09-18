package com.hcjcch.sqlbritetest.sql;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;

/**
 * 请添加至少一句话描述
 *
 * @author huangchen
 * @version 1.0
 * @time 16/9/10 13:17
 */

public class TodoSqlDataSource {
    public static final String TABLE_NAME = "todo";
    public static final String CREATE_LOG_DATA =
        "CREATE TABLE " + TABLE_NAME
            + "("
            + TodoItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TodoItem.TODO_NAME + " TEXT,"
            + TodoItem.IS_FINISH + " TINYINT(1)"
            + ")";
    public static final String QUERY_ALL_TODO_ITEM = "SELECT * FROM todo";
    private BriteDatabase briteDatabase;

    public TodoSqlDataSource(BriteDatabase briteDatabase) {
        this.briteDatabase = briteDatabase;
    }

    public Observable<List<TodoItem>> queryAllTodoItems(){
        return briteDatabase.createQuery(TABLE_NAME, QUERY_ALL_TODO_ITEM).mapToList(TodoItem.MAPPER);
    }

    //插入一条TODO
    public void insertTodoItem(TodoItem todoItem) {
        ContentValues values =
            new TodoItem
                .Builder()
                .todoName(todoItem.getTodoName())
                .isFinish(todoItem.isFinish()).build();
        briteDatabase.insert(TABLE_NAME, values);
    }

    //插入很多TODOS
    public void insertTodoItems(List<TodoItem> todoItemList) {
        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
            for (TodoItem todoItem : todoItemList){
                insertTodoItem(todoItem);
            }
            transaction.markSuccessful();
        }finally {
            transaction.end();
        }
    }

    //根据ID删除TODO
    public void deleteTodoItemById(int id) {
        briteDatabase.delete(TABLE_NAME, TodoItem._ID + "=?", id + "");
    }

    //删除所有TODO
    public void deleteAllTodoItems() {
        briteDatabase.delete(TABLE_NAME, null);
    }
}
