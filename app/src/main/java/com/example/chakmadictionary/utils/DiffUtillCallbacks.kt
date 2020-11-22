package com.example.chakmadictionary.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.chakmadictionary.database.BookmarkWord
import com.example.chakmadictionary.database.HistoryWord

class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryWord>(){
    override fun areItemsTheSame(oldItem: HistoryWord, newItem: HistoryWord): Boolean {
        return oldItem.historyId==newItem.historyId
    }

    override fun areContentsTheSame(oldItem: HistoryWord, newItem: HistoryWord): Boolean {
        return oldItem==newItem
    }
}


class BookmarkDiffCallback : DiffUtil.ItemCallback<BookmarkWord>(){
    override fun areItemsTheSame(oldItem: BookmarkWord, newItem: BookmarkWord): Boolean {
        return oldItem.wordId==newItem.wordId
    }

    override fun areContentsTheSame(oldItem: BookmarkWord, newItem: BookmarkWord): Boolean {
        return oldItem==newItem
    }
}