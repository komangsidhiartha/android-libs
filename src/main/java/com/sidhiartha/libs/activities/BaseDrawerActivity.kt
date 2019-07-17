package com.sidhiartha.libs.activities

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.sidhiartha.libs.R

abstract class BaseDrawerActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener
{
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null

    protected abstract val menuItemClickListener: (MenuItem) -> Unit
    protected abstract val userName: String?
    protected abstract val userEmail: String?

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        navigationView?.setNavigationItemSelectedListener(this)

        val headerLayout = navigationView?.getHeaderView(0)
        val nameLabel = headerLayout?.findViewById<TextView>(R.id.nav_header_name_label)
        val emailLabel = headerLayout?.findViewById<TextView>(R.id.nav_header_email_label)

        if (userName != null) nameLabel?.text = userName
        if (userEmail != null) emailLabel?.text = userEmail

        viewDidLoad()
    }

    override fun onBackPressed()
    {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START) == true)
        {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else
        {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        menuItemClickListener(item)
        return true
    }
}