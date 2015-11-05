package sj.android.stock.article;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sj.android.stock.R;
import sj.android.stock.URL;
import sj.http.JsonArrayRequest;
import sj.http.NetworkDispatcher;
import sj.http.Request;
import sj.http.Response;
import sj.utils.LogUtils;
import sj.xListview.XListView;

/**
 * Created by Administrator on 2015/10/22.
 */
@SuppressLint("ValidFragment")
public class ArticleListFragment extends Fragment implements XListView.IXListViewListener, Response.Listener<JSONArray> {
    String typeName;
    private ArrayList<String> items = new ArrayList<String>();
    private static int refreshCnt = 0;
    private int typeId = 0;
    NetworkDispatcher dispatcher;
    private List<ArticleInfo> articleInfoList = new ArrayList<ArticleInfo>();
    public int position;

    public ArticleListFragment(int typeId, String typeName, int position) {
        super();
        this.typeName = typeName;
        this.typeId = typeId;
        this.position = position;
        dispatcher = new NetworkDispatcher(new Handler());
//        geneItems();
        load(0, 5, true);
        LogUtils.D("############ArticleListFragment###########" + typeName);
    }

    XListView mListView;

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("�ո�");
    }

    ArticleListAdapter articleListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fg_news_list, null);
        mListView = (XListView) root.findViewById(R.id.acticleListView);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                position--;
                Intent intent = new Intent(getActivity(), BodyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", articleInfoList.get(position));
                intent.putExtra("info", bundle);
                startActivity(intent);
            }
        });
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (articleInfoList.size() == 0)
            load(0, 5, true);
        else {
            articleListAdapter = new ArticleListAdapter(articleInfoList);
            mListView.setAdapter(articleListAdapter);
        }
        return root;
    }

    int requestid = -1;
    boolean isReflesh;

    private void load(int start, int end, boolean reflesh) {
        if (requestid != -1) return;
        isReflesh = reflesh;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL.getURL());
        request.params.add(new BasicNameValuePair("arttype", "" + typeId));
        request.params.add(new BasicNameValuePair("start", "" + start));
        request.params.add(new BasicNameValuePair("end", "" + end));
        request.setListener(this);
        requestid = request.getId();
        dispatcher.dispatch(request);
        LogUtils.D("load " + requestid + " " + typeName + " " + typeId);
    }

    public void setText(String string) {
        LogUtils.D(string);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("log", typeId + " onResume " + " " + " " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("log", typeId + " onPause " + this.getClass().getSimpleName());
    }


    @Override
    public void onRefresh() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                load(0, 5, true);
                onLoad();
            }
        }, 800);
    }

    @Override
    public void onLoadMore() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                load(articleInfoList.size(), articleInfoList.size() + 5, false);
                onLoad();
            }
        }, 800);
    }

    boolean isLoadNothing = false;

    @Override
    public void onResponse(Request<JSONArray> request, Response<JSONArray> response) {
        LogUtils.D("onResponse " + requestid + " " + typeName);
        if (requestid == request.getId()) requestid = -1;
        if (response.result == null) return;
        LogUtils.D("onResponse " + requestid + " " + typeName);
        if (isReflesh) {
            articleInfoList.clear();
        }
        if (response.result.length() == 0) {
            isLoadNothing = true;
        }
        for (int i = 0; i < response.result.length(); i++) {
            ArticleInfo info = new ArticleInfo();
            try {
                info.parse((JSONObject) response.result.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            articleInfoList.add(info);
        }

        if (isReflesh && mListView != null) {
            articleListAdapter = new ArticleListAdapter(articleInfoList);
            mListView.setAdapter(articleListAdapter);
        } else {
            if (articleListAdapter != null)
                articleListAdapter.notifyDataSetChanged();
        }

    }
}
