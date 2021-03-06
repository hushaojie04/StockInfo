package sj.android.stock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;

import sj.android.stock.article.ArticleFragment;
import sj.android.stock.fragment.FindFragment;
import sj.android.stock.fragment.MessageFragment;
import sj.android.stock.mine.MyFragment;
import sj.android.stock.update.UpdateDialogHelper;
import sj.android.stock.view.FragmentTabHost;
import sj.utils.FileUtils;
import sj.utils.LogUtils;

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
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
        LogUtils.D("######################mainactivity onCreate");
        new UpdateDialogHelper(this).loadApkInfo();
        ImageLoader.from(this);
        FileUtils.mContext = this;
        Cache.from(this);
        rootView = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(R.layout.activity_main);
        initHead();
        initView();
        ToastManager.getManager().init(this);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        isConfirmExit = true;
        LogUtils.D("######################mainactivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        ToastManager.getManager().cancelToast();
    }

    /**
     * ��ʼ�����
     */
    private void initView() {
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        ArticleFragment articleFragment = new ArticleFragment();
        ScreenManager.getInstance().addActivityHandle(articleFragment);
        fragmentList.add(articleFragment);
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
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
    }

    public void updateTitle(String title, boolean isShowSearch) {
        titleView.setText(title);
        searchBtn.setVisibility(isShowSearch ? View.VISIBLE : View.INVISIBLE);
    }


    public void showFg(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            //前面两个动画是进来时Framgment的切换动画，后面两个是退出去时的切换动画
//            transaction.setCustomAnimations(R.anim.move_left, R.anim.move_right, R.anim.move_left, R.anim.move_right);
            transaction.add(R.id.fg_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void dimissFg(Fragment fragment, boolean isNeedExitAnim) {
        if (fragment != null) {
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
//            if (isNeedExitAnim)
//                transaction.setCustomAnimations(R.anim.move_left, R.anim.move_right, R.anim.move_left, R.anim.move_right);
            transaction.hide(fragment);
            transaction.detach(fragment);
            transaction.commit();
        }
    }

    boolean isConfirmExit = true;

    @Override
    public void onBackPressed() {
        if (ScreenManager.getInstance().handle()) return;
        if (isConfirmExit) {
            ToastManager.getManager().showToast(R.string.again_press);
            isConfirmExit = false;
            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isConfirmExit = true;
                }
            }, 3000);
            return;
        }
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
