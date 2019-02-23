package com.iliasavin.rocketbanktest.util

import android.graphics.Color
import android.graphics.Paint

fun Paint.setPixelColor(value: Int){
    color = if (value == 0) {
        Color.WHITE
    } else {
        Color.BLACK
    }
}