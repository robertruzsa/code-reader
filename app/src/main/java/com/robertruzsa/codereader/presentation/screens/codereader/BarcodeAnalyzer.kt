package com.robertruzsa.codereader.presentation.screens.codereader

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.robertruzsa.codereader.extensions.TAG
import com.robertruzsa.codereader.model.BarcodeType

class BarcodeAnalyzer(
    private val barcodeType: BarcodeType,
    private val barcodeListener: (String) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        processImageProxy(image)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(imageProxy: ImageProxy) {
        val inputImage = InputImage.fromMediaImage(
            imageProxy.image ?: return,
            imageProxy.imageInfo.rotationDegrees
        )

        val scannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(BarcodeType.getBarcodeFormatInt(barcodeType))
            .build()

        BarcodeScanning.getClient(scannerOptions)
            .process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach {
                    barcodeListener.invoke(it?.rawValue.toString())
                }
            }
            .addOnFailureListener {
                Log.e(this.TAG, it.message.toString())
            }.addOnCompleteListener {
                imageProxy.close()
            }
    }
}
