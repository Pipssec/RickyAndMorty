package com.example.rickyandmorty.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rickyandmorty.data.db.dao.LocationDao
import com.example.rickyandmorty.data.db.entity.location.LocationDbModel


@Database(entities = [LocationDbModel::class], version = 2)
abstract class LocationDatabase: RoomDatabase() {
    abstract fun getLocationDao(): LocationDao

    companion object{
        private var database: LocationDatabase? = null
        private val ANY = Any()

        fun getMainDatabase(context: Context): LocationDatabase {
            synchronized(ANY){
                database?.let {
                    return it
                }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "locationDb"
                ).build()
                database = instance
                return instance
            }
        }
    }
}