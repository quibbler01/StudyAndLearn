package cn.quibbler.coroutine.kotlin.contract

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.internal.Ref


/**
 * https://blog.csdn.net/HJXASLZYY/article/details/122676634
 */

interface File

class PdfFile : File {

    fun convertToWord() {

    }

}

fun move(file: File) {
    if (file is PdfFile) {
        file.convertToWord()
    }
}


//fun isBird(animal: Animal) = animal is Bird

@OptIn(ExperimentalContracts::class)
fun isPDF(file: File): Boolean {
    contract {
        returns(true) implies (file is PdfFile)
    }
    return file is PdfFile
}

@ExperimentalContracts
fun move2(file: File) {
    if (isPDF(file)) {
        file.convertToWord()
    }
}

@ExperimentalContracts
fun test() {
    var data: String
    init {
        data = ""
    }
    data.length
}

@ExperimentalContracts
fun init(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
}


