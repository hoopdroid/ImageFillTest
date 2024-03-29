package com.iliasavin.rocketbanktest.data.fill

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import java.util.*

class DummyFillManager : FillManager {
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

        stack.add(startPixel)
        isRunning = true

        while (stack.isNotEmpty() && isRunning) {
            val obj = stack.pop()
            val x = obj.x
            var y = obj.y
            if (image.pixels[x][y] != PixelColorState.EMPTY) {
                continue
            }
            while (y > 0 && image.pixels[x][y - 1] == PixelColorState.EMPTY) {
                y--
            }
            while (y < image.pixels.size && image.pixels[x][y] == PixelColorState.EMPTY) {
                val pixel = Point(x, y)
                image.pixels[x][y] = PixelColorState.COLORED
                onNextWithDelay(pixel)
                if (x > 0) {
                    if (image.pixels[x - 1][y] == PixelColorState.EMPTY) {
                        stack.add(Point(x - 1, y))
                    }
                }
                if (x < image.pixels.first().size - 1) {
                    if (image.pixels[x + 1][y] == PixelColorState.EMPTY) {
                        stack.add(Point(x + 1, y))
                    }
                }

                y++
            }
        }

        onComplete()
    }

    override fun clear() {
        isRunning = false
        stack.clear()
        image.clear()
        startPixel = Point(0, 0)
    }
}