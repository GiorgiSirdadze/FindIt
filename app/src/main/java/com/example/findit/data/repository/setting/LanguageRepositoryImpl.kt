package com.example.findit.data.repository.setting

import com.example.findit.data.local.LanguageDataStore
import com.example.findit.domain.repository.setting.LanguageRepository
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val dataStore: LanguageDataStore
) : LanguageRepository {

    override suspend fun setAppLanguage(language: String) {
        dataStore.saveLanguage(language)
    }

    override suspend fun getAppLanguage(): String {
        return dataStore.getLanguage()
    }
}
