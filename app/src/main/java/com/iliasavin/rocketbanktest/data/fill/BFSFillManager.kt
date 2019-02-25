package com.iliasavin.rocketbanktest.data.fill

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import java.util.*

class BFSFillManager : FillManager, DirectionSearch {
    private var rows = DEFAULT_PIXEL_SIZE
    private var columns = DEFAULT_PIXEL_SIZE

    override var image: PixelImage = PixelImage(rows, columns)
    override var startPixel: Point = Point(0, 0)
    override var speed: Long = DEFAULT_SPEED
    override lateinit var handleNext: (Point) -> Unit
    override var isRunning: Boolean = false
    private var queue: Queue<Point> = LinkedList()

    override fun setSize(rows: Int, columns: Int) {
        this.rows = rows
        this.columns = columns

        this.image = PixelImage(this.rows, this.columns)
    }

    override fun start() {
        if (image.pixels[startPixel.x][startPixel.y] != PixelColorState.EMPTY) return
        isRunning = true

        image.pixels[startPixel.x][startPixel.y] = PixelColorState.COLORED
        queue.add(startPixel)
        handleNext(startPixel)

        while (queue.isNotEmpty() && isRunning) {
            val pixel = queue.poll()
            searchLeft(image, pixel)
            searchRight(image, pixel)
            searchTop(image, pixel)
            searchBottom(image, pixel)
        }

        onComplete()
    }


    override fun performColorAction(pixel: Point) {
        queue.add(pixel)
        onNextWithDelay(pixel)
    }

    override fun clear() {
        isRunning = false
        queue.clear()
        image.clear()
        startPixel = Point(0, 0)
    }
}