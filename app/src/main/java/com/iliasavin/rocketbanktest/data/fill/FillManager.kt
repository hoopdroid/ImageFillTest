package com.iliasavin.rocketbanktest.data.fill

import android.graphics.Point
import android.util.Log
import com.iliasavin.rocketbanktest.data.model.PixelImage

const val DEFAULT_PIXEL_SIZE = 32
const val DEFAULT_SPEED = 500L

interface FillManager {
    var image: PixelImage
    var startPixel: Point
    var handleNext: (Point) -> Unit
    var speed: Long
    var isRunning: Boolean

    fun setSize(rows: Int, columns: Int)

    fun start()

    fun onComplete() {
        isRunning = false
        val classMessage = this.javaClass.simpleName
        Log.d(classMessage, "$classMessage has finished work")
    }

    fun onNextWithDelay(pixel: Point) {
        if (!isRunning) return

        Thread.sleep(speed)
        handleNext(pixel)
    }

    fun clear()

    fun changeSpeed(speed: Long) {
        this.speed = speed
    }
}