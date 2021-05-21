package com.example.gepst

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayOutputStream

class RotationImage() {

    // вот эти 10 строчек кто-то пытался сделать 3 недели
    fun rotate(bitmap: Bitmap): Bitmap { //получаем Bitmap
        val width: Int = bitmap.width // Записываем в переменные его ширину и длину
        val height: Int = bitmap.height
        val newBitmap: Bitmap = Bitmap.createBitmap(height, width, bitmap.config) //Создаём новый Bitmap
        //Далее заполняем новый Bitmap пиксялими, конвертирую длину и ширину
        var pixel: Int
        for (x in 0 until width) {
            for (y in 0 until height) {
                pixel = bitmap.getPixel(x, y)
                newBitmap.setPixel(y, width - 1 - x, pixel)
            }
        }
        // Возвращаем уже новый Bitmap, а не изменненый старый!
        return newBitmap
    }







}