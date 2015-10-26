package sj.android.stock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/10/26.
 */
public class TabItemView extends TextView {
    static int _id = 0;

    public TabItemView(Context context) {
        this(context, null);
    }

    public TabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText("TabItemView " + _id++);
        setPadding(20,0,20,0);
    }
}
