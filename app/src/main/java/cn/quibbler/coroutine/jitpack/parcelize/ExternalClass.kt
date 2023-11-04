package cn.quibbler.coroutine.jitpack.parcelize

import android.os.Parcel
import kotlinx.parcelize.Parceler

data class ExternalClass(val value: Int)

object ExternalClassParceler : Parceler<ExternalClass> {
    override fun create(parcel: Parcel): ExternalClass {
        return ExternalClass(parcel.readInt())
    }

    override fun ExternalClass.write(parcel: Parcel, flags: Int) {
        parcel.writeInt(value)
    }
}