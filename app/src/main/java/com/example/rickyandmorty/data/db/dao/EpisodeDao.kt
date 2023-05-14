package com.example.rickyandmorty.data.db.dao

import androidx.room.*
import com.example.rickyandmorty.data.db.entity.episode.EpisodeDbModel


@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(list: List<EpisodeDbModel>)

    @Query(
        "SELECT * FROM item_episode WHERE (name LIKE  '%' || :searchName || '%' OR :searchName = '') " +
                "AND (episode LIKE :episode OR :episode = '') LIMIT :limit OFFSET :offset"
    )
    suspend fun getAllEpisodesPage(
        offset: Int,
        limit: Int,
        searchName: String,
        episode: String
    ): List<EpisodeDbModel>

    @Query("DELETE FROM item_episode")
    suspend fun deleteAllEpisodes()

    @Transaction
    suspend fun refreshEpisodes(list: List<EpisodeDbModel>) {
        deleteAllEpisodes()
        insertEpisode(list)
    }
}