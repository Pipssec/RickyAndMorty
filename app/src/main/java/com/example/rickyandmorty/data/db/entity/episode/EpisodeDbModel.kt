package com.example.rickyandmorty.data.db.entity.episode

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_episode")
data class EpisodeDbModel (
    val air_date: String,
    val created: String,
    val episode: String,
    @PrimaryKey()
    val id: Int,
    val name: String,
    val url: String
        )