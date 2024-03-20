package cn.quibbler.coroutine.ui.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

fun drawableToBitmap(drawable: Drawable?): Bitmap? {
    return when (drawable) {
        is BitmapDrawable -> {
            drawable.bitmap
        }

        is ColorDrawable -> {
            val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }

        else -> {
            var bitmap: Bitmap? = null
            drawable?.let {
                bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap!!)
                it.setBounds(0, 0, canvas.width, canvas.height)
                it.draw(canvas)
            }
            bitmap
        }
    }

}