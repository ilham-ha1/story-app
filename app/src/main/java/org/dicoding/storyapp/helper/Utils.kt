package org.dicoding.storyapp.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file: File): File {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(file.path, options)

    val srcWidth = options.outWidth
    val srcHeight = options.outHeight
    var scaleFactor = 1

    if (srcWidth > 1080 || srcHeight > 1080) {
        scaleFactor = if (srcWidth > srcHeight) {
            srcWidth / 1080
        } else {
            srcHeight / 1080
        }
    }

    val exif = ExifInterface(file.path)
    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

    val bitmapOptions = BitmapFactory.Options()
    bitmapOptions.inSampleSize = scaleFactor
    bitmapOptions.inMutable = true
    val bitmap = BitmapFactory.decodeFile(file.path, bitmapOptions)

    var rotation = 0
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90
        ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180
        ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270
    }

    if (rotation != 0) {
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, FileOutputStream(file))
        return file
    } else {
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
}