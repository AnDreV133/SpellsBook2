package com.example.spellsbook.app.ui.compose

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

fun <T> ViewModel.emitState(destination: MutableStateFlow<T>, state: CoroutineScope.() -> T) {
    viewModelScope.launch {
        destination.emit(
            state()
        )
    }
}

suspend fun export(context: Context, streamData: Pair<String, InputStream>) =
    withContext(Dispatchers.IO) {
        try {
            val fileName = streamData.first
            val fileExtension = fileName.substringAfterLast(".")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/$fileExtension")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = context.contentResolver
                val uri: Uri? =
                    resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        streamData.second.use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
            } else {
                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                File(downloadsDir, fileName).outputStream().use { destinationFile ->
                    streamData.second.copyTo(destinationFile)
                }
            }

            true
        } catch (e: Exception) {
            Log.e("Export Util", e.toString())
            false
        } finally {
            streamData.second.close()
        }
    }