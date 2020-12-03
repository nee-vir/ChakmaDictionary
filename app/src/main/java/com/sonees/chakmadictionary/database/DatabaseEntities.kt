package com.sonees.chakmadictionary.database

import androidx.room.*


@Entity(tableName = "words_table",indices = [Index(value = ["word","translation"],unique = true)])
data class DatabaseWord constructor(
    val word:String?=null,
    val translation: String?=null,
    val definition:String?=null,
    val example: String?=null,
    val example2: String?=null,
    val imageUrl:String?=null,
    val soundUrl:String?=null,
    val synonyms:String?=null){

    @PrimaryKey(autoGenerate = true)
    var wordId:Long=0L
}


@Entity(tableName = "history_table")
data class HistoryWord constructor(
        val wordId:Long?=0L,
        val word: String?=null,
        val searchTime: String?=null
){
    @PrimaryKey(autoGenerate = true)
    var historyId:Long=0L
}


@Entity(tableName = "bookmark_table")
data class BookmarkWord constructor(
        @PrimaryKey
        val wordId:Long=0L,
        val word: String?=null,
        val time: Long?=0L
)
