package com.robertruzsa.codereader.model

import com.google.mlkit.vision.barcode.Barcode

enum class BarcodeType(val value: Int) {
    ANY(0),
    QR(1),
    AZTEC(2);

    companion object {
        fun getBarcodeType(value: Int): BarcodeType {
            return values().find { it.value == value } ?: ANY
        }

        fun getBarcodeFormatInt(barcodeType: BarcodeType): Int {
            return when (barcodeType) {
                ANY -> Barcode.FORMAT_ALL_FORMATS
                QR -> Barcode.FORMAT_QR_CODE
                AZTEC -> Barcode.FORMAT_AZTEC
            }
        }
    }
}
