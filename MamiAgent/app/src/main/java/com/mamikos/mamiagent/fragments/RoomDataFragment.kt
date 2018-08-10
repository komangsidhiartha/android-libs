package com.mamikos.mamiagent.fragments

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.mamikos.mamiagent.DetailRoomActivity
import com.mamikos.mamiagent.ListRoomActivity
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.adapters.ListDataRoomAdapter
import com.mamikos.mamiagent.entities.RoomEntity
import com.mamikos.mamiagent.networks.apis.RoomApi
import com.mamikos.mamiagent.networks.responses.ListRoomResponse
import com.sidhiartha.libs.apps.logIfDebug
import com.sidhiartha.libs.fragments.BaseSupportFragment
import kotlinx.android.synthetic.main.fragment_room_data.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class RoomDataFragment : BaseSupportFragment() {
    companion object {
        fun newInstance(type: String): RoomDataFragment {
            val fragment = RoomDataFragment()
            val args = Bundle()
            args.putString("type", type)
            fragment.setArguments(args)
            return fragment
        }
    }

    var type = ""

    lateinit var roomAdapter: ListDataRoomAdapter
    var api: RoomApi? = null

    var rooms: ArrayList<RoomEntity> = ArrayList()
    var hasMoreData = true
    var nextPageData = 1

    override val layoutResource = R.layout.fragment_room_data

    override fun viewDidLoad() {
        type = arguments?.getString("type").toString()
        rvDataRooms.layoutManager = LinearLayoutManager(this.context)
        rvDataRooms.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

        roomAdapter = ListDataRoomAdapter(this.context!!, rooms, { getListDataRooms() }) {
            openDetailRoom(it)
        }
        rvDataRooms.adapter = roomAdapter
        getListDataRooms()
    }


    fun reload() {
        rooms.clear()
        roomAdapter.notifyDataSetChanged()
        hasMoreData = true
        nextPageData = 1
        getListDataRooms()
    }

    fun getListDataRooms() {
        if (!hasMoreData || roomAdapter.isLoading)
            return

        logIfDebug("TYPE $type")
        (this.activity as ListRoomActivity).showLoadingBar()
        roomAdapter.isLoading = true
        if (type == ListRoomActivity.TYPE_AVAIL) {
            api = RoomApi.ListDataRoomApi()
            api?.queryParam = mapOf(
                    Pair("page", nextPageData.toString()),
                    Pair("lat", (this.activity as ListRoomActivity).mLocation?.latitude.toString()),
                    Pair("long", (this.activity as ListRoomActivity).mLocation?.longitude.toString())).toList()
            api?.exec(ListRoomResponse::class.java) { response: ListRoomResponse?, errorMessage: String? ->
                (this.activity as ListRoomActivity).hideLoadingBar()
                roomAdapter.isLoading = false
                when (response) {
                    null ->
                        errorMessage?.let { toast(it) }
                    else -> {
                        logIfDebug("response " + response.toString())
                        if (response.status) {
                            proccessResponse(response)
                        } else
                            toast("" + response.message)
                    }
                }
            }
        } else {
            api = RoomApi.ListEditedRoomApi()
            api?.queryParam = mapOf(Pair("page", nextPageData.toString())).toList()
            api?.exec(ListRoomResponse::class.java) { response: ListRoomResponse?, errorMessage: String? ->
                (this.activity as ListRoomActivity).hideLoadingBar()
                roomAdapter.isLoading = false
                when (response) {
                    null ->
                        errorMessage?.let { toast(it) }
                    else -> {
                        logIfDebug("response " + response.toString())
                        if (response.status) {
                            proccessResponse(response)
                        } else
                            toast("" + response.message)
                    }
                }
            }
        }
    }

    fun proccessResponse(response: ListRoomResponse) {
        if (nextPageData == 1)
            rooms.clear()
        rooms.addAll(response.data.rooms)
        nextPageData = response.data.nextPage
        hasMoreData = response.data.hasMore

        roomAdapter.notifyDataSetChanged()
    }

    fun openDetailRoom(roomEntity: RoomEntity) {
        logIfDebug("entity " + roomEntity.toString())
        when (roomEntity.statuses) {
            RoomEntity.STATUS_DEFAULT -> {
                activity?.startActivity<DetailRoomActivity>(ListRoomActivity.ROOM_EXTRA to roomEntity)
            }
            RoomEntity.STATUS_CHECKIN -> {
                activity?.startActivity<DetailRoomActivity>(ListRoomActivity.ROOM_EXTRA to roomEntity)
            }
            RoomEntity.STATUS_PHOTO -> {
                activity?.startActivity<DetailRoomActivity>(ListRoomActivity.ROOM_EXTRA to roomEntity)
            }
            RoomEntity.STATUS_REVIEW -> {
                activity?.startActivity<DetailRoomActivity>(ListRoomActivity.ROOM_EXTRA to roomEntity)
            }
            RoomEntity.STATUS_SUBMIT -> {

            }
            RoomEntity.STATUS_VALID -> {

            }
            RoomEntity.STATUS_INVALID -> {
                activity?.startActivity<DetailRoomActivity>(ListRoomActivity.ROOM_EXTRA to roomEntity)
            }
        }
    }

}