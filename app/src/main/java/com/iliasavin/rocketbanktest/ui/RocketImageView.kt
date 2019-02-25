package com.iliasavin.rocketbanktest.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.core.extension.setPixelColor
import com.iliasavin.rocketbanktest.core.extension.toPixel
import com.iliasavin.rocketbanktest.data.fill.DEFAULT_PIXEL_SIZE

class RocketImageView : View {
    private var startPixel = Point()
    private var pixelsWidth = 0f
    private var pixelsHeight = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    var pixelImage: PixelImage = PixelImage(DEFAULT_PIXEL_SIZE, DEFAULT_PIXEL_SIZE)
    var onPixelTouch: (pixel: Point) -> Unit = {}

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)


    fun updatePixel(pixel: Point, color: PixelColorState) {
        pixelImage.pixels[pixel.x][pixel.y] = color
        invalidate()
    }

    fun updateImage(image: PixelImage) {
        pixelImage = PixelImage(image.pixels.size, image.pixels.first().size)
        pixelImage.updatePixels(image.pixels)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pixelImage.pixels.isEmpty()) return

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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startPixel = Point(event.x.toInt(), event.y.toInt())
                    .toPixel(pixelsWidth, pixelsHeight)
                startPixel.let(onPixelTouch)
                return true
            }

            MotionEvent.ACTION_UP -> {
                startPixel.let(onPixelTouch)
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
            pixelImage = PixelImage(state.pixels.size, state.pixels.first().size)
            pixelImage.updatePixels(state.pixels)
            invalidate()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState(superState: Parcelable) : View.BaseSavedState(superState) {
        var pixels = arrayOf<Array<PixelColorState>>()
    }

}
