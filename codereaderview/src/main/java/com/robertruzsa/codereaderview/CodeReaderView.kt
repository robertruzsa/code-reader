package com.robertruzsa.codereaderview

import android.Manifest
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
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
import com.robertruzsa.codereaderview.databinding.ViewCodeReaderBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CodeReaderView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var binding: ViewCodeReaderBinding =
        ViewCodeReaderBinding.inflate(LayoutInflater.from(context), this, true)

    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private var onBarcodeScanned: (String) -> Unit = {}

    private var barcodeFormat: BarcodeFormat = BarcodeFormat.ALL_FORMATS
    lateinit var cameraType: CameraType

    init {
        init(context, attrs)
        checkCameraPermission(
            onPermissionGranted = ::startCamera,
            onPermissionDenied = ::showPermissionDeniedToast
        )
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CodeReaderView)

        val cameraTypeValue = attributes.getInt(R.styleable.CodeReaderView_cameraType, 0)
        cameraType = CameraType.getCameraType(cameraTypeValue)

        binding.toggleCamera.setOnClickListener {
            toggleCamera()
        }

        attributes.recycle()
    }

    fun setOnBarcodeScannedListener(
        barcodeFormat: BarcodeFormat,
        action: (String) -> Unit
    ) {
        this.barcodeFormat = barcodeFormat
        onBarcodeScanned = action
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()
                    .also {
                        it.setSurfaceProvider(binding.previewView.surfaceProvider)
                    }

                val imageAnalyzer = ImageAnalysis.Builder().build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            BarcodeAnalyzer(barcodeFormat) { data ->
                                onBarcodeScanned.invoke(data)
                                stopCamera()
                            }
                        )
                    }

                try {
                    stopCamera()
                    cameraProvider?.bindToLifecycle(
                        context as LifecycleOwner,
                        CameraType.getCameraSelector(cameraType),
                        preview,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    Log.e(this.TAG, "Use case binding failed", exc)
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    private fun checkCameraPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        Dexter.withContext(context).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    onPermissionGranted.invoke()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    onPermissionDenied.invoke()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun showPermissionDeniedToast() {
        Toast.makeText(
            context,
            "Permissions not granted by the user.",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cameraExecutor.shutdown()
    }

    fun toggleCamera() {
        cameraType = when (cameraType) {
            CameraType.BACK_CAMERA -> CameraType.FRONT_CAMERA
            CameraType.FRONT_CAMERA -> CameraType.BACK_CAMERA
        }
        startCamera()
    }
}
