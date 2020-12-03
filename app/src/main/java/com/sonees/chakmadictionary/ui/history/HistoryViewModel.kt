package com.sonees.chakmadictionary.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.sonees.chakmadictionary.database.WordsDao
import kotlinx.coroutines.*

class HistoryViewModel(application: Application, private val datasource: WordsDao): AndroidViewModel(application) {




   val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
  private val limit=sharedPreferences.getString("historyItemLimit","10")
  val historyList=datasource.getEntireHistory(limit!!.toInt())

//    private val _isEmpty= MutableLiveData<Boolean>()
//    val isEmpty:LiveData<Boolean>
//    get() = _isEmpty


  fun deleteHistory(){
    viewModelScope.launch {
      datasource.deleteEntireHistory()
    }
  }













}