package com.blink.blinkid.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blink.blinkid.commons.FileUtil
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.StudentExamValidations
import com.blink.blinkid.model.ValidateStudentRequest
import com.blink.blinkid.repo.ExamRepository
import com.blink.blinkid.repo.ValidationRepository
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ValidateImageViewModel @Inject constructor(
    private val storageReference: StorageReference,
    private val validationRepository: ValidationRepository,
    private val examRepository: ExamRepository,
    private val application: Application
) :
    ViewModel() {

    private val _result =
        MutableStateFlow<NetworkResult<StudentExamValidations>>(NetworkResult.Initial)
    val result = _result.asStateFlow()


    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    fun uploadImage(
        part: Uri,
        list: List<String> = emptyList(),
        examId: Int = 0,
        studentId: Int = 0
    ) {
        _result.value = NetworkResult.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val imageResult = async {
                uploadImageAsync(part) { image ->
                    Log.e("ValidateImageViewModel", "uploadImage: $image")
                    viewModelScope.launch {
                        val validationRes = async {
                            validationRepository.validate(ValidateStudentRequest(list, image))
                                .collect { res ->
                                    when (res) {
                                        is NetworkResult.Success -> {
                                            res.body?.let {
                                                if (it.count { it.result == 0 } < it.count{it.result == 1}){
                                                    examRepository.addStudentExamValidation(
                                                        StudentExamValidations(
                                                            examId = examId,
                                                            studentId = studentId,
                                                            status = true,
                                                            image = image
                                                        )
                                                    ).collect {
                                                        _result.value = it
                                                    }
                                                    _error.value = "Validation successful"
                                                }
                                                else{
                                                    examRepository.addStudentExamValidation(
                                                        StudentExamValidations(
                                                            examId = examId,
                                                            studentId = studentId,
                                                            status = false,
                                                            image = image
                                                        )
                                                    ).collect {
                                                        _result.value = it
                                                    }
                                                    _error.value = "Validation failed"
                                                }

                                            }

                                        }

                                        is NetworkResult.Error -> {
                                            _result.value = NetworkResult.Initial
                                            _error.value = res.errorMessage
//                                            examRepository.addStudentExamValidation(
//                                                StudentExamValidations(
//                                                    examId = examId,
//                                                    studentId = studentId,
//                                                    status = false,
//                                                    image = image
//                                                )
//                                            ).collect {
//                                                _result.value = it
//                                            }
                                        }

                                        else -> {}
                                    }
                                }
                        }
                        validationRes.await()

                    }
                }
            }
            imageResult.await()
        }
    }

    private suspend fun uploadImageAsync(part: Uri, imageUploadCallback: (String) -> Unit) {

        val actualPart = FileUtil.from(application.applicationContext, part)
        val compressed = Compressor.compress(application.applicationContext, actualPart) {
            resolution(512,512)
            format(Bitmap.CompressFormat.JPEG)
            quality(80)
            size(204_800)
        }
        val ref = storageReference
            .child(
                "images/"
                        + UUID.randomUUID().toString()+".jpeg"
            )

        ref.putFile(compressed.toUri())
            .addOnSuccessListener {
                val task = it.storage.downloadUrl
                task.addOnSuccessListener { uri ->
                    imageUploadCallback(uri.toString())
                }
            }.addOnFailureListener {
                _error.value = it.message.toString()
                _result.value = NetworkResult.Initial
                Log.e("ImageViewModel", "uploadImageAsync: failure ${it.message}")
            }
            .addOnProgressListener {
                Log.e("ImageViewModel", "uploadImageAsync:  progress${it.bytesTransferred}")
            }

    }
}
