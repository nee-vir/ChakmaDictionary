package com.example.chakmadictionary.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.network.NetworkObjectContainer
import com.example.chakmadictionary.network.NetworkWord
import com.example.chakmadictionary.network.asDatabaseModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MySettingsFragment :PreferenceFragmentCompat(),Preference.OnPreferenceClickListener{

    private val list= mutableListOf<NetworkWord>()
    private lateinit var db: WordsDao
    private val job= Job()
    private val coroutineScope= CoroutineScope(Dispatchers.IO+job)

    override fun onPreferenceClick(preference: Preference?): Boolean {
            coroutineScope.launch {
                retrieveWordsAndStore()
                Timber.i("Words retrived and stored")
            }
        return true
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)
        db= WordsDatabase.getInstance(requireActivity().applicationContext).wordsDao
        val pref=findPreference<Preference>("downloadData")
        pref?.onPreferenceClickListener=this


    }

    private suspend fun retrieveWordsAndStore(){
        val fs= FirebaseFirestore.getInstance()
        fs.collection("words").get().addOnSuccessListener { result ->
            for(document in result){
                val obj=document.toObject(NetworkWord::class.java)
                list.add(obj)
            }
            coroutineScope.launch {
                val wordsContainer= NetworkObjectContainer(list)
                db.insertAll(wordsContainer.asDatabaseModel())
            }
        }.addOnFailureListener {
            Timber.d("Error getting documents : $it")
        }
    }



}

