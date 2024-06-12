//package com.jmnetwork.e_jartas.utils
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.preferencesKey
//import androidx.datastore.preferences.createDataStore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//class MyDataStore(context: Context) {
//
//    companion object {
//        const val USER_PREF = "USER_PREF"
//    }
//
//    private val mDataStore: DataStore<Preferences> = context.createDataStore(name = "my_preferences")
//
//    suspend fun setValue(key: String, value: String) {
//        val dataStoreKey = preferencesKey<String>(key)
//        dataStore.edit { preferences ->
//            preferences[dataStoreKey] = value
//        }
//    }
//
//    fun getValue(key: String): Flow<String?> {
//        val dataStoreKey = preferencesKey<String>(key)
//        return dataStore.data.map { preferences ->
//            preferences[dataStoreKey]
//        }
//    }
//
//    // Add similar methods for Boolean and Int values
//}