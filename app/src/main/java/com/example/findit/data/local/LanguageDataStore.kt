package com.example.findit.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("app_settings")

class LanguageDataStore @Inject constructor(
@ApplicationContext private val context: Context
) {

    private val LANGUAGE_KEY = stringPreferencesKey("language_key")

    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }

    suspend fun getLanguage(): String {
        return context.dataStore.data.map {
            it[LANGUAGE_KEY] ?: "en"
        }.first()
    }
}
