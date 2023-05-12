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

    @Query("SELECT * FROM item_character WHERE (name LIKE  '%' || :searchName || '%' OR :searchName = '') " +
            "AND (gender LIKE :gender OR :gender = '') " +
            "AND (status LIKE :status OR :status = '') " +
            "AND (species LIKE :species OR :species = '') LIMIT :limit OFFSET :offset")
    suspend fun getCharactersPage(offset: Int, limit: Int, searchName: String,
                                  gender: String,status: String,species: String): List<CharacterDbModel>

    @Query("DELETE FROM item_character")
    suspend fun deleteAllCharacters()

    @Transaction
    suspend fun refreshCharacters(list: List<CharacterDbModel>){
        deleteAllCharacters()
        insertCharacter(list)
    }
}