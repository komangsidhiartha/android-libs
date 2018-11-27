package com.mamikos.mamiagent.networks.responses

import com.mamikos.mamiagent.entities.MediaEntity

class MediaResponse(error: Boolean, status: Boolean, meta: MetaEntity, message: String,
                    var media: MediaEntity,
                    val id: Int, val photo: String) : StatusResponse(error, status, meta, message)