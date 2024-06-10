package com.blink.blinkid.repo

import android.util.Log
import com.blink.blinkid.model.Exam
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.commons.toErrorMessage
import com.blink.blinkid.model.AddStudentRequest
import com.blink.blinkid.model.Group
import com.blink.blinkid.model.StudentExamValidations
import com.blink.blinkid.model.User
import com.blink.blinkid.model.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExamRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun addUser(user: User): Flow<NetworkResult<User>> = flow {
        try {
            val response = apiService.addUser(user)
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

    suspend fun addStudent(addStudentRequest: AddStudentRequest): Flow<NetworkResult<User>> = flow {
        try {
            val response = apiService.addStudent(addStudentRequest)
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

    suspend fun getExams(): Flow<NetworkResult<List<Exam>>> = flow {
        try {
            val response = apiService.getExams()
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
            e.printStackTrace()
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }

    suspend fun getGroups(): Flow<NetworkResult<List<Group>>> = flow {
        try {
            val response = apiService.getGroups()
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
            e.printStackTrace()
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }

    suspend fun addExam(exam: Exam): Flow<NetworkResult<Exam>> = flow {
        try {
            val response = apiService.addExam(exam)
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

    suspend fun addGroup(exam: Group): Flow<NetworkResult<Group>> = flow {
        try {
            val response = apiService.addGroup(exam)
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


    suspend fun updateExam(examId: Int, exam: Exam): Flow<NetworkResult<Exam>> = flow {
        try {
            val response = apiService.updateExam(examId, exam)
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

    suspend fun updateGroup(examId: Int, exam: Group): Flow<NetworkResult<Group>> = flow {
        try {
            val response = apiService.updateGroup(examId, exam)
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

    suspend fun deleteExam(examId: Int): Flow<NetworkResult<Exam>> = flow {
        try {
            val response = apiService.deleteExam(examId)
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

    suspend fun deleteGroup(examId: Int): Flow<NetworkResult<Group>> = flow {
        try {
            val response = apiService.deleteGroup(examId)
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


    suspend fun addStudentToExam(examId: Int, userId: Int): Flow<NetworkResult<Exam>> = flow {
        try {
            val response = apiService.addExamStudent(examId, userId)
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

    suspend fun addStudentToGroup(examId: Int, userId: Int): Flow<NetworkResult<Group>> = flow {

        val response = apiService.addGroupStudent(examId, userId)
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

    }

    suspend fun addAdminToExam(examId: Int, userId: Int): Flow<NetworkResult<Exam>> = flow {
        try {
            val response = apiService.addExamAdmin(examId, userId)
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

    suspend fun addAdminToGroup(examId: Int, userId: Int): Flow<NetworkResult<Group>> = flow {
        try {
            val response = apiService.addGroupAdmin(examId, userId)
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


    suspend fun deleteStudentFromExam(examId: Int, userId: Int): Flow<NetworkResult<Exam>> =
        flow {
            try {
                val response = apiService.deleteExamStudent(examId, userId)
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

    suspend fun deleteStudentFromGroup(examId: Int, userId: Int): Flow<NetworkResult<Group>> =
        flow {
            try {
                val response = apiService.deleteGroupStudent(examId, userId)
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


    suspend fun deleteAdminFromExam(examId: Int, userId: Int): Flow<NetworkResult<Exam>> =
        flow {
            try {
                val response = apiService.deleteExamAdmin(examId, userId)
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

    suspend fun deleteAdminFromGroup(examId: Int, userId: Int): Flow<NetworkResult<Group>> =
        flow {
            try {
                val response = apiService.deleteGroupAdmin(examId, userId)
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

    fun getExam(examId: Int): Flow<NetworkResult<Exam>> = flow {
        try {
            val response = apiService.getExam(examId)
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

    fun getGroup(examId: Int): Flow<NetworkResult<Group>> = flow {
        try {
            val response = apiService.getGroup(examId)
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

    fun getStudentExamValidations(examId: Long): Flow<NetworkResult<List<StudentExamValidations>>> =
        flow {
            try {
                val response = apiService.getStudentExamValidations(examId)
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


    suspend fun getStudentExamValidation(
        examId: Long,
        userId: Long
    ): Flow<NetworkResult<StudentExamValidations>> = flow {
        try {
            val response = apiService.getStudentExamValidation(examId, userId)
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

    suspend fun addStudentExamValidation(studentExamValidations: StudentExamValidations): Flow<NetworkResult<StudentExamValidations>> =
        flow {
            try {
                val response = apiService.addStudentExamValidation(studentExamValidations)
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