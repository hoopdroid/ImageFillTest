package com.iliasavin.rocketbanktest.presentation

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.fill.BFSFillManager
import com.iliasavin.rocketbanktest.fill.FillManager
import com.iliasavin.rocketbanktest.ui.RocketView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RocketPresenter : BasePresenter<RocketView>() {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val bfsFillManager: FillManager = BFSFillManager()
    val dfsFillManager: FillManager = BFSFillManager()

    override fun onAttach(view: RocketView) {
        super.onAttach(view)
    }

    override fun onDestroy() {
        super.onDestroy()

//        compositeDisposable.dispose()
    }

    fun updateImage() {
        view?.regenerate()
    }

    fun initImages(image: PixelImage) {
        bfsFillManager.image = PixelImage(image.pixels.size, image.pixels.first().size)
        bfsFillManager.image.pixels.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, column ->
                bfsFillManager.image.pixels[rowIndex][columnIndex] = image.pixels[rowIndex][columnIndex]
            }
        }
        bfsFillManager.onNext = { pixel ->
            view?.invalidate(pixel, PixelColorState.COLORED)
        }
    }

    fun fill(startPixel: Point) {
        bfsFillManager.startPixel = startPixel
        compositeDisposable.add(Single.fromCallable {
            bfsFillManager.start()
            false
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe ({
                val a = it
            }, {
                val a = 5
            })
        )
    }


}