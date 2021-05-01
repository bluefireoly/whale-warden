package net.axay.whalewarden.common.file

import java.io.File

fun File.createIfNotExists(): Boolean {
    return if (!exists()) {
        if (isDirectory)
            mkdirs()
        else {
            val absolute = absoluteFile
            if (!absolute.parentFile.exists())
                absolute.parentFile.mkdirs()
            createNewFile()
        }
    } else false
}
