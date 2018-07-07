package com.sidhiartha.libs.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import com.sidhiartha.libs.R

/**
 * Created by sidhiartha on 23/01/18.
 */
abstract class BaseActivity : AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var toolbar: Toolbar? = null

    protected abstract fun layoutResource(): Int

    protected abstract fun viewDidLoad()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource())
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbars)

        viewDidLoad()
    }

    override fun onStop()
    {
        hideLoadingBar()
        super.onStop()
    }

    fun showLoadingBar()
    {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideLoadingBar()
    {
        progressBar?.visibility = View.GONE
    }
}