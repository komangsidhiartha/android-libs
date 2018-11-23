package com.mamikos.mamiagent.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by root on 7/9/17.
 */

public class AnchoredImageView extends android.support.v7.widget.AppCompatImageView
{
    public AnchoredImageView(Context context) {
        super(context);
    }

    public AnchoredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnchoredImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setTranslationY(-h/2);
    }
}
