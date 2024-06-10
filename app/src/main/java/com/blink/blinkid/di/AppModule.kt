package com.blink.blinkid.di

import android.content.Context
import android.content.SharedPreferences
import com.blink.blinkid.model.User
import com.blink.blinkid.commons.LocalDataStore
import com.blink.blinkid.model.Constants
import com.blink.blinkid.model.network.ApiService
import com.blink.blinkid.repo.ExamRepository
import com.blink.blinkid.repo.LoginRepository
import com.blink.blinkid.repo.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLocalDataStore(sharedPreferences: SharedPreferences, gson: Gson): LocalDataStore {
        return LocalDataStore(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(apiService: ApiService): LoginRepository {
        return LoginRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideExamRepository(apiService: ApiService): ExamRepository {
        return ExamRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideFirebase(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideRef(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }


}