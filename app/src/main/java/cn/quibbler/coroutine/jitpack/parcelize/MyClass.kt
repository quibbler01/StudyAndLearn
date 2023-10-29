package cn.quibbler.coroutine.jitpack.parcelize

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.parcelize.WriteWith

@Parcelize
@TypeParceler<ExternalClass, ExternalClassParceler>
class MyClass(val externalClass: ExternalClass) : Parcelable

@Parcelize
class MyClass1(@TypeParceler<ExternalClass, ExternalClassParceler> val externalClass: ExternalClass) : Parcelable

@Parcelize
class MyClass2(val externalClass: @WriteWith<ExternalClassParceler>() ExternalClass) : Parcelable
