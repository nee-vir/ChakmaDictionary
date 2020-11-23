package com.example.chakmadictionary.ui.bookmark

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chakmadictionary.database.WordsDao
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class BookmarkViewModelFactory(private val application: Application, private val dataSource:WordsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookmarkViewModel::class.java)){
            return BookmarkViewModel(application,dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}