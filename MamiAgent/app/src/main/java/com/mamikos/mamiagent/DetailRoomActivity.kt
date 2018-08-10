package com.mamikos.mamiagent

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.mamikos.mamiagent.entities.RoomEntity
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.activity_detail_room.*
import kotlinx.android.synthetic.main.item_room.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity


class DetailRoomActivity : BaseActivity(), OnMapReadyCallback {
    override val layoutResource: Int = R.layout.activity_detail_room
    private lateinit var mMap: GoogleMap
    lateinit var room: RoomEntity
    lateinit var myLocation: LatLng

    override fun viewDidLoad() {
        room = intent.extras.getParcelable(ListRoomActivity.ROOM_EXTRA)
        setupActionBar()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Glide.with(this).load(room.photoUrl.small)
                .apply(RequestOptions().override(100, 100).transform(CenterInside()).transform(RoundedCorners(10)))
                .into(ivRoom)
        tvRoomName.text = room.roomTitle
        tvAddress.text = room.address
        tvDistance.text = room.distanceString
        tvPhotoCount.text = "Photo : ${room.photoCount}"
        tvReviewCount.text = "Review : ${room.reviewCount}"
        tvOwnerName.text = "Pemilik: ${room.ownerName}"
        tvOwnerPhone.text = "Phone: ${room.ownerPhone}"
        btnCheckIn.onClick {
            startActivity<IntroCheckInActivity>(ListRoomActivity.ROOM_EXTRA to room)
            finish()
        }
        llPhoneOwner.onClick {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", room.ownerPhone, null))
            startActivity(intent)
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
            toolbar?.title = room.roomTitle
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val loc = LatLng(room.latitude, room.longitude)
        mMap.addMarker(MarkerOptions().position(loc).title(room.roomTitle))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 18F))
        val permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED)
            mMap.isMyLocationEnabled = true

        mMap.setOnMyLocationChangeListener {
            myLocation = LatLng(it.latitude, it.longitude)
            setBound()
        }
    }

    fun setBound()
    {
        val builder = LatLngBounds.Builder()
        builder.include(LatLng(room.latitude, room.longitude))
        builder.include(myLocation)
        val bounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 30)
        mMap.animateCamera(cu)
    }
}
