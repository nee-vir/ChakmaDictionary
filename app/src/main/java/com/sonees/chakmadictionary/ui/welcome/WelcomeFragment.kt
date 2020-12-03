package com.sonees.chakmadictionary.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sonees.chakmadictionary.databinding.FragmentWelcomeBinding
import com.sonees.chakmadictionary.network.Quote
import com.google.firebase.firestore.FirebaseFirestore
import com.sonees.chakmadictionary.utils.quoteFormat
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