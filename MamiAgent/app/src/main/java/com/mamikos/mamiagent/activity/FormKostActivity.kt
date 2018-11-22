package com.mamikos.mamiagent.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.mamikos.mamiagent.R
import com.sidhiartha.libs.activities.BaseActivity
import com.google.android.gms.maps.model.MarkerOptions
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.google.android.gms.common.api.GoogleApiClient
import com.mamikos.mamiagent.googleapi.TouchableWrapper
import com.mamikos.mamiagent.helpers.ReverseGeocodeTask
import kotlinx.android.synthetic.main.activity_form_kost.*
import kotlinx.android.synthetic.main.view_form_kost_step_1.*

/**
 * Created by Dedi Dot on 11/21/2018.
 * Happy Coding!
 */

class FormKostActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks,
        TouchableWrapper.TouchAction {

    private lateinit var mMap: GoogleMap
    private var googleApiClient: GoogleApiClient? = null

    override val layoutResource: Int
        get() = R.layout.activity_form_kost

    override fun viewDidLoad() {
        buildGoogleApiClient()
        setMap()
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addApi(LocationServices.API).build()
        googleApiClient?.connect()
    }

    private fun setMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapKostStep1Fragments) as SupportMapFragment
        mapFragment.getMapAsync {

            this.mMap = it

            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            } else {
                mMap.isMyLocationEnabled = true
            }

            mMap.isBuildingsEnabled = true
            mMap.isTrafficEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true

        }

        centerMarkerPinA2.setOnClickListener {

            centerMarkerPinA1.visibility = View.VISIBLE
            centerMarkerPinA2.visibility = View.GONE

            val reverse = ReverseGeocodeTask(this)

            reverse.setCallBack(object : ReverseGeocodeTask.GeoCodeTaskCallBack {
                override fun getFullAddress(address: String) {
                    UtilsHelper.log("location full $address")
                }
            })

            reverse.run(mMap.cameraPosition.target)

        }

    }

    private fun updateMarkerMyLocation() {
        UtilsHelper.makeHandler(1000, Runnable {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            } else {
                val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                mFusedLocationClient.lastLocation.addOnSuccessListener(this) {
                    val ltLng = LatLng(it.latitude, it.longitude)
                    mMap.addMarker(MarkerOptions().position(ltLng).title("Saya disini"))
                            //.isDraggable = true
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltLng, 15.0f))
                    UtilsHelper.log("my location ${it.latitude}, ${it.longitude}")
                }
            }
        })
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            updateMarkerMyLocation()
        }
    }

    override fun onConnectionSuspended(code: Int) {

    }

    override fun onTouchMove() {
        if (formKostScrollView!!.isScrollable) {
            formKostScrollView!!.setScrollingEnabled(false)
        }
    }

    override fun onTouchUp() {
        if (!formKostScrollView!!.isScrollable) {
            formKostScrollView!!.setScrollingEnabled(true)
        }
    }

    override fun onTouchDown() {
        if (formKostScrollView!!.isScrollable) {
            formKostScrollView!!.setScrollingEnabled(false)
        }
    }

    override fun onDestroy() {
        googleApiClient?.disconnect()
        super.onDestroy()
    }

}