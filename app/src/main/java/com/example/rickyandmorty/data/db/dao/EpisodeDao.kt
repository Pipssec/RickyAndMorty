package com.example.rickyandmorty.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.rickyandmorty.data.db.entity.episode.EpisodeDbModel


@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(list: List<EpisodeDbModel>)

    @Query("SELECT * FROM item_episode")
    fun getAllEpisodes(): List<EpisodeDbModel>

    @Query("DELETE FROM item_episode")
    suspend fun deleteAllEpisodes()

    @Transaction
    suspend fun refreshEpisodes(list: List<EpisodeDbModel>){
        deleteAllEpisodes()
        insertEpisode(list)
    }
}