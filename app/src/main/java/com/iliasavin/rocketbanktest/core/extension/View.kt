package com.iliasavin.rocketbanktest.core.extension

import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.Spinner

fun SeekBar.onChange(passProgress: (Int) -> Unit) {
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) = passProgress(progress)
        override fun onStartTrackingTouch(seekBar: SeekBar) = Unit
        override fun onStopTrackingTouch(seekBar: SeekBar) = Unit

    })
}

fun Spinner.onSelection(passPosition: (Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>) = Unit
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) =
            passPosition(position)
    }
}