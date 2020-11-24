package com.example.chakmadictionary.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.HistoryWord
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.databinding.HistoryViewBinding
import com.example.chakmadictionary.utils.HistoryDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HistoryAdapter(val dataSource:WordsDao): ListAdapter<HistoryWord, HistoryAdapter.ViewHolder>(HistoryDiffCallback()){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=HistoryViewBinding.inflate(inflater,parent,false)

        return ViewHolder(binding,dataSource)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=getItem(position)
        holder.bind(item)

    }

    class ViewHolder(val binding:HistoryViewBinding,val dataSource: WordsDao):RecyclerView.ViewHolder(binding.root){
        fun bind(item:HistoryWord){
            binding.historyItem=item
            binding.historyWord.setOnClickListener {
                it.findNavController().navigate(HistoryFragmentDirections.actionHistoryFragmentToDefinitionFragment(item.word!!,item.wordId!!))
            }
            binding.deleteHistoryButton.setOnClickListener {
                val coroutineScope= CoroutineScope(Dispatchers.IO+ Job())
                coroutineScope.launch {
                    dataSource.deleteHistoryItem(item.historyId)
                }

            }
            binding.executePendingBindings()
        }

    }



}