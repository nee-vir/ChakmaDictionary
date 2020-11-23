package com.example.chakmadictionary.ui.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentBookmarkBinding


class BookmarkFragment : Fragment() {


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
        return binding.root
    }

}