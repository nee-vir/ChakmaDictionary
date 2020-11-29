package com.example.chakmadictionary.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.ActivityMainBinding
import com.example.chakmadictionary.ui.DefinationFragment
import com.example.chakmadictionary.ui.DefinitionViewModel
import com.example.chakmadictionary.ui.DefinitionViewModelFactory
import com.google.android.material.navigation.NavigationView
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var definitionViewModel: DefinitionViewModel
    lateinit var dataSource:WordsDao
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var sharedPreferences: SharedPreferences


    val listener:SharedPreferences.OnSharedPreferenceChangeListener=SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if(key=="myTheme"){
            this.finish()
            startActivity(intent)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMyTheme()//Theme has to be set before setContentView, once the view is inflated the theme cannot be changed
        val binding=DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
//        setContentView(R.layout.activity_main)
        val application= requireNotNull(this.application)
        dataSource=WordsDatabase.getInstance(application).wordsDao
        val viewModelFactory=DefinitionViewModelFactory(application,dataSource)
        definitionViewModel=ViewModelProvider(this,viewModelFactory).get(DefinitionViewModel::class.java)
        val toolbar=binding.appBarContent.toolbar
        val drawerLayout=binding.drawerLayout
        val navView=binding.navView
        val navController=findNavController(R.id.nav_host_fragment)
        setSupportActionBar(toolbar)
        appBarConfiguration= AppBarConfiguration(navController.graph,drawerLayout)
        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.exit_item).setOnMenuItemClickListener {
            this.finish()
            return@setOnMenuItemClickListener true
        }



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
                definitionViewModel.retrieveFromDatabase(query?.toLowerCase())
                definitionViewModel.loaded()
                navigateToDefinitionFragment()
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
                navigateToDefinitionFragment()
            }
        }
    }

    private fun navigateToDefinitionFragment(){
        hideKeyboard()
        val navController=findNavController(R.id.nav_host_fragment)
        val currentDestination:NavDestination=navController.currentDestination as NavDestination
        currentDestination?.let {
            when(currentDestination.id){
                R.id.historyFragment -> navController.navigate(R.id.action_historyFragment_to_definitionFragment)

                R.id.bookmarkFragment -> navController.navigate(R.id.action_bookmarkFragment_to_definitionFragment)

                R.id.welcomeFragment -> navController.navigate(R.id.action_welcomeFragment_to_definitionFragment)

                R.id.aboutFragment -> navController.navigate(R.id.action_aboutFragment_to_definitionFragment)

            }
        }
    }

    private fun hideKeyboard(){
        val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    private fun setMyTheme(){
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        Timber.i(sharedPreferences.getString("myTheme","0"))
        when(sharedPreferences.getString("myTheme","0")){
            "0" -> setTheme(R.style.Theme_ChakmaDictionary_NoActionBar)
            "1" -> setTheme(R.style.Theme_ChakmaDictionary_Cream)
            "2" -> setTheme(R.style.Theme_ChakmaDictionary_Purple)
            "3" -> setTheme(R.style.Theme_ChakmaDictionary_DarkMode)
            "4" -> setTheme(R.style.Theme_ChakmaDictionary_Blue)
        }
    }


    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }


    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }





















}