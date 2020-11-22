package com.example.chakmadictionary.utils

import android.os.Build
import androidx.annotation.RequiresApi
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

