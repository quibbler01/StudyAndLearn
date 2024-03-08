package cn.quibbler.coroutine.ui.view

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivitySignInfoBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale


class SignInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInfoBinding

    val str = "BC C3 5D 4D 36 06 F1 54 F0 40 2A B7 63 4E 84 90 C0 B2 44 C2 67 5C 3C 62 38 98 69 87 02 4F 0C 02".replace(" ", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppSigning.getSingInfo(this, "com.tencent.mm", AppSigning.SHA256).apply {
            binding.text2.append("SHA256:")
            binding.text2.append(this)
            binding.text2.append("\n")

            //BCC35D4D3606F154F0402AB7634E8490C0B244C2675C3C6238986987024F0C02
            binding.text2.append("SHA-256 签名: BC C3 5D 4D 36 06 F1 54 F0 40 2A B7 63 4E 84 90 C0 B2 44 C2 67 5C 3C 62 38 98 69 87 02 4F 0C 02 \n${str == this}")
        }

    }

}

object AppSigning {
    var TAG = "AppSigning"
    const val MD5 = "MD5"
    const val SHA1 = "SHA1"
    const val SHA256 = "SHA256"

    /**
     * Returns a string of the corresponding type for a signature
     *
     * @param context
     * @param packageName
     * @param type
     */
    fun getSingInfo(context: Context, packageName: String, type: String): String {
        var signStr = "error!"
        try {
            val signs: Array<Signature>? = getSignatures(context, packageName)
            val sig: Signature = signs!![0]
            signStr = getSignatureString(sig, type)
        } catch (_: Exception) {
        }
        return signStr
    }

    /**
     * Return the signature information of the corresponding package
     *
     * @param context
     * @param packageName
     */
    fun getSignatures(context: Context, packageName: String): Array<Signature>? {
        return try {
            val packageInfo: PackageInfo? = context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            packageInfo?.signatures
        } catch (_: PackageManager.NameNotFoundException) {
            null
        }
    }

    /**
     * Obtain the corresponding type of string (convert the signed byte [] information to hexadecimal)
     *
     * @param sig
     * @param type
     */
    fun getSignatureString(sig: Signature, type: String): String {
        var fingerprint = "error!"
        try {
            val hexBytes: ByteArray = sig.toByteArray()
            val digest: MessageDigest = MessageDigest.getInstance(type).apply {
                reset()
                update(hexBytes)
            }
            val buffer = StringBuffer()
            val byteArray = digest.digest()
            for (i in byteArray.indices) {
                val bi = Integer.toHexString(0xFF and byteArray[i].toInt())
                if (bi.length == 1) {
                    //Fill in 0 and convert to hexadecimal
                    buffer.append("0").append(bi)
                } else {
                    //Convert to hexadecimal
                    buffer.append(bi)
                }
            }
            //Convert to uppercase
            fingerprint = buffer.toString().uppercase(Locale.getDefault())
        } catch (_: NoSuchAlgorithmException) {
        }
        return fingerprint
    }

}