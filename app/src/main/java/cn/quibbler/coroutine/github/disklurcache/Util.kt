package cn.quibbler.coroutine.github.disklurcache

import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.Reader
import java.io.StringWriter
import java.lang.Exception
import java.nio.charset.Charset

val US_ASCII = Charset.forName("US-ASCII")

val UTF_8 = Charset.forName("UTF-8")

@Throws(IOException::class)
fun readFully(reader: Reader): String {
    reader.use {
        val writer = StringWriter()
        val buffer = CharArray(1024)
        var count: Int
        while (reader.read(buffer).apply { count = this } != -1) {
            writer.write(buffer, 0, count)
        }
        return writer.toString()
    }
}

/**
 * Deletes the contents of dir.
 * Throws an IOException if any file could not be deleted, or if dir is not a readable directory.
 *
 * @param dir File
 * @throws IOException
 */
@Throws(IOException::class)
fun deleteContents(dir: File) {
    val files = dir.listFiles() ?: throw IOException("not a readable directory: $dir")
    for (file in files) {
        if (file.isDirectory) {
            deleteContents(file)
        }
        if (!file.delete()) {
            throw IOException("failed to delete file: $file")
        }
    }
}

fun closeQuietly(closeable: Closeable?) {
    try {
        closeable?.close()
    } catch (rethrown: RuntimeException) {
        throw rethrown
    } catch (_: Exception) {

    }
}