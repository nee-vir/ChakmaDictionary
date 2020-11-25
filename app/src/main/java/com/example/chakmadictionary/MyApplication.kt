package com.example.chakmadictionary

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.ui.history.HistoryViewModel
import com.example.chakmadictionary.ui.history.HistoryViewModelFactory
import timber.log.Timber

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}