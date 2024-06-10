package com.blink.blinkid.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blink.blinkid.commons.FileUtil
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ImageViewModel @Inject constructor(
    private val application: Application,
    private val storageReference: StorageReference
) :
    ViewModel() {

    private val _uploadedImage = MutableStateFlow("")
    val uploadedImage = _uploadedImage.asStateFlow()


    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    fun uploadImage(part: Uri) {
        viewModelScope.launch(Dispatchers.IO) {

            val actualPart = FileUtil.from(application.applicationContext, part)

            val compressed = Compressor.compress(application.applicationContext, actualPart) {
                resolution(512,512)
                format(Bitmap.CompressFormat.JPEG)
                quality(80)
                size(204_800)
            }
            uploadImageAsync(compressed)
        }
    }



    private suspend fun uploadImageAsync(part: File) {

        val ref = storageReference
            .child(
                "images/"
                        + UUID.randomUUID().toString() + ".jpeg"
            )

        ref.putFile(part.toUri())
            .addOnSuccessListener {
                val task = it.storage.downloadUrl
                task.addOnSuccessListener { uri ->
                    _uploadedImage.value = uri.toString()
                }
            }.addOnFailureListener {
                _error.value = it.message.toString()
                Log.e("ImageViewModel", "uploadImageAsync: failure ${it.message}")
            }
            .addOnProgressListener {
                Log.e("ImageViewModel", "uploadImageAsync:  progress${it.bytesTransferred}")
            }

    }
}
