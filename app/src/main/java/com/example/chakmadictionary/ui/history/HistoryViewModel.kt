package com.example.chakmadictionary.ui.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chakmadictionary.database.HistoryWord
import com.example.chakmadictionary.database.WordsDao
import kotlinx.coroutines.*
import timber.log.Timber

class HistoryViewModel(application: Application, private val datasource: WordsDao): AndroidViewModel(application) {



  val historyList=datasource.getEntireHistory()

//    private val _isEmpty= MutableLiveData<Boolean>()
//    val isEmpty:LiveData<Boolean>
//    get() = _isEmpty


  fun deleteHistory(){
    viewModelScope.launch {
      datasource.deleteEntireHistory()
    }
  }






}