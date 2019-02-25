package com.iliasavin.rocketbanktest.data.model

import java.util.*

class PixelImage(width: Int, height: Int) {
    private var pixelsRows = width
    private var pixelsColumns = height
    private val random = Random()

    var pixels: Array<Array<PixelColorState>> = Array(pixelsRows) {
        Array(pixelsColumns) { PixelColorState.EMPTY }
    }

    fun setRandomly() {
        (0 until pixelsRows).forEach { i ->
            (0 until pixelsColumns).forEach { j ->
                pixels[i][j] = if (random.nextBoolean()) PixelColorState.EMPTY else PixelColorState.FILLED
            }
        }
    }

    fun updatePixels(pixels: Array<Array<PixelColorState>>) {
        if (pixels.size != pixelsRows) {
            pixelsRows = pixels.size
            pixelsColumns = pixels.first().size
            this.pixels = Array(pixelsRows) {
                Array(pixelsColumns) { PixelColorState.EMPTY }
            }
        }
        this.pixels.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, column ->
                this.pixels[rowIndex][columnIndex] = pixels[rowIndex][columnIndex]
            }
        }
    }

    fun clear() {
        this.pixels.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, column ->
                this.pixels[rowIndex][columnIndex] = PixelColorState.EMPTY
            }
        }
    }

    fun isColored(): Boolean {
        this.pixels.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, column ->
                if (this.pixels[rowIndex][columnIndex] == PixelColorState.COLORED) return true
            }
        }
        return false
    }

}