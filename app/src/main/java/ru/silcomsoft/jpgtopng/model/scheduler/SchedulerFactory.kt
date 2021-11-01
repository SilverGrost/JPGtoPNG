package ru.silcomsoft.jpgtopng.model.scheduler


object SchedulerFactory {
    fun create(): Schedulers = DefaultSchedulers()
}