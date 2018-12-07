package com.mamikos.mamiagent.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize data class HistoryEntity(val name: String, val reason: String, val ownerPhone: String,
                                    val uploadedAt: String, val status: String,
                                    val verifOrNotAt: String, val isDuplicate: Boolean) : Parcelable