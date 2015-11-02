package sj.android.stock.article;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/1.
 */
public class ArticleInfo {
    public int id;
    public int typeid;
    public String title;
    public long senddate;
    public String shorttitle;
    public String writer;
    public String description;
    public String flag;
    public int postnum;
    public void parse(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            typeid = jsonObject.getInt("typeid");
            title = jsonObject.getString("title");
            senddate = jsonObject.getLong("senddate");
            shorttitle = jsonObject.getString("shorttitle");
            writer = jsonObject.getString("writer");
            description = jsonObject.getString("description");
            flag = jsonObject.getString("flag");
            postnum = jsonObject.getInt("postnum");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
