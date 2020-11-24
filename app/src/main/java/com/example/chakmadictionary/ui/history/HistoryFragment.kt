package com.example.chakmadictionary.ui.history

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
import com.example.chakmadictionary.databinding.FragmentHistoryBinding
import com.example.chakmadictionary.ui.definition.DefinitionViewModel
import com.example.chakmadictionary.ui.definition.DefinitionViewModelFactory


class HistoryFragment : Fragment() {

    lateinit var definitionViewModel: DefinitionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding=FragmentHistoryBinding.inflate(inflater)
        val activity= requireNotNull(activity)
        val dataSource=WordsDatabase.getInstance(activity.application).wordsDao
        val historyViewModelFactory=HistoryViewModelFactory(activity.application,dataSource)
        val historyViewModel=ViewModelProvider(this,historyViewModelFactory).get(HistoryViewModel::class.java)
        binding.lifecycleOwner=this
        val adapter= HistoryAdapter(dataSource)
        binding.historyList.adapter=adapter
        historyViewModel.historyList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        setUpNavigationToDefinition(dataSource,activity.application)

        return binding.root
    }


    private fun setUpNavigationToDefinition(dataSource: WordsDao, application: Application){
        val definitionViewModelFactory=
            DefinitionViewModelFactory(application,dataSource)
        activity?.run {
            definitionViewModel= ViewModelProvider(this,definitionViewModelFactory).get(DefinitionViewModel::class.java)
        }
        definitionViewModel.navigateToDefinition.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(R.id.action_historyFragment_to_definitionFragment)
                definitionViewModel.doneNavigating()
            }
        })
    }

}