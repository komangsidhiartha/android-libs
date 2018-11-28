package com.mamikos.mamiagent.networks.responses

import com.mamikos.mamiagent.entities.PassCodeEntity

/**
 * Created by root on 3/6/18.
 *
 */

data class PassCodeResponse(val data: PassCodeEntity ) : StatusResponse()