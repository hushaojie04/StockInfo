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

import com.umeng.analytics.MobclickAgent;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sj.android.stock.Cache;
import sj.android.stock.R;
import sj.android.stock.MURL;
import sj.android.stock.ToastManager;
import sj.android.stock.memento.Caretaker;
import sj.android.stock.memento.Originator;
import sj.http.JsonArrayRequest;
import sj.http.NetworkDispatcher;
import sj.http.Request;
import sj.http.Response;
import sj.utils.LogUtils;
import sj.utils.MD5Util;
import sj.utils.NetUtils;
import sj.xListview.XListView;

/**
 * Created by Administrator on 2015/10/22.
 */
@SuppressLint("ValidFragment")
public class ArticleListFragment extends Fragment implements XListView.IXListViewListener, Response.Listener<JSONArray> {
    String typeName;
    private ArrayList<String> items = new ArrayList<String>();
    private static int refreshCnt = 0;
    private int arctype = 0;
    NetworkDispatcher dispatcher;
    private List<ArticleInfo> articleInfoList = new ArrayList<ArticleInfo>();
    public int position;
    public static final int NUM = 10;
    private boolean isRefresh = false;
    private boolean isLoadAndRefresh = false;
    private Originator mOriginator;
    private String mementoKey;

    public ArticleListFragment(int typeId, String typeName, int position) {
        super();
        this.typeName = typeName;
        this.arctype = typeId;
        this.position = position;
        dispatcher = new NetworkDispatcher(new Handler());
        mOriginator = new Originator();
        mementoKey = MD5Util.MD5("arctype" + arctype);
        isLoadAndRefresh = false;
    }

    XListView mListView;

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("");
    }

    ArticleListAdapter articleListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOriginator.restoreMemento(Caretaker.getInstance().getMemento(mementoKey));
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
            load(0, NUM, true, "oncreate");
        else {
            articleListAdapter = new ArticleListAdapter(articleInfoList);
            mListView.setAdapter(articleListAdapter);
        }
        return root;
    }

    int requestid = -1;

    private void load(int start, int num, boolean isReflesh, String tag) {
        LogUtils.D("uuu", "handle tag " + tag);
        if (requestid != -1) return;
        this.isRefresh = isReflesh;
        if (isReflesh) {
            isLoadAndRefresh = true;
        }
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, MURL.getReadURL());
        request.params.add(new BasicNameValuePair("arttype", "" + arctype));
        request.params.add(new BasicNameValuePair("start", "" + start));
        request.params.add(new BasicNameValuePair("end", "" + num));
        request.setListener(this);
        LogUtils.D("start-num:" + start + " " + num);
        LogUtils.D("mOriginator.isContain(start):" + mOriginator.isContain(start));
        if (!mOriginator.isEmpty() && mOriginator.isContain(start)) {
            ArticleInfo[] articleInfos = mOriginator.get(start, start + num);
            if (articleInfos != null) {
                LogUtils.D("articleInfos len:" + articleInfos[0].toString());
                handle(articleInfos);
                return;
            }
        }
        if (NetUtils.isNetworkAvailable(getActivity())) {
            request.setListener(this);
            dispatcher.dispatch(request);
        } else {
            ToastManager.getManager().showToast(R.string.network_is_not_available);
        }
        LogUtils.D("load " + requestid + " " + typeName + " " + arctype);
    }

    private void refresh(int arctype, int aid, int typeid) {
        if (requestid != -1) return;
        isRefresh = true;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, MURL.getRefreshURL());
        request.params.add(new BasicNameValuePair("arttype", "" + arctype));
        request.params.add(new BasicNameValuePair("aid", "" + aid));
        request.params.add(new BasicNameValuePair("typeid", "" + typeid));
        if (NetUtils.isNetworkAvailable(getActivity())) {
            request.setListener(this);
            dispatcher.dispatch(request);
        } else {
            ToastManager.getManager().showToast(R.string.network_is_not_available);
        }
        LogUtils.D("refresh " + requestid + " " + typeName + " " + arctype);
    }

    public void setText(String string) {
        LogUtils.D(string);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


    @Override
    public void onRefresh() {
        check();
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (articleInfoList.size() > 0)
                    refresh(arctype, articleInfoList.get(0).id, articleInfoList.get(0).typeid);
                onLoad();
            }
        }, 800);
    }

    private void check() {
        if (articleInfoList.size() > 0 && !articleInfoList.get(0).equals(mOriginator.getDataQueue().getFirst())) {
            LogUtils.D("##########check############error");
        }
    }

    @Override
    public void onLoadMore() {
        LogUtils.D("articleInfoList.size()=" + articleInfoList.size());
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                load(articleInfoList.size(), NUM, false, "onLoadMore");
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
        handle(response.result);
        if (isLoadAndRefresh && articleInfoList.size() > 0) {
            refresh(arctype, articleInfoList.get(0).id, articleInfoList.get(0).typeid);
        }
        isRefresh = false;
    }

    private void handle(JSONArray result) {
        if (result.length() == 0) {
            isLoadNothing = true;
            return;
        }
        LogUtils.D("handle result length=" + result.length());
        List<ArticleInfo> temp = new ArrayList<ArticleInfo>();
        for (int i = 0; i < result.length(); i++) {
            ArticleInfo info = new ArticleInfo();
            try {
                info.parse((JSONObject) result.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            temp.add(info);
        }
        if (isRefresh && mListView != null) {
            mOriginator.addFirstAll(temp);
            //增加到articleInfoList的头部
            temp.addAll(articleInfoList);
            articleInfoList.clear();
            articleInfoList.addAll(temp);
            articleListAdapter = new ArticleListAdapter(articleInfoList);
            mListView.setAdapter(articleListAdapter);
        } else {
            articleInfoList.addAll(temp);
            mOriginator.addLastAll(temp);
            if (articleListAdapter != null)
                articleListAdapter.notifyDataSetChanged();
        }
    }

    private void handle(ArticleInfo[] result) {
        LogUtils.D("uuu", "handle result " + result.length);
        for (int i = 0; i < result.length; i++) {
            articleInfoList.add(result[i]);
        }
        if (isRefresh && mListView != null) {
            articleListAdapter = new ArticleListAdapter(articleInfoList);
            mListView.setAdapter(articleListAdapter);
        } else {
            if (articleListAdapter != null)
                articleListAdapter.notifyDataSetChanged();
        }
    }
}
