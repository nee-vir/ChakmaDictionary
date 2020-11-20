package com.example.chakmadictionary.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.ui.DefinationFragment
import com.example.chakmadictionary.ui.DefinitionViewModel
import com.example.chakmadictionary.ui.DefinitionViewModelFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var definitionViewModel: DefinitionViewModel
    lateinit var dataSource:WordsDao
    val handleIntent=MutableLiveData<Intent>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val application= requireNotNull(this.application)
        dataSource=WordsDatabase.getInstance(application).wordsDao
        val viewModelFactory=DefinitionViewModelFactory(application,dataSource)
        definitionViewModel=ViewModelProvider(this,viewModelFactory).get(DefinitionViewModel::class.java)


        Timber.d(intent.action.toString())


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater=menuInflater
        inflater.inflate(R.menu.search_menu,menu)
        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView=menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                definitionViewModel.retrieveFromDatabase(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when(intent?.action){
            Intent.ACTION_VIEW -> {
                definitionViewModel.handleSuggestionIntent(intent)
            }
        }
    }






}