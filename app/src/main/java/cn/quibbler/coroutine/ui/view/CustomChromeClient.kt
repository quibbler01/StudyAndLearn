package cn.quibbler.coroutine.ui.view

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient

class CustomChromeClient : WebChromeClient() {

    companion object {
        const val TAG = "consoleCustomChromeClient"
    }

    /*android 低版本 Desperate*/
    override fun onConsoleMessage(message: String?, lineNumber: Int, sourceID: String?) {
        Log.d(TAG, "$message($sourceID:$lineNumber)")
        super.onConsoleMessage(message, lineNumber, sourceID)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d(
            TAG,
            "[" + consoleMessage?.messageLevel() + "] " + consoleMessage?.message() + "(" + consoleMessage?.sourceId() + ":" + consoleMessage?.lineNumber() + ")"
        )
        return super.onConsoleMessage(consoleMessage)
    }

}

class Console {

    @android.webkit.JavascriptInterface
    fun log(tag: String, msg: String) {
        Log.d(tag, msg)
    }

}