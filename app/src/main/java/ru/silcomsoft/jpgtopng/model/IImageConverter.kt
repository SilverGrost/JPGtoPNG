package ru.silcomsoft.jpgtopng.model

import android.net.Uri
import io.reactivex.Single
import java.io.File

interface IImageConverter {
    fun jpegToPng(uriTargetImage: Uri, toFile: File): Single<Uri>
}