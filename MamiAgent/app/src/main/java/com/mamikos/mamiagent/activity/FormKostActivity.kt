package com.mamikos.mamiagent.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ScrollView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.mamikos.mamiagent.R
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.mamikos.mamiagent.entities.AreaEntity
import com.mamikos.mamiagent.googleapi.TouchableWrapper
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import com.mamikos.mamiagent.networks.apis.LocationApi
import com.mamikos.mamiagent.networks.responses.AreaResponse
import kotlinx.android.synthetic.main.activity_form_kost.*
import kotlinx.android.synthetic.main.view_form_kost_location.*
import kotlinx.android.synthetic.main.view_form_kost_location.view.*
import org.jetbrains.anko.toast

import android.support.annotation.NonNull
import android.widget.Toast
import com.git.dabang.database.table.FormDataTable
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.entities.PhotoFormEntity
import com.mamikos.mamiagent.entities.SaveKostEntity
import com.mamikos.mamiagent.googleapi.DabangMapFragment
import com.mamikos.mamiagent.helpers.*
import com.mamikos.mamiagent.networks.apis.PhotosApi
import com.mamikos.mamiagent.networks.apis.SaveKosApi
import com.mamikos.mamiagent.networks.apis.TelegramApi
import com.mamikos.mamiagent.networks.responses.MediaResponse
import com.mamikos.mamiagent.networks.responses.MessagesResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.utils.GSONManager
import kotlinx.android.synthetic.main.view_form_data_kost.view.*
import kotlinx.android.synthetic.main.view_form_kost_facility.*
import kotlinx.android.synthetic.main.view_form_kost_facility.view.*
import kotlinx.android.synthetic.main.view_form_kost_owner.view.*
import kotlinx.android.synthetic.main.view_grey_square.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File

/**
 * Created by Dedi Dot on 11/21/2018.
 * Happy Coding!
 */

class FormKostActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks,
        TouchableWrapper.TouchAction {

    private var mMap: GoogleMap? = null
    private var googleApiClient: GoogleApiClient? = null
    private var fullAddressLatLng: LatLng? = null
    private var myLatLng: LatLng? = null
    var photoKosBuildingId = 0
    var photoBathroomBuildingId = 0
    var photoInsideBuildingId = 0
    private var photoKosBuildingDao = ""
    private var photoBathroomBuildingDao = ""
    private var photoInsideBuildingDao = ""

    override val layoutResource: Int = R.layout.activity_form_kost

    override fun viewDidLoad() {

        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))

        if (checkError()) {
            return
        }

        setContentView(R.layout.activity_form_kost)

        EventBus.getDefault().register(this)

        UtilsPermission.checkPermissionGps(this)

        UtilsPermission.checkPermissionStorageAndCamera(this)

        setLayoutNextBack()

        requestProvinceApi()

        buildGoogleApiClient()

        setMap()

        titleLocalDataTextView.setOnClickListener {
            val intent = Intent(this, ListDataFormActivity::class.java)
            startActivity(intent)
        }

        titleHistoryDataTextView.setOnClickListener {
            val intent = Intent(this, ListAgentHistoryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkError(): Boolean {
        if (intent != null) {
            if (intent.getStringExtra("error") != null && intent.getStringExtra("error").isNotEmpty()) {
                sendReport(intent.getStringExtra("error"))
                UtilsHelper.showDialogYes(this, "", intent.getStringExtra("error"), Runnable {
                    val intents = Intent(this, FormKostActivity::class.java)
                    intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intents)
                    android.os.Process.killProcess(android.os.Process.myPid())
                    System.exit(10)
                }, 0)
                return true
            }
        }
        return false
    }

    private fun sendReport(s: String) {
        val reportApi = TelegramApi.SendReport(s)
        reportApi.exec(MessagesResponse::class.java) { _: MessagesResponse?, _: String? -> }
    }

    private fun requestProvinceApi() {
        val provinceApi = LocationApi.ProvinceApi()
        provinceApi.exec(AreaResponse::class.java) { response: AreaResponse?, errorMessage: String? ->
            when (response) {
                null -> errorMessage?.let {
                    toast(it)
                    requestProvinceApi()
                }
                else -> {
                    if (response.status) {
                        formKostLocationView.setProvince(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                loading?.show()
                                formKostLocationView.provinceSpinnerCustomView.setName("${data.name}")
                                requestCityApi(data.id)

                                formKostLocationView.citySpinnerCustomView.setName("")
                                formKostLocationView.citySpinnerCustomView.setData(null)
                                formKostLocationView.districtSpinnerCustomView.setName("")
                                formKostLocationView.districtSpinnerCustomView.setData(null)

                            }
                        })
                    } else {
                        requestProvinceApi()
                    }
                }
            }
            formKostLocationView.provinceSpinnerCustomView.setName("")
            loading?.hide()
        }
    }

    private fun requestCityApi(cityId: Int) {
        val cityApi = LocationApi.CityApi(cityId.toString())
        cityApi.exec(AreaResponse::class.java) { response: AreaResponse?, errorMessage: String? ->
            when (response) {
                null -> errorMessage?.let {
                    toast(it)
                    requestCityApi(cityId)
                }
                else -> {
                    if (response.status) {
                        formKostLocationView.setCity(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                loading?.show()
                                requestSubDistrictApi(data.id)
                                formKostLocationView.citySpinnerCustomView.setName("${data.name}")
                            }
                        })
                    } else {
                        requestCityApi(cityId)
                    }
                }
            }
            formKostLocationView.citySpinnerCustomView.setName("")
            formKostLocationView.districtSpinnerCustomView.setName("")
            loading?.hide()
        }
    }

    private fun requestSubDistrictApi(subDistrictId: Int) {
        val subDistrictApi = LocationApi.SubDistrictApi(subDistrictId.toString())
        subDistrictApi.exec(AreaResponse::class.java) { response: AreaResponse?, errorMessage: String? ->
            when (response) {
                null -> errorMessage?.let {
                    toast(it)
                    requestSubDistrictApi(subDistrictId)
                }
                else -> {
                    if (response.status) {
                        formKostLocationView.setSubdistrict(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                formKostLocationView.districtSpinnerCustomView.setName("${data.name}")
                            }
                        })
                    } else {
                        requestSubDistrictApi(subDistrictId)
                    }
                }
            }
            formKostLocationView.districtSpinnerCustomView.setName("")
            loading?.hide()
        }
    }

    @Synchronized private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addApi(LocationServices.API).build()
        googleApiClient?.connect()
    }

    private fun setMap() {

        val fragment = DabangMapFragment()
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.mapKostStep1FrameLayout, fragment)
        ft.commit()
        fragment.getMapAsync {

            this.mMap = it

            UtilsHelper.log("cek kesini5")

            mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            } else {
                mMap?.isMyLocationEnabled = true
            }

            mMap?.isBuildingsEnabled = true
            mMap?.isTrafficEnabled = true
            mMap?.uiSettings?.isZoomControlsEnabled = true
            mMap?.uiSettings?.isMyLocationButtonEnabled = true

            mMap?.setOnCameraMoveListener {
                centerMarkerPinA1.visibility = View.GONE
                centerMarkerPinA2.visibility = View.VISIBLE
                UtilsHelper.log("cek kesini6")
            }

        }

        centerMarkerPinA2.setOnClickListener {

            centerMarkerPinA1.visibility = View.VISIBLE
            centerMarkerPinA2.visibility = View.GONE

            val reverseGeoCode = ReverseGeocodeTask(this)
            reverseGeoCode.setCallBack(object : ReverseGeocodeTask.GeoCodeTaskCallBack {
                override fun getFullAddress(address: String, latLng: LatLng) {
                    UtilsHelper.log("cek kesini9")
                    UtilsHelper.log("location full $address $latLng")
                    locationEditText.setText(address)
                    locationEditText.isFocusable = true
                    locationEditText.isFocusableInTouchMode = true
                    fullAddressLatLng = latLng
                }
            })

            val cp = mMap?.cameraPosition
            cp?.let { cm ->
                reverseGeoCode.run(cm.target)
            }
            UtilsHelper.log("cek kesini10")

        }

        locationEditText.setOnClickListener {
            formKostScrollView.post {
                formKostScrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }

    }

    private fun updateMarkerMyLocation() {
        UtilsHelper.makeHandler(1000, Runnable {
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    UtilsHelper.log("cek kesini3")
                } else {
                    UtilsHelper.log("cek kesini4")
                    val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    mFusedLocationClient.lastLocation.addOnSuccessListener(this) {
                        try {
                            val ltLng = LatLng(it.latitude, it.longitude)
                            myLatLng = ltLng
                            mMap?.addMarker(MarkerOptions().position(ltLng).title("Saya disini"))
                            //.isDraggable = true
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(ltLng, 15.0f))
                            UtilsHelper.log("my location ${it.latitude}, ${it.longitude}")
                        } catch (e: Exception) {
                            sendReport(e.toString())
                            MamiApp.instance?.sendEvent("XXxs", e.toString())
                            return@addOnSuccessListener
                        }
                    }
                }
            } catch (e: Exception) {
                sendReport(e.toString())
                MamiApp.instance?.sendEvent("errorAAAB", e.toString())
                return@Runnable
            }
        })
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            UtilsHelper.log("cek kesini 1")
        } else {
            UtilsHelper.log("cek kesini 2")
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

        formKostOwnerView.setNextOnClick(Runnable {
            formKostOwnerView.visibility = View.GONE
            formKostLocationView.visibility = View.VISIBLE
            scrollUp()

            imageOneView.setImageResource(R.drawable.ic_check_circle_done)
            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_circle_undone)
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

            UtilsHelper.hideSoftInput(this)

        })

        formKostLocationView.setNextOnClick(Runnable {
            formKostLocationView.visibility = View.GONE
            formDataKostView.visibility = View.VISIBLE
            scrollUp()

            imageOneView.setImageResource(R.drawable.ic_check_circle_done)
            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_check_circle_done)
            imageFourView.setImageResource(R.drawable.ic_circle_undone)
            lineOneView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

            UtilsHelper.hideSoftInput(this)

        })

        formDataKostView.setNextOnClick(Runnable {
            formDataKostView.visibility = View.GONE
            formKostFacilityView.visibility = View.VISIBLE
            scrollUp()

            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_check_circle_done)
            imageFourView.setImageResource(R.drawable.ic_check_circle_done)
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

            UtilsHelper.hideSoftInput(this)

        })

        formKostFacilityView.setNextOnClick(Runnable {
            UtilsHelper.showDialogYesNoCustomString(this, "", getString(R.string.msg_data_confirmation), getString(R.string.msg_yes), getString(R.string.msg_later), object :
                    OnClickInterfaceObject<Int> {
                override fun dataClicked(data: Int) {
                    UtilsHelper.hideSoftInput(this@FormKostActivity)
                    goSaveKos(data)
                }
            }, 0)
        })



        formKostOwnerView.setBackOnClick(Runnable {
            UtilsHelper.showDialogYesNo(this, "", getString(R.string.msg_exit), Runnable {
                UtilsHelper.hideSoftInput(this)
                onBackPressed()
            }, 0)
        })

        formKostLocationView.setBackOnClick(Runnable {
            formKostOwnerView.visibility = View.VISIBLE
            formKostLocationView.visibility = View.GONE
            scrollUp()

            imageOneView.setImageResource(R.drawable.ic_check_circle_done)
            imageTwoView.setImageResource(R.drawable.ic_circle_undone)
            imageThreeView.setImageResource(R.drawable.ic_circle_grey)
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.alto_solid))
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.alto_solid))

            UtilsHelper.hideSoftInput(this)

        })

        formDataKostView.setBackOnClick(Runnable {
            formKostLocationView.visibility = View.VISIBLE
            formDataKostView.visibility = View.GONE
            scrollUp()

            imageOneView.setImageResource(R.drawable.ic_check_circle_done)
            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_circle_undone)
            lineOneView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.alto_solid))
            imageFourView.setImageResource(R.drawable.ic_circle_grey)

            UtilsHelper.hideSoftInput(this)
        })

        formKostFacilityView.setBackOnClick(Runnable {
            formKostFacilityView.visibility = View.GONE
            formDataKostView.visibility = View.VISIBLE
            scrollUp()

            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_check_circle_done)
            imageFourView.setImageResource(R.drawable.ic_circle_undone)
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

            UtilsHelper.hideSoftInput(this)

        })

    }

    private fun goSaveKos(code: Int) {

        try {

            if (fullAddressLatLng == null) {
                UtilsHelper.showSnackbar(contentSquareConstraintLayout, "titik lokasi kosong, mohon pilih kembali titik lokasi pada peta")
                return
            }

            val saveKos = SaveKostEntity()

            saveKos.province = formKostLocationView.provinceSpinnerCustomView.getName()
            saveKos.areaCity = formKostLocationView.citySpinnerCustomView.getName()
            saveKos.city = formKostLocationView.citySpinnerCustomView.getName()
            saveKos.subdistrict = formKostLocationView.districtSpinnerCustomView.getName()
            saveKos.latitude = fullAddressLatLng?.latitude!!
            saveKos.longitude = fullAddressLatLng?.longitude!!
            saveKos.agentLat = myLatLng?.latitude!!
            saveKos.agentLong = myLatLng?.longitude!!
            saveKos.address = formKostLocationView.fullAddressEditText.text.toString()

            saveKos.name = formDataKostView.kosNameEditText.text.toString()
            saveKos.gender = formDataKostView.typeGender.toInt()

            val roomSize: ArrayList<String> = arrayListOf()
            roomSize.add(formDataKostView.roomSize.split(",")[0])
            roomSize.add(formDataKostView.roomSize.split(",")[1])
            saveKos.roomSize = roomSize

            saveKos.roomCount = formDataKostView.roomTotalEditText.text.toString().toInt()
            saveKos.roomAvailable = formDataKostView.roomTotalNowEditText.text.toString().toInt()
            if (!formDataKostView.dayPayEditText.text.isEmpty()) {
                saveKos.priceDaily = formDataKostView.dayPayEditText.text.toString()
                        .replace(".", "").toInt()
            }
            if (!formDataKostView.weekPayEditText.text.isEmpty()) {
                saveKos.priceWeekly = formDataKostView.weekPayEditText.text.toString()
                        .replace(".", "").toInt()
            }
            if (!formDataKostView.monthPayEditText.text.isEmpty()) {
                saveKos.priceMonthly = formDataKostView.monthPayEditText.text.toString()
                        .replace(".", "").toInt()
            }
            if (!formDataKostView.yearPayEditText.text.isEmpty()) {
                saveKos.priceYearly = formDataKostView.yearPayEditText.text.toString()
                        .replace(".", "").toLong()
            }

            saveKos.minMonth = formDataKostView.minPaySelected.toInt()

            val selectedFacRoom = arrayListOf<Int>()
            val selectedFacBathRoom = arrayListOf<Int>()

            if (formKostFacilityView.mattressSquareGreyView.isChecked) {
                selectedFacRoom.add(10)
            }
            if (formKostFacilityView.cupboardSquareGreyView.isChecked) {
                selectedFacRoom.add(11)
            }
            if (formKostFacilityView.tableSquareGreyView.isChecked) {
                selectedFacRoom.add(14)
            }
            if (formKostFacilityView.chairSquareGreyView.isChecked) {
                selectedFacRoom.add(17)
            }
            if (formKostFacilityView.acSquareGreyView.isChecked) {
                selectedFacRoom.add(13)
            }
            if (formKostFacilityView.tvSquareGreyView.isChecked) {
                selectedFacRoom.add(12)
            }
            if (formKostFacilityView.fanSquareGreyView.isChecked) {
                selectedFacRoom.add(58)
            }
            if (formKostFacilityView.facRoom == "0") {
                selectedFacRoom.add(62)
            }

            if (!formKostFacilityView.speedTestEditText.text.isEmpty()) {
                saveKos.wifiSpeed = formKostFacilityView.speedTestEditText.text.toString()
                selectedFacRoom.add(15)
            }

            if (formKostFacilityView.twentyFourSquareGreyView.isChecked) {
                selectedFacRoom.add(59)
            }

            if (formKostFacilityView.coupleSquareGreyView.isChecked) {
                selectedFacRoom.add(60)
            }

            if (formKostFacilityView.parkSquareGreyView.isChecked) {
                selectedFacRoom.add(22)
            }

            saveKos.facRoom = selectedFacRoom

            if (formKostFacilityView.showerSquareGreyView.isChecked) {
                selectedFacRoom.add(3)
            }
            if (formKostFacilityView.toiletSeatSquareGreyView.isChecked) {
                selectedFacRoom.add(2)
            }
            if (formKostFacilityView.squatToiletSquareGreyView.isChecked) {
                selectedFacRoom.add(5)
            }
            if (formKostFacilityView.hotWaterSquareGreyView.isChecked) {
                selectedFacRoom.add(8)
            }

            saveKos.photos = PhotoFormEntity()

            if (photoBathroomBuildingId > 0) {
                saveKos.photos.bath = arrayListOf()
                saveKos.photos.bath!!.add(photoBathroomBuildingId)
            }
            if (photoInsideBuildingId > 0) {
                saveKos.photos.mains = photoInsideBuildingId
            }
            if (photoKosBuildingId > 0) {
                saveKos.photos.cover = photoKosBuildingId
            }

            if (formKostFacilityView.facBathRoom == "0") {
                selectedFacBathRoom.add(1)
            } else {
                selectedFacBathRoom.add(4)
            }

            saveKos.facBath = selectedFacBathRoom

            if (formDataKostView.isElectricity == "0") {
                saveKos.withListrik = 1
                saveKos.withoutListrik = 0
            } else {
                saveKos.withListrik = 0
                saveKos.withoutListrik = 1
            }

            saveKos.ownerName = formKostOwnerView.ownerNameEditText.text.toString()
            saveKos.ownerEmail = formKostOwnerView.ownerEmailEditText.text.toString()
            saveKos.ownerPhone = formKostOwnerView.ownerPhoneEditText.text.toString()
            //saveKos.password = formKostOwnerView.ownerPasswordEditText.text.toString()
            saveKos.inputAs = "agen"

            if (code == 1) {
                val apiSave = SaveKosApi.SaveKost()
                loading?.show()
                apiSave.postParam = GSONManager.toJson(saveKos)
                UtilsHelper.log("wooooo " + apiSave.postParam)

                apiSave.exec(MessagesResponse::class.java) { response: MessagesResponse?, errorMessage: String? ->

                    loading?.hide()
                    var msg = ""

                    if (!UtilsHelper.isNetworkConnected(this@FormKostActivity)) {
                        UtilsHelper.showDialogYes(this, "", "Koneksi tidak stabil, simpan dilokal dulu ya?", Runnable {
                            SavingDataLocal().execute(saveKos)
                        }, 0)
                        return@exec
                    }

                    if (response == null) {
                        sendReport(errorMessage.toString())
                        UtilsHelper.showDialogYesNoCustomString(this, "", "Server lagi error, hubungi developer", "Coba lagi", "Iya", object :
                                OnClickInterfaceObject<Int> {
                            override fun dataClicked(data: Int) {
                                if (data == 1) {
                                    goSaveKos(1)
                                }
                            }
                        }, 0)
                        return@exec
                    }

                    if (response.status) {
                        msg = "Berhasil tambah kos, bersihkan form?"
                        UtilsHelper.showDialogYesNo(this, "", msg, Runnable {
                            val intent = Intent(this, FormKostActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }, 0)
                    } else {
                        response.messages?.size?.let {
                            for (i in 0 until it) {
                                msg += response.messages[i] + " "
                            }
                        }
                        UtilsHelper.showDialogYes(this, "", "$msg gagal kirim", Runnable {}, 0)
                    }

                }
            } else {
                UtilsHelper.log(GSONManager.toJson(saveKos))
                SavingDataLocal().execute(saveKos)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            MamiApp.instance?.sendEvent("errorAAA", e.toString())
            loading?.hide()
            sendReport(e.toString())
        }
    }

    private fun scrollUp() {
        formKostScrollView.post {
            formKostScrollView.fullScroll(ScrollView.FOCUS_UP)
        }
    }

    override fun onBackPressed() {
        UtilsHelper.showDialogYesNo(this, "", getString(R.string.msg_exit), Runnable {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            setResult(0, intent)
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(10)
            return@Runnable
        }, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
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

    @Subscribe fun onEvent(bundle: Bundle) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        UtilsHelper.log("requestCode $requestCode")
        UtilsHelper.log("resultCode $resultCode")
        UtilsHelper.log("data " + data)

        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == GlobalConst.CODE_CAMERA_BATHROOM || requestCode == GlobalConst.CODE_CAMERA_INSIDEROOM || requestCode == GlobalConst.CODE_CAMERA_BUILDING) {
                    try {
                        successGetImage(Uri.parse(MamiApp.sessionManager.pathCamera), requestCode)
                    } catch (e: Exception) {
                        toast("Gagal mengambil kamera")
                        e.printStackTrace()
                        sendReport(e.toString())
                        return
                    }
                } else if (requestCode == GlobalConst.CODE_GALLERY_BATHROOM || requestCode == GlobalConst.CODE_GALLERY_INSIDEROOM || requestCode == GlobalConst.CODE_GALLERY_BUILDING) {
                    if (data == null) {
                        return
                    }
                    successGetImage(data.data, requestCode)
                }
            }
        } catch (e: Exception) {
            MamiApp.instance?.sendEvent("errorXXX", e.toString())
            e.printStackTrace()
            sendReport(e.toString())
            return
        }

    }

    private fun successGetImage(uri: Uri?, requestCode: Int) {

        try {
            if (uri != null) {

                UtilsHelper.log("cek1 ${uri.path}")

                val file: File

                if (requestCode == GlobalConst.CODE_GALLERY_BATHROOM || requestCode == GlobalConst.CODE_GALLERY_INSIDEROOM || requestCode == GlobalConst.CODE_GALLERY_BUILDING) {
                    file = File(MediaHelper.setImageResourceFromGallery(this, uri))
                } else {
                    file = File(uri.path)
                }

                UtilsHelper.log("cek2 ${file.path}")

                uploadImage(file, requestCode, object : OnClickInterfaceObject<Int> {
                    override fun dataClicked(data: Int) {
                        if (data == 1) {
                            val options = BitmapFactory.Options()
                            options.inSampleSize = 4

                            val path = file.path
                            val bitmap = BitmapFactory.decodeFile(path, options)

                            if (requestCode == GlobalConst.CODE_CAMERA_BATHROOM || requestCode == GlobalConst.CODE_GALLERY_BATHROOM) {
                                photoBathroomImageView.setImageBitmap(bitmap)
                                photoBathroomImageView.visibility = View.VISIBLE
                            } else if (requestCode == GlobalConst.CODE_CAMERA_INSIDEROOM || requestCode == GlobalConst.CODE_GALLERY_INSIDEROOM) {
                                photoInsideRoomImageView.setImageBitmap(bitmap)
                                photoInsideRoomImageView.visibility = View.VISIBLE
                            } else if (requestCode == GlobalConst.CODE_CAMERA_BUILDING || requestCode == GlobalConst.CODE_GALLERY_BUILDING) {
                                photoBuildingImageView.setImageBitmap(bitmap)
                                photoBuildingImageView.visibility = View.VISIBLE
                            }
                        } else {
                            toast("gagal upload gambar, coba lagi")
                            MamiApp.instance?.sendEvent("errorXXXX", "gagal mengambil foto  V")
                        }
                    }
                })

            } else {
                toast("Gagal mengambil foto, coba lagi")
                MamiApp.instance?.sendEvent("errorXX", "gagal mengambil foto")
                return
            }
        } catch (e: Exception) {
            toast("coba lagi ${e}")
            MamiApp.instance?.sendEvent("errorX", e.toString())
            e.printStackTrace()
            sendReport(e.toString())
            return
        }

    }

    private fun uploadImage(file: File, requestCode: Int, status: OnClickInterfaceObject<Int>) {
        try {
            loading?.show()
            val upload = PhotosApi.MediaApi()
            try {
                upload.fileUpload = MediaHelper.compressImage(this, file)
                if (upload.fileUpload == null) {
                    UtilsHelper.log("kompres gambar tidak support, gambar akan pakai yang asli")
                    upload.fileUpload = file
                }
            } catch (e: Exception) {
                toast("Gagal mengambil gambar coba lagi bro, atau coba ambil dari galeri")
                e.printStackTrace()
                loading?.hide()
                MamiApp.instance?.sendEvent("errorA", e.toString())
                sendReport(e.toString())
                return
            }
            upload.formData = listOf(Pair("", ""))

            upload.exec(MediaResponse::class.java) { response: MediaResponse?, errorMessage: String? ->
                UtilsHelper.log("errorMessage $errorMessage")
                when (response) {
                    null -> errorMessage?.let {
                        toast(it)
                        status.dataClicked(0)
                    }
                    else -> {
                        UtilsHelper.log("data $response")
                        UtilsHelper.log("dataM ${response.media.id}")
                        if (requestCode == GlobalConst.CODE_CAMERA_BATHROOM || requestCode == GlobalConst.CODE_GALLERY_BATHROOM) {
                            photoBathroomBuildingId = response.media.id
                            photoBathroomBuildingDao = "${response.media.id},${file.path}"
                        } else if (requestCode == GlobalConst.CODE_CAMERA_INSIDEROOM || requestCode == GlobalConst.CODE_GALLERY_INSIDEROOM) {
                            photoInsideBuildingId = response.media.id
                            photoInsideBuildingDao = "${response.media.id},${file.path}"
                        } else if (requestCode == GlobalConst.CODE_CAMERA_BUILDING || requestCode == GlobalConst.CODE_GALLERY_BUILDING) {
                            photoKosBuildingId = response.media.id
                            photoKosBuildingDao = "${response.media.id},${file.path}"
                        }
                        status.dataClicked(1)
                    }
                }
                loading?.hide()
            }

        } catch (e: Exception) {
            toast("coba lagix ${e}")
            MamiApp.instance?.sendEvent("errorAA", e.toString())
            e.printStackTrace()
            sendReport(e.toString())
            return
        }

    }

    @SuppressLint("StaticFieldLeak") private inner class SavingDataLocal :
            AsyncTask<SaveKostEntity, Void, String>() {

        var facRoomData = ""
        var facBathRoomData = ""

        override fun doInBackground(vararg data: SaveKostEntity): String? {
            val saveKos = data[0]
            for (i in 0 until saveKos.facRoom.size) {
                facRoomData += "${saveKos.facRoom[i]},"
            }
            for (i in 0 until saveKos.facBath.size) {
                facBathRoomData = "${saveKos.facBath[i]}"
            }
            val formDataTable = FormDataTable(saveKos.province, saveKos.city, saveKos.subdistrict, saveKos.latitude, saveKos.longitude, saveKos.agentLat, saveKos.agentLong, saveKos.address, saveKos.name, saveKos.gender, "${saveKos.roomSize[0]},${saveKos.roomSize[1]}", saveKos.roomCount, saveKos.roomAvailable, saveKos.priceDaily.toString(), saveKos.priceWeekly.toString(), saveKos.priceMonthly.toString(), saveKos.roomCount.toString(), saveKos.minMonth, saveKos.roomCount.toString(), facRoomData, facBathRoomData, photoBathroomBuildingDao, photoInsideBuildingDao, photoKosBuildingDao, saveKos.withListrik, saveKos.ownerName, saveKos.ownerEmail, saveKos.ownerPhone)
            MamiApp.instance?.appDatabase?.formDataDao()?.insert(formDataTable)
            return ""
        }

        override fun onPostExecute(z: String) {
            val msg = "Data berhasil simpan di lokal, bersihkan form?"
            UtilsHelper.showDialogYesNo(this@FormKostActivity, "", msg, Runnable {
                val intent = Intent(this@FormKostActivity, FormKostActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }, 0)
        }
    }


}