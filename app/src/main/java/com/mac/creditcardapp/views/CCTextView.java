package com.mac.creditcardapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CCTextView extends TextView {

    public CCTextView(Context context) {
        super(context);
        init(context);
    }

    public CCTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CCTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ccfont.ttf");
        this.setTypeface(myTypeface);
    }
}