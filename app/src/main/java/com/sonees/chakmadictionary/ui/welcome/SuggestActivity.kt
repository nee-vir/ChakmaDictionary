package com.sonees.chakmadictionary.ui.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.sonees.chakmadictionary.R
import com.sonees.chakmadictionary.databinding.ActivitySuggestBinding
import com.sonees.chakmadictionary.network.SuggestionObject
import timber.log.Timber

class SuggestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_suggest)
        val binding=DataBindingUtil.setContentView<ActivitySuggestBinding>(this,R.layout.activity_suggest)

        binding.submitSuggestionButton.setOnClickListener {
            if(binding.suggestWordText.text.isNotEmpty()&&binding.suggestWordText.text.isNotBlank()||binding.suggestWordText2.text.isNotEmpty()&&binding.suggestWordText2.text.isNotBlank()
                    ||binding.suggestWordDefinitionText.text.isNotEmpty()&&binding.suggestWordDefinitionText.text.isNotBlank()||binding.additionalText.text.isNotEmpty()&&binding.additionalText.text.isNotBlank()){
                uploadSuggestion(binding)
            } else{
                Snackbar.make(binding.root,"All the fields cannot be empty. At least one field is required.",Snackbar.LENGTH_SHORT).show()
            }

        }


    }


    private fun uploadSuggestion(binding:ActivitySuggestBinding){
        binding.submitSuggestionButton.isEnabled=false
        val fs=FirebaseFirestore.getInstance()
        val mySuggestion=SuggestionObject(binding.suggestWordText.text.toString(),
                binding.suggestWordText2.text.toString(),binding.suggestWordDefinitionText.text.toString(),
                binding.additionalText.text.toString())
        fs.collection("suggestions").document().set(mySuggestion).addOnSuccessListener {
            Timber.i("Suggestion Uploaded Successfully")
        }.addOnFailureListener {
            Timber.i("Suggestion Upload Failed")
        }.addOnCompleteListener {
            Toast.makeText(this,"Thank you for your suggestion!",Toast.LENGTH_SHORT).show()
            Snackbar.make(binding.root,"Submitted Suggestion.",Snackbar.LENGTH_SHORT).show()
//            binding.submitSuggestionButton.isEnabled=true
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            },2000)
        }

    }



}