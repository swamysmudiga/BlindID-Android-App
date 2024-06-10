package com.blink.blinkid.viewmodel

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blink.blinkid.model.LoginRequest
import com.blink.blinkid.model.LoginResponse
import com.blink.blinkid.commons.LocalDataStore
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.Constants
import com.blink.blinkid.model.User
import com.blink.blinkid.repo.LoginRepository
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val localDataStore: LocalDataStore
) : ViewModel() {


    private val _loginResponse =
        MutableStateFlow<NetworkResult<LoginResponse>>(NetworkResult.Initial)
    val loginResponse: StateFlow<NetworkResult<LoginResponse>> = _loginResponse.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginRepository.login(LoginRequest(email, password)).collect {
                _loginResponse.value = it
                if (it is NetworkResult.Success) {
                    localDataStore.saveObject(Constants.USER, it.body?.user)
                    it.body?.user?.id?.let { it1 -> localDataStore.saveInt(Constants.USER_ID, it1) }
                    localDataStore.saveBoolean(Constants.IS_LOGGED_IN, true)
                    localDataStore.saveString(Constants.TOKEN, it.body?.token ?: "")
                }
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return localDataStore.getBoolean(Constants.IS_LOGGED_IN, false)
    }

    fun isTeacher(): Boolean {
        localDataStore.getObject(Constants.USER, object : TypeToken<User>() {})?.let {
            it.roles.forEach { role ->
                if (role.name.lowercase(Locale.ROOT).contains("teacher", true)) {
                    return true
                }
            }
        }
        return false
    }

    fun isStaff(): Boolean {
        localDataStore.getObject(Constants.USER, object : TypeToken<User>() {})?.let {
            it.roles.forEach { role ->
                if (role.name.lowercase(Locale.ROOT).contains("staff", true)) {
                    return true
                }
            }
        }
        return false
    }


    fun getUser(): User? {
        return localDataStore.getObject(Constants.USER, object : TypeToken<User>() {})
    }


    fun logout() {
        localDataStore.saveBoolean(Constants.IS_LOGGED_IN, false)
        localDataStore.clearPreferences()
    }

}