package com.blink.blinkid.repo

import com.blink.blinkid.model.LoginRequest
import com.blink.blinkid.model.LoginResponse
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.commons.toErrorMessage
import com.blink.blinkid.model.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(loginRequest: LoginRequest): Flow<NetworkResult<LoginResponse>> = flow {
        try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(NetworkResult.Success(it))
                }
            } else {
                emit(
                    NetworkResult.Error(
                        response.errorBody()?.toErrorMessage()?.message ?: "An error occurred"
                    )
                )
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }

}