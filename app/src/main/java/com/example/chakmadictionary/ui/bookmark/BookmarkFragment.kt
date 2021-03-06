package com.example.chakmadictionary.ui.bookmark

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentBookmarkBinding
import com.example.chakmadictionary.ui.definition.DefinitionViewModel
import com.example.chakmadictionary.ui.definition.DefinitionViewModelFactory


class BookmarkFragment : Fragment() {

    lateinit var definitionViewModel: DefinitionViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=FragmentBookmarkBinding.inflate(inflater)
        val application= requireNotNull(activity).application
        val dataSource=WordsDatabase.getInstance(application).wordsDao
        val bookmarkViewModelFactory=BookmarkViewModelFactory(application,dataSource)
        val bookmarkViewModel=ViewModelProvider(this,bookmarkViewModelFactory).get(BookmarkViewModel::class.java)
        val adapter=BookmarkAdapter(dataSource)
        binding.bookmarkList.adapter=adapter
        bookmarkViewModel.bookmarkList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        setUpNavigationToDefinition(dataSource,application)
        return binding.root
    }

    private fun setUpNavigationToDefinition(dataSource:WordsDao,application:Application){
        val definitionViewModelFactory=
            DefinitionViewModelFactory(application,dataSource)
        activity?.run {
            definitionViewModel= ViewModelProvider(this,definitionViewModelFactory).get(DefinitionViewModel::class.java)
        }
        definitionViewModel.navigateToDefinition.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(R.id.action_bookmarkFragment_to_definitionFragment)
                definitionViewModel.doneNavigating()
            }
        })
    }

}