package com.iliasavin.rocketbanktest.ui

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage

interface RocketView {
    fun invalidate(pixel: Point, stateColor: PixelColorState)
    fun regenerate()
}