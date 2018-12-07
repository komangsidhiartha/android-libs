package com.mamikos.mamiagent.networks.responses

class ListRoomResponse(error: Boolean, status: Boolean, meta: MetaEntity, message: String,
                       val data: RoomResponse) : StatusResponse(error, status, meta, message)