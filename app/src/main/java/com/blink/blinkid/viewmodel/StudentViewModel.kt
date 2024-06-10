package com.blink.blinkid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blink.blinkid.model.LoginRequest
import com.blink.blinkid.model.LoginResponse
import com.blink.blinkid.commons.LocalDataStore
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.Constants
import com.blink.blinkid.model.Exam
import com.blink.blinkid.repo.LoginRepository
import com.blink.blinkid.repo.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
) : ViewModel() {

    private val _studentExams = MutableStateFlow<NetworkResult<List<Exam>>>(NetworkResult.Initial)
    val studentExams = _studentExams.asStateFlow()

    fun getStudentExams() {
        viewModelScope.launch {
            studentRepository.getStudentExams().collect {
                _studentExams.value = it
            }
        }
    }





}