package com.example.spellsbook.data.store.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

object AppSharedPreferencesConnection {

    @Volatile
    var sp: AppSharedPreferences? = null

    fun instance(context: Context) =
        AppSharedPreferences(context).also { sp = it }

}

class AppSharedPreferences(context: Context) {
    private val SHARED_PREF_NAME = "spellsbook_shared_pref"

    val sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
}

suspend inline fun <reified T : Any> AppSharedPreferences.get(key: String): T? =
    withContext(Dispatchers.IO) {
        async {
            when (T::class) {
                String::class -> sharedPref.getString(key, null)
                Boolean::class -> sharedPref.getBoolean(key, false)
                Int::class -> sharedPref.getInt(key, 0)
                else -> null
            } as T?
        }.await()
    }

suspend inline fun <reified T : Any> AppSharedPreferences.set(key: String, value: T) =
    withContext(Dispatchers.IO) {
        async {
            when (T::class) {
                String::class -> sharedPref.edit().putString(key, value as String)
                Boolean::class -> sharedPref.edit().putBoolean(key, value as Boolean)
                Int::class -> sharedPref.edit().putInt(key, value as Int)
                else -> null
            }?.commit()
        }.await()
    }