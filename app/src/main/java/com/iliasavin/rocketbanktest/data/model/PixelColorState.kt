package com.iliasavin.rocketbanktest.data.model

import android.graphics.Color

enum class PixelColorState(val color: Int) {
    EMPTY(Color.WHITE),
    FILLED(Color.BLACK),
    COLORED(Color.CYAN)
}
