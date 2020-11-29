package com.example.chakmadictionary.network

import com.example.chakmadictionary.database.DatabaseWord


data class NetworkObjectContainer(val words: List<NetworkWord>)

data class NetworkWord(val wordId:Long=0L,
                       val word:String?=null,
                       val translation: String?=null,
                       val definition:String?=null,
                       val example: String?=null,
                       val example2: String?=null,
                       val imageUrl:String?=null,
                       val soundUrl:String?=null,
                       val synonyms:String?=null)


fun NetworkObjectContainer.asDatabaseModel():List<DatabaseWord>{
    return words.map {
        DatabaseWord(
            word = it.word?.toLowerCase(),
            translation = it.translation?.toLowerCase(),
            definition = it.definition.toString(),
            example = it.example.toString(),
            example2 = it.example2.toString(),
            imageUrl = it.imageUrl.toString(),
            soundUrl = it.soundUrl.toString(),
            synonyms = it.synonyms?.toLowerCase()
        )
    }
}


data class Quote(val quote:String?=null,
                 val author:String?=null)