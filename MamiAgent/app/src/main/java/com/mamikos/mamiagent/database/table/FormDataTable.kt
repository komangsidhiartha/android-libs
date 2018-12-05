package com.git.dabang.database.table

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Dedi Dot on 9/18/2018.
 * Happy Coding!
 */

@Entity(tableName = "form_data") data class FormDataTable(@ColumnInfo(name = "province_name")
                                                          var provinceName: String,
                                                          @ColumnInfo(name = "city_name")
                                                          var cityName: String,
                                                          @ColumnInfo(name = "sub_district_name")
                                                          var subDistrictName: String,
                                                          @ColumnInfo(name = "latitude")
                                                          var latitude: Double,
                                                          @ColumnInfo(name = "longitude")
                                                          var longitude: Double,
                                                          @ColumnInfo(name = "agent_latitude")
                                                          var agentLatitude: Double,
                                                          @ColumnInfo(name = "agent_longitude")
                                                          var agentLongitude: Double,
                                                          @ColumnInfo(name = "address")
                                                          var address: String,
                                                          @ColumnInfo(name = "kos_name")
                                                          var kosName: String,
                                                          @ColumnInfo(name = "gender")
                                                          var gender: Int,
                                                          @ColumnInfo(name = "room_size")
                                                          var roomSize: String,
                                                          @ColumnInfo(name = "room_count")
                                                          var roomCount: Int,
                                                          @ColumnInfo(name = "room_available")
                                                          var roomAvailable: String,
                                                          @ColumnInfo(name = "price_daily")
                                                          var priceDaily: String,
                                                          @ColumnInfo(name = "price_weekly")
                                                          var priceWeekly: String,
                                                          @ColumnInfo(name = "price_monthly")
                                                          var priceMonthly: String,
                                                          @ColumnInfo(name = "price_yearly")
                                                          var priceYearly: String,
                                                          @ColumnInfo(name = "min_month")
                                                          var minMonth: Int,
                                                          @ColumnInfo(name = "wifi_speed")
                                                          var wifiSpeed: String,
                                                          @ColumnInfo(name = "room_facility")
                                                          var roomFacility: String,
                                                          @ColumnInfo(name = "bathroom_facility")
                                                          var bathroomFacility: String,
                                                          @ColumnInfo(name = "photo_bathroom_building")
                                                          var photoBathroomBuilding: String,
                                                          @ColumnInfo(name = "photo_inside_building")
                                                          var photoInsideBuilding: String,
                                                          @ColumnInfo(name = "photo_kos_building")
                                                          var photoKosBuildingId: String,
                                                          @ColumnInfo(name = "owner_name")
                                                          var ownerName: String,
                                                          @ColumnInfo(name = "owner_email")
                                                          var ownerEmail: String,
                                                          @ColumnInfo(name = "owner_phone")
                                                          var ownerPhone: String) {

    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0

}
