package com.hcjcch.sqlbritetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hcjcch.sqlbritetest.sql.TodoItem;

import java.util.List;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.hello);
        TodoApplication.getSqlLiteDataSource()
            .getTodoSqlDataSource()
            .queryAllTodoItems()
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
    }
}
