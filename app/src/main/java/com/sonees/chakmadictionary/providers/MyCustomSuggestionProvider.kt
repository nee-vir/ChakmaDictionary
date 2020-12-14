package com.sonees.chakmadictionary.providers

import android.app.SearchManager
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.preference.PreferenceManager
import com.sonees.chakmadictionary.database.WordsDao
import com.sonees.chakmadictionary.database.WordsDatabase
import timber.log.Timber
import java.lang.IllegalArgumentException

class MyCustomSuggestionProvider : ContentProvider() {

    companion object{
        const val AUTHORITY="com.sonees.chakmadictionary.providers.MyCustomSuggestionProvider"
        val CONTENT_URI:Uri= Uri.parse("content://"+ AUTHORITY+ "/words_database")

        const val WORDS_MIME_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/vnd.sonees.chakmadictionary.provider"
        const val DEFINATION_MIME_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/vnd.sonees.chakmadictionary.provider"

        //Database

        //UriMatcher stuff
        private const val  SEARCH_WORDS=0
        private const val GET_WORD=1;
        private const val SEARCH_SUGGEST=2;
        private const val REFRESH_SHORTCUT=3;
        private  val uriMatcher=buildUriMatcher();

        fun buildUriMatcher():UriMatcher{
            val matcher= UriMatcher(UriMatcher.NO_MATCH)
            //to get definitions
            matcher.addURI(AUTHORITY,"words_database", SEARCH_WORDS)
            matcher.addURI(AUTHORITY,"words_database/#", GET_WORD)

            //to get suggestions...
            matcher.addURI(AUTHORITY,SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST)
            matcher.addURI(AUTHORITY,SearchManager.SUGGEST_URI_PATH_QUERY+ "/*", SEARCH_SUGGEST)

            return matcher

        }

    }



    lateinit var dataSource:WordsDao

    override fun onCreate(): Boolean {
        dataSource=WordsDatabase.getInstance(context?.applicationContext!!).wordsDao
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val query= "%"+selectionArgs?.get(0)+"%"
        return if(selectionArgs?.get(0).equals("")){
            Timber.i("Empty Query")
            null
        } else{
            val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
            if(sharedPreferences.getString("searchAppearancePriority","0")=="0"){
                val cursor=dataSource.getSuggestionWord2(query)
                Timber.i(cursor?.getColumnName(0))
                cursor
            } else{
                val cursor=dataSource.getSuggestionWord(query)
                Timber.i(cursor?.getColumnName(0))
                cursor
            }

        }


    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)){
            SEARCH_WORDS -> WORDS_MIME_TYPE
            GET_WORD -> DEFINATION_MIME_TYPE
            else -> throw IllegalArgumentException()
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return dataSource.deleteWords(selectionArgs?.get(0))
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {

        return 0
    }
}

