package com.iliasavin.rocketbanktest.fill

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import java.util.*


class BFSFillManager : FillManager {
    private val queue: Queue<Point> = LinkedList()
    override var image: PixelImage = PixelImage(64, 64)

    override var startPixel: Point = Point(0, 0)

    override fun start() {
        if (image.pixels[startPixel.x][startPixel.y] != PixelColorState.EMPTY) {
            return
        }

        image.pixels[startPixel.x][startPixel.y] = PixelColorState.COLORED
        queue.add(startPixel)
        onNext(startPixel)

        while (queue.isNotEmpty()) {
            val pixel = queue.poll()
            // West
            if (pixel.y > 0) {
                val westPixel = Point(pixel.x, pixel.y - 1)
                if (image.pixels[pixel.x][pixel.y - 1] == PixelColorState.EMPTY) {
                    image.pixels[pixel.x][pixel.y - 1] = PixelColorState.COLORED
                    queue.add(westPixel)

                    onNextWithDelay(westPixel)
                }
            }

            // East
            if (pixel.y < image.pixels.size - 1) {
                val eastPixel = Point(pixel.x, pixel.y + 1)
                if (image.pixels[pixel.x][pixel.y + 1] == PixelColorState.EMPTY) {
                    image.pixels[pixel.x][pixel.y + 1] = PixelColorState.COLORED
                    queue.add(eastPixel)

                    onNextWithDelay(eastPixel)
                }
            }

            // North
            if (pixel.x > 0) {
                val northPixel = Point(pixel.x - 1, pixel.y)
                if (image.pixels[pixel.x - 1][pixel.y] == PixelColorState.EMPTY) {
                    image.pixels[pixel.x - 1][pixel.y] = PixelColorState.COLORED
                    queue.add(northPixel)

                    onNextWithDelay(northPixel)

                }
            }

            // South
            if (pixel.x < image.pixels.first().size - 1) {
                val southPixel = Point(pixel.x + 1, pixel.y)
                if (image.pixels[pixel.x + 1][pixel.y] == PixelColorState.EMPTY) {
                    image.pixels[pixel.x + 1][pixel.y] = PixelColorState.COLORED
                    queue.add(southPixel)

                    onNextWithDelay(southPixel)
                }
            }
        }
    }

    override lateinit var onNext: (Point) -> Unit

    fun onNextWithDelay(pixel: Point) {
        Thread.sleep(100)
        onNext(pixel)
    }

    override fun finish() {
    }

    override fun onComplete() {
    }

    override fun onError() {
    }
}