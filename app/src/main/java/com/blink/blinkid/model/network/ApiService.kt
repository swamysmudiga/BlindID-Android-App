package com.blink.blinkid.model.network

import com.blink.blinkid.model.AddStudentRequest
import com.blink.blinkid.model.Exam
import com.blink.blinkid.model.Group
import com.blink.blinkid.model.LoginRequest
import com.blink.blinkid.model.LoginResponse
import com.blink.blinkid.model.StudentExamValidations
import com.blink.blinkid.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @POST("users")
    suspend fun addUser(@Body user: User): Response<User>

    @POST("users/student")
    suspend fun addStudent(@Body addStudentRequest: AddStudentRequest): Response<User>

    @GET("users/students")
    suspend fun getStudents(): Response<List<User>>

    @GET("users/students")
    suspend fun getAdmins(): Response<List<User>>

    @POST("exams")
    suspend fun addExam(@Body exam: Exam): Response<Exam>

    @POST("groups")
    suspend fun addGroup(@Body group: Group) :Response<Group>

    @GET("exams")
    suspend fun getExams(): Response<List<Exam>>

    @GET("groups")
    suspend fun getGroups(): Response<List<Group>>

    @GET("exams/{examId}")
    suspend fun getExam(@Path("examId") examId: Int): Response<Exam>

    @GET("groups/{examId}")
    suspend fun getGroup(@Path("examId") examId: Int): Response<Group>

    @PUT("exams/{examId}")
    suspend fun updateExam(
        @Path("examId") examId: Int,
        @Body exam: Exam
    ): Response<Exam>


    @PUT("groups/{examId}")
    suspend fun updateGroup(
        @Path("examId") examId: Int,
        @Body group: Group
    ): Response<Group>

    @POST("exams/{examId}/users/{userId}")
    suspend fun addExamStudent(
        @Path("examId") examId: Int,
        @Path("userId") userId: Int
    ): Response<Exam>


    @POST("groups/{examId}/users/{userId}")
    suspend fun addGroupStudent(
        @Path("examId") examId: Int,
        @Path("userId") userId: Int
    ): Response<Group>

    @POST("exams/{examId}/admins/{userId}")
    suspend fun addExamAdmin(
        @Path("examId") examId: Int,
        @Path("userId") userId: Int
    ): Response<Exam>


    @POST("groups/{examId}/admins/{userId}")
    suspend fun addGroupAdmin(
        @Path("examId") examId: Int,
        @Path("userId") userId: Int
    ): Response<Group>

    @DELETE("groups/{examId}/users/{userId}")
    suspend fun deleteGroupStudent(
        @Path("examId") examId: Int,
        @Path("userId") userId: Int
    ): Response<Group>

    @DELETE("exams/{examId}/users/{userId}")
    suspend fun deleteExamStudent(
        @Path("examId") examId: Int,
        @Path("userId") userId: Int
    ): Response<Exam>


    @DELETE("exams/{examId}/admins/{userId}")
    suspend fun deleteExamAdmin(
        @Path("examId") examId: Int,
        @Path("userId") userId: Int
    ): Response<Exam>

    @DELETE("groups/{examId}/admins/{userId}")
    suspend fun deleteGroupAdmin(
        @Path("examId") examId: Int,
        @Path("userId") userId: Int
    ): Response<Group>


    @DELETE("exams/{examId}")
    suspend fun deleteExam(@Path("examId") examId: Int): Response<Exam>

    @DELETE("groups/{examId}")
    suspend fun deleteGroup(@Path("examId") examId: Int): Response<Group>


    @GET("users/{userId}/exams")
    suspend fun getStudentExams(@Path("userId") userID: Int): Response<List<Exam>>


    @GET("users/{userId}/groups")
    suspend fun getStudentGroups(@Path("userId") userID: Int): Response<List<Group>>


    @GET("student-exams-validations/{examId}")
    suspend fun getStudentExamValidations(@Path("examId") examId: Long): Response<List<StudentExamValidations>>

    @GET("student-exams-validations/{examId}/{userId}")
    suspend fun getStudentExamValidation(
        @Path("examId") examId: Long,
        @Path("userId") userId: Long
    ): Response<StudentExamValidations>

    @POST("student-exams-validations")
    suspend fun addStudentExamValidation(@Body studentExamValidations: StudentExamValidations): Response<StudentExamValidations>

    @PUT("student-exams-validations/{id}")
    suspend fun updateStudentExamValidation(
        @Path("id") id: Long,
        @Body studentExamValidations: StudentExamValidations
    ): Response<StudentExamValidations>



}