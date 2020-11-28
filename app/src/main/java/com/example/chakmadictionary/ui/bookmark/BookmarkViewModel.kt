package com.example.chakmadictionary.ui.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chakmadictionary.database.WordsDao
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application,private val dataSource:WordsDao) : AndroidViewModel(application) {

    val bookmarkList=dataSource.getAllBookmarks()


    fun deleteBookmark(){

        viewModelScope.launch {
            dataSource.deleteEntireBookmark()
        }

    }

}