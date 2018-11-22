package com.mamikos.mamiagent.googleapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by angga on 6/25/2015.
 */
public class DabangMapFragment extends SupportMapFragment
{
    public View mOriginalContentView;
    public TouchableWrapper mTouchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        try
        {
            mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
            mTouchView = new TouchableWrapper(getActivity());
            mTouchView.addView(mOriginalContentView);
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return mTouchView;
    }

    @Override
    public View getView()
    {
        return mOriginalContentView;
    }

    /*@Override
    public void onDestroyView()
    {
        if (getActivity() != null && !getActivity().isFinishing())
        {
            Fragment fragment = (getActivity().getSupportFragmentManager().findFragmentById(R.id.map_search_kost));
            if (fragment != null)
            {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
        super.onDestroyView();
    }*/
}
