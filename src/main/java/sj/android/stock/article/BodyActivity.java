package sj.android.stock.article;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sj.android.stock.R;
import sj.android.stock.ScreenAdapter;
import sj.android.stock.URL;
import sj.android.stock.db.ArticleBodyDao;
import sj.http.JsonArrayRequest;
import sj.http.NetworkDispatcher;
import sj.http.Request;
import sj.http.Response;
import sj.utils.LogUtils;
import sj.utils.Utils;

/**
 * Created by Administrator on 2015/11/1.
 */
public class BodyActivity extends Activity implements Response.Listener<JSONArray> {
    WebView mWebView;
    ArticleInfo mArticleInfo;
    NetworkDispatcher dispatcher;
    ArticleBodyDao mArticleBodyDao;
    TextView writerinfo, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_news_comment);
        mArticleBodyDao = new ArticleBodyDao(this);
        initHead();
        initView();
        processExtraData();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
        writerinfo = (TextView) findViewById(R.id.writerinfo);
        title = (TextView) findViewById(R.id.title);
        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);
    }

    private void initHead() {
        FrameLayout titlebar = (FrameLayout) findViewById(R.id.titlebar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
        params.height = ScreenAdapter.getInstance(this).getHeadHeight();
        titlebar.requestLayout();
    }

    @Override
    public void onResponse(Request<JSONArray> request, Response<JSONArray> response) {
        try {
            JSONObject jsonObject = response.result.getJSONObject(0);
            String body = jsonObject.getString("body");
            LogUtils.D("body#########" + body.contains("/uploads/"));
            body = body.replaceAll("/uploads/", URL.HOST + "/uploads/");
            LogUtils.D("body#########" + body.contains("/uploads/"));
            mArticleBodyDao.insertData(mArticleInfo, body);
            mWebView.loadDataWithBaseURL("", body, "text/html", "UTF-8", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtraData();
    }

    private void processExtraData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("info");
        mArticleInfo = (ArticleInfo) bundle.get("info");
        LogUtils.D("info:" + mArticleInfo.typeid);
        setArticleInfo(mArticleInfo);
        String body = mArticleBodyDao.query(mArticleInfo);
        if (body == null || body.equals("")) {
            dispatcher = new NetworkDispatcher(new Handler());
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL.getURL("aid=" + mArticleInfo.id + "&" + "typeid=" + mArticleInfo.typeid));
            request.setListener(this);
            dispatcher.dispatch(request);
        } else {
            mWebView.loadDataWithBaseURL("", body, "text/html", "UTF-8", "");
        }
    }

    private void setArticleInfo(ArticleInfo info) {
        if (info == null) return;
        //Í·Ìõ
        String original = getResources().getString(R.string.original);
        if (info.flag != null && info.flag.equals(original)) {
            writerinfo.setText(info.writer + " " + getResources().getString(R.string.publish) + " " + Utils.parseTimestamp(info.senddate));
        } else {
            writerinfo.setText(getResources().getString(R.string.reprint) + " " + getResources().getString(R.string.publish) + " " + Utils.parseTimestamp(info.senddate));
        }
        title.setText( info.title);
    }
}
