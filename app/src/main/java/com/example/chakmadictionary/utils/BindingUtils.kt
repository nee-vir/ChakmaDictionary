package com.example.chakmadictionary.utils

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.chakmadictionary.network.Quote

@BindingAdapter("checkboxState")
fun setCheckboxState(checkBox: CheckBox,state: Boolean=false){
    checkBox.isChecked=state
    if(state){
        checkBox.text="Bookmarked"
    } else{
        checkBox.text="Bookmark Word"
    }
}

@BindingAdapter("wordNotFoundVisibility")
fun setVisibility(view:View,wordNotFound: Boolean){
    if(wordNotFound){
        view.visibility= View.GONE
    } else{
        view.visibility=View.VISIBLE
    }
}


@BindingAdapter("wordNotFoundViewVisibility")
fun setViewVisibility(view:ImageView,wordNotFound: Boolean){
    if(wordNotFound){
        view.visibility= View.VISIBLE
    } else{
        view.visibility=View.GONE
    }
}


@BindingAdapter("definitionFormat")
fun formatDefinition(textView: TextView,definition:String?){
    //If you don't provide null check ,it will crash
    val list=definition?.split("###")
    var newString=""
    if(list!=null){
        val size=list.size
        var counter=1 // We have the counter to avoid the unnecessary new added lines at the end of the loop
        for (def in list){
            newString+=def
            if(counter!=size){
                newString+="\n\n"
            }
            counter++
        }
        textView.text=newString
    }

}







