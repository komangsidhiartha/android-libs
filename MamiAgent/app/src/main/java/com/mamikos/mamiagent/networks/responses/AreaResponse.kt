package com.mamikos.mamiagent.networks.responses

import com.mamikos.mamiagent.entities.AreaEntity

/**
 * Created by root on 3/6/18.
 *
 */

data class AreaResponse(val areaSubdistrict: ArrayList<AreaEntity>,
                        val areaCity: ArrayList<AreaEntity>,
                        val data: ArrayList<AreaEntity>) : StatusResponse()