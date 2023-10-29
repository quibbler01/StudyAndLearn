package cn.quibbler.coroutine.jitpack.parcelize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityParcelizeBinding

class ParcelizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParcelizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParcelizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val parcel: Parcel = Parcel.obtain()
        val user = User("Zhao", "Peng", 32, ExternalClass(10086))
        parcel.writeParcelable(user, 0)
        parcel.writeString("Quibbler")
        parcel.setDataPosition(0)

        val user1: User? = parcel.readParcelable(User::class.java.classLoader)
        binding.console.append(user1.toString())

        binding.console.append("\n")

        binding.console.append("\n${parcel.readString()}")

        parcel.recycle()
    }
}