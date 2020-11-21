package com.example.chakmadictionary.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "words_table")
data class DatabaseWord constructor(
    @PrimaryKey
    val wordId:Long=0L,
    val word:String?=null,
    val translation: String?=null,
    val definition:String?=null,
    val example: String?=null,
    val example2: String?=null,
    val imageUrl:String?=null,
    val soundUrl:String?=null,
    val synonyms:String?=null)


@Entity(tableName = "history_table")
data class HistoryWord constructor(
        @PrimaryKey
        val wordId:Long=0L,
        val word: String?=null
)


@Entity(tableName = "bookmark_table")
data class BookmarkWord constructor(
        @PrimaryKey
        val wordId:Long=0L,
        val word: String?=null
)
