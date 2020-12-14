package com.sonees.chakmadictionary.ui.welcome

import android.content.res.Resources
import android.graphics.Color.red
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sonees.chakmadictionary.databinding.FragmentWelcomeBinding
import com.sonees.chakmadictionary.network.Quote
import com.google.firebase.firestore.FirebaseFirestore
import com.sonees.chakmadictionary.R
import com.sonees.chakmadictionary.database.WordsDatabase
import com.sonees.chakmadictionary.utils.quoteFormat
import com.sonees.chakmadictionary.utils.questionFormat
import kotlinx.coroutines.*


class WelcomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private lateinit var welcomeViewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val act=requireActivity()
        val dataSource=WordsDatabase.getInstance(act.application).wordsDao
        val welcomeViewModelFactory=WelcomeViewModelFactory(act.application,dataSource)
        welcomeViewModel=ViewModelProvider(this,welcomeViewModelFactory).get(WelcomeViewModel::class.java)
        val binding=FragmentWelcomeBinding.inflate(inflater,container,false)//This is the required format for startDestinations in navGraph

        binding.quizFragment.welcomeViewModel=welcomeViewModel

        //Retrieving the quote from firebase firestore
        val fs=FirebaseFirestore.getInstance()
        val coroutineScope= CoroutineScope(Dispatchers.IO+ Job())
        coroutineScope.launch {
            fs.collection("quotes").document("quote").get().addOnSuccessListener {
                val obj=it.toObject(Quote::class.java)
                binding.textView.quoteFormat(obj)
            }
        }

        welcomeViewModel.quizWord.observe(viewLifecycleOwner, Observer {
            binding.quizFragment.quizQuestion.questionFormat(it.word,it.translation,act.application)
        })


        binding.lifecycleOwner=this

        binding.quizFragment.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioButton ->{
                    val choiceIndex=0
                    if (choiceIndex==welcomeViewModel.answerIndex){
                        onCorrect()
                        group.clearCheck()
                    } else{
                        onWrong()
                    }
                }
                R.id.radioButton2 ->{
                    val choiceIndex=1
                    if (choiceIndex==welcomeViewModel.answerIndex){
                        onCorrect()
                        group.clearCheck()
                    } else{
                        onWrong()
                    }
                }
                R.id.radioButton3 ->{
                    val choiceIndex=2
                    if (choiceIndex==welcomeViewModel.answerIndex){
                        onCorrect()
                        group.clearCheck()
                    } else{
                        onWrong()
                    }
                }
                R.id.radioButton4 ->{
                    val choiceIndex=3
                    if (choiceIndex==welcomeViewModel.answerIndex){
                        onCorrect()
                        group.clearCheck()
                    } else{
                        onWrong()
                    }
                }
            }
        }

        binding.suggestWordButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_suggestActivity)
        }


//        binding.quizFragment.radioGroup.check(R.id.radioButton2)
//        Toast.makeText(context,binding.quizFragment.radioGroup.checkedRadioButtonId.toString(),Toast.LENGTH_SHORT).show()
        return binding.root
    }


    private fun onCorrect(){
        Toast.makeText(context,"Correct Answer",Toast.LENGTH_SHORT).show()
        welcomeViewModel.getRandomWords()
        welcomeViewModel.increaseScore()
        val mp=MediaPlayer.create(context,R.raw.correct_answer)
        mp.start()

    }

    private fun onWrong(){
        welcomeViewModel.resetScore()
        val mp=MediaPlayer.create(context,R.raw.wrong_answer)
        mp.start()
    }










}