package ru.silcomsoft.jpgtopng.model

import android.content.Context

object ImageConverterFactory {

    fun create(context: Context): IImageConverter = ImageConverterImpl(context)
}