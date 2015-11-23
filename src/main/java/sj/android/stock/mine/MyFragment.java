package sj.android.stock.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sj.android.stock.MainActivity;
import sj.android.stock.R;
import sj.android.stock.fragment.LoginFragment;

/**
 * Created by Administrator on 2015/10/22.
 */
public class MyFragment extends Fragment {
    LinearLayout root;
    private int[] resimage = {R.drawable.icon_zhanghaoxinxi, R.drawable.icon_wodeguanzhu, R.drawable.icon_shoucang,
            R.drawable.icon_yijianfankui, R.drawable.icon_shezhi};
    private int[] restitle = {R.string.account_info, R.string.mywatchlist, R.string.favourites,
            R.string.feedback, R.string.setting};
    private int[] resid = {R.id.accountinfo, R.id.mywatchlist, R.id.favourites,
            R.id.feedback, R.id.setting};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (LinearLayout) inflater.inflate(R.layout.fg_mine, null);
        for (int i = 0; i < resimage.length; i++) {
            root.addView(newItem(resimage[i], restitle[i], resid[i]));
        }
        activity = ((MainActivity) getActivity());
        return root;
    }

    MainActivity activity;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("log", "onResume " + " " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("log", "onPause " + this.getClass().getSimpleName());
    }

    private View newItem(int image, int title, int id) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View parent = inflater.inflate(R.layout.mine_item, null);
        ImageView imageView = (ImageView) parent.findViewById(R.id.icon);
        imageView.setImageResource(image);
        TextView textView = (TextView) parent.findViewById(R.id.title);
        textView.setText(getResources().getText(title));
        parent.setId(id);
        parent.setOnClickListener(new MyOnClickListener());
        return parent;
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.accountinfo:
                    activity.showFg(new LoginFragment());
                    break;
            }
        }
    }

}
