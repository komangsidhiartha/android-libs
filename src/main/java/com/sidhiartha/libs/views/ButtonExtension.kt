package com.sidhiartha.libs.views

import android.widget.Button
import com.sidhiartha.libs.apps.BaseApplication
import org.jetbrains.anko.onClick

fun Button.onTrackedClick(callback: () -> Unit)
{

    this.onClick {
        it as Button
        BaseApplication.instance?.sendEvent("${it.id}", "${it.text}")
        callback()
    }
}