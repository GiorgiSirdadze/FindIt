package com.example.findit.di

import com.example.findit.data.repository.chat.GetAllChatsRepositoryImpl
import com.example.findit.data.repository.chat.GetMessageRepositoryImpl
import com.example.findit.data.repository.user.GetProfilePictureRepositoryImpl
import com.example.findit.data.repository.user.GetUserProfileRepositoryImpl
import com.example.findit.data.repository.setting.LanguageRepositoryImpl
import com.example.findit.data.repository.auth.LogInRepositoryImpl
import com.example.findit.data.repository.post.PostsRepositoryImpl
import com.example.findit.data.repository.auth.RegisterRepositoryImpl
import com.example.findit.data.repository.chat.SendMessageRepositoryImpl
import com.example.findit.data.repository.user.UpdateProfileImageRepositoryImpl
import com.example.findit.data.repository.user.UpdateUserProfileRepositoryImpl
import com.example.findit.data.repository.user.UploadImageRepositoryImpl
import com.example.findit.data.repository.post.UploadPostRepositoryImpl
import com.example.findit.data.repository.user.UserRepositoryImpl
import com.example.findit.domain.repository.chat.GetAllChatsRepository
import com.example.findit.domain.repository.chat.GetMessagesRepository
import com.example.findit.domain.repository.user.GetProfilePictureRepository
import com.example.findit.domain.repository.user.GetUserProfileRepository
import com.example.findit.domain.repository.setting.LanguageRepository
import com.example.findit.domain.repository.auth.LogInRepository
import com.example.findit.domain.repository.post.PostsRepository
import com.example.findit.domain.repository.auth.RegisterRepository
import com.example.findit.domain.repository.chat.SendMessageRepository
import com.example.findit.domain.repository.user.UpdateProfileImageRepository
import com.example.findit.domain.repository.user.UpdateUserProfileRepository
import com.example.findit.domain.repository.user.UploadImageRepository
import com.example.findit.domain.repository.post.UploadPostRepository
import com.example.findit.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideLogInRepository(impl: LogInRepositoryImpl) : LogInRepository

    @Binds
    abstract fun provideRegisterRepository(impl: RegisterRepositoryImpl) : RegisterRepository

    @Binds
    abstract fun provideLanguageRepository(impl : LanguageRepositoryImpl): LanguageRepository
    @Binds
    abstract fun provideUploadPostRepository(impl: UploadPostRepositoryImpl) : UploadPostRepository

    @Binds
    abstract fun provideUploadImageRepository(impl: UploadImageRepositoryImpl) : UploadImageRepository

    @Binds
    abstract fun provideUserRepository(impl: UserRepositoryImpl) : UserRepository


    @Binds
    abstract fun provideGetUserProfileRepository(impl : GetUserProfileRepositoryImpl): GetUserProfileRepository

    @Binds
    abstract fun provideUpdateUserProfileRepository(impl : UpdateUserProfileRepositoryImpl) : UpdateUserProfileRepository

    @Binds
    abstract fun provideGetPostsRepository(impl : PostsRepositoryImpl) : PostsRepository

    @Binds
    abstract fun provideUpdateProfileImageRepository(impl: UpdateProfileImageRepositoryImpl) : UpdateProfileImageRepository

    @Binds
    abstract fun provideGetAllChatsRepository(impl : GetAllChatsRepositoryImpl) : GetAllChatsRepository

    @Binds
    abstract fun provideGetMessagesRepository(impl : GetMessageRepositoryImpl) : GetMessagesRepository

    @Binds
    abstract fun provideSendMessageRepository(impl : SendMessageRepositoryImpl) : SendMessageRepository
    @Binds
    abstract fun provideGetProfilePictureRepository(impl: GetProfilePictureRepositoryImpl) : GetProfilePictureRepository
}