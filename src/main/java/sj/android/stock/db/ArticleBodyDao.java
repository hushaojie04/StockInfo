package sj.android.stock.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import sj.android.stock.article.ArticleInfo;
import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ArticleBodyDao {
    DBHelper helper = null;

    public ArticleBodyDao(Context context) {
        helper = new DBHelper(context);
    }

    public void insertData(ArticleInfo info, String body) {
        String sql = "insert into addonarticle (aid,typeid,body)values(?,?,?)";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql, new Object[]{info.id, info.typeid, body});
    }

    public String query(ArticleInfo info) {
        String result = "";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select body from addonarticle where aid=" + info.id + " and typeid=" + info.typeid,
                null);
        if (cursor.moveToNext()) {
            String body = cursor.getString(cursor.getColumnIndex("body"));
            LogUtils.D("-->" + body);
            result = body;
        }
        return result;
    }
}
