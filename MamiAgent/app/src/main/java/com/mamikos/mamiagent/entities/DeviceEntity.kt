package com.mamikos.mamiagent.entities

data class DeviceEntity(var deviceIdentifier: String,
                              var deviceUuid: String,
                              var devicePlatform: String,
                              var deviceModel: String,
                              var deviceEmail: String,
                              var devicePlatformVersionCode: Int,
                              var appVersionCode: Int)