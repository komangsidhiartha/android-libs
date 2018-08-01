package com.mamikos.mamiagent.networks.responses

import com.sidhiartha.libs.networks.StatusResponse

open class StatusResponse(error: Boolean, status: Int, meta: MetaEntity) : StatusResponse(error, status)

class MetaEntity(var responseCode: Int, var code: Int, line: Int, responseTime: String,
                 severity: String, var message: String, var file: String)