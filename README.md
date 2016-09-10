# SqlBriteDemo
SqlBriteDemo
### DbHelper 封装了一些获取数据库每一列的方法
```JAVA
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
```

### SqlLiteOpenHelper 继承SQLiteOpenHelper
```JAVA
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
```

###TodoSqlDataSource Todo表管理类，所有todo表的操作都放在这个文件中，一半包括最基本的增删改查
```JAVA
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

    public void insertTodoItem(TodoItem todoItem) {
        ContentValues values =
            new TodoItem
                .Builder()
                .todoName(todoItem.getTodoName())
                .isFinish(todoItem.isFinish()).build();
        briteDatabase.insert(TABLE_NAME, values);
    }

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

    public void deleteTodoItemById(int id) {
        briteDatabase.delete(TABLE_NAME, TodoItem._ID + "=?", id + "");
    }
}
```

###TodoItem,具体的实例类，包括Field，生成ContentValues的Builder，列名常量：
```JAVA
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
```

###SqlLiteDataSource 负责管理SqlLite所有表的类，它里面包括每张表的管理类，负责它们的初始化：
```JAVA
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
```

###TodoApplication 初试化SqlLiteDataSource，这样在应用启动时就初始化了所有表的管理类每次只需要
```JAVA
TodoApplication.getSqlLiteDataSource().getTodoSqlDataSource()
```
这样一层一层调用，个人觉得这样并不是最好的解决方案，这样会在应用启动时初始化所有对象，非常耗时。最好使用dagger这样的依赖注入框架，在需要的地方初始化。


TodoApplication
```JAVA
public class TodoApplication extends Application {
    @Getter private static SqlLiteDataSource sqlLiteDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        sqlLiteDataSource = new SqlLiteDataSource(this);
    }
}
```
