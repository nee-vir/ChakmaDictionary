package com.example.chakmadictionary.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentDefinitionBinding
import kotlinx.coroutines.*


class DefinationFragment : Fragment() {

    lateinit var definitionViewModel:DefinitionViewModel

    private val args:DefinationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= FragmentDefinitionBinding.inflate(inflater,container,false)

        binding.definationLayout.requestFocus()

        val activity= requireNotNull(activity)

        val dataSource=WordsDatabase.getInstance(activity.application).wordsDao

        val viewModelFactory=DefinitionViewModelFactory(activity.application,dataSource)

        activity.run{
            definitionViewModel=ViewModelProvider(this,viewModelFactory).get(DefinitionViewModel::class.java)//?: throw Exception("Invalid Activity")
        }


        binding.lifecycleOwner=this

        if(args.wordId>=0){
            definitionViewModel.retrieveFromDatabase(args.word)
        }

        binding.definitionViewModel=definitionViewModel
        /*lifecycleScope.launch {
            definitionViewModel.retrieveWords()
        }
        definitionViewModel.retrieveFromDatabase("Baluk")*/
        definitionViewModel.myWord.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context,"The word changed",Toast.LENGTH_SHORT).show()
        })

//
        definitionViewModel.handled.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context,"Working",Toast.LENGTH_SHORT).show()
        })

        definitionViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.progressBar.visibility=View.VISIBLE
            } else{
                binding.progressBar.visibility=View.INVISIBLE
            }

        })

        setHasOptionsMenu(true)

        return binding.root
    }


    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu,menu)
        val searchManager= activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView=menu.findItem(R.id.app_bar_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                definitionViewModel.retrieveFromDatabase(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }*/


}