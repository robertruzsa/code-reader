package com.robertruzsa.codereader.model

import androidx.camera.core.CameraSelector

enum class CameraType(val value: Int) {
    DEFAULT_BACK_CAMERA(0),
    DEFAULT_FRONT_CAMERA(1);

    companion object {
        fun getCameraType(value: Int): CameraType {
            return values().find { it.value == value } ?: DEFAULT_BACK_CAMERA
        }

        fun getCameraSelector(cameraType: CameraType): CameraSelector {
            return when (cameraType) {
                DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
                DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
            }
        }
    }
}
