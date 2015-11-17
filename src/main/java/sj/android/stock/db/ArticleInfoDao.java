package sj.android.stock.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import sj.android.stock.article.ArticleInfo;
import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ArticleInfoDao {
    DBHelper helper = null;

    public ArticleInfoDao(Context context) {
        helper = new DBHelper(context);
    }

    public void insertData(ArticleInfo info, String articleinfo) {
        String sql = "insert into archives (aid,typeid,articleinfo)values(?,?,?)";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql, new Object[]{info.id, info.typeid, articleinfo});
    }

    public String query(ArticleInfo info,String start,String end) {
        String result = "";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select body,url from addonarticle where aid=" + info.id + " and typeid=" + info.typeid,
                null);
        if (cursor.moveToNext()) {
            String body = cursor.getString(cursor.getColumnIndex("body"));
            LogUtils.D("-->" + body);
            result = body;
        }
        return result;
    }
}
