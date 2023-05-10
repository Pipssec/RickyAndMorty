package com.example.rickyandmorty.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.rickyandmorty.data.db.entity.location.LocationDbModel

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(list: List<LocationDbModel>)

    @Query("SELECT * FROM item_location")
    fun getAllLocation(): List<LocationDbModel>

    @Query("DELETE FROM item_location")
    suspend fun deleteAllLocations()

    @Transaction
    suspend fun refreshLocations(list: List<LocationDbModel>){
        deleteAllLocations()
        insertLocation(list)
    }
}