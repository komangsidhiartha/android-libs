package com.mamikos.mamiagent.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.entities.RoomEntity
import com.mamikos.mamiagent.helpers.MediaHelper
import com.mamikos.mamiagent.networks.apis.CheckInApi
import com.mamikos.mamiagent.networks.responses.StatusResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.activity_intro_check_in.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File

class IntroCheckInActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    override val layoutResource: Int = R.layout.activity_intro_check_in

    companion object {
        val SETTING_CAMERA = 200
        val TAKE_PHOTO_REQUEST = 201
    }

    lateinit var room: RoomEntity
    var photoFile: File? = null
    private lateinit var mCurrentPhotoPath: String
    private lateinit var photoURI: Uri
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private var locationManager: LocationManager? = null

    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    override fun viewDidLoad() {
        room = intent.extras.getParcelable(ListRoomActivity.ROOM_EXTRA)

        setupActionBar()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        ivCheckIn.onClick {
            if (checkLocation())
                launchCamera()
        }

        btnChangePhoto.onClick {
            launchCamera()
        }

        btnSubmitCheckIn.onClick {
            validateAndCheckIn()
        }
    }

    private fun setupActionBar() {
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
            toolbar?.setNavigationOnClickListener(View.OnClickListener { view ->
                if (view.id == -1) {
                    onBackPressed()
                }
            })
            toolbar?.title = "Check In Kost"
        }
    }

    fun launchCamera() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), SETTING_CAMERA)
            return
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SETTING_CAMERA)
            return
        }

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            try {
                photoFile = MediaHelper.createImageFile(this)
                mCurrentPhotoPath = photoFile?.absolutePath!!
                photoURI = FileProvider.getUriForFile(this, "com.mamikos.mamiagent.provider", photoFile!!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
            } catch (ex: Exception) {
                // Error occurred while creating the File
                toast("Capture Image Bug: " + ex.message.toString())
            }
        } else {
            toast("Nullll")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SETTING_CAMERA -> {
                launchCamera()
                return
            }
            ListRoomActivity.SETTING_LOCATION -> {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (requestCode == TAKE_PHOTO_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val handler  = Handler()
                handler.postDelayed({
                    photoFile = File(mCurrentPhotoPath)
                    logIfDebug("PHOTO " + photoFile)
                    processCapturedPhoto()
                },1000)
            } else {
                photoFile = null
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processCapturedPhoto() {
        Glide.with(this).load(photoFile).into(ivCheckIn)
        llBtnCheckIn.visibility = View.VISIBLE
    }

    fun validateAndCheckIn()
    {
        if (room.statuses == RoomEntity.STATUS_DEFAULT && photoFile == null)
        {
            toast("Foto harus diisi")
        }
        else
            checkIn()
    }

    fun checkIn() {
        showLoadingBar()
        val checkInApi: CheckInApi
        if (room.statuses == RoomEntity.STATUS_DEFAULT)
        {
            checkInApi = CheckInApi.FirstCheckInApi()
        }
        else
        {
            checkInApi = CheckInApi.UpdateCheckInApi()
        }

        checkInApi.formData = listOf(
                Pair("latitude", mLocation!!.latitude),
                Pair("longitude", mLocation!!.longitude),
                Pair("id", room._id))


        if (photoFile != null)
            checkInApi.fileUpload = MediaHelper.compressImage(this, photoFile)

        checkInApi.exec(StatusResponse::class.java) { response: StatusResponse?, errorMessage: String? ->
            hideLoadingBar()
            when (response) {
                null ->
                    errorMessage?.let { toast(it) }
                else -> {
                    toast("" + response.message)
                    if (response.status) {
                        startActivity<InputPhotoActivity>(ListRoomActivity.ROOM_EXTRA to room)
                        finish()
                    }
                }
            }
        }
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
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION), ListRoomActivity.SETTING_LOCATION);
                }.setCancelable(false)
        dialog.show()
    }

    override fun onConnectionSuspended(p0: Int) {
        logIfDebug("Connection Suspended")
        mGoogleApiClient?.connect()
    }
}
