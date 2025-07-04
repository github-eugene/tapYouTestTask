package com.eugene.testtaskfortechspire.usecase

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream


class SaveGraphUseCase(
    private val context: Context
) {

    suspend operator fun invoke(bitmap: Bitmap): Result<String> = withContext(Dispatchers.IO) {
        try {
            val filename = "graph_${System.currentTimeMillis()}.png"
            val mimeType = "image/png"
            val outputStream: OutputStream?

            val contentResolver = context.contentResolver

            val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val imageUri = contentResolver.insert(imageCollection, contentValues)
                ?: return@withContext Result.failure(Exception("Failed to create new MediaStore record."))

            outputStream = contentResolver.openOutputStream(imageUri)
                ?: return@withContext Result.failure(Exception("Failed to get output stream."))

            outputStream.use {
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {
                    return@withContext Result.failure(Exception("Failed to save bitmap."))
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(imageUri, contentValues, null, null)
            }

            Result.success(filename)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}