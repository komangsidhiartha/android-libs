package com.mamikos.mamiagent.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.adapters.ListDataFormAdapter
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.sidhiartha.libs.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_list_data_form.*
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.git.dabang.database.table.FormDataTable
import com.mamikos.mamiagent.entities.PhotoFormEntity
import com.mamikos.mamiagent.entities.SaveKostEntity
import com.mamikos.mamiagent.helpers.ExceptionHandler
import com.mamikos.mamiagent.networks.apis.SaveKosApi
import com.mamikos.mamiagent.networks.responses.MessagesResponse
import com.sidhiartha.libs.utils.GSONManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Dedi Dot on 12/4/2018.
 * Happy Coding!
 */

class ListDataFormActivity : BaseActivity() {

    var dataKos: List<FormDataTable>? = null
    var adapter: ListDataFormAdapter? = null

    override val layoutResource: Int = R.layout.activity_list_data_form

    override fun viewDidLoad() {

        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))

        EventBus.getDefault().register(this)

        adapter = ListDataFormAdapter()
        dataFormRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dataFormRecyclerView.adapter = adapter

        LoadDataLocal().execute()

        UtilsHelper.setupToolbar(this, dataFormToolbar, getString(R.string.msg_local_data), "", R.drawable.ic_arrow_back_white_24dp, Runnable {
            onBackPressed()
        })

        swipeRefreshLayout.isEnabled = false
    }

    @SuppressLint("StaticFieldLeak") private inner class LoadDataLocal :
            AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            loading?.show()
        }

        override fun doInBackground(vararg dataDoInBackground: String): String? {
            dataKos = MamiApp.instance?.appDatabase?.formDataDao()?.getAll?.toList()
            adapter?.setData(dataKos)
            return ""
        }

        override fun onPostExecute(z: String) {
            adapter?.notifyDataSetChanged()
            loading?.hide()
        }
    }

    @Subscribe fun onEvent(data: FormDataTable?) {
        UtilsHelper.log("${data?.kosName}")

        val saveKos = SaveKostEntity()

        saveKos.province = data?.provinceName
        saveKos.areaCity = data?.cityName
        saveKos.city = data?.cityName
        saveKos.subdistrict = data?.subDistrictName

        data?.latitude?.let {
            saveKos.latitude = it
        }
        data?.longitude?.let {
            saveKos.longitude = it
        }
        data?.agentLatitude?.let {
            saveKos.agentLat = it
        }
        data?.agentLongitude?.let {
            saveKos.agentLong = it
        }

        saveKos.address = data?.address
        saveKos.name = data?.kosName
        saveKos.wifiSpeed = data?.wifiSpeed

        data?.gender?.let {
            saveKos.gender = it
        }

        val roomSize: ArrayList<String> = arrayListOf()
        val splitData = data?.roomSize?.split(",")
        splitData?.let {
            roomSize.add(it[0])
            roomSize.add(it[1])
        }
        saveKos.roomSize = roomSize

        data?.roomCount?.let {
            saveKos.roomCount = it
        }

        data?.roomAvailable?.let {
            saveKos.roomAvailable = it
        }

        data?.priceDaily?.let {
            saveKos.priceDaily = it.toInt()
        }

        data?.priceMonthly?.let {
            saveKos.priceMonthly = it.toInt()
        }

        data?.priceYearly?.let {
            saveKos.priceYearly = it.toLong()
        }

        data?.minMonth?.let {
            saveKos.minMonth = it
        }

        val splitRoomFac = data?.roomFacility?.split(",")
        splitRoomFac?.let {
            val selectedFacRoom = arrayListOf<Int>()
            for (i in 0 until it.size) {
                if (it[i].isNotEmpty()) {
                    selectedFacRoom.add(it[i].toInt())
                }
            }
            saveKos.facRoom = selectedFacRoom
        }

        val splitBathRoomFac = data?.bathroomFacility?.split(",")
        splitBathRoomFac?.let {
            val selectedFacBathRoom = arrayListOf<Int>()
            for (i in 0 until it.size) {
                if (it[i].isNotEmpty()) {
                    selectedFacBathRoom.add(it[i].toInt())
                }
            }
            saveKos.facBath = selectedFacBathRoom
        }

        saveKos.photos = PhotoFormEntity()

        val splitBathRoomPhoto = data?.photoBathroomBuilding?.split(",")
        splitBathRoomPhoto?.let {
            saveKos.photos.bath = arrayListOf()
            saveKos.photos.bath?.let { y ->
                val photoId = splitBathRoomPhoto[0].toInt()
                y.add(photoId)
            }
        }

        val splitInsideRoomPhoto = data?.photoInsideBuilding?.split(",")
        splitInsideRoomPhoto?.let {
            val photoId = it[0].toInt()
            saveKos.photos.mains = photoId
        }

        val splitKosPhoto = data?.photoKosBuildingBuilding?.split(",")
        splitKosPhoto?.let {
            val photoId = it[0].toInt()
            saveKos.photos.cover = photoId
        }

        if (data?.isElectricity == 1) {
            saveKos.withListrik = 1
            saveKos.withoutListrik = 0
        } else {
            saveKos.withListrik = 0
            saveKos.withoutListrik = 1
        }

        saveKos.ownerName = data?.ownerName
        saveKos.ownerEmail = data?.ownerEmail
        saveKos.ownerPhone = data?.ownerPhone
        saveKos.inputAs = "agen"

        goSave(saveKos, data?.id.toString())

    }

    private fun goSave(saveKos: SaveKostEntity?, formId: String?) {
        val apiSave = SaveKosApi.SaveKost()
        loading?.show()
        apiSave.postParam = GSONManager.toJson(saveKos)
        UtilsHelper.log(apiSave.postParam)

        apiSave.exec(MessagesResponse::class.java) { response: MessagesResponse?, _: String? ->

            loading?.hide()
            var msg = ""

            if (response == null) {
                UtilsHelper.showDialogYes(this, "", "Server lagi error, hubungi pihak developer", Runnable {}, 0)
                return@exec
            }

            if (response.status) {
                MamiApp.instance?.appDatabase?.formDataDao()?.deleteById(formId.toString())
                LoadDataLocal().execute()
                msg = "Berhasil kirim kos, terima kasih!"
                UtilsHelper.showDialogYes(this, "", msg, Runnable {}, 0)
            } else {
                response.messages?.size?.let {
                    for (i in 0 until it) {
                        msg += response.messages[i] + " "
                    }
                }
                UtilsHelper.showDialogYes(this, "", "$msg gagal kirim", Runnable {}, 0)
            }
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_list_data_form_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.clearMenu) {
            MamiApp.instance?.appDatabase?.formDataDao()?.clearAll()
            LoadDataLocal().execute()
        }
        return true
    }

}