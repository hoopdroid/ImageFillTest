package com.iliasavin.rocketbanktest.util

import android.graphics.Paint
import android.graphics.Point
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import kotlin.math.truncate

fun Paint.setPixelColor(pixelColorState: PixelColorState) {
    color = when (pixelColorState) {
        PixelColorState.COLORED -> PixelColorState.COLORED.color
        PixelColorState.EMPTY -> PixelColorState.EMPTY.color
        PixelColorState.FILLED -> PixelColorState.FILLED.color
        else -> PixelColorState.EMPTY.color
    }
}

fun Point.toPixel(pixelWidth: Float, pixelHeight: Float): Point {
    return Point(
        truncate(this.y / pixelHeight).toInt(),
        truncate(this.x / pixelWidth).toInt()
    )
}

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun SeekBar.onChange(passProgress: (Int) -> Unit) {
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            passProgress(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }

    })
}

fun Spinner.onSelection(passPosition: (Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            passPosition(position)
        }

    }
}