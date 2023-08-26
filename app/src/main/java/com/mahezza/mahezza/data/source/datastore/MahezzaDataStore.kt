package com.mahezza.mahezza.data.source.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val MAHEZZA_PREFERENCES_NAME = "mahezza_preferences"

private val Context.datastore : DataStore<Preferences> by preferencesDataStore(
    name = MAHEZZA_PREFERENCES_NAME
)

class MahezzaDataStore(
    private val context: Context
) {
    private val IS_LOGIN = booleanPreferencesKey("is_login")
    private val FIREBASE_USER_ID = stringPreferencesKey("firebase_user_id")

    suspend fun saveLoginToPreferencesStore(isLogin : Boolean) {
        context.datastore.edit { preferences ->
            preferences[IS_LOGIN] = isLogin
        }
    }

    val isLoginPreference : Flow<Boolean> = context.datastore.data
        .catch {
            if (it is IOException){
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_LOGIN] ?: false
        }

    suspend fun saveFirebaseUserIdToPreferencesStore(firebaseUserId : String) {
        context.datastore.edit { preferences ->
            preferences[FIREBASE_USER_ID] = firebaseUserId
        }
    }

    val firebaseUserIdPreference : Flow<String?> = context.datastore.data
        .catch {
            if (it is IOException){
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[FIREBASE_USER_ID]
        }
}