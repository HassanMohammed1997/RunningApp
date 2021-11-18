package com.tutorial.runningapp.data.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.tutorial.runningapp.utils.toByteArray

class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray = bitmap.toByteArray()

    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)


}