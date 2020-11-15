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
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentDefinationBinding
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DefinationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DefinationFragment : Fragment() {

    lateinit var definitionViewModel:DefinitionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding=FragmentDefinationBinding.inflate(inflater,container,false)

        val activity= requireNotNull(activity)

        val dataSource=WordsDatabase.getInstance(activity.application).wordsDao

        val viewModelFactory=DefinitionViewModelFactory(activity.application,dataSource)

         definitionViewModel=ViewModelProvider(this,viewModelFactory).get(DefinitionViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.definitionViewModel=definitionViewModel

        definitionViewModel.retrieveFromDatabase("Baluk")
        definitionViewModel.myWord.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context,"The word changed",Toast.LENGTH_SHORT).show()
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
    }

}