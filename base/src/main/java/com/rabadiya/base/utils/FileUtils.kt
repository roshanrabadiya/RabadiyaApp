package com.rabadiya.base.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.File

object FileUtils {
     /*fun compressFile(file: File, targetSize: Long = 300_000L): File {
        var currentFile = file
        var currentSize = file.length()

        while (currentSize > targetSize) {
            val compressedFile = File(currentFile.parent, currentFile.nameWithoutExtension + "_compressed.jpg")
            currentFile.inputStream().use { input ->
                compressedFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // Compress the image using a lower quality setting
            val options = BitmapFactory.Options()
            options.inSampleSize = 2 // Reduce the image size by half
            val bitmap = BitmapFactory.decodeFile(compressedFile.path, options)
            val outputStream = compressedFile.outputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.flush()
            outputStream.close()

            currentFile = compressedFile
            currentSize = currentFile.length()
        }

        return currentFile
    }*/

    fun compressFile(file: File, targetSize: Long = 300_000L): File {
        var currentFile = file
        var currentSize = file.length()
        var quality = 90 // Start with high quality

        // Get the original image orientation
        val exif = ExifInterface(file.path)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        while (currentSize > targetSize && quality > 10) {
            val compressedFile = File(currentFile.parent,
                "${file.nameWithoutExtension}_compressed.jpg")

            // Decode with options to get original bitmap
            val bitmap = BitmapFactory.decodeFile(file.path)

            // Rotate bitmap according to EXIF data if needed
            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }

            // Compress bitmap
            compressedFile.outputStream().use { output ->
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
            }

            // Clean up if we're not using the original bitmap
            if (rotatedBitmap != bitmap) {
                rotatedBitmap.recycle()
            }

            // Check new size - if still too large, reduce quality more for next iteration
            currentFile = compressedFile
            currentSize = currentFile.length()
            quality -= 10
        }

        if (currentSize > targetSize) {
            val finalCompressedFile = File(currentFile.parent,
                "${file.nameWithoutExtension}_final.jpg")

            val options = BitmapFactory.Options()
            options.inSampleSize = 2 // Reduce dimensions by half

            val bitmap = BitmapFactory.decodeFile(currentFile.path, options)
            finalCompressedFile.outputStream().use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
            }
            bitmap.recycle()

            return finalCompressedFile
        }

        return currentFile
    }

    private fun rotateBitmap(bitmap: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

