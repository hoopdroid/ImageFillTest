package com.iliasavin.rocketbanktest.fill

import android.graphics.Point
import android.util.Log
import com.iliasavin.rocketbanktest.data.model.PixelImage

interface FillManager {
    var image: PixelImage
    var startPixel: Point
    var onNext: (Point) -> Unit
    var speed: Long
    var isRunning: Boolean

    fun setSize(rows: Int, columns: Int)

    fun start()
    fun onComplete() {
        isRunning = false
        Log.d(this.javaClass.simpleName, "${this.javaClass.simpleName} has finished work")
    }

    fun onNextWithDelay(pixel: Point) {
        if (!isRunning) return

        Thread.sleep(speed)
        onNext(pixel)
    }

    fun clear()

    fun changeSpeed(speed: Long) {
        this.speed = speed
    }
}