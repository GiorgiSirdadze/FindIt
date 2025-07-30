package com.example.findit.domain.repository.setting

interface LanguageRepository {
    suspend fun setAppLanguage(language: String)
    suspend fun getAppLanguage(): String
}
