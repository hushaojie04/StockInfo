package sj.android.stock;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sj.android.stock.fragment.ArticleFragment;
import sj.android.stock.fragment.FindFragment;
import sj.android.stock.fragment.MyFragment;

public class MainActivity extends FragmentActivity {
    private ViewPager vp;
    //定义一个布局
    private LayoutInflater layoutInflater;
    private View rootView;

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_obd_selector, R.drawable.tab_aircleaner_selector, R.drawable.tab_setup_selector};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"车况", "过滤器", "设置"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(R.layout.activity_main);

    }

    /**
     * 初始化Fragment
     */
    private void initPage() {
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new ArticleFragment());
        fragmentList.add(new FindFragment());
        fragmentList.add(new MyFragment());

        //给ViewPager设置适配器
        vp = (ViewPager) findViewById(R.id.realtabcontent);
        vp.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        vp.setOffscreenPageLimit(3);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                checkTabBtn(mTabButtonLayout, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.itemview, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        view.setLayoutParams(params);
        view.setTag(index);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
//        ScreenAdapter.getInstance(this).setup(ScreenAdapter.AT_TabItem, imageView, 3);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = (int) v.getTag();
                vp.setCurrentItem(item);
            }
        });
        return view;
    }
}
