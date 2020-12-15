package com.sonees.chakmadictionary.ui.welcome

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.sonees.chakmadictionary.database.DatabaseWord
import com.sonees.chakmadictionary.database.WordsDao
import kotlinx.coroutines.launch
import timber.log.Timber

class WelcomeViewModel(private val app: Application, private val dataSource:WordsDao) :AndroidViewModel(app){

     private lateinit var wordList:List<DatabaseWord>
     private val randomWords= mutableListOf<DatabaseWord>()
     private val _quizWord=MutableLiveData<DatabaseWord>()
     val quizWord:LiveData<DatabaseWord>
     get() = _quizWord

    private val _answerList= MutableLiveData<MutableList<String>>()
    val answerList:LiveData<MutableList<String>>
    get() = _answerList


    var answerIndex=0;

    private val _score=MutableLiveData<Int>()
    val score:LiveData<Int>
    get() = _score

    var myScore=0

    private val _responseVisibility=MutableLiveData<Int>()
    val responseVisibility:LiveData<Int>
    get() = _responseVisibility

    private val _radioGroupVisibility=MutableLiveData<Int>()
    val radioGroupVisibility:LiveData<Int>
    get() = _radioGroupVisibility

//    var answer=""
    private val _quizResponse=MutableLiveData<String>()
    val quizResponse:LiveData<String>
    get() = _quizResponse
    init {
       loadQuiz()
    }

     fun loadQuiz(){
        _score.value=0
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(app)
        when(sharedPreferences.getString("quizCategory","0")){
            "0" -> viewModelScope.launch {
                wordList=dataSource.getAllWords()
                if(wordList.isNotEmpty()&&wordList.size>=4){
                    _responseVisibility.value= View.GONE
                    _radioGroupVisibility.value=View.VISIBLE
                    getRandomWords()
                } else{
                    _quizResponse.value="Go to the settings screen and download the resources to play the Quiz. After downloading, comeback and TAP HERE to begin the quiz."
                    _responseVisibility.value= View.VISIBLE
                    _radioGroupVisibility.value=View.INVISIBLE
                }

            }
            "1" -> viewModelScope.launch {
                wordList=dataSource.getHistoryFromWords()
                if(wordList.isNotEmpty()&&wordList.size>=4){
                    _responseVisibility.value= View.GONE
                    _radioGroupVisibility.value=View.VISIBLE
                    getRandomWords()
                } else{
                    _quizResponse.value="You don't have history. Change the Quiz Category to \"All\" from the settings screen. Comeback and Tap Here to Begin the Quiz."
                    _responseVisibility.value=View.VISIBLE
                    _radioGroupVisibility.value=View.INVISIBLE
                }

            }
            "2" -> viewModelScope.launch {
                wordList=dataSource.getBookMarksFromWords()
                if(wordList.isNotEmpty()&&wordList.size>=4){
                    _responseVisibility.value= View.GONE
                    _radioGroupVisibility.value=View.VISIBLE
                    getRandomWords()
                } else{
                    _quizResponse.value="You don't have bookmarks. Change the Quiz Category to \"All\" from the settings screen. Comeback and Tap Here to Begin the Quiz."
                    _responseVisibility.value=View.VISIBLE
                    _radioGroupVisibility.value=View.INVISIBLE
                }

            }
        }
    }

     fun getRandomWords(){
        randomWords.clear()
        _answerList.value= mutableListOf<String>()
        val listSize=wordList.size
         Timber.i(listSize.toString())
         var randomIndex=(0 until listSize).random()
        for(i in 1..4){
            if(randomIndex==listSize-1){
                randomWords.add(wordList[randomIndex])
                randomIndex=0
            } else{
                randomWords.add(wordList[randomIndex])
                randomIndex++
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


}