package com.iliasavin.rocketbanktest.data.fill

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage

interface DirectionSearch {
     fun searchLeft(image: PixelImage, pixel: Point) {
        if (pixel.y > 0) {
            val leftPixel = Point(pixel.x, pixel.y - 1)
            if (image.pixels[pixel.x][pixel.y - 1] == PixelColorState.EMPTY) {
                image.pixels[pixel.x][pixel.y - 1] = PixelColorState.COLORED
                performColorAction(leftPixel)
            }
        }
    }

     fun searchRight(image: PixelImage, pixel: Point) {
        if (pixel.y < image.pixels.size - 1) {
            val rightPixel = Point(pixel.x, pixel.y + 1)
            if (image.pixels[pixel.x][pixel.y + 1] == PixelColorState.EMPTY) {
                image.pixels[pixel.x][pixel.y + 1] = PixelColorState.COLORED
                performColorAction(rightPixel)
            }
        }
    }

     fun searchTop(image: PixelImage, pixel: Point) {
        if (pixel.x > 0) {
            val topPixel = Point(pixel.x - 1, pixel.y)
            if (image.pixels[pixel.x - 1][pixel.y] == PixelColorState.EMPTY) {
                image.pixels[pixel.x - 1][pixel.y] = PixelColorState.COLORED
                performColorAction(topPixel)
            }
        }

    }

     fun searchBottom(image: PixelImage, pixel: Point) {
        if (pixel.x < image.pixels.first().size - 1) {
            val bottomPixel = Point(pixel.x + 1, pixel.y)
            if (image.pixels[pixel.x + 1][pixel.y] == PixelColorState.EMPTY) {
                image.pixels[pixel.x + 1][pixel.y] = PixelColorState.COLORED
                performColorAction(bottomPixel)
            }
        }
    }
    

    fun performColorAction(pixel: Point)
}
