package com.example.chakmadictionary.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.chakmadictionary.database.HistoryWord
import com.example.chakmadictionary.database.WordsDao
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application, private val datasource: WordsDao): AndroidViewModel(application) {

//    val historyList=datasource.getEntireHistory()

    lateinit var historyList: LiveData<List<HistoryWord>>

    init {
        viewModelScope.launch {
            historyList=datasource.getEntireHistory()
        }
    }

}