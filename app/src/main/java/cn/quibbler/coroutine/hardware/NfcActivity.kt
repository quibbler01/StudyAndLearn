package cn.quibbler.coroutine.hardware

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.colorspace.connect
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityNfcBinding
import java.io.IOException
import java.nio.charset.Charset
import java.util.Arrays
import kotlin.text.format
import kotlin.text.toByteArray

class NfcActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "NfcActivity"
    }

    private lateinit var binding: ActivityNfcBinding

    private var nfcAdapter: NfcAdapter? = null

    // Mapping the hex byte to the URI prefix
    private val uriPrefixMap = mapOf(
        0x00.toByte() to "",
        0x01.toByte() to "http://www.",
        0x02.toByte() to "https://www.",
        0x03.toByte() to "http://",
        0x04.toByte() to "https://",
        0x05.toByte() to "tel:",
        0x06.toByte() to "mailto:",
        0x07.toByte() to "ftp://anonymous:anonymous@",
        0x08.toByte() to "ftp://ftp.",
        0x09.toByte() to "ftps://",
        0x0A.toByte() to "sftp://",
        0x0B.toByte() to "smb://",
        0x0C.toByte() to "nfs://",
        0x0D.toByte() to "ftp://",
        0x0E.toByte() to "dav://",
        0x0F.toByte() to "news:",
        0x10.toByte() to "telnet://",
        0x11.toByte() to "imap:",
        0x12.toByte() to "rtsp://",
        0x13.toByte() to "urn:",
        0x14.toByte() to "pop:",
        0x15.toByte() to "sip:",
        0x16.toByte() to "sips:",
        0x17.toByte() to "tftp:",
        0x18.toByte() to "btspp://",
        0x19.toByte() to "btl2cap://",
        0x1A.toByte() to "btgoep://",
        0x1B.toByte() to "tcpobex://",
        0x1C.toByte() to "irdaobex://",
        0x1D.toByte() to "file://",
        0x1E.toByte() to "urn:epc:id:",
        0x1F.toByte() to "urn:epc:tag:",
        0x20.toByte() to "urn:epc:pat:",
        0x21.toByte() to "urn:epc:raw:",
        0x22.toByte() to "urn:epc:",
        0x23.toByte() to "urn:nfc:"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNfcBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(
                this,
                "NFC is not supported on this device.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle the tag detected while app is open
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
        ) {

            val tag: Tag? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            }

            tag?.let { processTag(it) }
        }
    }

    private fun enableForegroundDispatch() {
        nfcAdapter?.let { adapter ->
            val intent = Intent(this, javaClass).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
            )
            val filters = arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))
            adapter.enableForegroundDispatch(this, pendingIntent, filters, null)
        }
    }

    private fun processTag(tag: Tag) {
        val ndef = Ndef.get(tag)
        if (ndef == null) {
            binding.tvNfcContent.text = "Tag is not NDEF formatted"
            return
        }

        try {
            ndef.connect()
            val ndefMessage = ndef.ndefMessage
            ndef.close()

            if (ndefMessage != null && ndefMessage.records.isNotEmpty()) {
                val record = ndefMessage.records[0]

                // Check if it is a URI record (TNF = 0x01, Type = "U")
                if (record.tnf == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(
                        record.type,
                        NdefRecord.RTD_URI
                    )
                ) {
                    val fullContent = parseUriRecord(record.payload)
                    binding.tvNfcContent.text = "URI Detected:\n$fullContent"
                }
                // Check if it is a Text record (Type = "T")
                else if (record.tnf == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(
                        record.type,
                        NdefRecord.RTD_TEXT
                    )
                ) {
                    val textContent = parseTextRecord(record.payload)
                    binding.tvNfcContent.text = "Text Detected:\n$textContent"
                } else if (record.tnf == NdefRecord.TNF_MIME_MEDIA && String(record.type) == "application/vnd.wfa.wsc") {
                    // Check if it is a Wi-Fi Configuration record (MIME type "application/vnd.wfa.wsc")
                    val wifiContent = parseWifiRecord(record.payload)
                    binding.tvNfcContent.text = "Wi-Fi Detected:\n$wifiContent"
                } else {
                    binding.tvNfcContent.text = "Unknown Record Type"
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "read nfc failed", e)
            binding.tvNfcContent.text = "Error reading tag: ${e.message}"
        }
    }

    /**
     * Parses the payload based on the URI Identifier Code (first byte).
     */
    private fun parseUriRecord(payload: ByteArray): String {
        if (payload.isEmpty()) return ""

        val prefixByte = payload[0]
        val prefix = uriPrefixMap[prefixByte] ?: "" // Look up 0x05 -> "tel:", etc.

        // The rest of the payload is the actual data
        val fullUriLength = payload.size - 1
        val restOfUri = String(payload, 1, fullUriLength, Charset.forName("UTF-8"))

        return prefix + restOfUri
    }

    /**
     * Helper for standard Text records (different from URI records)
     */
    private fun parseTextRecord(payload: ByteArray): String {
        val textEncoding = if ((payload[0].toInt() and 128) == 0) "UTF-8" else "UTF-16"
        val languageCodeLength = payload[0].toInt() and 63
        return String(
            payload,
            languageCodeLength + 1,
            payload.size - languageCodeLength - 1,
            Charset.forName(textEncoding)
        )
    }

    /**
     * Parses Wi-Fi configuration payload (WSC).
     * Extracts SSID (0x1045) and Network Key (0x1027).
     */
    private fun parseWifiRecord(payload: ByteArray): String {
        val buffer = java.nio.ByteBuffer.wrap(payload)
        var ssid = ""
        var networkKey = ""

        while (buffer.hasRemaining()) {
            // Header (Type + Length) is 4 bytes
            if (buffer.remaining() < 4) break

            val type = buffer.short.toInt() and 0xFFFF
            val length = buffer.short.toInt() and 0xFFFF

            if (buffer.remaining() < length) break

            val value = ByteArray(length)
            buffer.get(value)

            when (type) {
                0x100E -> {
                    // 0x100E is the Credential Attribute. content is nested inside.
                    return parseWifiRecord(value)
                }
                0x1045 -> ssid = String(value, Charset.forName("UTF-8"))
                0x1027 -> networkKey = String(value, Charset.forName("UTF-8"))
            }
        }

        return if (ssid.isNotEmpty()) {
            "SSID: $ssid\nPassword: $networkKey"
        } else {
            "Valid Wi-Fi tag detected, but could not parse SSID."
        }
    }

    /**
     * Creates an NDEF message with a single text record.
     */
    private fun createNdefMessage(text: String): NdefMessage {
        val lang = "en" // Language code
        val langBytes = lang.toByteArray(Charset.forName("US-ASCII"))
        val textBytes = text.toByteArray(Charset.forName("UTF-8"))
        val status = (langBytes.size).toByte()

        val payload = ByteArray(1 + langBytes.size + textBytes.size)
        payload[0] = status
        System.arraycopy(langBytes, 0, payload, 1, langBytes.size)
        System.arraycopy(textBytes, 0, payload, 1 + langBytes.size, textBytes.size)

        val record = NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload)
        return NdefMessage(arrayOf(record))
    }

    /**
     * Writes an NDEF message to a tag.
     */
    private fun writeNdefMessage(tag: Tag, message: NdefMessage) {
        try {
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                // Tag is already NDEF formatted.
                ndef.connect()
                if (!ndef.isWritable) {
                    Toast.makeText(this, "Tag is read-only.", Toast.LENGTH_SHORT).show()
                    ndef.close()
                    return
                }
                if (ndef.maxSize < message.toByteArray().size) {
                    Toast.makeText(this, "Tag is too small.", Toast.LENGTH_SHORT).show()
                    ndef.close()
                    return
                }
                ndef.writeNdefMessage(message)
                ndef.close()
                Toast.makeText(this, "Successfully wrote to tag!", Toast.LENGTH_SHORT).show()
            } else {
                // Tag is not NDEF formatted, try to format it.
                val formatable = NdefFormatable.get(tag)
                if (formatable != null) {
                    formatable.connect()
                    formatable.format(message)
                    formatable.close()
                    Toast.makeText(this, "Successfully formatted and wrote to tag!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Tag does not support NDEF formatting.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to write NDEF message", e)
            Toast.makeText(this, "Failed to write tag.", Toast.LENGTH_SHORT).show()
        }
    }

}