package com.example.chakmadictionary.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*


@Dao
interface WordsDao {

    @Insert(entity = DatabaseWord::class,onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAll(words: List<DatabaseWord>)

    @Query("SELECT * FROM words_table WHERE word=:name OR translation=:name")
     suspend fun getWord(name: String?): DatabaseWord?

    @Query("SELECT * FROM words_table")
     suspend fun getAllWords(): List<DatabaseWord>

    @Query("SELECT * FROM words_table WHERE wordId=:id")
    suspend fun getById(id:Long): DatabaseWord?


    //ContentProvider suggestion table queries
    //The column alias names has to be the same as these or the system will not understand
    @Query("SELECT wordId AS _id, word AS suggest_text_1, translation AS suggest_text_2, wordId AS suggest_intent_data FROM words_table WHERE word LIKE :name OR translation LIKE :name")
    fun getSuggestionWord(name:String?):Cursor?

    @Query("DELETE FROM words_table WHERE word= :name OR translation=:name")
    fun deleteWords(name:String?):Int

    @Query("SELECT*FROM words_table where wordId=:id")
    suspend fun getWordById(id:Int?): DatabaseWord?



    //History
    //When you want to return an observable there is no need to make the function suspend
    @Query("SELECT*FROM history_table ORDER BY historyId DESC LIMIT :limit")
    fun getEntireHistory(limit:Int=20):LiveData<List<HistoryWord>?>

    @Insert(entity = HistoryWord::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(word:HistoryWord)

    @Query("DELETE FROM history_table where historyId=:hId")
    suspend fun deleteHistoryItem(hId:Long?)

    @Query("DELETE FROM history_table")
    suspend fun deleteEntireHistory()




    //Bookmark
    @Query("SELECT*FROM bookmark_table ORDER BY time DESC LIMIT :limit")
     fun getAllBookmarks(limit: Int=20):LiveData<List<BookmarkWord>>

    @Query("SELECT EXISTS(SELECT*FROM bookmark_table WHERE word=:wordString)")
     suspend fun getBookmarkByWord(wordString:String?):Boolean

    @Insert(entity = BookmarkWord::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(word:BookmarkWord)

    @Query("DELETE FROM bookmark_table where wordId=:id")
    suspend fun deleteBookmark(id:Long?)

    @Query("DELETE FROM bookmark_table")
    suspend fun deleteEntireBookmark()

}