package com.everfrost.remak_compose.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

class TokenRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

//    suspend fun saveToken(token: SignInModel.TokenData) {
//        dataStore.edit { preferences ->
//            preferences[PreferencesKeys.ACCESS_TOKEN] = token.accessToken
//        }
//    }
//
//    suspend fun deleteToken() {
//        dataStore.edit { preferences ->
//            preferences.remove(PreferencesKeys.ACCESS_TOKEN)
//        }
//    }
//
//    suspend fun fetchToken(): SignInModel.TokenData? {
//        return user.first()
//    }
//
//    suspend fun checkToken(): Boolean {
//        return fetchToken() != null
//    }
//
//    val user: Flow<SignInModel.TokenData?> = dataStore.data
//        .catch { exception ->
//            if (exception is Exception) {
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }
//        .map { preferences ->
//            val accessToken = preferences[PreferencesKeys.ACCESS_TOKEN] ?: ""
//            if (accessToken.isNotEmpty()) {
//                SignInModel.TokenData(accessToken)
//            } else {
//                null
//            }
//        }

}