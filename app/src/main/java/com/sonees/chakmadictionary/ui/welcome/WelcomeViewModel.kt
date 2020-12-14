package com.sonees.chakmadictionary.ui.welcome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.sonees.chakmadictionary.database.DatabaseWord
import com.sonees.chakmadictionary.database.WordsDao
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.random.Random

class WelcomeViewModel(application: Application,private val dataSource:WordsDao) :AndroidViewModel(application){

     private lateinit var wordList:List<DatabaseWord>
     private val randomWords= mutableListOf<DatabaseWord>()
     private val _quizWord=MutableLiveData<DatabaseWord>()
     val quizWord:LiveData<DatabaseWord>
     get() = _quizWord

    private val _answerList= MutableLiveData<MutableList<String>>()
    val answerList:LiveData<MutableList<String>>
    get() = _answerList

    private val randomNumbers= mutableListOf<Int>()

    var answerIndex=0;

    private val _score=MutableLiveData<Int>()
    val score:LiveData<Int>
    get() = _score

    var myScore=0

    private var numberExists=false

//    var answer=""

    init {
        _score.value=0
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(application)
        val category=sharedPreferences.getString("quizCategory","0")
        viewModelScope.launch {
            wordList=dataSource.getAllWords()
            if(wordList.isNotEmpty())
            getRandomWords()
        }


    }

     fun getRandomWords(){
        randomWords.clear()
         randomNumbers.clear()
        _answerList.value= mutableListOf<String>()
        val listSize=wordList.size
         Timber.i(listSize.toString())
        for(i in 1..4){
            val randomIndex=(0 until listSize).random()
            checkIfItExists(randomIndex)
            if(numberExists){
//                randomIndex=(0 until listSize).random()
                randomNumbers.add(randomIndex/2)
                randomWords.add(wordList[randomIndex/2])
            } else{
                randomNumbers.add(randomIndex)
                randomWords.add(wordList[randomIndex])
            }
//            val randomIndex=Random.nextInt(listSize-1)
        }
        Timber.i(randomWords.toString())
        _quizWord.value= randomWords[0]
        Timber.i(quizWord.value.toString())
//        answer=quizWord.value?.definition.toString()
        randomWords.shuffle()
        randomWords.forEachIndexed{index, databaseWord ->
            if(databaseWord.wordId==quizWord.value?.wordId){
                answerIndex=index
                Timber.i(answerIndex.toString())
            }
        }
        for(i in 0..3){
            _answerList.value?.add(randomWords[i].definition.toString())
        }

        Timber.i(_answerList.value.toString())
    }


    fun increaseScore(){
        myScore+=1
        _score.value=myScore
    }

    fun resetScore(){
        myScore=0
        _score.value=0
    }

    private fun checkIfItExists(random:Int){
        for (i in randomNumbers){
            numberExists = i==random
        }

    }

}