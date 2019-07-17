package com.sidhiartha.libs.activities

import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.sidhiartha.libs.R
import com.sidhiartha.libs.fragments.BaseSupportFragment

/**
 * Created by sidhiartha on 23/01/18.
 */
abstract class BaseActivity : AppCompatActivity()
{
    private val doubleBackDelay = 2000

    var lastBackPressedTimeInMillis: Long = 0
    var progressBar: ProgressBar? = null
    var toolbar: Toolbar? = null

    protected abstract val layoutResource: Int

    protected abstract fun viewDidLoad()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        if (toolbar != null) setSupportActionBar(toolbar)

        if (this !is BaseDrawerActivity) viewDidLoad()
    }

    override fun onStop()
    {
        hideLoadingBar()
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        if (this is BaseDrawerActivity) return super.onOptionsItemSelected(item)

        return when (item?.itemId)
        {
            android.R.id.home, R.id.home ->
            {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showLoadingBar()
    {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideLoadingBar()
    {
        progressBar?.visibility = View.GONE
    }

    fun replaceContainerWithFragment(containerId: Int, fragment: BaseSupportFragment)
    {
        supportFragmentManager.beginTransaction().replace(containerId, fragment).commit()
    }

    fun delayedProcess(callback: () -> Unit)
    {
        Handler().postDelayed(callback, 1500)
    }

    fun isNeedToShowCloseWarning(): Boolean
    {
        return System.currentTimeMillis() - lastBackPressedTimeInMillis > doubleBackDelay
    }
}