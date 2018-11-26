package com.mamikos.mamiagent.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ScrollView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.mamikos.mamiagent.R
import com.sidhiartha.libs.activities.BaseActivity
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.mamikos.mamiagent.entities.AreaEntity
import com.mamikos.mamiagent.googleapi.TouchableWrapper
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import com.mamikos.mamiagent.networks.apis.LocationApi
import com.mamikos.mamiagent.networks.responses.AreaResponse
import com.mamikos.mamiagent.views.CustomLoadingView
import kotlinx.android.synthetic.main.activity_form_kost.*
import kotlinx.android.synthetic.main.view_form_kost_step_1.*
import kotlinx.android.synthetic.main.view_form_kost_step_1.view.*
import org.jetbrains.anko.toast

import android.support.annotation.NonNull
import android.widget.Toast
import com.mamikos.mamiagent.helpers.*
import kotlinx.android.synthetic.main.view_form_kost_step_3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File

/**
 * Created by Dedi Dot on 11/21/2018.
 * Happy Coding!
 */

class FormKostActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks,
        TouchableWrapper.TouchAction {

    private lateinit var mMap: GoogleMap
    private lateinit var loading: CustomLoadingView
    private var googleApiClient: GoogleApiClient? = null

    override val layoutResource: Int
        get() = R.layout.activity_form_kost

    override fun viewDidLoad() {

        EventBus.getDefault().register(this)

        UtilsPermission.checkPermissionGps(this)

        loading = CustomLoadingView(this)
        loading.show()

        setLayoutNextBack()

        requestProvinceApi()

        setClickCameraGallery()

    }

    private fun setClickCameraGallery() {

    }

    private fun requestProvinceApi() {
        val provinceApi = LocationApi.ProvinceApi()
        provinceApi.exec(AreaResponse::class.java) { response: AreaResponse?, errorMessage: String? ->
            when (response) {
                null -> errorMessage?.let {
                    toast(it)
                }
                else -> {
                    if (response.status) {
                        formKostStep1View.setProvince(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                loading.show()
                                formKostStep1View.provinceSpinnerCustomView.setName("${data.name}")
                                requestCityApi(data.id)

                                formKostStep1View.citySpinnerCustomView.setName("")
                                formKostStep1View.citySpinnerCustomView.setData(null)
                                formKostStep1View.districtSpinnerCustomView.setName("")
                                formKostStep1View.districtSpinnerCustomView.setData(null)

                            }
                        })
                    }
                }
            }
            formKostStep1View.provinceSpinnerCustomView.setName("")
            loading.hide()
        }
    }

    private fun requestCityApi(cityId: Int) {
        val cityApi = LocationApi.CityApi(cityId.toString())
        cityApi.exec(AreaResponse::class.java) { response: AreaResponse?, errorMessage: String? ->
            when (response) {
                null -> errorMessage?.let {
                    toast(it)
                }
                else -> {
                    if (response.status) {
                        formKostStep1View.setCity(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                loading.show()
                                requestSubDistrictApi(data.id)
                                formKostStep1View.citySpinnerCustomView.setName("${data.name}")
                            }
                        })
                    }
                }
            }
            formKostStep1View.citySpinnerCustomView.setName("")
            formKostStep1View.districtSpinnerCustomView.setName("")
            loading.hide()
        }
    }

    private fun requestSubDistrictApi(subDistrictId: Int) {
        val subDistrictApi = LocationApi.SubDistrictApi(subDistrictId.toString())
        subDistrictApi.exec(AreaResponse::class.java) { response: AreaResponse?, errorMessage: String? ->
            when (response) {
                null -> errorMessage?.let {
                    toast(it)
                }
                else -> {
                    if (response.status) {
                        formKostStep1View.setSubdistrict(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                formKostStep1View.districtSpinnerCustomView.setName("${data.name}")
                            }
                        })
                    }
                }
            }
            formKostStep1View.districtSpinnerCustomView.setName("")
            loading.hide()
        }
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

            mMap.setOnCameraMoveListener {
                centerMarkerPinA1.visibility = View.GONE
                centerMarkerPinA2.visibility = View.VISIBLE
            }

        }

        centerMarkerPinA2.setOnClickListener {

            centerMarkerPinA1.visibility = View.VISIBLE
            centerMarkerPinA2.visibility = View.GONE

            val reverseGeoCode = ReverseGeocodeTask(this)
            reverseGeoCode.setCallBack(object : ReverseGeocodeTask.GeoCodeTaskCallBack {
                override fun getFullAddress(address: String, latLng: LatLng) {
                    UtilsHelper.log("location full $address $latLng")
                    locationEditText.setText(address)
                    locationEditText.isFocusable = true
                    locationEditText.isFocusableInTouchMode = true
                }
            })

            reverseGeoCode.run(mMap.cameraPosition.target)

        }

        locationEditText.setOnClickListener {
            formKostScrollView.post {
                formKostScrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
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
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun setLayoutNextBack() {
        formKostStep1View.setNextOnClick(Runnable {
            formKostStep1View.visibility = View.GONE
            formKostStep2View.visibility = View.VISIBLE
            scrollUp()

            imageOneView.setImageResource(R.drawable.ic_check_circle_done)
            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_circle_undone)
            lineOneView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

        })

        formKostStep1View.setBackOnClick(Runnable {
            UtilsHelper.showDialogYesNo(this, "", getString(R.string.msg_exit), Runnable {
                finish()
            }, 0)
        })

        formKostStep2View.setNextOnClick(Runnable {
            formKostStep2View.visibility = View.GONE
            formKostStep3View.visibility = View.VISIBLE
            scrollUp()

            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_check_circle_done)
            imageFourView.setImageResource(R.drawable.ic_circle_undone)
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

        })

        formKostStep2View.setBackOnClick(Runnable {
            formKostStep1View.visibility = View.VISIBLE
            formKostStep2View.visibility = View.GONE
            scrollUp()

            imageOneView.setImageResource(R.drawable.ic_check_circle_done)
            imageTwoView.setImageResource(R.drawable.ic_circle_undone)
            lineOneView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.alto_solid))
            imageThreeView.setImageResource(R.drawable.ic_circle_grey)
        })

        formKostStep3View.setNextOnClick(Runnable {
            formKostStep3View.visibility = View.GONE
            formKostStep4View.visibility = View.VISIBLE
            scrollUp()

            imageThreeView.setImageResource(R.drawable.ic_check_circle_done)
            imageFourView.setImageResource(R.drawable.ic_check_circle_done)
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

        })

        formKostStep3View.setBackOnClick(Runnable {
            formKostStep3View.visibility = View.GONE
            formKostStep2View.visibility = View.VISIBLE
            scrollUp()

            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_circle_undone)
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.alto_solid))
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            imageFourView.setImageResource(R.drawable.ic_circle_grey)

        })

        formKostStep4View.setNextOnClick(Runnable {
            UtilsHelper.showDialogYesNo(this, "", getString(R.string.msg_data_confirmation), Runnable {
                finish()
            }, 0)
        })

        formKostStep4View.setBackOnClick(Runnable {
            formKostStep4View.visibility = View.GONE
            formKostStep3View.visibility = View.VISIBLE
            scrollUp()

            imageThreeView.setImageResource(R.drawable.ic_check_circle_done)
            imageFourView.setImageResource(R.drawable.ic_circle_undone)
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.apptheme_color))
        })
    }

    private fun scrollUp() {
        formKostScrollView.post {
            formKostScrollView.fullScroll(ScrollView.FOCUS_UP)
        }
    }

    override fun onBackPressed() {
        UtilsHelper.showDialogYesNo(this, "", getString(R.string.msg_exit), Runnable {
            super.onBackPressed()
        }, 0)
    }


    override fun onRequestPermissionsResult(
            requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val booleanPermission = arrayListOf<Boolean>()
        for (i in permissions.indices) {
            if (requestCode == UtilsPermission.PERMISSION_GPS) {
                if (permissions[i] == Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    booleanPermission.add(true)
                } else if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    booleanPermission.add(true)
                }
                if (booleanPermission.size >= 1) {
                    buildGoogleApiClient()
                    setMap()
                    UtilsPermission.checkPermissionStorageAndCamera(this)
                } else {
                    Toast.makeText(this, "Fitur yang di minta tidak dizinkan", Toast.LENGTH_LONG)
                            .show()
                    finish()
                }
            } else if (requestCode == UtilsPermission.PERMISSION_STORAGE_AND_CAMERA) {
                if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    booleanPermission.add(true)
                } else if (permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    booleanPermission.add(true)
                } else if (permissions[i] == Manifest.permission.CAMERA && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    booleanPermission.add(true)
                }
            }
        }

    }

    @Subscribe
    fun onEvent(bundle: Bundle) {
        UtilsHelper.log("afaas asfas asf $bundle")

        pathCamera = bundle.getString("path")
    }

    var pathCamera = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        UtilsHelper.log("requestCode $requestCode")
        UtilsHelper.log("resultCode $resultCode")
        UtilsHelper.log("data " + data)
        UtilsHelper.log("datacuik " + data?.flags)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == ShowCamera.CODE_CAMERA) {

                UtilsHelper.log("path CAMERA: " + pathCamera)

                successGetImage(Uri.parse(pathCamera))

            } else if (requestCode == ShowGallery.CODE_GALLERY) {

                if (data == null) {
                    return
                }

                UtilsHelper.log("GALLERY " + UtilsHelper.getPathFromURI(this, data.data) + "\n" + data.data)

                successGetImage(data.data)

            }
        }

    }

    private fun successGetImage(uri: Uri) {

        val file = File(UtilsHelper.getPathFromURI(this, uri))

        val options = BitmapFactory.Options()
        options.inSampleSize = 4

        val path = file.path
        val bitmap = BitmapFactory.decodeFile(path, options)

        photoBathroomImageView.setImageBitmap(bitmap)

    }
}