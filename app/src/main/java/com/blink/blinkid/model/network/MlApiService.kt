package com.blink.blinkid.model.network

import com.blink.blinkid.model.StudentExamValidations
import com.blink.blinkid.model.ValidateStudentRequest
import com.blink.blinkid.model.ValidationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MlApiService {

    @POST("/validate")
    suspend fun validateUser(@Body validateStudentRequest: ValidateStudentRequest): Response<List<ValidationResponse>>

}