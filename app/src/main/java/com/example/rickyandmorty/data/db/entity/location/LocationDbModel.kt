package com.example.rickyandmorty.data.db.entity.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_location")
data class LocationDbModel(
    var created: String,
    var dimension: String,
    @PrimaryKey()
    var id: Int,
    var name: String,
    var type: String,
    var url: String
)
