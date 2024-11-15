package cn.quibbler.coroutine.kotlin

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

class Expand {

    companion object {

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

fun outline(view: View) {
    view.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            // 设置圆角率为
            outline.setRoundRect(0, 0, view.width, view.height, 10f)
        }
    }

    //视图会根据outlineProvider提供的轮廓进行裁剪。任何超出轮廓的部分都会被裁剪掉
    view.clipToOutline = true
}