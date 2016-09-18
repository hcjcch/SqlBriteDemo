package com.hcjcch.sqlbritetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hcjcch.sqlbritetest.sql.TodoItem;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author huangchen
 * @version 1.0
 * @time 16/9/18 12:10
 */

public class SecondActivity extends AppCompatActivity {
    private TextView textView;
    private EditText name;
    private CheckBox isFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.hello);
        name = (EditText) findViewById(R.id.todo_name);
        isFinish = (CheckBox) findViewById(R.id.is_finish);
        Button ok = (Button) findViewById(R.id.ok);
        Button deleteAll = (Button) findViewById(R.id.delete_all);
        TodoApplication.getSqlLiteDataSource()
            .getTodoSqlDataSource()
            .queryAllTodoItems()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<TodoItem>>() {
                @Override
                public void call(List<TodoItem> todoItems) {
                    String todo = "";
                    for (TodoItem todoItem : todoItems) {
                        todo += todoItem;
                    }
                    textView.setText(todo);
                }
            });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(name.getText().toString())) {
                    TodoApplication.getSqlLiteDataSource()
                        .getTodoSqlDataSource()
                        .insertTodoItem(new TodoItem(isFinish.isChecked(), name.getText().toString()));
                }
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoApplication.getSqlLiteDataSource()
                    .getTodoSqlDataSource()
                    .deleteAllTodoItems();
            }
        });
    }
}
