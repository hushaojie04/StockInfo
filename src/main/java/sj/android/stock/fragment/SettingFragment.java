package sj.android.stock.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import sj.android.stock.DataCleanManager;
import sj.android.stock.R;
import sj.android.stock.ScreenAdapter;
import sj.android.stock.ToastManager;

/**
 * Created by Administrator on 2015/10/22.
 */
public class SettingFragment extends Fragment {
    View root;
    View clearcache;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fg_setting, null);
        initHead();
        clearcache = root.findViewById(R.id.clearcache);
        clearcache.setOnClickListener(new MyOnClickListener());
        return root;
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
        Log.d("log", "onResume " + " " + " " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("log", "onPause " + this.getClass().getSimpleName());
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
