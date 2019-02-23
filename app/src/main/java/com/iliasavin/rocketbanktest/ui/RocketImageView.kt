package com.iliasavin.rocketbanktest.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.util.setPixelColor
import com.iliasavin.rocketbanktest.util.toPixel

class RocketImageView : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.style = Paint.Style.FILL
    }

    private var selectedPixels = mutableListOf<Pair<Int, Int>>()
    private var startPixel = Point()

    var onPixelTouch: (pixel: Point) -> Unit = {}

    var pixelImage: PixelImage = PixelImage(64, 64)

    var pixelsWidth = 0f
    var pixelsHeight = 0f

    fun generatePixels(width: Int, height: Int) {
        pixelImage = PixelImage(width, height)

        invalidate()
    }

    fun updateImage(pixel: Point, color: PixelColorState){
        pixelImage.pixels[pixel.x][pixel.y] = color

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        pixelsWidth = measuredWidth.toFloat() / pixelImage.pixels.first().size
        pixelsHeight = measuredHeight.toFloat() / pixelImage.pixels.size

        pixelImage.pixels.forEachIndexed { rowIndex, row ->
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
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startPixel = Point(event.x.toInt(), event.y.toInt())
                    .toPixel(pixelsWidth, pixelsHeight)
                return true
            }

            MotionEvent.ACTION_UP -> {
                startPixel.let(onPixelTouch)
                Toast.makeText(this.context, startPixel.toString(), Toast.LENGTH_SHORT).show()
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.pixels = pixelImage.pixels

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            pixelImage.pixels = state.pixels
            invalidate()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState(superState: Parcelable) : View.BaseSavedState(superState) {
        var pixels = arrayOf<Array<PixelColorState>>()
    }

}
