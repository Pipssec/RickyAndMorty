package com.example.rickyandmorty.data.db.entity.character

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_character")
data class CharacterDbModel (
    var created: String,
    var gender: String,
    @PrimaryKey()
    var id: Int,
    var image: String,
    var name: String,
    var species: String,
    var status: String,
    var type: String,
    var url: String,
    var location: String,
    var origin: String,
    )