package com.iliasavin.rocketbanktest.core.extension

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.applyComputationScheduler() = applyComputationThreadScheduler(Schedulers.computation())

fun <T> Single<T>.applyMainThreadScheduler(scheduler: Scheduler) =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applyComputationThreadScheduler(scheduler: Scheduler) =
    subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(Schedulers.computation())
        .subscribe({}, {})