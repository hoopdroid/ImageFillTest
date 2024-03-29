package com.iliasavin.rocketbanktest.core.extension

import android.graphics.Paint
import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import kotlin.math.truncate

fun Paint.setPixelColor(pixelColorState: PixelColorState) {
    color = when (pixelColorState) {
        PixelColorState.COLORED -> PixelColorState.COLORED.color
        PixelColorState.EMPTY -> PixelColorState.EMPTY.color
        PixelColorState.FILLED -> PixelColorState.FILLED.color
    }
}

fun Point.toPixel(pixelWidth: Float, pixelHeight: Float): Point = Point(
    truncate(this.y / pixelHeight).toInt(),
    truncate(this.x / pixelWidth).toInt()
)
