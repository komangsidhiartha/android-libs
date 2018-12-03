package com.mamikos.mamiagent.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.activity.CameraActivity
import com.mamikos.mamiagent.activity.FormKostActivity
import com.mamikos.mamiagent.helpers.GlobalConst
import com.mamikos.mamiagent.helpers.ShowCamera
import com.mamikos.mamiagent.helpers.ShowGallery
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import kotlinx.android.synthetic.main.activity_form_kost.*
import kotlinx.android.synthetic.main.view_btn_back_next.view.*
import kotlinx.android.synthetic.main.view_form_kost_step_3.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.onCheckedChange

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FormKostStep44View : FrameLayout {

    private lateinit var nextClick: Runnable
    private lateinit var backClick: Runnable
    private var scrollView: LockableScrollView? = null
    var facBathRoom = "0"
    var facRoom = "1"

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_form_kost_step_3, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setupRoomFacility()
        setupRoomOtherFacility()
        setupBathRoomFacility()

        viewBtnBackNextStep3.nextLinearLayout.setOnClickListener {
            if (scrollView == null) {
                scrollView = (context as Activity).formKostScrollView
            }

            validation()
            //nextClick.run()
        }

        viewBtnBackNextStep3.backLinearLayout.setOnClickListener {
            backClick.run()
        }

        photoBathroomLinearLayout.setOnClickListener {
            val dialogSelectImage = DialogOpenFileView(context)
            dialogSelectImage.setOnClick(object : OnClickInterfaceObject<Int> {
                override fun dataClicked(data: Int) {
                    dialogSelectImage.dismiss()
                    if (ShowCamera.CODE_CAMERA == data) {
                        showCamera1(GlobalConst.CODE_CAMERA_BATHROOM)
                    } else if (ShowCamera.CODE_CAMERA_2 == data) {
                        showCamera2(GlobalConst.CODE_CAMERA_BATHROOM)
                    } else if (ShowGallery.CODE_GALLERY == data) {
                        val showGallery = ShowGallery(context)
                        showGallery.showNow(GlobalConst.CODE_GALLERY_BATHROOM)
                    }
                }
            })
            dialogSelectImage.showDialog()
        }

        photoInsideRoomLinearLayout.setOnClickListener {
            val dialogSelectImage = DialogOpenFileView(context)
            dialogSelectImage.setOnClick(object : OnClickInterfaceObject<Int> {
                override fun dataClicked(data: Int) {
                    dialogSelectImage.dismiss()
                    if (ShowCamera.CODE_CAMERA == data) {
                        showCamera1(GlobalConst.CODE_CAMERA_INSIDEROOM)
                    } else if (ShowCamera.CODE_CAMERA_2 == data) {
                        showCamera2(GlobalConst.CODE_CAMERA_INSIDEROOM)
                    } else if (ShowGallery.CODE_GALLERY == data) {
                        val showGallery = ShowGallery(context)
                        showGallery.showNow(GlobalConst.CODE_GALLERY_INSIDEROOM)
                    }
                }
            })
            dialogSelectImage.showDialog()
        }

        photoBuildingLinearLayout.setOnClickListener {
            val dialogSelectImage = DialogOpenFileView(context)
            dialogSelectImage.setOnClick(object : OnClickInterfaceObject<Int> {
                override fun dataClicked(data: Int) {
                    dialogSelectImage.dismiss()
                    if (ShowCamera.CODE_CAMERA == data) {
                        showCamera1(GlobalConst.CODE_CAMERA_BUILDING)
                    } else if (ShowCamera.CODE_CAMERA_2 == data) {
                        showCamera2(GlobalConst.CODE_CAMERA_BUILDING)
                    } else if (ShowGallery.CODE_GALLERY == data) {
                        val showGallery = ShowGallery(context)
                        showGallery.showNow(GlobalConst.CODE_GALLERY_BUILDING)
                    }
                }
            })
            dialogSelectImage.showDialog()
        }

    }

    private fun showCamera1(code: Int) {
        val intent = Intent(context, CameraActivity::class.java)
        (context as Activity).startActivityForResult(intent, code)
    }

    private fun showCamera2(code: Int) {
        val showCamera = ShowCamera(context)
        showCamera.showNow(code)
        val file = showCamera.fileCamera
        val bundle = Bundle()
        bundle.putString("path", file?.path)
        EventBus.getDefault().post(bundle)
    }

    private fun validation() {

        if (notEmptyRoomRadioButton.isChecked) {
            val alreadySelected = mattressSquareGreyView.isChecked || cupboardSquareGreyView.isChecked || tableSquareGreyView.isChecked || chairSquareGreyView.isChecked || acSquareGreyView.isChecked || tvSquareGreyView.isChecked || fanSquareGreyView.isChecked
            if (!alreadySelected) {
                UtilsHelper.showSnackbar(this, "Data fasilitas kamar tidak boleh kosong")
                UtilsHelper.autoFocusScroll(mattressSquareGreyView, scrollView)
                return
            }
        }

        if (wifiSquareGreyView.isChecked) {
            if (speedTestEditText.text.toString().isEmpty()) {
                UtilsHelper.showSnackbar(this, "Data speed test tidak boleh kosong")
                UtilsHelper.autoFocusScroll(speedTestEditText, scrollView)
                return
            }
        }

        if (context is FormKostActivity) {
            val photoBuildingId = (context as FormKostActivity).photoKosBuildingId
            val photoBathroomBuildingId = (context as FormKostActivity).photoBathroomBuildingId
            val photoInsideBuildingId = (context as FormKostActivity).photoInsideBuildingId
            if (photoBuildingId == 0) {
                UtilsHelper.showSnackbar(this, "Data foto bangunan tidak boleh kosong, mohon kirim ulang")
                return
            } else if (photoBathroomBuildingId == 0) {
                UtilsHelper.showSnackbar(this, "Data foto kamar mandi tidak boleh kosong, mohon kirim ulang")
                return
            } else if (photoInsideBuildingId == 0) {
                UtilsHelper.showSnackbar(this, "Data foto dalam kamar tidak boleh kosong, mohon kirim ulang")
                return
            }
        }

        nextClick.run()
    }

    private fun setupBathRoomFacility() {

        insideBathroomRadioButton.onCheckedChange { _, b ->
            if (b) {
                facBathRoom = "0"
            }
        }

        outsideBathroomRadioButton.onCheckedChange { _, b ->
            if (b) {
                facBathRoom = "1"
            }
        }

        showerSquareGreyView.setString(context.getString(R.string.msg_shower))
        showerSquareGreyView.setImage(R.drawable.ic_shower)
        showerSquareGreyView.setCheckList(false)
        showerSquareGreyView.setOnClick(Runnable {
            if (!showerSquareGreyView.isChecked) {
                showerSquareGreyView.setCheckList(true)
            } else {
                showerSquareGreyView.setCheckList(false)
            }
        })

        squatToiletSquareGreyView.setString(context.getString(R.string.msg_squat_toilet))
        squatToiletSquareGreyView.setImage(R.drawable.ic_toilet_jongkok)
        squatToiletSquareGreyView.setCheckList(false)
        squatToiletSquareGreyView.setOnClick(Runnable {
            if (!squatToiletSquareGreyView.isChecked) {
                squatToiletSquareGreyView.setCheckList(true)
            } else {
                squatToiletSquareGreyView.setCheckList(false)
            }
        })

        toiletSeatSquareGreyView.setString(context.getString(R.string.msg_squat_seat))
        toiletSeatSquareGreyView.setImage(R.drawable.ic_toilet_duduk)
        toiletSeatSquareGreyView.setCheckList(false)
        toiletSeatSquareGreyView.setOnClick(Runnable {
            if (!toiletSeatSquareGreyView.isChecked) {
                toiletSeatSquareGreyView.setCheckList(true)
            } else {
                toiletSeatSquareGreyView.setCheckList(false)
            }
        })

        hotWaterSquareGreyView.setString(context.getString(R.string.msg_hot_water))
        hotWaterSquareGreyView.setImage(R.drawable.ic_bath_air_panas)
        hotWaterSquareGreyView.setCheckList(false)
        hotWaterSquareGreyView.setOnClick(Runnable {
            if (!hotWaterSquareGreyView.isChecked) {
                hotWaterSquareGreyView.setCheckList(true)
            } else {
                hotWaterSquareGreyView.setCheckList(false)
            }
        })


    }

    private fun setupRoomOtherFacility() {
        wifiSquareGreyView.setString(context.getString(R.string.msg_wifi))
        wifiSquareGreyView.setImage(R.drawable.ic_wifi_signal)
        wifiSquareGreyView.setCheckList(false)
        wifiSquareGreyView.setOnClick(Runnable {
            if (!wifiSquareGreyView.isChecked) {
                wifiSquareGreyView.setCheckList(true)
                speedTestLinearLayout.visibility = View.VISIBLE
            } else {
                wifiSquareGreyView.setCheckList(false)
                speedTestLinearLayout.visibility = View.GONE
            }
            speedTestEditText.setText("")

        })

        twentyFourSquareGreyView.setString(context.getString(R.string.msg_twenty_four))
        twentyFourSquareGreyView.setImage(R.drawable.ic_clock)
        twentyFourSquareGreyView.setCheckList(false)
        twentyFourSquareGreyView.setOnClick(Runnable {
            if (!twentyFourSquareGreyView.isChecked) {
                twentyFourSquareGreyView.setCheckList(true)
            } else {
                twentyFourSquareGreyView.setCheckList(false)
            }
        })

        coupleSquareGreyView.setString(context.getString(R.string.msg_can_couple))
        coupleSquareGreyView.setImage(R.drawable.ic_couple)
        coupleSquareGreyView.setCheckList(false)
        coupleSquareGreyView.setOnClick(Runnable {
            if (!coupleSquareGreyView.isChecked) {
                coupleSquareGreyView.setCheckList(true)
            } else {
                coupleSquareGreyView.setCheckList(false)
            }
        })

        parkSquareGreyView.setString(context.getString(R.string.msg_park_car))
        parkSquareGreyView.setImage(R.drawable.ic_parking_sign)
        parkSquareGreyView.setCheckList(false)
        parkSquareGreyView.setOnClick(Runnable {
            if (!parkSquareGreyView.isChecked) {
                parkSquareGreyView.setCheckList(true)
            } else {
                parkSquareGreyView.setCheckList(false)
            }
        })
    }

    private fun setupRoomFacility() {

        notEmptyRoomRadioButton.onCheckedChange { _, b ->
            if (b) {
                facilityRoomOneLinearLayout.visibility = View.VISIBLE
                facilityRoomTwoLinearLayout.visibility = View.VISIBLE
                facRoom = "1"
            }
        }

        emptyRoomRadioButton.onCheckedChange { _, b ->
            if (b) {
                facilityRoomOneLinearLayout.visibility = View.GONE
                facilityRoomTwoLinearLayout.visibility = View.GONE
                mattressSquareGreyView.setCheckList(false)
                cupboardSquareGreyView.setCheckList(false)
                tableSquareGreyView.setCheckList(false)
                chairSquareGreyView.setCheckList(false)
                acSquareGreyView.setCheckList(false)
                tvSquareGreyView.setCheckList(false)
                fanSquareGreyView.setCheckList(false)
                facRoom = "0"
            }
        }

        mattressSquareGreyView.setString(context.getString(R.string.msg_bed))
        mattressSquareGreyView.setImage(R.drawable.ic_bed)
        mattressSquareGreyView.setCheckList(false)
        mattressSquareGreyView.setOnClick(Runnable {
            if (!mattressSquareGreyView.isChecked) {
                mattressSquareGreyView.setCheckList(true)
            } else {
                mattressSquareGreyView.setCheckList(false)
            }
        })

        cupboardSquareGreyView.setString(context.getString(R.string.msg_cupboard))
        cupboardSquareGreyView.setImage(R.drawable.ic_wardrobe)
        cupboardSquareGreyView.setCheckList(false)
        cupboardSquareGreyView.setOnClick(Runnable {
            if (!cupboardSquareGreyView.isChecked) {
                cupboardSquareGreyView.setCheckList(true)
            } else {
                cupboardSquareGreyView.setCheckList(false)
            }
        })

        tableSquareGreyView.setString(context.getString(R.string.msg_table))
        tableSquareGreyView.setImage(R.drawable.ic_table)
        tableSquareGreyView.setCheckList(false)
        tableSquareGreyView.setOnClick(Runnable {
            if (!tableSquareGreyView.isChecked) {
                tableSquareGreyView.setCheckList(true)
            } else {
                tableSquareGreyView.setCheckList(false)
            }
        })

        chairSquareGreyView.setString(context.getString(R.string.msg_chair))
        chairSquareGreyView.setImage(R.drawable.ic_armchair)
        chairSquareGreyView.setCheckList(false)
        chairSquareGreyView.setOnClick(Runnable {
            if (!chairSquareGreyView.isChecked) {
                chairSquareGreyView.setCheckList(true)
            } else {
                chairSquareGreyView.setCheckList(false)
            }
        })

        acSquareGreyView.setString(context.getString(R.string.msg_ac))
        acSquareGreyView.setImage(R.drawable.ic_air_conditioner)
        acSquareGreyView.setCheckList(false)
        acSquareGreyView.setOnClick(Runnable {
            if (!acSquareGreyView.isChecked) {
                acSquareGreyView.setCheckList(true)
            } else {
                acSquareGreyView.setCheckList(false)
            }
        })

        tvSquareGreyView.setString(context.getString(R.string.msg_tv))
        tvSquareGreyView.setImage(R.drawable.ic_television)
        tvSquareGreyView.setCheckList(false)
        tvSquareGreyView.setOnClick(Runnable {
            if (!tvSquareGreyView.isChecked) {
                tvSquareGreyView.setCheckList(true)
            } else {
                tvSquareGreyView.setCheckList(false)
            }
        })

        fanSquareGreyView.setString(context.getString(R.string.msg_fan))
        fanSquareGreyView.setImage(R.drawable.ic_fan)
        fanSquareGreyView.setCheckList(false)
        fanSquareGreyView.setOnClick(Runnable {
            if (!fanSquareGreyView.isChecked) {
                fanSquareGreyView.setCheckList(true)
            } else {
                fanSquareGreyView.setCheckList(false)
            }
        })
    }

    fun setNextOnClick(click: Runnable) {
        nextClick = click
    }

    fun setBackOnClick(click: Runnable) {
        backClick = click
    }


}