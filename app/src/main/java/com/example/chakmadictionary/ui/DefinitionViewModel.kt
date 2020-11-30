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
import com.example.chakmadictionary.network.Quote
import com.example.chakmadictionary.network.asDatabaseModel
import com.example.chakmadictionary.utils.getCurrentTime
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class DefinitionViewModel(application: Application,private val dataSource:WordsDao): AndroidViewModel(application) {


    private val _myWord= MutableLiveData<DatabaseWord>()

    val myWord: LiveData<DatabaseWord>
    get() = _myWord

//    private val list= mutableListOf<NetworkWord>()

    private val _bookmarkState=MutableLiveData<Boolean>()

    val bookMarkState:LiveData<Boolean>
    get() = _bookmarkState

    private val _showProgressBar=MutableLiveData<Boolean>()

    val showProgressBar:LiveData<Boolean>
    get() = _showProgressBar


    private val _wordNotFound=MutableLiveData<Boolean>()
    val wordNotFound:LiveData<Boolean>
    get() = _wordNotFound



    //Data is retrived from firestore and stored to the database from the application class

    /*private suspend  fun retrieveWordsFromFirebase(){
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
        }.addOnFailureListener {
            Timber.d("Error getting documents : $it")
        }
    }*/

//    val handled=MutableLiveData<Boolean>()
    init {
//        handled.value=false
        _showProgressBar.value=false
        _wordNotFound.value=false
        /*viewModelScope.launch {
            retrieveWordsFromFirebase()
        }*/

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
            word?.let {
                addUsersQueryToFireStore(it)
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


    private fun addUsersQueryToFireStore(query: String){

        if(_wordNotFound.value==true){
            val db=FirebaseFirestore.getInstance()
            val map= hashMapOf<String,Any>(
                    "timeStamp" to FieldValue.serverTimestamp()
            )

            db.collection("searched_words").document(query).set(map).addOnSuccessListener {
                Timber.i("Successfully added $query to improve the app for better user experience")
            }.addOnFailureListener {
                Timber.e(it,"Could not add the the query")
            }
        }


    }








}