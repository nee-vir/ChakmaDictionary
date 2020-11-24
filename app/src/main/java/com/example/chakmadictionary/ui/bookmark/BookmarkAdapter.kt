package com.example.chakmadictionary.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chakmadictionary.database.BookmarkWord
import com.example.chakmadictionary.database.WordsDao
import com.example.chakmadictionary.databinding.BookmarkViewBinding
import com.example.chakmadictionary.utils.BookmarkDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BookmarkAdapter(val dataSource: WordsDao): ListAdapter<BookmarkWord,BookmarkAdapter.ViewHolder>(BookmarkDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=BookmarkViewBinding.inflate(inflater,parent,false)
        return ViewHolder(binding,dataSource)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=getItem(position)
        holder.bind(item)
    }

    class ViewHolder(val binding:BookmarkViewBinding, val dataSource: WordsDao):RecyclerView.ViewHolder(binding.root){
        fun bind(item: BookmarkWord){
            binding.bookmarkItem=item
            binding.bookmarkText.setOnClickListener {
                it.findNavController().navigate(BookmarkFragmentDirections.actionBookmarkFragmentToDefinitionFragment(item.word!!,item.wordId))
            }
            binding.deleteBookmarkButton.setOnClickListener {
                val coroutineScope= CoroutineScope(Dispatchers.IO+ Job())
                coroutineScope.launch {
                    dataSource.deleteBookmark(item.wordId)
                }

            }
        }
    }
}