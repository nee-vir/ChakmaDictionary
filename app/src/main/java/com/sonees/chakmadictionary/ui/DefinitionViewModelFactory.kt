package com.sonees.chakmadictionary.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sonees.chakmadictionary.database.WordsDao
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class DefinitionViewModelFactory(private val application: Application, private val dataSource:WordsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DefinitionViewModel::class.java)){
            return DefinitionViewModel(application,dataSource) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}