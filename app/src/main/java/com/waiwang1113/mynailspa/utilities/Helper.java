package com.waiwang1113.mynailspa.utilities;

import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Weige on 2/12/17.
 */

public class Helper {
    public static void setTextViewValue(int id, ViewGroup pararentView, CharSequence text){
        TextView tv = (TextView) pararentView.findViewById(id);
        tv.setText(text);
    }
}
