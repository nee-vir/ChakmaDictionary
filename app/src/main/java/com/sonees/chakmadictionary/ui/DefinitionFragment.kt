package com.sonees.chakmadictionary.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.sonees.chakmadictionary.database.WordsDatabase
import com.sonees.chakmadictionary.databinding.FragmentDefinitionBinding
import kotlinx.coroutines.*


class DefinationFragment : Fragment() {

    lateinit var definitionViewModel:DefinitionViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private val args:DefinationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= FragmentDefinitionBinding.inflate(inflater,container,false)

        binding.definationLayout.requestFocus()

        val activity= requireNotNull(activity)

        val dataSource=WordsDatabase.getInstance(activity.application).wordsDao

        val viewModelFactory=DefinitionViewModelFactory(activity.application,dataSource)

        activity.run{
            definitionViewModel=ViewModelProvider(this,viewModelFactory).get(DefinitionViewModel::class.java)//?: throw Exception("Invalid Activity")
        }

        binding.lifecycleOwner=this

        setFontSize(binding)


        //If the transition is from bookmark or history fragment
        if(args.wordId>=0){
            definitionViewModel.retrieveFromDatabase(args.word,args.from)
        }

        binding.definitionViewModel=definitionViewModel

       /* definitionViewModel.myWord.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context,"The word changed",Toast.LENGTH_SHORT).show()
        })

//For testing
        definitionViewModel.handled.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context,"Working",Toast.LENGTH_SHORT).show()
        })*/

        definitionViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.progressBar.visibility=View.VISIBLE
            } else{
                binding.progressBar.visibility=View.INVISIBLE
            }

        })

        setHasOptionsMenu(true)

        return binding.root
    }


   private fun setFontSize( binding: FragmentDefinitionBinding){
       sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context)
       val sizeOfFont=sharedPreferences.getInt("fontSize",16) //Seekbar stores values as "String" to "Int" where key is string and value is int
       val sizeOfHeader= sharedPreferences.getInt("headerFontSize",20)
       binding.wordText.textSize=sizeOfHeader.toFloat()+10
       binding.chakmaExampleLabel.textSize=sizeOfHeader.toFloat()
       binding.chakmaExampleText.textSize=sizeOfFont.toFloat()
       binding.definitionText.textSize=sizeOfFont.toFloat()
       binding.definitionLabel.textSize=sizeOfHeader.toFloat()
       binding.englishLabel.textSize=sizeOfHeader.toFloat()
       binding.translationText.textSize=sizeOfHeader.toFloat()
       binding.englishExampleLabel.textSize=sizeOfHeader.toFloat()
       binding.englishExampleText.textSize=sizeOfFont.toFloat()
       binding.synonymsLabel.textSize=sizeOfHeader.toFloat()
       binding.synonymsText.textSize=sizeOfFont.toFloat()

   }


}