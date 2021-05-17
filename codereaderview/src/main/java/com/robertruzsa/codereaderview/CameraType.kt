package com.robertruzsa.codereaderview

import androidx.camera.core.CameraSelector

enum class CameraType(val value: Int) {
    BACK_CAMERA(0),
    FRONT_CAMERA(1);

    companion object {
        fun getCameraType(value: Int): CameraType {
            return values().find { it.value == value } ?: BACK_CAMERA
        }

        fun getCameraSelector(cameraType: CameraType): CameraSelector {
            return when (cameraType) {
                BACK_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
                FRONT_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
            }
        }
    }
}
