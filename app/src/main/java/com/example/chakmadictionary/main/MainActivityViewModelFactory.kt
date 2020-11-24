package com.example.chakmadictionary.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chakmadictionary.database.WordsDao
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory(private val application: Application, private val dataSource: WordsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(application,dataSource) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}