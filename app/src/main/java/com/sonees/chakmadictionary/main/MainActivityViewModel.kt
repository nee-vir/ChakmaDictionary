package com.sonees.chakmadictionary.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sonees.chakmadictionary.database.WordsDao

class MainActivityViewModel(application: Application,dataSource: WordsDao) : AndroidViewModel(application) {

}