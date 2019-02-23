package com.iliasavin.rocketbanktest.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.iliasavin.rocketbanktest.util.setPixelColor

class RocketImageView : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.style = Paint.Style.FILL
    }

    private var pixelsWidth = 24
    private var pixelsHeight = 24

    private var pixels = Array(pixelsWidth) { Array(pixelsHeight) { (0..1).random() } }
    private var selectedPixels = mutableListOf<Pair<Int, Int>>()

    fun generatePixels(width: Int, height: Int) {
        this.pixelsWidth = width
        this.pixelsHeight = height

        pixels = Array(this.pixelsWidth) { Array(this.pixelsHeight) { (0..1).random() } }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

            pixelsWidth = measuredWidth / pixels.first().size
            pixelsHeight = measuredHeight / pixels.size

            pixels.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, value ->
                    paint.setPixelColor(value)

                    val left = columnIndex.toFloat() * pixelsWidth
                    val top = rowIndex.toFloat() * pixelsHeight
                    val right = (columnIndex + 1).toFloat() * pixelsWidth
                    val bottom = (rowIndex + 1).toFloat() * pixelsHeight

                    canvas.drawRect(
                        left, top,
                        right, bottom, paint
                    )
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.pixels = pixels

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            pixels = state.pixels
            invalidate()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState(superState: Parcelable) : View.BaseSavedState(superState) {
        var pixels = arrayOf<Array<Int>>()
    }

}
