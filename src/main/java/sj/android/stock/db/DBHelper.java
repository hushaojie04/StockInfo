package sj.android.stock.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/11/3.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "article.db";
    private static final int VERSION = 1;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DBHelper(Context context, String name) {
        this(context, name, VERSION);
    }

    public DBHelper(Context cxt) {
        this(cxt, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table addonarticle(" +
                "id integer primary key autoincrement," +
                "aid int," +
                "typeid int," +
                "body text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
