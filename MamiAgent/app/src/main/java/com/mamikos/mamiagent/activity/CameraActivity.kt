package com.mamikos.mamiagent.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.helpers.UtilsHelper
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.configuration.UpdateConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.parameter.Flash
import io.fotoapparat.parameter.Zoom
import io.fotoapparat.result.transformer.scaled
import io.fotoapparat.selector.*
import kotlinx.android.synthetic.main.camera_activity.*
import org.jetbrains.anko.toast
import java.io.File
import kotlin.math.roundToInt

/**
 * Created by Dedi Dot on 12/3/2018.
 * Happy Coding!
 */

class CameraActivity : AppCompatActivity() {

    private var activeCamera: Camera? = null

    private lateinit var fotoApparat: Fotoapparat
    private lateinit var cameraZoom: Zoom.VariableZoom
    private lateinit var file: File
    var isSupportAutoFocus1 = false
    var isSupportAutoFocus2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)

        isSupportAutoFocus1 = packageManager.hasSystemFeature("android.hardware.camera.autofocus")
        isSupportAutoFocus2 = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)

        cekCameraFocusSupport()

        fotoApparat = Fotoapparat(context = this, view = cameraView, focusView = focusView, logger = logcat(), lensPosition = activeCamera!!.lensPosition, cameraConfiguration = activeCamera!!.configuration, cameraErrorCallback = {
            Log.e(LOGGING_TAG, "Camera error: ", it)
            toast("Kamera tidak support 1")
            return@Fotoapparat
        })

        capture.setOnClickListener {
            takePicture()
        }

        switchCamera.setOnClickListener {
            changeCamera()
        }

        torchSwitch.setOnCheckedChangeListener { _, isChecked ->
            toggleFlash(isChecked)
        }

        capturedResultImageView.setOnClickListener {
            UtilsHelper.log("data fileX ${file.path}")
            UtilsHelper.log("data fileXX ${file.absolutePath}")
            UtilsHelper.log("data fileXXX ${file.canonicalPath}")
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun cekCameraFocusSupport() {
        if (isSupportAutoFocus1 || isSupportAutoFocus2) {
            if (activeCamera == null) {
                activeCamera = Camera.BackSupportFocus
            } else if (activeCamera == Camera.FrontSupportFocus) {
                activeCamera = Camera.BackSupportFocus
            } else if (activeCamera == Camera.BackSupportFocus) {
                activeCamera = Camera.FrontSupportFocus
            } else {
                activeCamera = Camera.BackSupportFocus
            }
        } else {
            if (activeCamera == null) {
                activeCamera = Camera.BackNotSupportFocus
            } else if (activeCamera == Camera.FrontNotSupportFocus) {
                activeCamera = Camera.BackNotSupportFocus
            } else if (activeCamera == Camera.BackNotSupportFocus) {
                activeCamera = Camera.FrontNotSupportFocus
            } else {
                activeCamera = Camera.BackNotSupportFocus
            }
        }
    }

    private fun takePicture() {

        val photoResult = fotoApparat.autoFocus().takePicture()

        file = UtilsHelper.createImageFile(this)

        photoResult.saveToFile(file)

        photoResult.toBitmap(scaled(scaleFactor = 0.25f)).whenAvailable { photo ->

            try {
                if (photo != null) {
                    Log.i(LOGGING_TAG, "New photo captured. Bitmap length: ${photo.bitmap.byteCount}")
                    capturedResultImageView.setImageBitmap(photo.bitmap)
                    capturedResultImageView.rotation = (-photo.rotationDegrees).toFloat()
                } else {
                    mainLooper.quit()
                    toast("kamera tidak support 2")
                    return@whenAvailable
                }
            }catch (e: Exception){
                toast("Coba lagi")
            }
        }
    }

    private fun changeCamera() {

        cekCameraFocusSupport()

        fotoApparat.switchTo(lensPosition = activeCamera!!.lensPosition, cameraConfiguration = activeCamera!!.configuration)

        torchSwitch.isChecked = false

    }

    private fun toggleFlash(isChecked: Boolean) {
        fotoApparat.updateConfiguration(UpdateConfiguration(flashMode = if (isChecked) {
            firstAvailable(torch(), off())
        } else {
            off()
        }))

        Log.i(LOGGING_TAG, "Flash is now ${if (isChecked) "on" else "off"}")
    }

    override fun onStart() {
        super.onStart()
        fotoApparat.start()
        adjustViewsVisibility()
    }

    override fun onStop() {
        super.onStop()
        fotoApparat.stop()
    }

    private fun setupZoom(zoom: Zoom.VariableZoom) {

        zoomSeekBar.max = zoom.maxZoom
        cameraZoom = zoom
        zoomSeekBar.visibility = View.VISIBLE
        updateZoom(0)

        zoomSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateZoom(zoomSeekBar.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun updateZoom(progress: Int) {
        fotoApparat.setZoom(progress.toFloat() / zoomSeekBar.max)
        val value = cameraZoom.zoomRatios[progress]
        val roundedValue = ((value.toFloat()) / 10).roundToInt().toFloat() / 10
        //zoomLvl.text = String.format("%.1f ×", roundedValue)
    }


    private fun adjustViewsVisibility() {
        fotoApparat.getCapabilities().whenAvailable { capabilities ->
            if (capabilities != null) {
                (capabilities.zoom as? Zoom.VariableZoom)?.let { zoom -> setupZoom(zoom) }
                        ?: run { zoomSeekBar.visibility = View.GONE }
                torchSwitch.visibility = if (capabilities.flashModes.contains(Flash.Torch)) View.VISIBLE else View.GONE
            } else {
                toast("kamera tidak support 3")
                return@whenAvailable
            }
        }

        switchCamera.visibility = if (fotoApparat.isAvailable(front())) View.VISIBLE else View.GONE

    }

    val LOGGING_TAG = "Fotoapparat Example"

    private sealed class Camera(val lensPosition: LensPositionSelector,
                                val configuration: CameraConfiguration) {

        object BackNotSupportFocus :
                Camera(lensPosition = back(), configuration = CameraConfiguration(previewResolution = firstAvailable(wideRatio(highestResolution()), standardRatio(highestResolution())), previewFpsRange = highestFps(), flashMode = off(), frameProcessor = {
                    // Do something with the preview frame
                }))

        object BackSupportFocus :
                Camera(lensPosition = back(), configuration = CameraConfiguration(previewResolution = firstAvailable(wideRatio(highestResolution()), standardRatio(highestResolution())), previewFpsRange = highestFps(), flashMode = off(), focusMode = firstAvailable(continuousFocusPicture(), autoFocus()), frameProcessor = {
                    // Do something with the preview frame
                }))

        object FrontSupportFocus :
                Camera(lensPosition = front(), configuration = CameraConfiguration(previewResolution = firstAvailable(wideRatio(highestResolution()), standardRatio(highestResolution())), previewFpsRange = highestFps(), flashMode = off(), focusMode = firstAvailable(fixed(), autoFocus())))

        object FrontNotSupportFocus :
                Camera(lensPosition = front(), configuration = CameraConfiguration(previewResolution = firstAvailable(wideRatio(highestResolution()), standardRatio(highestResolution())), previewFpsRange = highestFps(), flashMode = off()))

    }

}

