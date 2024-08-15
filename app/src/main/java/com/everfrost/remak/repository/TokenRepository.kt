package com.everfrost.remak.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.everfrost.remak.model.account.SignInModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    suspend fun saveToken(token: SignInModel.TokenData) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = token.accessToken
        }
    }

    suspend fun deleteToken() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ACCESS_TOKEN)
        }
    }

    suspend fun fetchToken(): SignInModel.TokenData? {
        return user.first()
    }

    suspend fun checkToken(): Boolean {
        return fetchToken() != null
    }

    val user: Flow<SignInModel.TokenData?> = dataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val accessToken = preferences[PreferencesKeys.ACCESS_TOKEN] ?: ""
            if (accessToken.isNotEmpty()) {
                SignInModel.TokenData(accessToken)
            } else {
                null
            }
        }

}