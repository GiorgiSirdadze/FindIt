package com.example.findit.di

import com.example.findit.domain.usecase.user.EditProfileValidationUseCase
import com.example.findit.domain.usecase.auth.RegisterValidationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideValidateRegisterUseCase() : RegisterValidationUseCase {
        return RegisterValidationUseCase()
    }

    @Provides
    fun provideEditProfileValidationUseCase() : EditProfileValidationUseCase {
        return EditProfileValidationUseCase()
    }

}