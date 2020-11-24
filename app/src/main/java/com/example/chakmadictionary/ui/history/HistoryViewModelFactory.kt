package com.example.chakmadictionary.ui.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chakmadictionary.database.WordsDao
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class HistoryViewModelFactory(private val application: Application, private val dataSource: WordsDao): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(application,dataSource) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}