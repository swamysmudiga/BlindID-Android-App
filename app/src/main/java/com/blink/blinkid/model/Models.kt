package com.blink.blinkid.model

data class ErrorResponse(
    val description: String,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val email: String,
    val token: String,
    val user: User
)

data class User(
    val email: String,
    val id: Int? = null,
    val images: List<Image>,
    val password: String,
    val roles: List<Role>,
    val status: String,
    val username: String
)

data class Role(
    val id: Int,
    val name: String
)

data class Image(
    val id: Int,
    val url: String
)

data class Exam(
    val admins: List<User> = emptyList(),
    val description: String,
    val examDate: String,
    val examDuration: String,
    val examLocation: String,
    val examTime: String,
    val id: Int? = null,
    val name: String,
    val users: List<User> = emptyList(),
    var examValidations: List<StudentExamValidations> = emptyList()
)

data class Group(
    val admins: List<User> = emptyList(),
    val description: String,
    val id: Int? = null,
    val name: String,
    val users: List<User> = emptyList(),
    var examValidations: List<StudentExamValidations> = emptyList()
)

data class AddStudentRequest(
    val email: String,
    val password: String,
    val username: String,
    val image: String
)

data class StudentExamValidations(
    val examId: Int,
    val id: Int? = null,
    val image: String,
    val status: Boolean,
    val studentId: Int
)

   data class ValidateStudentRequest(
    val image_urls: List<String>,
    val validation_url: String
)

data class ValidationResponse(
    val image_url: String,
    val result: Int
)