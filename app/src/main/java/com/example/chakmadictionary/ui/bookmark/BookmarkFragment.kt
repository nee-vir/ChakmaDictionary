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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


class BookmarkFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var bookmarkViewModel: BookmarkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=FragmentBookmarkBinding.inflate(inflater)
        val application= requireNotNull(activity).application
        val dataSource=WordsDatabase.getInstance(application).wordsDao
        val bookmarkViewModelFactory=BookmarkViewModelFactory(application,dataSource)
        bookmarkViewModel=ViewModelProvider(this,bookmarkViewModelFactory).get(BookmarkViewModel::class.java)
        val adapter=BookmarkAdapter(dataSource)
        binding.bookmarkViewModel=bookmarkViewModel
        binding.bookmarkList.adapter=adapter
        bookmarkViewModel.bookmarkList.observe(viewLifecycleOwner, Observer {
            if(it==null|| it.isEmpty()){
                binding.bookmarkNotFoundImage.visibility=View.VISIBLE
            } else{
                binding.bookmarkNotFoundImage.visibility=View.GONE
            }
            adapter.submitList(it)
        })

        binding.clearBookmarkButton.setOnClickListener {
            showDialog()
        }


        return binding.root
    }

    private fun showDialog(){
        val activity= requireNotNull(activity)
        MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_content_bookmark)
                .setPositiveButton(R.string.dialog_accept) { dialog, which ->
                    bookmarkViewModel.deleteBookmark()
                }
                .setNegativeButton(R.string.dialog_decline){dialog, which ->
                    Timber.i("Delete declined")
                }.show()
    }



//    override fun onDetach() {
//        super.onDetach()
//    }




//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        return inflater.inflate(R.menu.bookmark_history_menu,menu)
//    }

}