package com.blink.blinkid.commons

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.blink.blinkid.model.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import java.io.File


fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ResponseBody.toErrorMessage(): ErrorResponse {
    val gson = Gson()
    return gson.fromJson(this.charStream(), ErrorResponse::class.java)
}

val File.size get() = if (!exists()) 0.0 else length().toDouble()
val File.sizeInKb get() = size / 1024
val File.sizeInMb get() = sizeInKb / 1024
val File.sizeInGb get() = sizeInMb / 1024
val File.sizeInTb get() = sizeInGb / 1024