package cn.quibbler.coroutine.jitpack.parcelize

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
data class User(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val externalClass: @WriteWith<ExternalClassParceler>() ExternalClass
) :
    Parcelable

@Parcelize
data class User1(val firstName: String, val lastName: String, var age: Int) : Parcelable {

    companion object : Parceler<User1> {
        override fun create(parcel: Parcel): User1 {
            return User1(parcel.readString()!!, parcel.readString()!!, parcel.readInt())
        }

        override fun User1.write(parcel: Parcel, flags: Int) {
            parcel.writeString(firstName)
            parcel.writeString(lastName)
            parcel.writeInt(age)
        }
    }

}