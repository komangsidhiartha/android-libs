package com.mamikos.mamiagent.networks.responses

import com.mamikos.mamiagent.entities.MediaEntity
import com.mamikos.mamiagent.entities.ReviewEntity
import com.mamikos.mamiagent.entities.SaveDataRoomEntity

class GetDataEditedResponse(error: Boolean, status: Boolean, meta: MetaEntity, message: String,
                            val data: SaveDataRoomEntity,
                            val review: ReviewEntity,
                            val photos: ArrayList<MediaEntity>) : StatusResponse(error, status, meta, message)