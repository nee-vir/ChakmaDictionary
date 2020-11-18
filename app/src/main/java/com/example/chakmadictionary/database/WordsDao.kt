package com.example.chakmadictionary.database

import android.database.Cursor
import androidx.room.*


@Dao
interface WordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAll(words: List<DatabaseWord>)

    @Query("SELECT * FROM words_table WHERE word=:name OR translation=:name")
     suspend fun getWord(name: String?): DatabaseWord?

    @Query("SELECT * FROM words_table")
     suspend fun getAllWords(): List<DatabaseWord>

    @Query("SELECT wordId AS _id, word AS suggest_text_1, definition AS suggest_text_2, wordId AS suggest_column_intent_data FROM words_table WHERE word LIKE :name OR translation LIKE :name")
    fun getSuggestionWord(name:String?):Cursor?

    @Query("DELETE FROM words_table WHERE word= :name OR translation=:name")
    fun deleteWords(name:String?):Int

}