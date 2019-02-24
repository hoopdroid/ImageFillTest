package com.iliasavin.rocketbanktest.data.model

import android.graphics.Color

enum class PixelColorState(val color: Int) {
    EMPTY(Color.WHITE),
    FILLED(Color.parseColor("#7F00FF")),
    COLORED(Color.parseColor("#FF6600"))
}

