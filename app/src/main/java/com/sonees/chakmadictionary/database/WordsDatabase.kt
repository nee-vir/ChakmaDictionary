package com.sonees.chakmadictionary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DatabaseWord::class,HistoryWord::class,BookmarkWord::class],version = 8)
abstract class WordsDatabase(): RoomDatabase() {
    abstract val wordsDao:WordsDao

    companion object{
        @Volatile
        private var INSTANCE: WordsDatabase?=null

        fun getInstance(context: Context): WordsDatabase{
            synchronized(this){
                var instance= INSTANCE
                if(instance==null){
                    instance= Room.databaseBuilder(context.applicationContext,
                        WordsDatabase::class.java,
                        "words_database").fallbackToDestructiveMigration().build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }

}