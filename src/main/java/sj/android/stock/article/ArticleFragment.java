package sj.android.stock.article;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sj.android.stock.ActivityHandle;
import sj.android.stock.Cache;
import sj.android.stock.MyFragmentPagerAdapter;
import sj.android.stock.R;
import sj.android.stock.ScreenAdapter;
import sj.android.stock.MURL;
import sj.android.stock.ToastManager;
import sj.android.stock.view.CatchTouchViewPager;
import sj.android.stock.view.ItemHScrollView;
import sj.http.JsonArrayRequest;
import sj.http.NetworkDispatcher;
import sj.http.Request;
import sj.http.Response;
import sj.utils.BitmapUtils;
import sj.utils.LogUtils;
import sj.utils.MD5Util;
import sj.utils.NetUtils;

/**
 * Created by Administrator on 2015/10/22.
 */
@SuppressLint("ValidFragment")
public class ArticleFragment extends Fragment implements ActivityHandle, Response.Listener<JSONArray> {
    CatchTouchViewPager mViewPager;
    ItemHScrollView mItemHScrollView;
    ViewGroup tabs;
    NetworkDispatcher dispatcher;
    Map<String, Integer> requestIds = new HashMap<String, Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fg_news, null);
        initView(root);
        dispatcher = new NetworkDispatcher(new Handler());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, MURL.getReadURL());
        request.params.add(new BasicNameValuePair("arttype", "0"));
        requestIds.put("type", request.getId());
        String cache = Cache.from(getActivity()).getData(MD5Util.MD5(request.getWholeURL()));
        if (cache != null && !cache.equals("")) {
            try {
                handleType(new JSONArray(cache));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (NetUtils.isNetworkAvailable(getActivity())) {
                request.setListener(this);
                dispatcher.dispatch(request);
            } else {
                ToastManager.getManager().showToast(R.string.network_is_not_available);
            }
        }
        return root;
    }

    ImageView arrowicon;
    View arrowiconParent;
    int count = 0;
    View list_title, list_content;
    ListView lanmu;

    private void initView(View root) {
        initScrollView(root);
        initViewPager(root);
        lanmu = (ListView) root.findViewById(R.id.lanmu);
        lanmu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(position);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (list_content.getVisibility() == View.VISIBLE) {
                            arrowlistener();
                        }
                    }
                }, 250);
            }
        });
        list_title = root.findViewById(R.id.list_title);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) list_title.getLayoutParams();
        params.height = (int) (ScreenAdapter.getInstance(null).getHeadHeight() * 0.7f);
        list_title.requestLayout();

        list_content = root.findViewById(R.id.list_content);
        arrowiconParent = root.findViewById(R.id.arrowiconParent);
        arrowicon = (ImageView) root.findViewById(R.id.arrowicon);
        arrowiconParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowlistener();
            }
        });

    }

    private void arrowlistener() {
        int width = arrowicon.getMeasuredWidth();
        int height = arrowicon.getMeasuredHeight();
        LogUtils.D("onClick" + width + " " + height);
        Matrix matrix = new Matrix();
        count++;
        matrix.postRotate(180 * count, width / 2, height / 2);
        arrowicon.setImageMatrix(matrix);
        if (count % 2 != 0) {
            list_title.setVisibility(View.VISIBLE);
            list_content.setVisibility(View.VISIBLE);
            View view = (View) mViewPager.getParent();
            Bitmap bitmap = BitmapUtils.shot(getView(),
                    (int) view.getX(),
                    (int) view.getY(),
                    mViewPager.getMeasuredWidth(),
                    mViewPager.getMeasuredHeight());
            if (bitmap != null)
                list_content.setBackground(new BitmapDrawable(BitmapUtils.blurBitmap(bitmap, getActivity())));
//                    list_content.setBackground(new BitmapDrawable(BitmapUtils.compressBmpFromBmp(bitmap)));
            mItemHScrollView.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
        } else {
            list_title.setVisibility(View.GONE);
            list_content.setVisibility(View.GONE);
            list_content.setBackground(null);
            mItemHScrollView.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
        }
    }

    private void initScrollView(View root) {
        mItemHScrollView = (ItemHScrollView) root.findViewById(R.id.typeTab);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mItemHScrollView.getLayoutParams();
        params.height = (int) (ScreenAdapter.getInstance(null).getHeadHeight() * 0.7f);
        mItemHScrollView.requestLayout();
        mItemHScrollView.setPositionOffset(0);
        mItemHScrollView.setOnItemClickListener(new ItemHScrollView.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                mViewPager.setCurrentItem(position);
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
                mItemHScrollView.onPageScrolled(position, convert(positionOffset), toRight);
                lastOffset = positionOffset;

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.D("onPageSelected " + position);
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
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onResponse(Request<JSONArray> request, Response<JSONArray> response) {
        if (response.result == null) return;
        Cache.from(getActivity()).save(MD5Util.MD5(request.getWholeURL()), response.result.toString());
        if (request.getId() == requestIds.get("type")) {
            handleType(response.result);
        }
    }

    private void handleType(JSONArray result) {
//        LogUtils.D(response.result.toString());
        fragmentList = new ArrayList<Fragment>();
        String[] typenames = new String[result.length()];
        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) result.get(i);
                int typeid = jsonObject.getInt("ID");
                String typename = jsonObject.getString("typename");
                fragmentList.add(new ArticleListFragment(typeid, typename, i));
                typenames[i] = typename;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        FragmentActivity fragmentActivity = getActivity();
        mViewPager.setAdapter(new MyFragmentPagerAdapter(fragmentActivity.getSupportFragmentManager(), fragmentList));
        mViewPager.setCurrentItem(0);
        mItemHScrollView.setAdpater(new TabButtonAdapter(typenames));
        lanmu.setAdapter(new LanmuAdapter(typenames));
    }

    private void initType() {

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

    class LanmuAdapter extends BaseAdapter {
        String[] array;

        public LanmuAdapter(String[] array) {
            this.array = array;
            LogUtils.D("LanmuAdapter " + array.length);
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
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LogUtils.D("getView " + position);

            TextView textView = new TextView(parent.getContext());
            textView.setText(array[position]);
            textView.setTag(array[position]);
            textView.setPadding(100, 40, 40, 40);
//            ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.tab_text_selector);
//            if (csl != null) {
//                textView.setTextColor(csl);//设置按钮文字颜色
//            }
            textView.setTag("position " + position);
            return textView;
        }
    }

    @Override
    public boolean onBack() {
        if (list_content.getVisibility() == View.VISIBLE) {
            arrowlistener();
            return true;
        }
        return false;
    }
}
