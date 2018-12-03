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
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.entities.PhotoFormEntity
import com.mamikos.mamiagent.entities.SaveKostEntity
import com.mamikos.mamiagent.helpers.*
import com.mamikos.mamiagent.networks.apis.PhotosApi
import com.mamikos.mamiagent.networks.apis.SaveKosApi
import com.mamikos.mamiagent.networks.responses.MediaResponse
import com.mamikos.mamiagent.networks.responses.MessagesResponse
import com.sidhiartha.libs.utils.GSONManager
import kotlinx.android.synthetic.main.view_form_kost_step_2.view.*
import kotlinx.android.synthetic.main.view_form_kost_step_3.*
import kotlinx.android.synthetic.main.view_form_kost_step_3.view.*
import kotlinx.android.synthetic.main.view_form_kost_step_4.view.*
import kotlinx.android.synthetic.main.view_grey_square.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity
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
    var fullAddressLatLng: LatLng? = null
    var myLatLng: LatLng? = null
    var photoKosBuildingId = 0
    var photoBathroomBuildingId = 0
    var photoInsideBuildingId = 0

    override val layoutResource: Int
        get() = R.layout.activity_form_kost

    override fun viewDidLoad() {

        EventBus.getDefault().register(this)

        UtilsPermission.checkPermissionGps(this)

        UtilsPermission.checkPermissionStorageAndCamera(this)

        loading = CustomLoadingView(this)

        setLayoutNextBack()

        requestProvinceApi()

        titleAddDataAds.setOnClickListener {
            startActivity<ListRoomActivity>()
        }

        buildGoogleApiClient()

        setMap()

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
                        formKostStep22View.setProvince(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                loading.show()
                                formKostStep22View.provinceSpinnerCustomView.setName("${data.name}")
                                requestCityApi(data.id)

                                formKostStep22View.citySpinnerCustomView.setName("")
                                formKostStep22View.citySpinnerCustomView.setData(null)
                                formKostStep22View.districtSpinnerCustomView.setName("")
                                formKostStep22View.districtSpinnerCustomView.setData(null)

                            }
                        })
                    }
                }
            }
            formKostStep22View.provinceSpinnerCustomView.setName("")
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
                        formKostStep22View.setCity(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                loading.show()
                                requestSubDistrictApi(data.id)
                                formKostStep22View.citySpinnerCustomView.setName("${data.name}")
                            }
                        })
                    }
                }
            }
            formKostStep22View.citySpinnerCustomView.setName("")
            formKostStep22View.districtSpinnerCustomView.setName("")
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
                        formKostStep22View.setSubdistrict(response, object :
                                OnClickInterfaceObject<AreaEntity> {
                            override fun dataClicked(data: AreaEntity) {
                                formKostStep22View.districtSpinnerCustomView.setName("${data.name}")
                            }
                        })
                    }
                }
            }
            formKostStep22View.districtSpinnerCustomView.setName("")
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

            UtilsHelper.log("cek kesini5")

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

            reverseGeoCode.run(mMap.cameraPosition.target)
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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                UtilsHelper.log("cek kesini3")
            } else {
                UtilsHelper.log("cek kesini4")
                val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                mFusedLocationClient.lastLocation.addOnSuccessListener(this) {
                    val ltLng = LatLng(it.latitude, it.longitude)
                    myLatLng = ltLng
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

        formKostStep11View.setNextOnClick(Runnable {
            formKostStep11View.visibility = View.GONE
            formKostStep22View.visibility = View.VISIBLE
            scrollUp()

            imageOneView.setImageResource(R.drawable.ic_check_circle_done)
            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_circle_undone)
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

            UtilsHelper.hideSoftInput(this)

        })

        formKostStep22View.setNextOnClick(Runnable {
            formKostStep22View.visibility = View.GONE
            formKostStep33View.visibility = View.VISIBLE
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

        formKostStep33View.setNextOnClick(Runnable {
            formKostStep33View.visibility = View.GONE
            formKostStep44View.visibility = View.VISIBLE
            scrollUp()

            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_check_circle_done)
            imageFourView.setImageResource(R.drawable.ic_check_circle_done)
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

            UtilsHelper.hideSoftInput(this)

        })

        formKostStep44View.setNextOnClick(Runnable {
            UtilsHelper.showDialogYesNo(this, "", getString(R.string.msg_data_confirmation), Runnable {
                UtilsHelper.hideSoftInput(this)
                goSaveKos()
            }, 0)
        })



        formKostStep11View.setBackOnClick(Runnable {
            UtilsHelper.showDialogYesNo(this, "", getString(R.string.msg_exit), Runnable {
                UtilsHelper.hideSoftInput(this)
                finish()
            }, 0)
        })

        formKostStep22View.setBackOnClick(Runnable {
            formKostStep11View.visibility = View.VISIBLE
            formKostStep22View.visibility = View.GONE
            scrollUp()

            imageOneView.setImageResource(R.drawable.ic_check_circle_done)
            imageTwoView.setImageResource(R.drawable.ic_circle_undone)
            imageThreeView.setImageResource(R.drawable.ic_circle_grey)
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.alto_solid))
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.alto_solid))

            UtilsHelper.hideSoftInput(this)

        })

        formKostStep33View.setBackOnClick(Runnable {
            formKostStep22View.visibility = View.VISIBLE
            formKostStep33View.visibility = View.GONE
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

        formKostStep44View.setBackOnClick(Runnable {
            formKostStep44View.visibility = View.GONE
            formKostStep33View.visibility = View.VISIBLE
            scrollUp()

            imageTwoView.setImageResource(R.drawable.ic_check_circle_done)
            imageThreeView.setImageResource(R.drawable.ic_check_circle_done)
            imageFourView.setImageResource(R.drawable.ic_circle_undone)
            lineThreeView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))
            lineTwoView.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_brand_color))

            UtilsHelper.hideSoftInput(this)

        })

    }

    private fun goSaveKos() {

        try {

            if (fullAddressLatLng == null) {
                UtilsHelper.showSnackbar(contentSquareConstraintLayout, "titik lokasi kosong, mohon pilih kembali titik lokasi pada peta")
                return
            }

            loading.show()

            val saveKos = SaveKostEntity()

            saveKos.province = formKostStep22View.provinceSpinnerCustomView.getName()
            saveKos.areaCity = formKostStep22View.citySpinnerCustomView.getName()
            saveKos.city = formKostStep22View.citySpinnerCustomView.getName()
            saveKos.subdistrict = formKostStep22View.districtSpinnerCustomView.getName()
            saveKos.latitude = fullAddressLatLng?.latitude!!
            saveKos.longitude = fullAddressLatLng?.longitude!!
            saveKos.agentLat = myLatLng?.latitude!!
            saveKos.agentLong = myLatLng?.longitude!!
            saveKos.address = formKostStep22View.fullAddressEditText.text.toString()

            saveKos.name = formKostStep33View.kosNameEditText.text.toString()
            saveKos.gender = formKostStep33View.typeGender.toInt()

            val roomSize: ArrayList<String> = arrayListOf()
            roomSize.add(formKostStep33View.roomSize.split(",")[0])
            roomSize.add(formKostStep33View.roomSize.split(",")[1])
            saveKos.roomSize = roomSize

            saveKos.roomCount = formKostStep33View.roomTotalEditText.text.toString().toInt()
            saveKos.roomAvailable = formKostStep33View.roomTotalNowEditText.text.toString().toInt()
            if (!formKostStep33View.dayPayEditText.text.isEmpty()) {
                saveKos.priceDaily = formKostStep33View.dayPayEditText.text.toString()
                        .replace(".", "").toInt()
            }
            if (!formKostStep33View.weekPayEditText.text.isEmpty()) {
                saveKos.priceWeekly = formKostStep33View.weekPayEditText.text.toString()
                        .replace(".", "").toInt()
            }
            if (!formKostStep33View.monthPayEditText.text.isEmpty()) {
                saveKos.priceMonthly = formKostStep33View.monthPayEditText.text.toString()
                        .replace(".", "").toInt()
            }
            if (!formKostStep33View.yearPayEditText.text.isEmpty()) {
                saveKos.priceYearly = formKostStep33View.yearPayEditText.text.toString()
                        .replace(".", "").toLong()
            }

            saveKos.minMonth = formKostStep33View.minPaySelected.toInt()

            val selectedFacRoom = arrayListOf<Int>()
            val selectedFacBathRoom = arrayListOf<Int>()

            if (formKostStep44View.mattressSquareGreyView.isChecked) {
                selectedFacRoom.add(10)
            }
            if (formKostStep44View.cupboardSquareGreyView.isChecked) {
                selectedFacRoom.add(11)
            }
            if (formKostStep44View.tableSquareGreyView.isChecked) {
                selectedFacRoom.add(14)
            }
            if (formKostStep44View.chairSquareGreyView.isChecked) {
                selectedFacRoom.add(17)
            }
            if (formKostStep44View.acSquareGreyView.isChecked) {
                selectedFacRoom.add(13)
            }
            if (formKostStep44View.tvSquareGreyView.isChecked) {
                selectedFacRoom.add(12)
            }
            if (formKostStep44View.fanSquareGreyView.isChecked) {
                selectedFacRoom.add(58)
            }
            if (formKostStep44View.facRoom == "0") {
                selectedFacRoom.add(62)
            }

            if (!formKostStep44View.speedTestEditText.text.isEmpty()) {
                saveKos.wifiSpeed = formKostStep44View.speedTestEditText.text.toString()
                selectedFacRoom.add(15)
            }

            if (formKostStep44View.twentyFourSquareGreyView.isChecked) {
                selectedFacRoom.add(59)
            }

            if (formKostStep44View.coupleSquareGreyView.isChecked) {
                selectedFacRoom.add(60)
            }

            if (formKostStep44View.parkSquareGreyView.isChecked) {
                selectedFacRoom.add(22)
            }

            saveKos.facRoom = selectedFacRoom

            if (formKostStep44View.showerSquareGreyView.isChecked) {
                selectedFacRoom.add(3)
            }
            if (formKostStep44View.toiletSeatSquareGreyView.isChecked) {
                selectedFacRoom.add(2)
            }
            if (formKostStep44View.squatToiletSquareGreyView.isChecked) {
                selectedFacRoom.add(5)
            }
            if (formKostStep44View.hotWaterSquareGreyView.isChecked) {
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

            if (formKostStep44View.facBathRoom == "0") {
                selectedFacBathRoom.add(1)
            } else {
                selectedFacBathRoom.add(4)
            }

            saveKos.facBath = selectedFacBathRoom

            if (formKostStep33View.isElectricity == "0") {
                saveKos.withListrik = 1
                saveKos.withoutListrik = 0
            } else {
                saveKos.withListrik = 0
                saveKos.withoutListrik = 1
            }

            saveKos.ownerName = formKostStep11View.ownerNameEditText.text.toString()
            saveKos.ownerEmail = formKostStep11View.ownerEmailEditText.text.toString()
            saveKos.ownerPhone = formKostStep11View.ownerPhoneEditText.text.toString()
            //saveKos.password = formKostStep11View.ownerPasswordEditText.text.toString()
            saveKos.inputAs = "agen"

            val apiSave = SaveKosApi.SaveKost()

            apiSave.postParam = GSONManager.toJson(saveKos)
            UtilsHelper.log("wooooo " + apiSave.postParam)

            apiSave.exec(MessagesResponse::class.java) { response: MessagesResponse?, errorMessage: String? ->

                var msg = ""

                if (response == null) {
                    UtilsHelper.showDialogYes(this, "", "Server lagi error, hubungi pihak developer", Runnable {}, 0)
                    loading.hide()
                    return@exec
                }

                if (response?.status!!) {
                    msg = "Berhasil tambah kos, bersihkan form?"
                    UtilsHelper.showDialogYesNo(this, "", msg, Runnable {
                        val intent = Intent(this, FormKostActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 0)
                } else {
                    for (i in 0 until response.messages?.size!!) {
                        msg += response.messages[i] + " "
                    }
                    UtilsHelper.showDialogYes(this, "", msg, Runnable {}, 0)
                }

                loading.hide()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            loading.hide()
        }
    }

    private fun scrollUp() {
        formKostScrollView.post {
            formKostScrollView.fullScroll(ScrollView.FOCUS_UP)
        }
    }

    override fun onBackPressed() {
        if (isNeedToShowCloseWarning()) {
            Toast.makeText(this, R.string.msg_back_pressed, Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
        lastBackPressedTimeInMillis = System.currentTimeMillis()
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
        MamiApp.sessionManager.pathCamera = pathCamera
    }

    var pathCamera = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        UtilsHelper.log("requestCode $requestCode")
        UtilsHelper.log("resultCode $resultCode")
        UtilsHelper.log("data " + data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == GlobalConst.CODE_CAMERA_BATHROOM || requestCode == GlobalConst.CODE_CAMERA_INSIDEROOM || requestCode == GlobalConst.CODE_CAMERA_BUILDING) {

                if (pathCamera.isNotEmpty()) {
                    successGetImage(Uri.parse(pathCamera), requestCode)
                } else if (data?.getStringExtra("camera_path") != null) {
                    successGetImage(Uri.parse(data.getStringExtra("camera_path")), requestCode)
                } else {
                    UtilsHelper.log("gagal mengambil gambar ambil path dari session")
                    successGetImage(Uri.parse(MamiApp.sessionManager.pathCamera), requestCode)
                }

            } else if (requestCode == GlobalConst.CODE_GALLERY_BATHROOM || requestCode == GlobalConst.CODE_GALLERY_INSIDEROOM || requestCode == GlobalConst.CODE_GALLERY_BUILDING) {
                if (data == null) {
                    return
                }
                successGetImage(data.data, requestCode)
            }
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
                        }
                    }
                })

            } else {
                toast("Gagal mengambil foto, coba lagi")
                return
            }
        } catch (e: Exception) {
            toast("coba lagi ${e}")
            return
        }

    }

    private fun uploadImage(file: File, requestCode: Int, status: OnClickInterfaceObject<Int>) {
        try {
            loading.show()
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
                loading.hide()
                return
            }
            upload.formData = listOf(Pair("", ""))

            upload.exec(MediaResponse::class.java) { response: MediaResponse?, errorMessage: String? ->
                hideLoadingBar()
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
                        } else if (requestCode == GlobalConst.CODE_CAMERA_INSIDEROOM || requestCode == GlobalConst.CODE_GALLERY_INSIDEROOM) {
                            photoInsideBuildingId = response.media.id
                        } else if (requestCode == GlobalConst.CODE_CAMERA_BUILDING || requestCode == GlobalConst.CODE_GALLERY_BUILDING) {
                            photoKosBuildingId = response.media.id
                        }
                        status.dataClicked(1)
                    }
                }
                loading.hide()
            }

        } catch (e: Exception) {
            toast("coba lagix ${e}")

            return
        }

    }
}