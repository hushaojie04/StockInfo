package sj.android.stock.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import sj.android.stock.ActivityHandle;
import sj.android.stock.DataCleanManager;
import sj.android.stock.R;
import sj.android.stock.ScreenAdapter;
import sj.android.stock.ScreenManager;
import sj.android.stock.ToastManager;

/**
 * Created by Administrator on 2015/10/22.
 */
public class SettingFragment extends Fragment implements ActivityHandle {
    View root;
    View clearcache;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fg_setting, null);
        initHead();
        clearcache = root.findViewById(R.id.clearcache);
        clearcache.setOnClickListener(new MyOnClickListener());
        ScreenManager.getInstance().addActivityHandle(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ScreenManager.getInstance().removeActivityHandle(this);
    }

    private void initHead() {
        FrameLayout titlebar = (FrameLayout) root.findViewById(R.id.titlebar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
        params.height = ScreenAdapter.getInstance(getActivity()).getHeadHeight();
        titlebar.requestLayout();
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
    public boolean onBack() {
        if (isAdded()) {
            getFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clearcache:
                    DataCleanManager.cleanApplicationData(getActivity());
                    ToastManager.getManager().showToast(R.string.clear_success);
                    break;
            }
        }
    }

}
