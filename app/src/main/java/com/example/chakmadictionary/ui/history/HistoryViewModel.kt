package com.example.chakmadictionary.ui.history

import android.app.Application
import android.content.DialogInterface
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.HistoryWord
import com.example.chakmadictionary.database.WordsDao
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import timber.log.Timber

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