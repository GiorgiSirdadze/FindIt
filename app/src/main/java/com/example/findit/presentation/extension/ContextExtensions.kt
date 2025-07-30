package com.example.findit.presentation.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


fun Context.getBitmapDescriptorFromVector(vectorResId: Int, width: Int = 40, height: Int = 40): BitmapDescriptor? {
    val vectorDrawable: Drawable = ContextCompat.getDrawable(this, vectorResId) ?: return null
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun Context.showAlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    positiveButtonClickAction: () -> Unit
) = with(AlertDialog.Builder(this)) {
    setTitle(title)
    setMessage(message)


    setPositiveButton(positiveButtonText) { _, _ ->
        positiveButtonClickAction.invoke()
    }

    setNegativeButton(negativeButtonText) { dialog, _ ->
        dialog.dismiss()
    }

    create().show()
}
