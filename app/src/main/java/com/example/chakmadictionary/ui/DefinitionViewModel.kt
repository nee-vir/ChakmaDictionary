package com.example.chakmadictionary.ui

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chakmadictionary.database.BookmarkWord
import com.example.chakmadictionary.database.DatabaseWord
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.network.NetworkObjectContainer
import com.example.chakmadictionary.network.NetworkWord
import com.example.chakmadictionary.network.asDatabaseModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class DefinitionViewModel(application: Application,private val datasource:WordsDao): AndroidViewModel(application) {


    private val _myWord= MutableLiveData<DatabaseWord>()

    val myWord: LiveData<DatabaseWord>
    get() = _myWord

    val list= mutableListOf<NetworkWord>()

    private val _bookmarkState=MutableLiveData<Boolean>()

    val bookMarkState:LiveData<Boolean>
    get() = _bookmarkState

//    private val _bookMarkWord= MutableLiveData<BookmarkWord>()
//
//    private val bookmarkWord:LiveData<BookmarkWord>
//    get() = _bookMarkWord

//    val job= Job()

//    val coroutineScope=(Dispatchers.Main+ Job())

    suspend fun retrieveWords(){
        val db=FirebaseFirestore.getInstance()
        db.collection("words").get().addOnSuccessListener { result ->
            for(document in result){
                val obj= document.toObject(NetworkWord::class.java)
                list.add(obj)
            }
            viewModelScope.launch {
                val wordsContainer=NetworkObjectContainer(list)
                datasource.insertAll(wordsContainer.asDatabaseModel())
            }
            viewModelScope.launch {
                val dbObject=datasource.getAllWords()
                Timber.i(dbObject.size.toString())
            }

        }.addOnFailureListener {
            Timber.d("Error getting documents : $it")
        }
    }

    val handled=MutableLiveData<Boolean>()
    init {
        handled.value=false
    }

     fun handleSuggestionIntent(intent:Intent?){
         viewModelScope.launch {
             _myWord.value=datasource.getWordById(intent?.dataString?.toInt())
             _bookmarkState.value=datasource.getBookmarkById(_myWord.value?.wordId)
         }
         Timber.i(intent?.data.toString())
    }



    fun retrieveFromDatabase(word:String?){
        viewModelScope.launch {
            _myWord.value=datasource.getWord(word)
            _bookmarkState.value=datasource.getBookmarkById(_myWord.value?.wordId)
        }
    }

    fun onBookmark(id:Long,word:String){
        if(_bookmarkState.value==false){
            val bookmarkWord=BookmarkWord(id,word)
            viewModelScope.launch {
                datasource.insertBookmark(bookmarkWord)
                _bookmarkState.value=true
            }

        } else{
            viewModelScope.launch {
                datasource.deleteBookmark(id)
                _bookmarkState.value=false
            }
        }

    }


}