package cn.quibbler.coroutine.kotlin

import android.view.View

class Expand {

    companion object{

    }

    fun test() {
        //val strs = PermissionRequester.STRS//arrayOf("quibbler", "potter")
        val strs = arrayOf("quibbler", "potter")

        PermissionRequester().requestPermissionWithArray(strs)
        PermissionRequester().requestPermissions(*strs)

        PermissionRequester().requestPermissions("quibbler", "potter")


        requestPermissionWithArray(strs)
        requestPermissions(*strs)
    }

    fun requestPermissionWithArray(strs: Array<String>) {

    }

    fun requestPermissions(vararg strs: String) {

    }

}

fun View?.isBlank() {

}