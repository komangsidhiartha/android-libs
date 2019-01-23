package com.sidhiartha.libs.views

import android.widget.Button
import com.sidhiartha.libs.apps.BaseApplication

fun Button.onTrackedClick(callback: () -> Unit)
{
    setOnClickListener {
        it as Button
        BaseApplication.instance?.sendEvent("${it.id}", "${it.text}")
        callback()
    }
}