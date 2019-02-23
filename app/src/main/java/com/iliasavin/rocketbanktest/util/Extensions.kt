package com.iliasavin.rocketbanktest.util

import android.graphics.Paint
import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import kotlin.math.truncate

fun Paint.setPixelColor(pixelColorState: PixelColorState) {
    color = when (pixelColorState) {
        PixelColorState.COLORED -> PixelColorState.COLORED.color
        PixelColorState.EMPTY -> PixelColorState.EMPTY.color
        PixelColorState.FILLED -> PixelColorState.FILLED.color
        else -> PixelColorState.EMPTY.color
    }
}

fun Point.toPixel(pixelWidth: Float, pixelHeight: Float): Point {
    return Point(
        truncate(this.y / pixelHeight).toInt(),
        truncate(this.x / pixelWidth).toInt()
    )
}