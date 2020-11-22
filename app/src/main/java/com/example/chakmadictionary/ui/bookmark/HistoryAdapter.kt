package com.example.chakmadictionary.ui.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chakmadictionary.database.DatabaseWord
import com.example.chakmadictionary.database.HistoryWord
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.databinding.HistoryViewBinding
import com.example.chakmadictionary.utils.HistoryDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HistoryAdapter(val dataSource:WordsDao): ListAdapter<HistoryWord,HistoryAdapter.ViewHolder>(HistoryDiffCallback()){



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
            binding.deleteHistoryButton.setOnClickListener {
                val couritineScope= CoroutineScope(Dispatchers.IO+ Job())
                couritineScope.launch {
                    dataSource.deleteHistoryItem(item.historyId)
                }

            }
            binding.executePendingBindings()
        }

    }



}