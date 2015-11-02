package sj.android.stock.article;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

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

    public ArticleListFragment(int typeId, String typeName) {
        super();
        this.typeName = typeName;
        this.typeId = typeId;
//        geneItems();
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
        if (typeId == 2)
            load();
        return root;
    }

    private void load() {
        dispatcher = new NetworkDispatcher(new Handler());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL.getURL("arttype=" + typeId));
        request.setListener(this);
        dispatcher.dispatch(request);
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
//
//    private void geneItems() {
//        for (int i = 0; i != 20; ++i) {
//            items.add("refresh cnt " + (++refreshCnt));
//        }
//    }

    @Override
    public void onRefresh() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                articleListAdapter = new ArticleListAdapter(articleInfoList);
                mListView.setAdapter(articleListAdapter);
                onLoad();
            }
        }, 800);
    }

    @Override
    public void onLoadMore() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                geneItems();
                articleListAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 800);
    }

    @Override
    public void onResponse(Request<JSONArray> request, Response<JSONArray> response) {
        if (response.result == null) return;
        LogUtils.D(typeId + "====" + response.result.toString());
        for (int i = 0; i < response.result.length(); i++) {
            ArticleInfo info = new ArticleInfo();
            try {
                info.parse((JSONObject) response.result.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            articleInfoList.add(info);
        }
        articleListAdapter = new ArticleListAdapter(articleInfoList);
        mListView.setAdapter(articleListAdapter);

    }
}
