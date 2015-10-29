package sj.android.stock.fragment;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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
import sj.android.stock.view.CatchTouchViewPager;
import sj.android.stock.view.ItemHScrollViewIndicator;
import sj.android.stock.view.ItemHScrollView;
import sj.http.JsonObjectRequest;
import sj.http.NetworkDispatcher;
import sj.http.Request;
import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/10/22.
 */
public class ArticleFragment extends Fragment {
    CatchTouchViewPager mViewPager;
    ItemHScrollView mItemHScrollView;
    ItemHScrollViewIndicator indicator;
    ViewGroup tabs;
    String[] array;
    NetworkDispatcher dispatcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Resources res = getResources();
        array = res.getStringArray(R.array.category);

        View root = inflater.inflate(R.layout.fg_news, null);
        initScrollView(root);
        initViewPager(root);
        dispatcher = new NetworkDispatcher(new Handler());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,"");
        return root;
    }

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
        mViewPager = (CatchTouchViewPager) root.findViewById(R.id.content);
        fragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < array.length; i++)
            fragmentList.add(new ArticleListFragment(array[i]));
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
                    case MotionEvent.ACTION_UP:
                        mItemHScrollView.touchUp();
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
//                if (Math.abs(lastOffset - positionOffset) < 0.005f) return;

                if (lastOffset == 0) {
                    lastOffset = positionOffset;
                    return;
                }
                boolean toRight = false;
                if (lastOffset > positionOffset) {//to right
                    toRight = true;
                } else {
                    toRight = false;
                }

                LogUtils.D(position + " " + convert(positionOffset));
                mItemHScrollView.onPageScrolled(position, convert(positionOffset), toRight);
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

    static float convert(float value) {
        long l1 = Math.round(value * 1000);   //四舍五入
        float ret = l1 / 1000.0f;               //注意：使用   100.0   而不是   100
        return ret;
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
            ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.tab_text_selector);
            if (csl != null) {
                textView.setTextColor(csl);//设置按钮文字颜色
            }
            textView.setTag("position " + position);
            return textView;
        }
    }
}
