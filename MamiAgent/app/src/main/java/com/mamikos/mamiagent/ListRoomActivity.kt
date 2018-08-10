package com.mamikos.mamiagent

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabItem
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.github.kittinunf.fuel.core.Request
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mamikos.mamiagent.adapters.ListDataRoomAdapter
import com.mamikos.mamiagent.adapters.ListRoomPagerAdapter
import com.mamikos.mamiagent.entities.RoomEntity
import com.mamikos.mamiagent.fragments.RoomDataFragment
import com.mamikos.mamiagent.networks.apis.RoomApi
import com.mamikos.mamiagent.networks.responses.ListRoomResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.activity_list_room.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast



class ListRoomActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    override val layoutResource: Int = R.layout.activity_list_room

    companion object {
        val SETTING_LOCATION = 100
        val ROOM_EXTRA = "room"
        val SUCCESS_INPUT = "success_input"
        var currentTabSelected = 0
        var TYPE_AVAIL = "available"
        var TYPE_EDITED = "edited"
    }

    lateinit var fragmentPagerAdapter: ListRoomPagerAdapter

    private var mGoogleApiClient: GoogleApiClient? = null
    var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null

    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private var locationManager: LocationManager? = null

    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    var currentLoad = ""

    override fun viewDidLoad()
    {
        setTitle("Agen Kost Mamikos")
        tabListRoom.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                ListRoomActivity.currentTabSelected = tab.position
                vpListRoom.currentItem = tab.position
                fragmentPagerAdapter.fragments.get(tab.position).reload()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })




        if (intent.hasExtra(SUCCESS_INPUT))
            tabListRoom.getTabAt(1)?.select()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
    }

    fun setAdapter()
    {
        val availFragment = RoomDataFragment.newInstance(TYPE_AVAIL)
        val editedFragment = RoomDataFragment.newInstance(TYPE_EDITED)
        fragmentPagerAdapter = ListRoomPagerAdapter(supportFragmentManager,
                arrayListOf(availFragment, editedFragment))
        vpListRoom.adapter = fragmentPagerAdapter
        vpListRoom.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabListRoom))
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected()) {
            mGoogleApiClient!!.disconnect()
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        logIfDebug("Connection failed. Error: " + connectionResult.getErrorCode())
    }

    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showAlertLocation()
            return
        }

        if (mLocation == null) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            startLocationUpdates()
        }

        if (mLocation != null) {
            logIfDebug("ON CONECT LOC")
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SETTING_LOCATION -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        startLocationUpdates()
                    }

                } else {
                    showAlertLocation()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL).setFastestInterval(FASTEST_INTERVAL)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showAlertLocation()
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    override fun onLocationChanged(location: Location) {
        mLocation = location
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        setAdapter()
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled)
            showAlertLocation()
        return isLocationEnabled
    }

    private fun showAlertLocation() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
                .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                    ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION),
                            SETTING_LOCATION);
                }.setCancelable(false)
        dialog.show()
    }

    override fun onConnectionSuspended(p0: Int) {
        logIfDebug("Connection Suspended")
        mGoogleApiClient?.connect()
    }

    override fun onBackPressed() {
        if (isNeedToShowCloseWarning()) {
            Toast.makeText(this, R.string.msg_back_pressed, Toast.LENGTH_SHORT).show()
        }
        else
        {
            super.onBackPressed()
        }
        lastBackPressedTimeInMillis = System.currentTimeMillis()
    }
}
