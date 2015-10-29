package sj.android.stock.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.List;

import sj.android.stock.MainActivity;
import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/10/23.
 */
public class FragmentTabHost extends RelativeLayout {
    private static final int CURRENT_TAB = 0x1;
    private int tabcontent = 0;
    private LinearLayout tabs;
    private TabAdapter tabAdapter;
    private MainActivity activity;
    private OnItemClickListener onItemClickListener;

    public FragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentTabHost(Context context) {
        this(context, null);
    }

    public FragmentTabHost setTabcontent(int id) {
        tabcontent = id;
        return this;
    }

    public FragmentTabHost setTabs(int id) {
        tabs = (LinearLayout) findViewById(android.R.id.tabs);
        return this;
    }

    public void setItem(int position) {
        if (position == getCurrentItem()) return;
        LogUtils.D("setItem " + position);
        if (getCurrentItem() != -1)
            tabAdapter.fragments.get(getCurrentItem()).onPause();
        setTag(position);
        Fragment fragment = tabAdapter.fragments.get(position);
        FragmentTransaction ft = obtainFragmentTransaction(position);
        if (fragment.isAdded()) {
            fragment.onResume(); // ����Ŀ��tab��onResume()
        } else {
            ft.add(tabcontent, fragment);
        }
        ft.commit();
        showTab(position);
    }

    private void showTab(int idx) {
        for (int i = 0; i < tabAdapter.getCount(); i++) {
            Fragment fragment = tabAdapter.fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
    }

    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        // �����л�����
        if (index > getCurrentItem()) {
//            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
//            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    public int getCurrentItem() {
        if (getTag() == null) return -1;
        return (Integer) getTag();
    }

    public void setTabAdapter(MainActivity activity, TabAdapter tabAdapter) {
        this.activity = activity;
        if (tabcontent == 0) throw new NullPointerException("tabcontent is 0");
        if (tabs == null) throw new NullPointerException("tabs is null");
        this.tabAdapter = tabAdapter;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < tabAdapter.getCount(); i++) {
            View itemView = inflater.inflate(tabAdapter.itemview, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.weight = 1;
            itemView.setLayoutParams(params);
            tabs.addView(tabAdapter.getView(i, itemView, this));
            if (i == 0) {
                //init tab ==0
                setItem(0);
                itemView.setSelected(true);
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(this, itemView, 0);
            }
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class TabAdapter extends BaseAdapter {
        List<Fragment> fragments;
        int[] images;
        int[] strings;
        int itemview;

        public TabAdapter(List<Fragment> fragments, int[] images, int[] strings, int itemview) {
            this.fragments = fragments;
            this.images = images;
            this.strings = strings;
            this.itemview = itemview;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /*
        get tabs item view
         */
        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            convertView.setTag(position);
            ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
            imageView.setImageResource(images[position]);

            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setTextColor(Color.BLACK);
            textView.setText(strings[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(parent, convertView, position);
                    setItem(position);
                    if (!v.isSelected()) {
                        ViewGroup parent = (ViewGroup) v.getParent();
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            View child = parent.getChildAt(i);
                            if (i == position) {
                                child.setSelected(true);
                            } else {
                                child.setSelected(false);

                            }
                        }
                    }

                }
            });
            return convertView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewGroup parent, View view, int position);
    }
}
