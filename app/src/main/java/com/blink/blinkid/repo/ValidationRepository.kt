package com.blink.blinkid.repo

import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.commons.toErrorMessage
import com.blink.blinkid.model.ValidateStudentRequest
import com.blink.blinkid.model.ValidationResponse
import com.blink.blinkid.model.network.ApiService
import com.blink.blinkid.model.network.MlApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidationRepository @Inject constructor(
    private val apiService: ApiService,
    private val mlApiService: MlApiService
) {

    suspend fun validate(validationRequest: ValidateStudentRequest): Flow<NetworkResult<List<ValidationResponse>>> =
        flow {
            try {
                val response = mlApiService.validateUser(validationRequest)
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