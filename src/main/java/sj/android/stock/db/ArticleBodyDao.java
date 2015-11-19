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

    public void insertData(ArticleInfo info, String body, String url) {
        String sql = "insert into addonarticle (aid,typeid,body,url)values(?,?,?,?)";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql, new Object[]{info.id, info.typeid, body, url});
    }

    public String[] query(ArticleInfo info) {
        String[] result = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select body,url from addonarticle where aid=" + info.id + " and typeid=" + info.typeid,
                null);
        if (cursor.moveToNext()) {
            result = new String[2];
            String body = cursor.getString(cursor.getColumnIndex("body"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            LogUtils.D("-->" + body);
            result[0] = body;
            result[1] = url;
        }
        return result;
    }
}
