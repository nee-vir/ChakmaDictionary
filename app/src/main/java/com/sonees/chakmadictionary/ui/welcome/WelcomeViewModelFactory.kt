package com.sonees.chakmadictionary.ui.welcome

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sonees.chakmadictionary.database.WordsDao
import java.lang.IllegalArgumentException

class WelcomeViewModelFactory(private val application: Application,private val dataSource:WordsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WelcomeViewModel::class.java)){
            return WelcomeViewModel(application,dataSource) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}