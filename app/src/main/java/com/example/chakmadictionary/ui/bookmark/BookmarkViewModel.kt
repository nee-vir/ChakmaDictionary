package com.example.chakmadictionary.ui.bookmark

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.chakmadictionary.database.WordsDao
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application,private val dataSource:WordsDao) : AndroidViewModel(application) {

    private val sharedPreferences =PreferenceManager.getDefaultSharedPreferences(application)
    private val limit=sharedPreferences.getString("bookmarkItemLimit","20")

    val bookmarkList=dataSource.getAllBookmarks(limit!!.toInt())


    fun deleteBookmark(){

        viewModelScope.launch {
            dataSource.deleteEntireBookmark()
        }

    }

}