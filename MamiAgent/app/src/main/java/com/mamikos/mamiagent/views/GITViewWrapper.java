package com.mamikos.mamiagent.views;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

/**
 * Created by sidhi on 31/08/15.
 */
public class GITViewWrapper<V extends FrameLayout> extends RecyclerView.ViewHolder
{
    private V view;

    public GITViewWrapper(V view)
    {
        super(view);

        this.view = view;
    }

    public V getView()
    {
        return view;
    }
}
