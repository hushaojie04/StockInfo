package sj.android.stock.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import sj.android.stock.MyFragmentPagerAdapter;
import sj.android.stock.R;
import sj.android.stock.ScreenAdapter;

/**
 * Created by Administrator on 2015/10/22.
 */
public class ArticleFragment extends Fragment {
    ViewPager mViewPager;
    HorizontalScrollView mHorizontalScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fg_news, null);
        initScrollView(root);
        initViewPager(root);
        return root;
    }

    private void initScrollView(View root) {
        mHorizontalScrollView = (HorizontalScrollView) root.findViewById(R.id.typeTab);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHorizontalScrollView.getLayoutParams();
        params.height = ScreenAdapter.getInstance(null).getHeadHeight()/2;
        mHorizontalScrollView.requestLayout();
    }

    private void initViewPager(View root) {
        mViewPager = (ViewPager) root.findViewById(R.id.content);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        Fragment btFragment = new ArticleListFragment();
        Fragment secondFragment = new ArticleListFragment();
        Fragment thirdFragment = new ArticleListFragment();
        Fragment fourthFragment = new ArticleListFragment();
        fragmentList.add(btFragment);
        fragmentList.add(secondFragment);
        fragmentList.add(thirdFragment);
        fragmentList.add(fourthFragment);
        FragmentActivity fragmentActivity = getActivity();
        //给ViewPager设置适配器
        mViewPager.setAdapter(new MyFragmentPagerAdapter(fragmentActivity.getSupportFragmentManager(), fragmentList));
        mViewPager.setCurrentItem(0);//设置当前显示标签页为第一页
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("log", "onResume " + " " + " " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("log", "onPause " + this.getClass().getSimpleName());
    }
}
