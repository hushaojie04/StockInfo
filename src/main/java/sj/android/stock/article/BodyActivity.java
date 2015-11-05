package sj.android.stock.article;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.webkit.WebSettings;
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
        dispatcher = new NetworkDispatcher(new Handler());
        mArticleBodyDao = new ArticleBodyDao(this);
        initHead();
        initView();
        processExtraData();
    }

    private void initView() {
        initWebView();
        writerinfo = (TextView) findViewById(R.id.writerinfo);
        title = (TextView) findViewById(R.id.title);
        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings ws = mWebView.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
    }

    private void initHead() {
        FrameLayout titlebar = (FrameLayout) findViewById(R.id.titlebar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
        params.height = ScreenAdapter.getInstance(this).getHeadHeight();
        titlebar.requestLayout();
    }

    String base = "http://player.youku.com/embed/XMTM3NTI3MjU2NA==?";
    String url = "";

    @Override
    public void onResponse(Request<JSONArray> request, Response<JSONArray> response) {
        try {
            JSONObject jsonObject = response.result.getJSONObject(0);
            String body = jsonObject.getString("body");
            String flash = jsonObject.getString("flash");
            String shipin = jsonObject.getString("shipin");
            String id = "";
            if (!shipin.equals("")) {
                id = shipin;
            } else if (flash.contains("player.youku.com")) {
                id = flash.substring(flash.lastIndexOf("/", flash.lastIndexOf("/")), flash.lastIndexOf("/"));
            } else if (body.contains("VideoIDS=")) {
                id = body.substring(body.indexOf("VideoIDS=") + "VideoIDS=".length());
            }
            if (!id.equals("")) {
                url = base + id;
            }
            LogUtils.D("url#" + url);
            mWebView.loadUrl(url);

//            body = body.replaceAll("/uploads/", URL.HOST + "/uploads/");
//            mArticleBodyDao.insertData(mArticleInfo, body, url);
//            mWebView.loadDataWithBaseURL(url, body, "text/html", "UTF-8", "");
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.D("body#########" + e.getMessage());
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
        LogUtils.D("mArticleInfo.typeid:" + mArticleInfo.typeid);
        LogUtils.D("mArticleInfo.id" + mArticleInfo.id);
        setArticleInfo(mArticleInfo);
        String body = mArticleBodyDao.query(mArticleInfo);
        if (body == null || body.equals("")) {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL.getURL("aid=" + mArticleInfo.id + "&" + "typeid=" + mArticleInfo.typeid));
            request.setListener(this);
            dispatcher.dispatch(request);
        } else {
//            mWebView.loadDataWithBaseURL("", "http://player.youku.com/embed/XMTM3NTI3MjU2NA==?XMzEwOTk4ODE2&qq-pf-to=pcqq.c2c", "text/html", "UTF-8", "");
        }
    }

    private void setArticleInfo(ArticleInfo info) {
        if (info == null) return;
        //头条
        String original = getResources().getString(R.string.original);
        if (info.flag != null && info.flag.equals(original)) {
            writerinfo.setText(info.writer + " " + getResources().getString(R.string.publish) + " " + Utils.parseTimestamp(info.senddate));
        } else {
            writerinfo.setText(getResources().getString(R.string.reprint) + " " + getResources().getString(R.string.publish) + " " + Utils.parseTimestamp(info.senddate));
        }
        title.setText(info.title);
    }
}
