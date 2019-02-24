package com.iliasavin.rocketbanktest.presentation

import android.graphics.Point
import com.iliasavin.rocketbanktest.core.extension.applyComputationScheduler
import com.iliasavin.rocketbanktest.data.fill.BFSFillManager
import com.iliasavin.rocketbanktest.data.fill.DEFAULT_PIXEL_SIZE
import com.iliasavin.rocketbanktest.data.fill.DFSFillManager
import com.iliasavin.rocketbanktest.data.fill.FillManager
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.ui.RocketView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RocketPresenter : BasePresenter<RocketView>() {
    private val subscriptions: CompositeDisposable = CompositeDisposable()
    private val bfsFillManager: FillManager = BFSFillManager().apply {
        handleNext = { point ->
            if (isRunning) {
                doInBackground { view?.refreshFirstImage(point, PixelColorState.COLORED) }
            }
        }
    }

    private val dfsFillManager: FillManager = DFSFillManager().apply {
        handleNext = { point ->
            if (isRunning) {
                doInBackground { view?.refreshSecondImage(point, PixelColorState.COLORED) }
            }
        }
    }

    var rowSize = DEFAULT_PIXEL_SIZE
    var columnSize = DEFAULT_PIXEL_SIZE

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

        bfsFillManager.setSize(rowSize, columnSize)
        bfsFillManager.image.updatePixels(image.pixels)

        dfsFillManager.setSize(rowSize, columnSize)
        dfsFillManager.image.updatePixels(image.pixels)

        view?.update(image)
    }

    fun fill(startPixel: Point) {
        var firstTimer = 0L

        bfsFillManager.startPixel = startPixel
        dfsFillManager.startPixel = startPixel

        val subscription = Single.fromCallable {
            firstTimer = System.currentTimeMillis()
            bfsFillManager.start()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.showTimeResult("BFS has been completed for a ${System.currentTimeMillis() - firstTimer}")
            }, {
            })
        subscriptions.add(subscription)

        var secondTimer = 0L
        subscriptions.add(Single.fromCallable {
            secondTimer = System.currentTimeMillis()
            dfsFillManager.start()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.showTimeResult("DFS has been completed for a ${System.currentTimeMillis() - secondTimer}")
            }, {
            })
        )
    }

    fun changeSpeed(speed: Long) {
        bfsFillManager.changeSpeed(speed)
        dfsFillManager.changeSpeed(speed)
    }

    fun clearAndStop() {
        bfsFillManager.clear()
        dfsFillManager.clear()
    }

    fun doInBackground(lambda: () -> Unit) {
        val subscription = Single.fromCallable { lambda() }
            .applyComputationScheduler()
            .subscribe({}, {})
        subscriptions.add(subscription)
    }
}
