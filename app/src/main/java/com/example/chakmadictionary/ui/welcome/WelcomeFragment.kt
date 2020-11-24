package com.example.chakmadictionary.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentWelcomeBinding
import com.example.chakmadictionary.ui.definition.DefinitionViewModel
import com.example.chakmadictionary.ui.definition.DefinitionViewModelFactory


class WelcomeFragment : Fragment() {

    lateinit var definitionViewModel: DefinitionViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding=FragmentWelcomeBinding.inflate(inflater,container,false)
        setUpNavigationToDefinition()

        return binding.root
    }



    private fun setUpNavigationToDefinition(){
        val application= requireNotNull(activity)
        val dataSource=WordsDatabase.getInstance(application.application).wordsDao
        val definitionViewModelFactory=DefinitionViewModelFactory(application.application,dataSource)
        activity?.run {
            definitionViewModel=ViewModelProvider(this,definitionViewModelFactory).get(DefinitionViewModel::class.java)
        }
        definitionViewModel.navigateToDefinition.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(R.id.action_welcomeFragment_to_definitionFragment)
                definitionViewModel.doneNavigating()
            }
        })
    }


}