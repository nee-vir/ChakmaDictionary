package com.example.chakmadictionary.utils

import android.widget.CheckBox
import androidx.databinding.BindingAdapter

@BindingAdapter("checkboxState")
fun setCheckboxState(checkBox: CheckBox,state: Boolean=false){
    checkBox.isChecked=state
}