package com.example.chakmadictionary.ui

import android.app.Application
import android.content.Intent
import android.view.View
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

    private val _showProgressBar=MutableLiveData<Boolean>()

    val showProgressBar:LiveData<Boolean>
    get() = _showProgressBar


    private val _wordNotFound=MutableLiveData<Boolean>()
    val wordNotFound:LiveData<Boolean>
    get() = _wordNotFound
//    private val _bookMarkWord= MutableLiveData<BookmarkWord>()
//
//    private val bookmarkWord:LiveData<BookmarkWord>
//    get() = _bookMarkWord

//    val job= Job()

//    val coroutineScope=(Dispatchers.Main+ Job())

    private suspend  fun retrieveWordsFromFirebase(){
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
        _showProgressBar.value=false
        _wordNotFound.value=false
        viewModelScope.launch {
            retrieveWordsFromFirebase()
        }

    }

     fun handleSuggestionIntent(intent:Intent?){
         viewModelScope.launch {
             _myWord.value=dataSource.getWordById(intent?.dataString?.toInt())
             handleVisibility()
             _bookmarkState.value=dataSource.getBookmarkById(_myWord.value?.wordId)

             //Insert history item
             val hWord=HistoryWord(_myWord.value?.wordId,_myWord.value?.word, getCurrentTime())
             dataSource.insertHistory(hWord)
         }
         Timber.i(intent?.data.toString())
    }




    fun retrieveFromDatabase(word:String?,from:Int=-1){
        viewModelScope.launch {
            _myWord.value=dataSource.getWord(word)
            Timber.i(_myWord.value.toString())
            handleVisibility()
            _bookmarkState.value=dataSource.getBookmarkById(_myWord.value?.wordId)
            //insert history item
            //Will not add history if its coming from the history fragment or if the query word is not found
            if(from!=0 && _myWord.value!=null){
                val hWord=HistoryWord(_myWord.value?.wordId,_myWord.value?.word, getCurrentTime())
                dataSource.insertHistory(hWord)
            }

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

    fun load(){
        _showProgressBar.value=true
    }

    fun loaded(){
        _showProgressBar.value=false
    }

    private fun handleVisibility(){
        _wordNotFound.value = _myWord.value==null
    }



}