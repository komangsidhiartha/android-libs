package com.mamikos.mamiagent

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import com.bumptech.glide.Glide
import com.mamikos.mamiagent.entities.ReviewEntity
import com.mamikos.mamiagent.entities.RoomEntity
import com.mamikos.mamiagent.helpers.MediaHelper
import com.mamikos.mamiagent.networks.apis.CheckInApi
import com.mamikos.mamiagent.networks.apis.RoomApi
import com.mamikos.mamiagent.networks.responses.StatusResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.activity_input_review.*
import kotlinx.android.synthetic.main.activity_intro_check_in.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File

class InputReviewActivity: BaseActivity()
{
    override val layoutResource: Int = R.layout.activity_input_review

    lateinit var room: RoomEntity
    var reviewEntity: ReviewEntity? = null
    var photoFile: File? = null
    var mCurrentPhotoPath: String? = null
    var photoURI: Uri? = null
    var isUpdate = false

    override fun viewDidLoad() {
        room = intent.extras.getParcelable(ListRoomActivity.ROOM_EXTRA)
        if (intent.hasExtra(InputPhotoActivity.REVIEW_DATA))
            reviewEntity = intent.extras.getParcelable(InputPhotoActivity.REVIEW_DATA)

        isUpdate = (reviewEntity != null)
        if (reviewEntity != null)
            bindView()

        setupActionBar()
        llAddPhotoReview.onClick {
            launchCamera()
        }
        btnAddPhoto.onClick {
            launchCamera()
        }

        ivDeletePhoto.onClick {
            deletePhotoReview()
        }

        btnSubmitReview.onClick {
            validateAndSend()
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
            toolbar?.title = "Review ${room.roomTitle}"
        }
    }

    private fun bindView()
    {
        rating_kebersihan.rating = reviewEntity?.clean?.toFloat() ?: 0F
        rating_kenyamanan.rating = reviewEntity?.happy?.toFloat() ?: 0F
        rating_keamanan.rating = reviewEntity?.safe?.toFloat() ?: 0F
        rating_harga.rating = reviewEntity?.pricing?.toFloat() ?: 0F
        rating_fasilitas_kamar.rating = reviewEntity?.roomFacilities?.toFloat() ?: 0F
        rating_fasilitas_umum.rating = reviewEntity?.publicFacilities?.toFloat() ?: 0F
        et_review_value.setText(reviewEntity?.content)
        bindPhotoReview()
        Glide.with(this).load(reviewEntity?.photo).into(ivPhotoReview)
    }

    fun validateAndSend()
    {
        if (rating_kebersihan.rating.toInt() == 0)
        {
            toast("Rating kebersihan harus diisi")
        }
        else if (rating_kenyamanan.rating.toInt() == 0)
        {
            toast("Rating kenyamanan harus diisi")
        }
        else if (rating_keamanan.rating.toInt() == 0)
        {
            toast("Rating keamanan harus diisi")
        }
        else if (rating_harga.rating.toInt() == 0)
        {
            toast("Rating harga harus diisi")
        }
        else if (rating_fasilitas_kamar.rating.toInt() == 0)
        {
            toast("Rating fasilitas kamar harus diisi")
        }
        else if (rating_fasilitas_umum.rating.toInt() == 0)
        {
            toast("Rating fasilitas umum harus diisi")
        }
        else if (et_review_value.text.toString().isNullOrEmpty())
        {
            toast("Isi review harus diisi")
        }
        else if ((reviewEntity == null && photoFile == null))
        {
            toast("Foto harus diisi")
        }
        else
        {
            sendReview()
        }
    }

    fun sendReview()
    {
        showLoadingBar()
        val reviewApi: RoomApi
        if (isUpdate) {
            reviewApi = RoomApi.UpdateReviewRoomApi(reviewEntity!!.id.toString())
            reviewEntity!!.clean = rating_kebersihan.rating.toInt()
            reviewEntity!!.happy = rating_kenyamanan.rating.toInt()
            reviewEntity!!.safe = rating_keamanan.rating.toInt()
            reviewEntity!!.pricing = rating_harga.rating.toInt()
            reviewEntity!!.roomFacilities = rating_fasilitas_kamar.rating.toInt()
            reviewEntity!!.publicFacilities = rating_fasilitas_umum.rating.toInt()
            reviewEntity!!.content = et_review_value.text.toString()
        }
        else {
            reviewApi = RoomApi.ReviewRoomApi()
            reviewEntity = ReviewEntity()
            reviewEntity!!.id = room._id.toInt()
            reviewEntity!!.clean = rating_kebersihan.rating.toInt()
            reviewEntity!!.happy = rating_kenyamanan.rating.toInt()
            reviewEntity!!.safe = rating_keamanan.rating.toInt()
            reviewEntity!!.pricing = rating_harga.rating.toInt()
            reviewEntity!!.roomFacilities = rating_fasilitas_kamar.rating.toInt()
            reviewEntity!!.publicFacilities = rating_fasilitas_umum.rating.toInt()
            reviewEntity!!.content = et_review_value.text.toString()
        }

        reviewApi.formData = reviewEntity?.toListPair()
        if (photoFile != null)
            reviewApi.fileUpload = MediaHelper.compressImage(this, photoFile)
        reviewApi.exec(StatusResponse::class.java) { response: StatusResponse?, errorMessage: String? ->
            hideLoadingBar()
            when (response) {
                null ->
                    errorMessage?.let { toast(it) }
                else -> {
                    toast("" + response.message)
                    if (response.status) {
                        startActivity<SuccessInputActivity>()
                        finish()
                    }
                }
            }
        }
    }

    fun launchCamera() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), IntroCheckInActivity.SETTING_CAMERA)
            return
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), IntroCheckInActivity.SETTING_CAMERA)
            return
        }

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            try {
                photoFile = MediaHelper.createImageFile(this)
                mCurrentPhotoPath = photoFile?.absolutePath
                photoURI = FileProvider.getUriForFile(this, "com.mamikos.mamiagent.provider", photoFile!!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, IntroCheckInActivity.TAKE_PHOTO_REQUEST)
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
            IntroCheckInActivity.SETTING_CAMERA -> {
                launchCamera()
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
                && requestCode == IntroCheckInActivity.TAKE_PHOTO_REQUEST) {
            logIfDebug("DATA " + data)
            photoFile = File(mCurrentPhotoPath)
            logIfDebug("PHOTO " + photoFile)
            bindPhotoReview()
            Glide.with(this).load(photoFile).into(ivPhotoReview)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun bindPhotoReview() {
        llPhotoReview.visibility = View.VISIBLE
        ivPhotoReview.visibility = View.VISIBLE
        ivPhotoReview.backgroundColor = android.R.color.transparent
        llAddPhotoReview.visibility = View.GONE
        ivDeletePhoto.visibility = View.VISIBLE
    }

    fun deletePhotoReview()
    {
        photoFile = null
        mCurrentPhotoPath = null
        photoURI = null
        llPhotoReview.visibility = View.GONE
        ivPhotoReview.visibility = View.GONE
        llAddPhotoReview.visibility = View.VISIBLE
        ivDeletePhoto.visibility = View.GONE
    }
}
