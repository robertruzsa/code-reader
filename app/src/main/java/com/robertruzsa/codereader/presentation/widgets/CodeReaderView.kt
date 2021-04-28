package com.robertruzsa.codereader.presentation.widgets

import android.Manifest
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.robertruzsa.codereader.databinding.ViewCodeReaderBinding
import com.robertruzsa.codereader.extensions.TAG
import com.robertruzsa.codereader.presentation.screens.codereader.BarcodeAnalyzer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CodeReaderView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var binding: ViewCodeReaderBinding =
        ViewCodeReaderBinding.inflate(LayoutInflater.from(context), this, true)

    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private val preview = Preview.Builder()
        .build()
        .also {
            it.setSurfaceProvider(binding.previewView.surfaceProvider)
        }

    private val imageAnalyzer = ImageAnalysis.Builder()
        .build()
        .also {
            it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { data ->
                onBarcodeScanned.invoke(data)
                stopCamera()
            })
        }

    private var onBarcodeScanned: (String) -> Unit = {}

    init {
        checkCameraPermission()
    }

    fun setOnBarcodeScannedListener(action: (String) -> Unit) {
        onBarcodeScanned = action
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            try {
                stopCamera()
                cameraProvider?.bindToLifecycle(
                    context as LifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e(this.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    private fun checkCameraPermission() {
        Dexter.withContext(context).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    startCamera()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        context,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                }

            }).check()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cameraExecutor.shutdown()
    }
}
