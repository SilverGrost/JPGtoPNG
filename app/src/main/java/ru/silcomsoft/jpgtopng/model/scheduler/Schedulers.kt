package ru.silcomsoft.jpgtopng.model.scheduler

import io.reactivex.Scheduler

interface Schedulers {

    fun background(): Scheduler
    fun main(): Scheduler
    fun computation(): Scheduler
}