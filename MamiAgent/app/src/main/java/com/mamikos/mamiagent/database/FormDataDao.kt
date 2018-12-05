package com.git.dabang.database

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.git.dabang.database.table.FormDataTable

/**
 * Created by Dedi Dot on 9/19/2018.
 * Happy Coding!
 * https://developer.android.com/training/data-storage/room/accessing-data
 */

@Dao interface FormDataDao {

    @get:Query("SELECT * FROM form_data") val getAll: List<FormDataTable>

    @Query("SELECT * FROM form_data WHERE province_name" + " LIKE :query") fun getAllByName(
            query: String): List<FormDataTable>

    @Query("SELECT * FROM form_data LIMIT :start,:end ") fun getDataByPage(start: String,
                                                                           end: String): List<FormDataTable>?

    @Insert fun insert(vararg insertData: FormDataTable)

    @Delete fun delete(deleteData: FormDataTable)

    @Query("DELETE FROM form_data") fun clearAll()

}