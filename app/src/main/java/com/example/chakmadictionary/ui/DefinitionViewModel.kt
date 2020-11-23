package com.example.chakmadictionary.ui

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chakmadictionary.database.*
import com.example.chakmadictionary.network.NetworkObjectContainer
import com.example.chakmadictionary.network.NetworkWord
import com.example.chakmadictionary.network.asDatabaseModel
import com.example.chakmadictionary.utils.getCurrentTime
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import timber.log.Timber

class DefinitionViewModel(application: Application,private val dataSource:WordsDao): AndroidViewModel(application) {


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

    private suspend  fun retrieveWords(){
        val db=FirebaseFirestore.getInstance()
        db.collection("words").get().addOnSuccessListener { result ->
            for(document in result){
                val obj= document.toObject(NetworkWord::class.java)
                list.add(obj)
            }
            viewModelScope.launch {
                val wordsContainer=NetworkObjectContainer(list)
                dataSource.insertAll(wordsContainer.asDatabaseModel())
            }
//            viewModelScope.launch {
//                val dbObject=datasource.getAllWords()
//                Timber.i(dbObject.size.toString())
//            }

        }.addOnFailureListener {
            Timber.d("Error getting documents : $it")
        }
    }

    val handled=MutableLiveData<Boolean>()
    init {
        handled.value=false
        viewModelScope.launch {
            retrieveWords()
        }

    }

     fun handleSuggestionIntent(intent:Intent?){
         viewModelScope.launch {
             _myWord.value=dataSource.getWordById(intent?.dataString?.toInt())
             _bookmarkState.value=dataSource.getBookmarkById(_myWord.value?.wordId)

             //Insert history item
             val hWord=HistoryWord(_myWord.value?.wordId,_myWord.value?.word, getCurrentTime())
             dataSource.insertHistory(hWord)
         }
         Timber.i(intent?.data.toString())
    }




    fun retrieveFromDatabase(word:String?){
        viewModelScope.launch {
            _myWord.value=dataSource.getWord(word)
            _bookmarkState.value=dataSource.getBookmarkById(_myWord.value?.wordId)

            //insert history item
            val hWord=HistoryWord(_myWord.value?.wordId,_myWord.value?.word, getCurrentTime())
            dataSource.insertHistory(hWord)
        }
    }


    fun onBookmark(id:Long,word:String){
        if(_bookmarkState.value==false){
            val bookmarkWord=BookmarkWord(id,word,System.currentTimeMillis())
            viewModelScope.launch {
                dataSource.insertBookmark(bookmarkWord)
                _bookmarkState.value=true
            }

        } else{
            viewModelScope.launch {
                dataSource.deleteBookmark(id)
                _bookmarkState.value=false
            }
        }

    }


}