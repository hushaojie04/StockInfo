package sj.android.stock.article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sj.android.stock.R;
import sj.android.stock.article.ArticleInfo;
import sj.utils.Utils;

/**
 * Created by Administrator on 2015/10/29.
 */
public class ArticleListAdapter extends BaseAdapter {
    List<ArticleInfo> articleInfoList;

    public ArticleListAdapter(List<ArticleInfo> articleInfoList) {
        this.articleInfoList = articleInfoList;
    }

    @Override
    public int getCount() {
        return articleInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.articleitemview, null);
            holder = new Holder();
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (!articleInfoList.get(position).title.equals(""))
            holder.title.setText(articleInfoList.get(position).title);
        if (!articleInfoList.get(position).description.equals(""))
            holder.description.setText(articleInfoList.get(position).description);
        return convertView;
    }

    class Holder {
        TextView title, description;
        ImageView pic;
    }
}
