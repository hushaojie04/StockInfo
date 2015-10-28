package sj.android.stock.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sj.android.stock.R;
import utils.LogUtils;

/**
 * Created by Administrator on 2015/10/22.
 */
@SuppressLint("ValidFragment")
public class ArticleListFragment extends Fragment {
    int id = 0;
    static int _ID;
    String dd;

    public ArticleListFragment(String dd) {
        super();
        id =  _ID++;
        this.dd = dd;
        LogUtils.D("onCreate" + id);
    }

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fg_news_list, null);
        textView = (TextView) root.findViewById(R.id.text);
        textView.setText("listfragment  " + id + " " + dd);
        return root;
    }

    public void setText(String string) {
        LogUtils.D(string);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("log", id + " onResume " + " " + " " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("log", id + " onPause " + this.getClass().getSimpleName());
    }
}
