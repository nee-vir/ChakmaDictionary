package com.example.chakmadictionary.ui.history

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    lateinit var historyViewModel:HistoryViewModel

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
        activity.run {
            historyViewModel=ViewModelProvider(this,historyViewModelFactory).get(HistoryViewModel::class.java)
        }

        binding.lifecycleOwner=this
        binding.historyViewModel=historyViewModel
        val adapter= HistoryAdapter(dataSource)
        binding.historyList.hasFixedSize()
        binding.historyList.adapter=adapter
        historyViewModel.historyList.observe(viewLifecycleOwner, Observer {
            if(it==null|| it.isEmpty()){
                binding.historyNotFoundImage.visibility=View.VISIBLE
            } else{
                binding.historyNotFoundImage.visibility=View.GONE
            }
            adapter.submitList(it)
        })

        return binding.root
    }


}