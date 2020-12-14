package com.sonees.chakmadictionary.utils

import android.app.Application
import android.os.Build
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.sonees.chakmadictionary.network.Quote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.random.Random


public fun getCurrentTime(): String{
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)

        return formatted
    } else{
        //Will implement later
        return ""
    }


}



//Extensions

fun TextView.quoteFormat(quote: Quote?){
    quote?.let {
        val formatedQuote="\""+quote.quote+"\""+"\n\n- "+quote.author
        text=formatedQuote
    }

}

fun TextView.questionFormat(text1:String?, text2:String?,application: Application){
    val sharedPreference=PreferenceManager.getDefaultSharedPreferences(application)
    text = if( sharedPreference.getBoolean("onlyEnglish",false)){
        val question= "How would you best define the term \"$text2\"?"
        question
    } else{
        val randomNumber= Random.nextBoolean()
        val myText=if(randomNumber){
            text1
        } else{
            text2
        }
        val question= "How would you best define the term \"$myText\"?"
        question
    }


}

