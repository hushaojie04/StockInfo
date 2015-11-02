package sj.android.stock.article;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import sj.http.JsonArrayRequest;
import sj.http.NetworkDispatcher;
import sj.http.Request;
import sj.http.Response;
import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/11/1.
 */
public class BodyActivity extends Activity implements Response.Listener<JSONArray> {
    WebView mWebView;
    ArticleInfo info;
    NetworkDispatcher dispatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_news_comment);
        initHead();
        mWebView = (WebView) findViewById(R.id.webView);
        processExtraData();
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
        info = (ArticleInfo) bundle.get("info");
        LogUtils.D("info:" + info.typeid);
        dispatcher = new NetworkDispatcher(new Handler());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL.getURL("aid=" + info.id + "&" + "typeid=" + info.typeid));
        request.setListener(this);
        dispatcher.dispatch(request);
    }
}
