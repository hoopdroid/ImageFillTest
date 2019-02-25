package com.iliasavin.rocketbanktest.presentation

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<View> {
    protected val subscriptions: CompositeDisposable = CompositeDisposable()

    protected var view: View? = null

    open fun onAttach(view: View) {
        this.view = view
    }

    open fun onDestroy() {
        this.view = null
    }
}
