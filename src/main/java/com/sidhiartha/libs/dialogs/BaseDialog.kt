package com.sidhiartha.libs.dialogs

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by sidhiartha on 12/02/18.
 */
abstract class BaseDialog : DialogFragment() {
    protected abstract fun layoutResource(): Int

    protected abstract fun viewDidLoad()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(layoutResource(), container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDidLoad()
    }
}