package com.sonees.chakmadictionary.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sonees.chakmadictionary.R
import com.sonees.chakmadictionary.database.WordsDao
import com.sonees.chakmadictionary.database.WordsDatabase
import com.sonees.chakmadictionary.network.NetworkObjectContainer
import com.sonees.chakmadictionary.network.NetworkWord
import com.sonees.chakmadictionary.network.asDatabaseModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class MySettingsFragment :PreferenceFragmentCompat(),Preference.OnPreferenceClickListener{

    private val list= mutableListOf<NetworkWord>()
    private lateinit var db: WordsDao
    private val job= Job()
    private val coroutineScope= CoroutineScope(Dispatchers.IO+job)
    private var isRetrieved=false

    override fun onPreferenceClick(preference: Preference?): Boolean {
        if(!isRetrieved){ //To prevent the app from crashing when clicked multiple times very fast
            isRetrieved=true
            if(preference?.key=="downloadData"){
                coroutineScope.launch {
                    retrieveWordsAndStore()
                    Timber.i("Words retrived and stored")
                    Snackbar.make(requireView(),"Successfully downloaded latest resources",Snackbar.LENGTH_SHORT).show()
                }
            }
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
                Timber.i(list.size.toString())
                coroutineScope.launch {
                    val wordsContainer= NetworkObjectContainer(list)
                    db.insertAll(wordsContainer.asDatabaseModel())
                }
            }.addOnFailureListener {
                Timber.d("Error getting documents : $it")
            }.addOnCompleteListener {
                isRetrieved=false
            }
        }

    }





