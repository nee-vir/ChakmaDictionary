package com.example.chakmadictionary.ui.about

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.preference.PreferenceManager
import com.example.chakmadictionary.R
import com.example.chakmadictionary.databinding.FragmentAboutBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment() {

    lateinit var sharedPreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding=FragmentAboutBinding.inflate(inflater)
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        val sizeOfFont=sharedPreferences.getInt("fontSize",14) //Seekbar stores values as "String" to "Int" where key is string and value is int
        binding.aboutHeader.textSize=sizeOfFont.toFloat()
        binding.aboutBody.textSize=sizeOfFont.toFloat()
        binding.psAbout.textSize=sizeOfFont.toFloat()
        return binding.root
    }








}