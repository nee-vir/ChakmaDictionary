package com.example.chakmadictionary.main

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.ViewModelProvider
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.ui.DefinitionViewModel
import com.example.chakmadictionary.ui.DefinitionViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var definitionViewModel: DefinitionViewModel
    lateinit var dataSource:WordsDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val application= requireNotNull(this.application)
        dataSource=WordsDatabase.getInstance(application).wordsDao
        val viewModelFactory=DefinitionViewModelFactory(application,dataSource)
        definitionViewModel=ViewModelProvider(this,viewModelFactory).get(DefinitionViewModel::class.java)


    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater= menuInflater
        inflater.inflate(R.menu.search_menu,menu)
        val searchView=menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity,query,Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                Toast.makeText(this@MainActivity,newText,Toast.LENGTH_SHORT).show()
                return true
            }
        })
        return true
    }*/

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when(item.itemId){
//            R.id.app_bar_search -> onSearchRequested()
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(Intent.ACTION_SEARCH == intent?.action){
            intent.getStringExtra(SearchManager.QUERY)?.also {
                definitionViewModel.retrieveFromDatabase(it)
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }
        }

    }





}