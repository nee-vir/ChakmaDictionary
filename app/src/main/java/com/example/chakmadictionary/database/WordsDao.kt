package com.example.chakmadictionary.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface WordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAll(words: List<DatabaseWord>)

    @Query("SELECT * FROM words_table WHERE word=:name OR translation=:name")
     suspend fun getWord(name: String?): DatabaseWord?

    @Query("SELECT * FROM words_table")
     suspend fun getAllWords(): List<DatabaseWord>

}