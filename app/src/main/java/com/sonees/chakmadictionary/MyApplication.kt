package com.sonees.chakmadictionary

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.sonees.chakmadictionary.database.WordsDao
import com.sonees.chakmadictionary.database.WordsDatabase
import com.sonees.chakmadictionary.network.NetworkObjectContainer
import com.sonees.chakmadictionary.network.NetworkWord
import com.sonees.chakmadictionary.network.asDatabaseModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import timber.log.Timber

class MyApplication : Application() {

    private val list= mutableListOf<NetworkWord>()
    private lateinit var db:WordsDao
    private val job= Job()
    private val coroutineScope= CoroutineScope(Dispatchers.IO+job)


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }

    private suspend fun retrieveWordsAndStore(){
        val fs=FirebaseFirestore.getInstance()
        fs.collection("words").get().addOnSuccessListener { result ->
            for(document in result){
                val obj=document.toObject(NetworkWord::class.java)
                list.add(obj)
            }
            coroutineScope.launch {
                val wordsContainer=NetworkObjectContainer(list)
                db.insertAll(wordsContainer.asDatabaseModel())
            }
        }.addOnFailureListener {
            Timber.d("Error getting documents : $it")
        }
    }

    init {
        coroutineScope.launch {
            db=WordsDatabase.getInstance(applicationContext).wordsDao
            val prefs:SharedPreferences=PreferenceManager.getDefaultSharedPreferences(this@MyApplication)
            if(!prefs.getBoolean("firstTime",false)){
                retrieveWordsAndStore()
                val editor=prefs.edit()
                editor.putBoolean("firstTime",true)
                editor.apply()
            }

        }
    }

}