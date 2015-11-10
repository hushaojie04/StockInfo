package sj.android.stock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import sj.android.stock.article.ArticleFragment;
import sj.android.stock.fragment.FindFragment;
import sj.android.stock.fragment.MessageFragment;
import sj.android.stock.fragment.MyFragment;
import sj.android.stock.view.FragmentTabHost;

public class MainActivity extends FragmentActivity {
    private ViewPager vp;
    private LayoutInflater layoutInflater;
    private View rootView;
    private FrameLayout titlebar;
    private int mImageViewArray[] = {R.drawable.tab_news_selector, R.drawable.tab_find_selector, R.drawable.tab_message_selector, R.drawable.tab_mine_selector};
    private int mTextviewArray[] = {R.string.news, R.string.find, R.string.message, R.string.mine};
    //����FragmentTabHost����
    private FragmentTabHost mTabHost;
    private TextView titleView;
    private ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoader.from(this);
        Cache.from(this);
        rootView = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(R.layout.activity_main);
        initHead();
        initView();
    }

    /**
     * ��ʼ�����
     */
    private void initView() {
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new ArticleFragment());
        fragmentList.add(new FindFragment());
        fragmentList.add(new MessageFragment());
        fragmentList.add(new MyFragment());
        layoutInflater = LayoutInflater.from(this);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setTabcontent(android.R.id.tabcontent).setTabs(android.R.id.tabs);
        mTabHost.setOnItemClickListener(new FragmentTabHost.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                updateTitle(getResources().getString(mTextviewArray[position]), position == 0);
            }
        });
        mTabHost.setTabAdapter(this, mTabHost.new TabAdapter(fragmentList, mImageViewArray, mTextviewArray, R.layout.itemview));

    }


    private void initHead() {
        titlebar = (FrameLayout) findViewById(R.id.titlebar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
        params.height = ScreenAdapter.getInstance(this).getHeadHeight();
        titlebar.requestLayout();
        titleView = (TextView) findViewById(R.id.title);
        searchBtn= (ImageButton) findViewById(R.id.searchBtn);
    }

    public void updateTitle(String title, boolean isShowSearch) {
        titleView.setText(title);
        searchBtn.setVisibility(isShowSearch ? View.VISIBLE : View.INVISIBLE);
    }

}
