package com.sidhiartha.libs.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by sidhiartha on 23/01/18.
 */
abstract class BaseActivity : AppCompatActivity() {
    protected abstract fun layoutResource(): Int

    protected abstract fun viewDidLoad()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource())

        viewDidLoad()
    }
}