package com.mamikos.mamiagent.networks.responses

class StatisticResponse(error: Boolean, status: Boolean, meta: MetaEntity, message: String,
                        val agentName: String, val data: StatisticModel) : StatusResponse(error, status, meta, message)

class StatisticModel(val submit: Int, val live: Int, val reject: Int)