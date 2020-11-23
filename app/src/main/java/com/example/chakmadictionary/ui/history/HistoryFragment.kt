package com.example.chakmadictionary.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

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

        return binding.root
    }

}