package com.example.myapplication.ui.util

import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import androidx.compose.material3.MaterialTheme

suspend fun extractDominantColor(
    context: Context,
    @DrawableRes imageRes: Int,
    fallbackColor: Int
): Color {
    val bitmap = BitmapFactory.decodeResource(context.resources, imageRes)
    val palette = Palette.from(bitmap).generate()
    val dominantColor = palette.getDominantColor(fallbackColor)
    return Color(dominantColor)
}

