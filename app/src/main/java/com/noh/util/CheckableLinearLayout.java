package com.noh.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    final String NS = "http://schemas.android.com/apk/res/com.huewu.example.checkable";
    final String ATTR = "checkable";

    int checkableId;
    Checkable checkable;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        checkableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
    }

    @Override
    public boolean isChecked() {
        checkable = (Checkable) findViewById(checkableId);
        if(checkable == null)
            return false;
        return checkable.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkable = (Checkable) findViewById(checkableId);
        if(checkable == null)
            return;
        checkable.setChecked(checked);
    }

    @Override
    public void toggle() {
        checkable = (Checkable) findViewById(checkableId);
        if(checkable == null)
            return;
        checkable.toggle();
    }
}


