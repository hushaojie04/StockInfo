package sj.android.stock.article;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sj.android.stock.R;
import sj.android.stock.URL;
import sj.http.JsonArrayRequest;
import sj.http.NetworkDispatcher;
import sj.http.Request;
import sj.http.Response;

/**
 * Created by Administrator on 2015/11/1.
 */
public class ArticleCommentFragment extends Fragment implements Response.Listener<JSONArray> {
    WebView mWebView;
    ArticleInfo info;
    NetworkDispatcher dispatcher;

    public ArticleCommentFragment(ArticleInfo info) {
        super();
        this.info = info;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fg_news_comment, null);
        mWebView = (WebView) root.findViewById(R.id.webView);
        dispatcher = new NetworkDispatcher(new Handler());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL.getURL("aid=" + info.id + "&" + "typeid=" + info.typeid));
        request.setListener(this);
        dispatcher.dispatch(request);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResponse(Request<JSONArray> request, Response<JSONArray> response) {
        try {
            JSONObject jsonObject = response.result.getJSONObject(0);
            String body = jsonObject.getString("body");
            mWebView.loadDataWithBaseURL("",body, "text/html", "UTF-8", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
