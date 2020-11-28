package com.example.chakmadictionary.network

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.chakmadictionary.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import timber.log.Timber

class CloudMessagingService : FirebaseMessagingService() {




    /*override fun onNewToken(p0: String) {
        super.onNewToken(p0)


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.w(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            //GET THE FIREBASE CLOUD MESSAGING TOKEN
            val token = task.result

            // Log and toast
            Timber.d(token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()


        })


    }*/


}