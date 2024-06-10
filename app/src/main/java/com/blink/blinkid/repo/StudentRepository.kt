package com.blink.blinkid.repo

import com.blink.blinkid.commons.LocalDataStore
import com.blink.blinkid.model.LoginRequest
import com.blink.blinkid.model.LoginResponse
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.commons.toErrorMessage
import com.blink.blinkid.model.Constants
import com.blink.blinkid.model.Exam
import com.blink.blinkid.model.Group
import com.blink.blinkid.model.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StudentRepository @Inject constructor(private val apiService: ApiService,private val localDataStore: LocalDataStore) {
    suspend fun getStudentExams(): Flow<NetworkResult<List<Exam>>> = flow {
        try {
            val response = apiService.getStudentExams(localDataStore.getInt(Constants.USER_ID,0))
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

    suspend fun getStudentGroups(): Flow<NetworkResult<List<Group>>> = flow {
        try {
            val response = apiService.getStudentGroups(localDataStore.getInt(Constants.USER_ID,0))
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