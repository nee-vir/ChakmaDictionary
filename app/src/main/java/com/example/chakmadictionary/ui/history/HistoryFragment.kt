package com.example.chakmadictionary.ui.history

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentHistoryBinding
import com.example.chakmadictionary.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


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

            historyViewModel=ViewModelProvider(this,historyViewModelFactory).get(HistoryViewModel::class.java)

        binding.lifecycleOwner=this
        binding.historyViewModel=historyViewModel
        val adapter= HistoryAdapter(dataSource)
        binding.historyList.hasFixedSize()
        binding.historyList.adapter=adapter
        historyViewModel.historyList.observe(viewLifecycleOwner, Observer {
            if(it==null|| it.isEmpty()){
                binding.historyNotFoundImage.visibility=View.VISIBLE
                binding.clearHistoryButton.visibility=View.GONE
            } else{
                binding.historyNotFoundImage.visibility=View.GONE
                binding.clearHistoryButton.visibility=View.VISIBLE
            }
            adapter.submitList(it)
        })
        binding.clearHistoryButton.setOnClickListener{
            showDialog()
        }

        return binding.root
    }

    private fun showDialog(){
        val activity= requireNotNull(activity)
        MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_content_history)
                .setPositiveButton(R.string.dialog_accept) { dialog, which ->
                    historyViewModel.deleteHistory()
                }
                .setNegativeButton(R.string.dialog_decline){dialog, which ->
                    Timber.i("Delete declined")
                }.show()
    }



}