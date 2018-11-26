package com.mamikos.mamiagent.networks.responses

import com.mamikos.mamiagent.entities.AreaEntity

/**
 * Created by root on 3/6/18.
 *
 */

data class AreaResponse(val areaSubdistrict: ArrayList<AreaEntity> = arrayListOf(),
                        val areaCity: ArrayList<AreaEntity> = arrayListOf(),
                        val data: ArrayList<AreaEntity> = arrayListOf()) : StatusResponse()