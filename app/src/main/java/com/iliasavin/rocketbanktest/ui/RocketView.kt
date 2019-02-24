package com.iliasavin.rocketbanktest.ui

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage

interface RocketView {
    fun refreshFirstImage(pixel: Point, stateColor: PixelColorState)
    fun refreshSecondImage(pixel: Point, stateColor: PixelColorState)
    fun update(pixelImage: PixelImage)
    fun showTimeResult(result: String)
}