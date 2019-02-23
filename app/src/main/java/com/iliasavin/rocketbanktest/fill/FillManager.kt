package com.iliasavin.rocketbanktest.fill

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelImage

interface FillManager {
    var image: PixelImage
    var startPixel: Point
    var onNext: (Point) -> Unit

    fun start()
    fun finish()

    fun onComplete()
    fun onError()
}