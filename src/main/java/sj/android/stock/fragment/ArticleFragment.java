package sj.android.stock.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sj.android.stock.MyFragmentPagerAdapter;
import sj.android.stock.R;
import sj.android.stock.ScreenAdapter;
import sj.android.stock.view.ItemHScrollViewIndicator;
import sj.android.stock.view.ItemHScrollView;
import utils.LogUtils;

/**
 * Created by Administrator on 2015/10/22.
 */
public class ArticleFragment extends Fragment {
    ViewPager mViewPager;
    ItemHScrollView mItemHScrollView;
    ItemHScrollViewIndicator indicator;
    ViewGroup tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fg_news, null);
        initScrollView(root);
        initViewPager(root);
        return root;
    }

    String[] array = {"aa", "bbbb", "ccccccc", "ddd", "eeeee", "ffffffffffff", "gggg", "hhhhhhhhhhhhhhhhh", "iii", "jjjjjjjjj", "kk"};

    private void initScrollView(View root) {
        mItemHScrollView = (ItemHScrollView) root.findViewById(R.id.typeTab);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mItemHScrollView.getLayoutParams();
        params.height = ScreenAdapter.getInstance(null).getHeadHeight() / 2;
        mItemHScrollView.requestLayout();
        mItemHScrollView.setPositionOffset(0);
        indicator = (ItemHScrollViewIndicator) root.findViewById(R.id.indicator);
        mItemHScrollView.setItemHScrollViewIndicator(indicator);
        mItemHScrollView.setAdpater(new TabButtonAdapter(array));
        mItemHScrollView.setOnItemClickListener(new ItemHScrollView.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                ((ArticleListFragment) fragmentList.get(position)).setText(view.getTag().toString());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    ArrayList<Fragment> fragmentList;

    private void initViewPager(View root) {
        mViewPager = (ViewPager) root.findViewById(R.id.content);
        fragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < array.length; i++)
            fragmentList.add(new ArticleListFragment());
        FragmentActivity fragmentActivity = getActivity();
        mViewPager.setAdapter(new MyFragmentPagerAdapter(fragmentActivity.getSupportFragmentManager(), fragmentList));
        mViewPager.setCurrentItem(0);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mItemHScrollView.touchDown();
                        break;
                }
                return false;
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            float lastOffset;
            boolean isScrolling = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (lastOffset == 0) {
                    lastOffset = positionOffset;
                    return;
                }
                //cv       ��ָ������
                boolean toRight = false;
                if (lastOffset > positionOffset) {//to right
                    toRight = true;
                } else {
                    toRight = false;
                }
                mItemHScrollView.onPageScrolled(position, positionOffset, toRight);
                lastOffset = positionOffset;

            }

            @Override
            public void onPageSelected(int position) {
                mItemHScrollView.setSelectedItem(position);
                lastOffset = 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }
            }
        });
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

    class TabButtonAdapter implements ItemHScrollView.Adapter {
        String[] array;

        public TabButtonAdapter(String[] array) {
            this.array = array;
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            return array[position];
        }

        @Override
        public View getView(ViewGroup parent, int position) {
            TextView textView = new TextView(parent.getContext());
            textView.setText(array[position]);
            textView.setTag(array[position]);
            return textView;
        }
    }
}
