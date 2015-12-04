package sj.android.stock.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import sj.android.stock.R;
import sj.android.stock.ScreenAdapter;
import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/11/23.
 */
public class LoginFragment extends Fragment {
    View root;
    private EditText accountET, passwordET;
    private LinearLayout userLayout, pswLayout;
    private Button confirm;
    private int allowLoginFlag = 0x0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.user_login, null);
        accountET = (EditText) root.findViewById(R.id.edittext1);
        passwordET = (EditText) root.findViewById(R.id.edittext2);
        accountET.setOnFocusChangeListener(new MyOnFocusChangeListener());
        accountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    allowLoginFlag |= 0x01;
                } else {
                    allowLoginFlag &= ~0x01;
                }
                if ((allowLoginFlag & 0x11) == 0x11) {
                    confirm.setEnabled(true);
                } else {
                    confirm.setEnabled(false);
                }
            }
        });
        passwordET.setOnFocusChangeListener(new MyOnFocusChangeListener());
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5) {
                    allowLoginFlag |= 0x10;
                } else {
                    allowLoginFlag &= ~0x10;
                }
                if ((allowLoginFlag & 0x11) == 0x11) {
                    confirm.setEnabled(true);
                } else {
                    confirm.setEnabled(false);
                }
            }
        });
        userLayout = (LinearLayout) root.findViewById(R.id.login_user);
        pswLayout = (LinearLayout) root.findViewById(R.id.login_password);
        confirm = (Button) root.findViewById(R.id.confirm);
        initHead();
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
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    class MyOnFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.edittext1:
                    LogUtils.D("userLayout " + hasFocus);
                    userLayout.setSelected(hasFocus);
                    break;
                case R.id.edittext2:
                    LogUtils.D("pswLayout " + hasFocus);
                    pswLayout.setSelected(hasFocus);
                    break;
            }
        }
    }

}
