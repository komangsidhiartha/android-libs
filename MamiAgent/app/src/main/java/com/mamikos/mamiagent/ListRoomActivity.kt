package com.mamikos.mamiagent

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mamikos.mamiagent.adapters.ListRoomPagerAdapter
import com.mamikos.mamiagent.fragments.RoomDataFragment
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.activity_list_room.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity


class ListRoomActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    override val layoutResource: Int = R.layout.activity_list_room

    companion object {
        val SETTING_LOCATION = 100
        val REQUEST_GPS_SETTINGS = 101
        val ROOM_EXTRA = "room"
        val SUCCESS_INPUT = "success_input"
        var currentTabSelected = 0
        var TYPE_AVAIL = "available"
        var TYPE_EDITED = "edited"
    }

    var fragmentPagerAdapter: ListRoomPagerAdapter? = null

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
                if (fragmentPagerAdapter != null)
                    fragmentPagerAdapter?.fragments?.get(tab.position)?.reload()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        btnStatistic.onClick { this.openStatistic() }

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()

        deleteFolderPictures()
    }

    private fun deleteFolderPictures() {//
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        storageDir.deleteRecursively()
    }

    fun setAdapter()
    {
        val availFragment = RoomDataFragment.newInstance(TYPE_AVAIL)
        val editedFragment = RoomDataFragment.newInstance(TYPE_EDITED)
        fragmentPagerAdapter = ListRoomPagerAdapter(supportFragmentManager,
                arrayListOf(availFragment, editedFragment))
        vpListRoom.adapter = fragmentPagerAdapter
        vpListRoom.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabListRoom))

        if (intent.hasExtra(SUCCESS_INPUT))
            tabListRoom.getTabAt(1)?.select()
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
                .setMessage("Untuk menggunakan App ini silahkan hidupkan GPS Anda dan beri akses untuk App ini")
                .setPositiveButton("OKE") { paramDialogInterface, paramInt ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION),
                            SETTING_LOCATION);
                    else
                        showSettingLocationDialog()
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

    private fun showSettingLocationDialog() {
        if (mLocationRequest == null)
            mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
                startLocationUpdates()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        /*Gps On, state Another High Accuracy*/
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this, REQUEST_GPS_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                        logIfDebug(e.localizedMessage)
                    } catch (e: ClassCastException) {
                        logIfDebug(e.localizedMessage)
                    }

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GPS_SETTINGS && resultCode == RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission location denied...", Toast.LENGTH_SHORT).show()
                return
            }
            startLocationUpdates()
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openStatistic()
    {
        startActivity<StatisticActivity>()
    }
}
