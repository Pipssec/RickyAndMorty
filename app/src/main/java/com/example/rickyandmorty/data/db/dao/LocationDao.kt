package com.example.rickyandmorty.data.db.dao

import androidx.room.*
import com.example.rickyandmorty.data.db.entity.location.LocationDbModel
import io.reactivex.Observable

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(list: List<LocationDbModel>)

    @Query(
        "SELECT * FROM item_location WHERE (name LIKE  '%' || :searchName || '%' OR :searchName = '') " +
                "AND (type LIKE :type OR :type = '') AND (dimension LIKE :dimension OR :dimension ='') " +
                "LIMIT :limit OFFSET :offset"
    )
    suspend fun getAllLocationPage(
        offset: Int,
        limit: Int,
        searchName: String,
        type: String,
        dimension: String
    ): List<LocationDbModel>

    @Query("DELETE FROM item_location")
    suspend fun deleteAllLocations()

    @Query("SELECT * FROM item_location WHERE name LIKE :name")
    fun getLocationDb(name: String): Observable<List<LocationDbModel>>

    @Transaction
    suspend fun refreshLocations(list: List<LocationDbModel>) {
        deleteAllLocations()
        insertLocation(list)
    }
}