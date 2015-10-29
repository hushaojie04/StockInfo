package sj.android.stock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sj.android.stock.R;

/**
 * Created by Administrator on 2015/10/29.
 */
public class ArticleListAdapter extends BaseAdapter {
    List<String> items;

    public ArticleListAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.articleitemview, null);
            TextView pre = (TextView) convertView.findViewById(R.id.pre);
            pre.setText("qewrqerqwerqeeeeeeeeeeeeeeeeeeeeeeeee\neeeeeeeeeeeeadfasdf\nasdfasdfasdfasdfasdf\n" + items.get(position));
        }

        return convertView;
    }
}
