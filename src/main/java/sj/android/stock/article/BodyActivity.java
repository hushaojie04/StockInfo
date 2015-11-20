package sj.android.stock.article;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import sj.android.stock.Cache;
import sj.android.stock.R;
import sj.android.stock.ScreenAdapter;
import sj.android.stock.MURL;
import sj.android.stock.db.ArticleBodyDao;
import sj.http.JsonArrayRequest;
import sj.http.NetworkDispatcher;
import sj.http.Request;
import sj.http.Response;
import sj.utils.FileUtils;
import sj.utils.LogUtils;
import sj.utils.StringUtils;

/**
 * Created by Administrator on 2015/11/1.
 */
public class BodyActivity extends Activity implements Response.Listener<JSONArray> {
    WebView mWebView, bodyWebView;
    ArticleInfo mArticleInfo;
    NetworkDispatcher dispatcher;
    ArticleBodyDao mArticleBodyDao;
    TextView writerinfo, title;
    FrameLayout video_view;
    View customView;
    int mWidth, mHeight;
    LinearLayout videoLayout;
    xWebChromeClient xwebchromeclient;
    WebChromeClient.CustomViewCallback xCustomViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉应用标题
        getScreenWH();
        setContentView(R.layout.fg_news_comment);
        dispatcher = new NetworkDispatcher(new Handler());
        mArticleBodyDao = new ArticleBodyDao(this);
        initHead();
        initView();
        processExtraData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            }
        } else {
            mWebView.loadUrl("about:blank");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public boolean inCustomView() {
        return (customView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
    }

    private void initView() {
        videoLayout = (LinearLayout) findViewById(R.id.videoLayout);
        writerinfo = (TextView) findViewById(R.id.writerinfo);
        title = (TextView) findViewById(R.id.title);
        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);
        initWebView();
    }

    private void getScreenWH() {
        DisplayMetrics dm = new DisplayMetrics();//获取当前显示的界面大小
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;//获取当前界面的高度
    }

    private void initWebView() {
        //body
        bodyWebView = (WebView) findViewById(R.id.bodyWebView);
        bodyWebView.getSettings().setJavaScriptEnabled(true);
        bodyWebView.getSettings().setJavaScriptEnabled(true);
        bodyWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        bodyWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
        bodyWebView.getSettings().setDomStorageEnabled(true);
        bodyWebView.getSettings().setDatabaseEnabled(true);
        bodyWebView.getSettings().setAppCacheEnabled(true);
        bodyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                LogUtils.D("onLoadResource url=" + url);

                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.D("intercept url=" + url);
                view.loadUrl(url);
                return true;
            }

            // 页面开始时调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtils.D("onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            }
        });

        bodyWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                LogUtils.D("onJsAlert " + message);

                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                LogUtils.D("onJsConfirm " + message);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                LogUtils.D("onJsPrompt " + url);
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }
        });
        //video
        mWebView = (WebView) findViewById(R.id.webView);
        video_view = (FrameLayout) findViewById(R.id.video_view);
        WebSettings ws = mWebView.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
//        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        xwebchromeclient = new xWebChromeClient();
        mWebView.setWebChromeClient(xwebchromeclient);
        mWebView.setWebViewClient(new xWebViewClientent());
        changeViewPort(false);
    }

    private void changeViewPort(boolean fullScreen) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) videoLayout.getLayoutParams();
        if (fullScreen) {
            if (mWidth < mHeight) {
                params.height = (int) (9f / 16 * mWidth);
                params.width = mWidth;
            } else {
                params.height = (int) (9f / 16 * mHeight);
                params.width = mHeight;
            }
            videoLayout.requestLayout();
        } else {
            if (mWidth < mHeight) {
                params.height = (int) (9f / 16 * mWidth);
                params.width = mWidth;
            } else {
                params.height = (int) (9f / 16 * mHeight);
                params.width = mHeight;
            }
            videoLayout.requestLayout();
        }
    }

    private void initHead() {
        FrameLayout titlebar = (FrameLayout) findViewById(R.id.titlebar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
        params.height = ScreenAdapter.getInstance(this).getHeadHeight();
        titlebar.requestLayout();
    }

    String url = "";


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
        String[] result = mArticleBodyDao.query(mArticleInfo);
        if (result != null) {
            if (result[0] != null) {
                mWebView.loadUrl(result[1]);
            }
            if (result[1] != null) {
//                String xx = "url://articleinfo/" + mArticleInfo.typeid + "/" + mArticleInfo.id;
//                String url = "file:///android_asset/index.html";
//                bodyWebView.loadUrl(url);
                bodyWebView.loadDataWithBaseURL("about:blank", result[0], "text/html", "UTF-8", null);
//                bodyWebView.loadUrl("http://business.sohu.com/20151119/n427023850.shtml");
            }
        } else {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, MURL.getReadURL("aid=" + mArticleInfo.id + "&" + "typeid=" + mArticleInfo.typeid));
            request.setListener(this);
            dispatcher.dispatch(request);
        }
    }

    @Override
    public void onResponse(Request<JSONArray> request, Response<JSONArray> response) {
        if (response.result == null) return;
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
                url = MURL.YOUKU_PALYVIDEO.replace("XXXXXXXX", id);
            }
            mWebView.loadUrl(url);
            body = body.replaceAll("/uploads/", MURL.SERVER_URL + "/uploads/");
//            List urlList = FileUtils.getImgStr(body);
//            LogUtils.D("urlList.size#########" + urlList.size());
//            for (int i = 0; i < urlList.size(); i++) {
//                LogUtils.D("##url " + i + " " + urlList.get(i));
//            }

            mArticleBodyDao.insertData(mArticleInfo, body, url);
//            String url = "file:///android_asset/index.html";
//            bodyWebView.loadUrl(url);

            bodyWebView.loadDataWithBaseURL(url, body, "text/html", "UTF-8", null);
            //            bodyWebView.loadUrl("http://business.sohu.com/20151119/n427
            // 023850.shtml");

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.D("body#########" + e.getMessage());
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    private void setArticleInfo(ArticleInfo info) {
        if (info == null) return;
        //头条
        String original = getResources().getString(R.string.original);
        if (info.flag != null && info.flag.equals(original)) {
            writerinfo.setText(info.writer + " " + getResources().getString(R.string.publish) + " " + StringUtils.parseTimestamp(info.senddate));
        } else {
            writerinfo.setText(getResources().getString(R.string.reprint) + " " + getResources().getString(R.string.publish) + " " + StringUtils.parseTimestamp(info.senddate));
        }
        title.setText(info.title);
    }

    public class xWebChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            LogUtils.D("onShowCustomView");
            if (customView != null) {
                callback.onCustomViewHidden();
                return;
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            customView = view;
            video_view.addView(view);
            video_view.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            xCustomViewCallback = callback;
        }


        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            LogUtils.D("onHideCustomView");
            if (customView == null) return;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            customView.setVisibility(View.GONE);
            customView = null;
            video_view.removeView(customView);
            video_view.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            xCustomViewCallback.onCustomViewHidden();
        }

    }

    public class xWebViewClientent extends WebViewClient {

    }
}
