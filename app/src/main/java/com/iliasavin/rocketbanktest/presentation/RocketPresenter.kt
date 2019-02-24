package com.iliasavin.rocketbanktest.presentation

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.fill.BFSFillManager
import com.iliasavin.rocketbanktest.fill.DFSFillManager
import com.iliasavin.rocketbanktest.fill.DummyFillManager
import com.iliasavin.rocketbanktest.fill.FillManager
import com.iliasavin.rocketbanktest.ui.RocketView
import com.iliasavin.rocketbanktest.util.DEFAULT_PIXEL_SIZE
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RocketPresenter : BasePresenter<RocketView>() {
    private val subscriptions: CompositeDisposable = CompositeDisposable()
    private val bfsFillManager: FillManager = BFSFillManager()
    private val dfsFillManager: FillManager = DummyFillManager()

    var rowSize = DEFAULT_PIXEL_SIZE
    var columnSize = DEFAULT_PIXEL_SIZE

    override fun onAttach(view: RocketView) {
        super.onAttach(view)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun initImages(sizeWidth: Int, sizeHeight: Int) {
        rowSize = sizeWidth
        columnSize = sizeHeight

        val image = PixelImage(rowSize, columnSize)
        image.setRandomly()

        bfsFillManager.setSize(rowSize, columnSize)
        bfsFillManager.image.updatePixels(image.pixels)
        bfsFillManager.onNext = { pixel ->
            if (bfsFillManager.isRunning)
                subscriptions.add(Single.fromCallable {
                    view?.refreshFirstImage(pixel, PixelColorState.COLORED)
                    true
                }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.computation())
                    .subscribe({}, {})
                )
        }

        dfsFillManager.setSize(rowSize, columnSize)
        dfsFillManager.image.updatePixels(image.pixels)
        dfsFillManager.onNext = {
            if (dfsFillManager.isRunning)
                subscriptions.add(Single.fromCallable {
                    view?.refreshSecondImage(it, PixelColorState.COLORED)
                    true
                }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.computation())
                    .subscribe({}, {})
                )
        }

        view?.regenerate(image)
    }

    fun fill(startPixel: Point) {
        var firstTimer = 0L

        bfsFillManager.startPixel = startPixel
        dfsFillManager.startPixel = startPixel

        subscriptions.add(Single.fromCallable {
            firstTimer = System.currentTimeMillis()
            bfsFillManager.start()
            false
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //                view?.showTimeResult("BFS has been completed for a ${System.currentTimeMillis() - firstTimer}")
            }, {
            })
        )

        var secondTimer = 0L
        subscriptions.add(Single.fromCallable {
            secondTimer = System.currentTimeMillis()
            dfsFillManager.start()
            false
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //                view?.showTimeResult("DFS has been completed for a ${System.currentTimeMillis() - secondTimer}")
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


}