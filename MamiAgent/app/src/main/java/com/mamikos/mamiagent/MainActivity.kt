package com.mamikos.mamiagent

import android.widget.Toast
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.entities.DeviceEntity
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import org.jetbrains.anko.toast

class MainActivity : BaseActivity()
{
    override val layoutResource: Int = R.layout.activity_main

    override fun viewDidLoad()
    {
//        ProductApi.HatApi().execute(ProductResponse::class.java){ response: ProductResponse?, errorMessage: String? ->
//            when (response)
//            {
//                null ->
//                {
//                    showErrorMessage(errorMessage)
//                }
//                else ->
//                {
//                    logIfDebug("response hat $response")
//                }
//            }
//        }
//
//        ProductApi.ShirtApi().execute(ProductResponse::class.java){ response: ProductResponse?, errorMessage: String? ->
//            when (response)
//            {
//                null ->
//                {
//                    showErrorMessage(errorMessage)
//                }
//                else ->
//                {
//                    logIfDebug("response shirt $response")
//                }
//            }
//        }
//
//        ProductApi.JeansApi().execute(ProductResponse::class.java){ response: ProductResponse?, errorMessage: String? ->
//            when (response)
//            {
//                null ->
//                {
//                    showErrorMessage(errorMessage)
//                }
//                else ->
//                {
//                    logIfDebug("response jeans $response")
//                }
//            }
//        }

//        val roomApi = RoomApi.DataRoomApi()
//        roomApi.queryParam = mapOf(Pair("page", "0")).toList()
//        roomApi.postParam = mapOf(Pair("filters", Pair("room_name", "kia"))).toString()
//        roomApi.exec(RoomEntity::class.java){ response: RoomEntity?, errorMessage: String? ->
//            when (response)
//            {
//                null ->
//                {
//                    showErrorMessage(errorMessage)
//                }
//                else ->
//                {
//                    logIfDebug("response jeans $response")
//                }
//            }
//        }

    }

    private fun onFirstTime() {
//        var postParam

        val entity = DeviceEntity(MamiApp.app.getAndroidId(), MamiApp.app.getUUID(),
                "android", MamiApp.app.getModel(), MamiApp.app.getDeviceEmail(),
                MamiApp.app.getDevicePlatformVersionCode(), MamiApp.app.getDevicePlatformVersionCode())
    }

    private fun showErrorMessage(errorMessage: String?)
    {
        if (errorMessage != null)
            toast(errorMessage)
    }

    override fun onBackPressed() {
        logIfDebug("" + System.currentTimeMillis() + " - " +
                lastBackPressedTimeInMillis + " = " + (System.currentTimeMillis() - lastBackPressedTimeInMillis) + " "
                + isNeedToShowCloseWarning())
        if (isNeedToShowCloseWarning()) {
            Toast.makeText(this, R.string.msg_back_pressed, Toast.LENGTH_SHORT).show()
        }
        else
        {
            super.onBackPressed()
        }
        lastBackPressedTimeInMillis = System.currentTimeMillis()
    }
}
