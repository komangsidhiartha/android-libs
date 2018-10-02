package com.sidhiartha.libs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by sidhiartha on 09/02/18.
 */
abstract class BaseSupportFragment : Fragment()
{
    protected abstract val layoutResource: Int

    protected abstract fun viewDidLoad()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(layoutResource, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewDidLoad()
    }

    fun replaceContainerWithFragment(containerId: Int, fragment: BaseSupportFragment)
    {
        childFragmentManager.beginTransaction().replace(containerId, fragment).commit()
    }
}