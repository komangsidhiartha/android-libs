package com.mamikos.mamiagent.networks.responses

class MediaResponse(error: Boolean, status: Boolean, meta: MetaEntity, message: String,
                    val id: Int, val photo: String) : StatusResponse(error, status, meta, message)