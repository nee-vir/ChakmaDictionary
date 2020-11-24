package com.example.chakmadictionary.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.ActivityMainBinding
import com.example.chakmadictionary.ui.definition.DefinitionViewModel
import com.example.chakmadictionary.ui.definition.DefinitionViewModelFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var definitionViewModel: DefinitionViewModel
    lateinit var dataSource:WordsDao
    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
//        setContentView(R.layout.activity_main)
        val application= requireNotNull(this.application)
        dataSource=WordsDatabase.getInstance(application).wordsDao
        val viewModelFactory= DefinitionViewModelFactory(application,dataSource)
        definitionViewModel=ViewModelProvider(this,viewModelFactory).get(DefinitionViewModel::class.java)
        val toolbar=binding.appBarContent.toolbar
        val drawerLayout=binding.drawerLayout
        val navView=binding.navView
        val navController=findNavController(R.id.nav_host_fragment)
        setSupportActionBar(toolbar)
        appBarConfiguration= AppBarConfiguration(navController.graph,drawerLayout) // setOf(R.id.definitionFragment,R.id.welcomeFragment)
        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)

        Timber.d(intent.action.toString())


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater=menuInflater
        inflater.inflate(R.menu.search_menu,menu)
        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView=menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        searchView.maxWidth= Int.MAX_VALUE
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                definitionViewModel.retrieveFromDatabase(query)
                definitionViewModel.loaded()
                definitionViewModel.navigateToDefinitionFragment()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                definitionViewModel.load()
                if(newText.equals("")){
                    definitionViewModel.loaded()
                }
                return true
            }
        })

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus -> definitionViewModel.loaded() }



        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)|| super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when(intent?.action){
            Intent.ACTION_VIEW -> {
                definitionViewModel.handleSuggestionIntent(intent)
                definitionViewModel.loaded()
                definitionViewModel.navigateToDefinitionFragment()
            }
        }
    }






}