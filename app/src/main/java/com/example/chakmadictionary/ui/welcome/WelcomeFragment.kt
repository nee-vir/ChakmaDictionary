package com.example.chakmadictionary.ui.welcome

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.chakmadictionary.R
import com.example.chakmadictionary.database.WordsDatabase
import com.example.chakmadictionary.databinding.FragmentWelcomeBinding
import com.example.chakmadictionary.main.MainActivity
import com.example.chakmadictionary.network.Quote
import com.example.chakmadictionary.ui.DefinitionViewModel
import com.example.chakmadictionary.ui.DefinitionViewModelFactory
import com.example.chakmadictionary.utils.quoteFormat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*


class WelcomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding=FragmentWelcomeBinding.inflate(inflater,container,false)//This is the required format for startDestinations in navGraph


        //Retrieving the quote from firebase firestore
        val fs=FirebaseFirestore.getInstance()
        val coroutineScope= CoroutineScope(Dispatchers.IO+ Job())
        coroutineScope.launch {
            fs.collection("quotes").document("quote").get().addOnSuccessListener {
                val obj=it.toObject(Quote::class.java)
                binding.textView.quoteFormat(obj)
            }
        }



        binding.lifecycleOwner=this


        return binding.root
    }








}