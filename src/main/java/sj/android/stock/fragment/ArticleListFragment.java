package sj.android.stock.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import sj.android.stock.R;
import sj.android.stock.adapter.ArticleListAdapter;
import sj.utils.LogUtils;
import sj.xListview.XListView;

/**
 * Created by Administrator on 2015/10/22.
 */
@SuppressLint("ValidFragment")
public class ArticleListFragment extends Fragment implements XListView.IXListViewListener {
    String dd;
    private ArrayList<String> items = new ArrayList<String>();
    private static int refreshCnt = 0;

    public ArticleListFragment(String dd) {
        super();
        this.dd = dd;
        geneItems();
    }

    XListView mListView;

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("¸Õ¸Õ");
    }

    ArticleListAdapter articleListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fg_news_list, null);
        mListView = (XListView) root.findViewById(R.id.acticleListView);
        articleListAdapter = new ArticleListAdapter(items);
        mListView.setAdapter(articleListAdapter);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(ArticleListFragment.this.getActivity().getApplicationContext(), items.get(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(ArticleListFragment.this.getActivity().getApplicationContext(), items.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return root;
    }

    public void setText(String string) {
        LogUtils.D(string);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("log", dd + " onResume " + " " + " " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("log", dd + " onPause " + this.getClass().getSimpleName());
    }

    private void geneItems() {
        for (int i = 0; i != 20; ++i) {
            items.add("refresh cnt " + (++refreshCnt));
        }
    }

    @Override
    public void onRefresh() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                articleListAdapter = new ArticleListAdapter(items);
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
                geneItems();
                articleListAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 800);
    }
}
