package com.example.findit.domain.usecase.settings

import com.example.findit.domain.repository.setting.LanguageRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
)  {
     suspend operator fun invoke(language: String) {
        repository.setAppLanguage(language)
    }
}
