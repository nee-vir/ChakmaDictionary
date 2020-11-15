package com.example.chakmadictionary.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.chakmadictionary.database.WordsDao

class MainActivityViewModel(application: Application,dataSource: WordsDao) : AndroidViewModel(application) {

}