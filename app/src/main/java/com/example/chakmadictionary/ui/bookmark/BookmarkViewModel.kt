package com.example.chakmadictionary.ui.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.chakmadictionary.database.WordsDao

class BookmarkViewModel(application: Application,private val dataSource:WordsDao) : AndroidViewModel(application) {

    val bookmarkList=dataSource.getAllBookmarks()
}