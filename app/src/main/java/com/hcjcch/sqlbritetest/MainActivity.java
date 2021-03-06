package com.hcjcch.sqlbritetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcjcch.sqlbritetest.sql.TodoItem;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText name;
    private CheckBox isFinish;
    private Button ok;
    private Button deleteAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.hello);
        name = (EditText) findViewById(R.id.todo_name);
        isFinish = (CheckBox) findViewById(R.id.is_finish);
        ok = (Button) findViewById(R.id.ok);
        deleteAll = (Button) findViewById(R.id.delete_all);
        TodoApplication.getSqlLiteDataSource()
            .getTodoSqlDataSource().queryAllTodoItems().observeOn(AndroidSchedulers.mainThread())
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
        Button button = new Button(this);
        RelativeLayout.LayoutParams params =
            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        ((RelativeLayout) findViewById(R.id.rel)).addView(button, params);
        button.setText("跳转到第二个Activity");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }
}
