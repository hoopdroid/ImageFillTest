package com.iliasavin.rocketbanktest.data.model

import java.util.*

class PixelImage {
    private var pixelsRows = 64
    private var pixelsColumns = 64

    var pixels: Array<Array<PixelColorState>> = Array(pixelsRows) { Array(pixelsColumns) { PixelColorState.EMPTY } }
    private val random = Random()

    constructor(width: Int, height: Int) {
        this.pixelsRows = height
        this.pixelsRows = width

        setRandomly()
    }

    constructor(pixels: Array<Array<PixelColorState>>) {
        this.pixelsRows = pixels.first().size
        this.pixelsColumns = pixels.size

        this.pixels = pixels
    }


    fun setRandomly() {
        (0 until pixelsRows).forEach { i ->
            (0 until pixelsColumns).forEach {j ->
                pixels[i][j] = if (random.nextBoolean()) PixelColorState.EMPTY else PixelColorState.FILLED
            }
        }
    }

}