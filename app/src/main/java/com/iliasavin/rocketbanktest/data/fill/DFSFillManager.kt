package com.iliasavin.rocketbanktest.data.fill

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import java.util.*

class DFSFillManager : FillManager, DirectionSearch {
    private var rows = DEFAULT_PIXEL_SIZE
    private var columns = DEFAULT_PIXEL_SIZE

    override var image: PixelImage = PixelImage(rows, columns)
    override var startPixel: Point = Point(0, 0)
    override var speed: Long = DEFAULT_SPEED
    override lateinit var handleNext: (Point) -> Unit
    override var isRunning: Boolean = false
    private val stack = Stack<Point>()

    override fun setSize(rows: Int, columns: Int) {
        this.rows = rows
        this.columns = columns

        this.image = PixelImage(this.rows, this.columns)
    }

    override fun start() {
        if (image.pixels[startPixel.x][startPixel.y] != PixelColorState.EMPTY) return

        isRunning = true

        image.pixels[startPixel.x][startPixel.y] = PixelColorState.COLORED
        stack.push(startPixel)
        onNextWithDelay(startPixel)

        while (stack.isNotEmpty() && isRunning) {
            val pixel = stack.pop()
            searchLeft(image, pixel)
            searchRight(image, pixel)
            searchTop(image, pixel)
            searchBottom(image, pixel)
        }

        onComplete()
    }

    override fun performColorAction(pixel: Point) {
        stack.push(pixel)
        onNextWithDelay(pixel)
    }

    override fun clear() {
        isRunning = false

        stack.clear()
        image.clear()
        startPixel = Point(0, 0)
    }
}