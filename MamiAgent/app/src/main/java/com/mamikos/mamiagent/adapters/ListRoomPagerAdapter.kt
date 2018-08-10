package com.mamikos.mamiagent.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mamikos.mamiagent.fragments.RoomDataFragment

class ListRoomPagerAdapter(fm: FragmentManager?, val fragments: ArrayList<RoomDataFragment>) : FragmentPagerAdapter(fm)
{
    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

}