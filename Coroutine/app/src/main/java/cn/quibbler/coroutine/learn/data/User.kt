package cn.quibbler.coroutine.learn.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val name: String) : Parcelable
