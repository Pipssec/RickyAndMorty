package com.example.rickyandmorty.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.rickyandmorty.data.db.entity.character.CharacterDbModel

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(list: List<CharacterDbModel>)

    @Query("SELECT * FROM item_character")
    fun getAllCharacters(): List<CharacterDbModel>

    @Query("DELETE FROM item_character")
    suspend fun deleteAllCharacters()

    @Transaction
    suspend fun refreshCharacters(list: List<CharacterDbModel>){
        deleteAllCharacters()
        insertCharacter(list)
    }
}