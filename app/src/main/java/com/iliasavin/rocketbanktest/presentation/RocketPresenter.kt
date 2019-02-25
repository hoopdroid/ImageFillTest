package com.iliasavin.rocketbanktest.presentation

import android.graphics.Point
import com.iliasavin.rocketbanktest.core.extension.applyComputationScheduler
import com.iliasavin.rocketbanktest.core.extension.applyMainThreadScheduler
import com.iliasavin.rocketbanktest.data.fill.*
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.ui.RocketView
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RocketPresenter : BasePresenter<RocketView>() {
    private var firstImageFillManager: FillManager = BFSFillManager().apply {
        handleNext = { point ->
            imagePainter(true, point, isRunning)()
        }
    }

    private var secondImageFillManager: FillManager = DFSFillManager().apply {
        handleNext = { point ->
            imagePainter(false, point, isRunning)()
        }
    }

    var rowSize = DEFAULT_PIXEL_SIZE
    var columnSize = DEFAULT_PIXEL_SIZE

    var savedSpeed = DEFAULT_SPEED

    override fun onAttach(view: RocketView) {
        super.onAttach(view)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun updateImages(rowsSize: Int, columnsSize: Int) {
        rowSize = rowsSize
        columnSize = columnsSize

        val image = PixelImage(rowSize, columnSize)
        image.setRandomly()

        firstImageFillManager.setSize(rowSize, columnSize)
        firstImageFillManager.image.updatePixels(image.pixels)

        secondImageFillManager.setSize(rowSize, columnSize)
        secondImageFillManager.image.updatePixels(image.pixels)

        view?.update(image)
    }

    fun fill(startPixel: Point) {
        firstImageFillManager.startPixel = startPixel
        secondImageFillManager.startPixel = startPixel

        var firstTimer = 0L
        val firstImageSubscription = Single.fromCallable {
            firstTimer = System.currentTimeMillis()
            firstImageFillManager.start()
        }.applyMainThreadScheduler(Schedulers.io())
            .subscribe({
                view?.showTimeResult(
                    "FIRST method has been completed for a" +
                            " ${System.currentTimeMillis() - firstTimer} millis"
                )
            }, {})

        var secondTimer = 0L
        val secondImageSubscription = Single.fromCallable {
            secondTimer = System.currentTimeMillis()
            secondImageFillManager.start()
        }.applyMainThreadScheduler(Schedulers.io())
            .subscribe({
                view?.showTimeResult(
                    "SECOND method has been completed for a " +
                            "${System.currentTimeMillis() - secondTimer} millis"
                )
            }, {})

        subscriptions.add(firstImageSubscription)
        subscriptions.add(secondImageSubscription)

    }

    fun changeSpeed(speed: Long) {
        this.savedSpeed = speed
        firstImageFillManager.changeSpeed(speed)
        secondImageFillManager.changeSpeed(speed)
    }

    fun clearAndStop() {
        firstImageFillManager.clear()
        secondImageFillManager.clear()
    }

    private fun imagePainter(isFirstImage: Boolean, point: Point, isRunning: Boolean): () -> Unit {
        return if (isFirstImage) {
            {
                fillImageInBackground(isRunning) { view?.refreshFirstImage(point, PixelColorState.COLORED) }
            }
        } else {
            {
                fillImageInBackground(isRunning) { view?.refreshSecondImage(point, PixelColorState.COLORED) }
            }
        }

    }

    fun fillImageInBackground(isRunning: Boolean, viewAction: () -> Unit) {
        if (!isRunning) return

        val subscription = Single.fromCallable { viewAction() }
            .applyComputationScheduler()
        subscriptions.add(subscription)
    }

    fun setImageFillMethod(method: Int, isFirst: Boolean) {
        if (firstImageFillManager.isRunning) return

        var manager: FillManager = if (isFirst) firstImageFillManager else secondImageFillManager

        val tempImage = PixelImage(rowSize, columnSize)
        tempImage.updatePixels(manager.image.pixels)

        when (method) {
            FillMethods.BFS.id -> {
                manager = BFSFillManager().apply {
                    configFillManager(tempImage, isFirst)
                }
            }
            FillMethods.DFS.id -> {
                manager = DFSFillManager().apply {
                    configFillManager(tempImage, isFirst)
                }
            }
            FillMethods.DUMMY.id -> {
                manager = DummyFillManager().apply {
                    configFillManager(tempImage, isFirst)
                }
            }
        }
        if (isFirst) {
            firstImageFillManager = manager
        } else {
            secondImageFillManager = manager
        }
    }

    private fun FillManager.configFillManager(
        tempImage: PixelImage,
        isFirst: Boolean
    ) {
        handleNext = { point ->
            imagePainter(isFirst, point, isRunning)()
        }
        image.updatePixels(tempImage.pixels)
        speed = savedSpeed
    }


    fun buildBFSFillManager(handleNext: (Point) -> Unit): FillManager {
        val manager = BFSFillManager()
        manager.handleNext = handleNext
        return manager
    }

    fun buildDFSFillManager(handleNext: (Point) -> Unit): FillManager {
        val manager = DFSFillManager()
        manager.handleNext = handleNext
        return manager
    }

    fun buildDummyFillManager(handleNext: (Point) -> Unit): FillManager {
        val manager = DummyFillManager()
        manager.handleNext = handleNext
        return manager
    }
}
