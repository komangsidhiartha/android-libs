package com.mamikos.mamiagent

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.mamikos.mamiagent.adapters.AddPhotoAdapter
import com.mamikos.mamiagent.entities.MediaEntity
import com.mamikos.mamiagent.entities.RoomEntity
import com.mamikos.mamiagent.entities.SaveDataRoomEntity
import com.mamikos.mamiagent.helpers.MediaHelper
import com.mamikos.mamiagent.networks.apis.PhotosApi
import com.mamikos.mamiagent.networks.apis.RoomApi
import com.mamikos.mamiagent.networks.responses.*
import com.mamikos.mamiagent.views.TextViewCustom
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import com.sidhiartha.libs.utils.GSONManager
import kotlinx.android.synthetic.main.activity_input_photo.*
import kotlinx.android.synthetic.main.text_view_custom.view.*
import org.jetbrains.anko.*
import java.io.File

class InputPhotoActivity : BaseActivity() {
    override val layoutResource: Int = R.layout.activity_input_photo
    companion object {
        var TYPE_COVER = "cover"
        var TYPE_BUILDING = "bangunan"
        var TYPE_ROOM = "kamar"
        var TYPE_BATH_ROOM = "kamar-mandi"
        var TYPE_OTHER = "lainnya"
        var TYPE_SPEED = "speed-test"
        var STORAGE_PERMISSION_CODE = 301
        var IMAGE_FROM_GALLERY = 21
        var REVIEW_DATA = "review_data"
        val TAKE_PHOTO_REQ = 222
    }

    lateinit var room: RoomEntity
    lateinit var photoFile: File
    var mCurrentPhotoPath: String? = null
    private lateinit var photoURI: Uri

    lateinit var originalPhotosResponse: ListPhotoResponse
    lateinit var originalDataEditedResponse: GetDataEditedResponse

    var coverMedias: ArrayList<MediaEntity> = arrayListOf(MediaEntity(-1, -1, "", TYPE_COVER))
    var buildingMedias: ArrayList<MediaEntity> = arrayListOf(MediaEntity(-2, -2, "", TYPE_BUILDING))
    var roomMedias: ArrayList<MediaEntity> = arrayListOf(MediaEntity(-3, -3, "", TYPE_ROOM))
    var bathRoomMedias: ArrayList<MediaEntity> = arrayListOf(MediaEntity(-4, -4, "", TYPE_BATH_ROOM))
    var otherMedias: ArrayList<MediaEntity> = arrayListOf(MediaEntity(-5, -5, "", TYPE_OTHER))
    var speedMedias: ArrayList<MediaEntity> = arrayListOf(MediaEntity(-6, -6, "", TYPE_SPEED))

    var coverAdapter = AddPhotoAdapter(this, coverMedias, TYPE_COVER,
            { onAddPhoto(TYPE_COVER) },
            { entity, type -> deleteMedia(entity, type) })
    var buildingAdapter = AddPhotoAdapter(this, buildingMedias, TYPE_BUILDING,
            { onAddPhoto(TYPE_BUILDING) },
            { entity, type -> deleteMedia(entity, type) })
    var roomAdapter = AddPhotoAdapter(this, roomMedias, TYPE_ROOM,
            { onAddPhoto(TYPE_ROOM) },
            { entity, type -> deleteMedia(entity, type) })
    var bathRoomAdapter = AddPhotoAdapter(this, bathRoomMedias, TYPE_BATH_ROOM,
            { onAddPhoto(TYPE_BATH_ROOM) },
            { entity, type -> deleteMedia(entity, type) })
    var otherAdapter = AddPhotoAdapter(this, otherMedias, TYPE_OTHER,
            { onAddPhoto(TYPE_OTHER) },
            { entity, type -> deleteMedia(entity, type) })
    var speedAdapter = AddPhotoAdapter(this, speedMedias, TYPE_SPEED,
            { onAddPhoto(TYPE_SPEED) },
            { entity, type -> deleteMedia(entity, type) })
    var currentUploaderType = ""

    override fun viewDidLoad() {
        room = intent.extras.getParcelable(ListRoomActivity.ROOM_EXTRA)
        setupActionBar()
        btnNextPhoto.onClick {
            validateAndSave()
        }
        setGridView()
        loadPhotos()

        getRoomDetail()
    }

    private fun getRoomDetail() {
        val roomAPI =  RoomApi.DetailRoom(room._id)
        roomAPI.exec(RoomDetailData::class.java){
            response: RoomDetailData?, errorMessage: String? ->
            if(response?.meta?.code == 200) {
                room = response.data
                setData()
            }
        }
    }

    private fun setData() {

        for (newData in room.facRoom.indices){
            val txtView  = TextViewCustom(this)
            txtView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txtView.txtViewCustom.text = room.facRoom[newData]
            layoutFac.addView(txtView)
        }

        for (newData in room.facShare.indices){
            val txtView  = TextViewCustom(this)
            txtView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txtView.txtViewCustom.text = room.facShare[newData]
            layoutFac.addView(txtView)
        }

        for (newData in room.facBath.indices){
            val txtView  = TextViewCustom(this)
            txtView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txtView.txtViewCustom.text  = room.facBath[newData]
            layoutFac.addView(txtView)
        }

        for (newData in room.facNear.indices){
            val txtView  = TextViewCustom(this)
            txtView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txtView.txtViewCustom.text  = room.facNear[newData]
            layoutFac.addView(txtView)
        }

        for (newData in room.facPark.indices){
            val txtView  = TextViewCustom(this)
            txtView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txtView.txtViewCustom.text  = room.facPark[newData]
            layoutFac.addView(txtView)
        }

        for (newData in room.facPrice.indices){
            val txtView  = TextViewCustom(this)
            txtView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txtView.txtViewCustom.text  = room.facPrice[newData]
            layoutFac.addView(txtView)
        }

        room.facRoomOther?.let {
            if(room.facRoomOther.isNotEmpty()) {
                val txtView = TextViewCustom(this)
                txtView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                txtView.txtViewCustom.text = room.facRoomOther
                layoutFac.addView(txtView)
            }
        }

        room.facBathOther?.let {
            if(room.facBathOther.isNotEmpty()) {
                val txtView2 = TextViewCustom(this)
                txtView2.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                txtView2.txtViewCustom.text = room.facBathOther
                layoutFac.addView(txtView2)
            }
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
            toolbar?.title = "Data ${room.roomTitle}"
        }
    }

    fun setGridView()
    {
        gvCover.adapter = coverAdapter
        gvBuilding.adapter = buildingAdapter
        gvRoom.adapter = roomAdapter
        gvBathRoom.adapter = bathRoomAdapter
        gvOther.adapter = otherAdapter
        gvSpeed.adapter = speedAdapter

        gvCover.isExpanded = true
        gvBuilding.isExpanded = true
        gvRoom.isExpanded = true
        gvBathRoom.isExpanded = true
        gvOther.isExpanded = true
        gvSpeed.isExpanded = true
    }

    fun loadPhotos()
    {
        val api: PhotosApi
        showLoadingBar()
        if (room.statuses == RoomEntity.STATUS_DEFAULT || room.statuses == RoomEntity.STATUS_CHECKIN) {
            api = PhotosApi.ListPhotoApi(room._id)
            api.exec(ListPhotoResponse::class.java) { response, errorMessage ->
                hideLoadingBar()
                when (response) {
                    null ->
                        errorMessage?.let { toast(it) }
                    else -> {
                        if (response.status) {
                            originalPhotosResponse = response
                            response.data.cover?.let {
                                coverMedias.clear()
                                bindDataToAdapter(coverMedias, it, coverAdapter)
                            }
                            response.data.bangunan?.let { bindDataToAdapter(buildingMedias, it, buildingAdapter) }
                            response.data.kamar?.let { bindDataToAdapter(roomMedias, it, roomAdapter) }
                            response.data.kamarMandi?.let { bindDataToAdapter(bathRoomMedias, it, bathRoomAdapter) }
                            response.data.lainnya?.let { bindDataToAdapter(otherMedias, it, otherAdapter) }
                        } else
                            toast("" + response.message)
                    }
                }
            }
        } else {
            api = PhotosApi.GetEditPhotoApi(room._id)
            api.exec(GetDataEditedResponse::class.java) { response, errorMessage ->
                hideLoadingBar()
                when (response) {
                    null ->
                        errorMessage?.let { toast(it) }
                    else -> {
                        if (response.status) {
                            originalDataEditedResponse = response
                            for (media in response.photos)
                            {
                                media.type?.let {
                                    if (it.equals(TYPE_COVER)) {
                                        coverMedias.clear()
                                        getMediaEntities(it).add(media)
                                    }
                                    else
                                        getMediaEntities(it).add(getMediaEntities(it).size-1, media)
                                    getAdapterFromType(it).notifyDataSetChanged() }
                            }
                            etRoomAvailable.setText("${response.data.roomAvailable}")
                        } else
                            toast("" + response.message)
                    }
                }
            }
        }
    }

    fun bindDataToAdapter(listMedias: ArrayList<MediaEntity>, medias: ArrayList<MediaEntity>, adapter: AddPhotoAdapter)
    {
        listMedias.addAll(0, medias)
        adapter.notifyDataSetChanged()
    }

    fun onAddPhoto(type: String)
    {
        currentUploaderType = type
        etRoomAvailable.clearFocus()
        if (type == TYPE_SPEED)
            pickFromGalery()
        else
            launchCamera()
    }

    fun pickFromGalery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasWritePermission = ContextCompat
                    .checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission()
                return
            }
        }
        addFromGallery()
    }

    private fun addFromGallery() {
        val toGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(toGallery, IMAGE_FROM_GALLERY)
    }

    fun requestStoragePermission() {
        //And finally ask for the permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = java.util.ArrayList<String>()
            val hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            //int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toTypedArray(), STORAGE_PERMISSION_CODE)
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
                mCurrentPhotoPath = photoFile.absolutePath
                photoURI = FileProvider.getUriForFile(this, "com.mamikos.mamiagent.provider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQ)
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

            STORAGE_PERMISSION_CODE -> {
                for (i in permissions.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        pickFromGalery()
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        toast("Permission harus disetujui")
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
                && requestCode == TAKE_PHOTO_REQ) {
            if (mCurrentPhotoPath != null) {
                photoFile = File(mCurrentPhotoPath)
                logIfDebug("PHOTO " + photoFile)
                if (MediaHelper.isPhotoLandscape(this, photoURI, mCurrentPhotoPath!!))
                    uploadImage()
                else
                    toast("Foto Harus Landscape.")
            }
            else
                toast("Ambil foto gagal.")
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_FROM_GALLERY && data != null) {
            if (data.data != null) {
                photoURI = data.data
                mCurrentPhotoPath = MediaHelper.setImageResourceFromGallery(this, photoURI)
                photoFile = File(mCurrentPhotoPath)
                uploadImage()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun getMediaEntities(type: String): ArrayList<MediaEntity>
    {
        return when (type)
        {
            TYPE_COVER -> coverMedias
            TYPE_BUILDING -> buildingMedias
            TYPE_ROOM -> roomMedias
            TYPE_BATH_ROOM -> bathRoomMedias
            TYPE_SPEED -> speedMedias
            TYPE_OTHER -> otherMedias
            else -> arrayListOf()
        }
    }

    fun getAdapterFromType(type: String): AddPhotoAdapter
    {
        return when (type)
        {
            TYPE_COVER -> coverAdapter
            TYPE_BUILDING -> buildingAdapter
            TYPE_ROOM -> roomAdapter
            TYPE_BATH_ROOM -> bathRoomAdapter
            TYPE_SPEED -> speedAdapter
            TYPE_OTHER -> otherAdapter
            else -> coverAdapter
        }
    }

    fun uploadImage()
    {
        showLoadingBar()
        val mediaApi = PhotosApi.MediaApi()
        mediaApi.formData = listOf(
                Pair("type", currentUploaderType),
                Pair("id", room._id)
        )

        mediaApi.fileUpload = MediaHelper.compressImage(this, photoFile)
        mediaApi.exec(MediaResponse::class.java) { response: MediaResponse?, errorMessage: String? ->
            hideLoadingBar()
            when (response) {
                null ->
                    errorMessage?.let { toast(it) }
                else -> {
                    toast("" + response.message)
                    if (response.status) {
                        val media = MediaEntity(response.id, 0, response.photo, currentUploaderType)
                        insertMedia(media)
                    }
                }
            }
        }
    }

    fun insertMedia(media: MediaEntity)
    {
        getMediaEntities(currentUploaderType).add(getMediaEntities(currentUploaderType).size-1, media)
        getAdapterFromType(currentUploaderType).notifyDataSetChanged()
    }

    fun deleteMedia(media: MediaEntity, type: String)
    {
        logIfDebug("MEDIA $media type $type")
        for (i in getMediaEntities(type).indices-1) {
            if (getMediaEntities(type)[i].id == media.id) {
                getMediaEntities(type).removeAt(i)
            }
        }

        if (getMediaEntities(type).size == 0)
        {
            getMediaEntities(type).add(MediaEntity(-1, -1, "", ""))
        }
        postDeleteMedia(media.id)
    }

    fun postDeleteMedia(id: Int)
    {
        showLoadingBar()
        val api = PhotosApi.DeleteMediaApi(id)
        api.exec(StatusResponse::class.java) { response, errorMessage ->
            hideLoadingBar()
            when (response) {
                null ->
                    errorMessage?.let { toast(it) }
                else -> {
                    logIfDebug("Response $response")
                    toast("" + response.message)
                }
            }
        }
    }

    fun getOriginalPhotoDeleted(): ArrayList<Int>
    {
        val ids = arrayListOf<Int>()
        for (id in getAllOriginalMediaIds())
        {
            if (id !in getAllMediaIds())
            {
                ids.add(id)
            }
        }
        return ids
    }

    fun getAllOriginalMediaIds(): ArrayList<Int>
    {
        val ids = arrayListOf<Int>()
        originalPhotosResponse.data.cover?.let { getMediaIdsFromMediaEntities(it) }?.let { ids.addAll(it) }
        originalPhotosResponse.data.bangunan?.let { getMediaIdsFromMediaEntities(it) }?.let { ids.addAll(it) }
        originalPhotosResponse.data.kamar?.let { getMediaIdsFromMediaEntities(it) }?.let { ids.addAll(it) }
        originalPhotosResponse.data.kamarMandi?.let { getMediaIdsFromMediaEntities(it) }?.let { ids.addAll(it) }
        originalPhotosResponse.data.lainnya?.let { getMediaIdsFromMediaEntities(it) }?.let { ids.addAll(it) }
        return ids
    }

    fun getAllMediaIds(): ArrayList<Int>
    {
        val ids = arrayListOf<Int>()
        ids.addAll(getMediaIdsFromMediaEntities(coverMedias))
        ids.addAll(getMediaIdsFromMediaEntities(buildingMedias))
        ids.addAll(getMediaIdsFromMediaEntities(roomMedias))
        ids.addAll(getMediaIdsFromMediaEntities(bathRoomMedias))
        ids.addAll(getMediaIdsFromMediaEntities(speedMedias))
        ids.addAll(getMediaIdsFromMediaEntities(otherMedias))
        return ids
    }

    fun getMediaIdsFromMediaEntities(medias: ArrayList<MediaEntity>): ArrayList<Int>
    {
        val ids = arrayListOf<Int>()
        for (media in medias)
        {
            if (media.id > 0)
                ids.add(media.id)
        }
        return ids
    }

    fun validateAndSave()
    {
        etRoomAvailable.error = null
        if (etRoomAvailable.text.isNullOrEmpty())
        {
            etRoomAvailable.error = "Kamar kosong wajib diisi"
            toast("Kamar kosong wajib diisi")
        }
        else if (getMediaIdsFromMediaEntities(coverMedias).size == 0)
        {
            toast("Foto cover wajib diisi")
        }
        else if (getMediaIdsFromMediaEntities(buildingMedias).size == 0)
        {
            toast("Foto bangunan wajib diisi")
        }
        else if (getMediaIdsFromMediaEntities(roomMedias).size == 0)
        {
            toast("Foto kamar wajib diisi")
        }
        else if (getMediaIdsFromMediaEntities(bathRoomMedias).size == 0)
        {
            toast("Foto kamar mandi wajib diisi")
        }
        else if (getMediaIdsFromMediaEntities(otherMedias).size == 0)
        {
            toast("Foto lainnya wajib diisi")
        }
        else
        {
            savePhotos()
        }
    }

    fun savePhotos()
    {
        showLoadingBar()
        val api = RoomApi.SaveDataRoomApi()
        val saveDataRoomEntity = SaveDataRoomEntity()
        saveDataRoomEntity.id = room._id
        saveDataRoomEntity.roomAvailable = etRoomAvailable.text.toString().toInt()
        saveDataRoomEntity.agentDescription = etComment.text.toString()
        if (room.statuses == RoomEntity.STATUS_DEFAULT || room.statuses == RoomEntity.STATUS_CHECKIN)
            saveDataRoomEntity.cardDelete = getOriginalPhotoDeleted()
        else
            saveDataRoomEntity.cardDelete = originalDataEditedResponse.data.cardDelete

        api.postParam = GSONManager.toJson(saveDataRoomEntity)
        api.exec(StatusResponse::class.java) { response, errorMessage ->
            hideLoadingBar()
            when (response) {
                null ->
                    errorMessage?.let { toast(it) }
                else -> {
                    toast("" + response.message)
                    if (response.status) {
                        if (room.statuses == RoomEntity.STATUS_DEFAULT || room.statuses == RoomEntity.STATUS_CHECKIN ||
                                room.statuses == RoomEntity.STATUS_PHOTO)
                            startActivity<InputReviewActivity>(ListRoomActivity.ROOM_EXTRA to room)
                        else
                            startActivity<InputReviewActivity>(ListRoomActivity.ROOM_EXTRA to room,
                                    REVIEW_DATA to originalDataEditedResponse.review)
                        finish()
                    }
                }
            }
        }
    }
}