package cn.quibbler.coroutine.jitpack.mlkit

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import cn.quibbler.coroutine.databinding.ActivityMlkitBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MlKitActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MlKitActivity"
    }

    private lateinit var binding: ActivityMlkitBinding
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var textRecognizer: TextRecognizer
    private lateinit var barcodeScanner: BarcodeScanner

    private var isTextRecognitionEnabled = true
    private var isBarcodeScanningEnabled = true

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMlkitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        initMlKit()
        initViews()
        checkCameraPermission()
    }

    private fun initMlKit() {
        // 初始化文字识别
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        // 初始化二维码扫描
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_DATA_MATRIX
            )
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)
    }

    private fun initViews() {
        // 文字识别按钮
        binding.btnTextRecognition.setOnClickListener {
            isTextRecognitionEnabled = !isTextRecognitionEnabled
            binding.btnTextRecognition.text = if (isTextRecognitionEnabled) {
                "文字识别: ON"
            } else {
                "文字识别: OFF"
            }
        }

        // 二维码扫描按钮
        binding.btnBarcodeScan.setOnClickListener {
            isBarcodeScanningEnabled = !isBarcodeScanningEnabled
            binding.btnBarcodeScan.text = if (isBarcodeScanningEnabled) {
                "二维码扫描: ON"
            } else {
                "二维码扫描: OFF"
            }
        }

        // 选择图片按钮
        binding.btnSelectImage.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, ImageAnalysisAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun pickImageFromGallery() {
        val intent = android.content.Intent(android.content.Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, 100)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    processImage(bitmap)
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading image", e)
                }
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        if (isTextRecognitionEnabled) {
            textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    binding.tvResult.append("\n--- 文字识别结果 ---\n")
                    binding.tvResult.append(visionText.text)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Text recognition failed", e)
                }
        }

        if (isBarcodeScanningEnabled) {
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        binding.tvResult.append("\n--- 二维码/条码 ---\n")
                        binding.tvResult.append("Value: ${barcode.rawValue}\n")
                        binding.tvResult.append("Format: ${barcode.format}\n")
                        binding.tvResult.append("Type: ${barcode.valueType}\n")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Barcode scanning failed", e)
                }
        }
    }

    private inner class ImageAnalysisAnalyzer : ImageAnalysis.Analyzer {
        @androidx.camera.core.ExperimentalGetImage
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(
                    mediaImage, imageProxy.imageInfo.rotationDegrees
                )

                if (isTextRecognitionEnabled) {
                    textRecognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            runOnUiThread {
                                if (visionText.text.isNotEmpty()) {
                                    binding.tvResult.text = "--- 文字识别 ---\n${visionText.text}"
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Text recognition failed", e)
                        }
                }

                if (isBarcodeScanningEnabled) {
                    barcodeScanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            runOnUiThread {
                                for (barcode in barcodes) {
                                    binding.tvResult.text = "--- 二维码/条码 ---\nValue: ${barcode.rawValue}"
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Barcode scanning failed", e)
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            } else {
                imageProxy.close()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        textRecognizer.close()
        barcodeScanner.close()
    }
}
