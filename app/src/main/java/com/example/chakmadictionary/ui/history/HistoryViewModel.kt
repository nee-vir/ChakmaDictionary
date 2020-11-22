package com.example.chakmadictionary.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.chakmadictionary.database.HistoryWord
import com.example.chakmadictionary.database.WordsDao

class HistoryViewModel(application: Application, private val datasource: WordsDao): AndroidViewModel(application) {

    val historyList=datasource.getEntireHistory()

}