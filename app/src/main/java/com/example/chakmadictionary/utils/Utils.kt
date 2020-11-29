package com.example.chakmadictionary.utils

import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.chakmadictionary.network.Quote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


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
        val formatedQuote=quote.quote+"\n\n- "+quote.author
        text=formatedQuote
    }

}

