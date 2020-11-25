package com.example.chakmadictionary.ui.bookmark

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        val destination=findNavController().currentDestination



        val act=activity as AppCompatActivity
//        act.supportActionBar.

//        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }

    override fun onDetach() {
        super.onDetach()
        hideKeyboard()
    }

    private fun hideKeyboard(){
        val inputManager: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        return inflater.inflate(R.menu.bookmark_history_menu,menu)
//    }

}