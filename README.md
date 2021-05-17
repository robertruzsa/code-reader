# CodeReaderView
This project implements a custom camera view for barcode scanning.
## Usage
### XML
```
<com.robertruzsa.codereader.presentation.widgets.CodeReaderView
    android:id="@+id/code_reader_view"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    app:cameraType="front" />
```
### Kotlin
#### Listener
```
binding.codeReaderView.setOnBarcodeScannedListener(
    barcodeType = Barcode.FORMAT_QR_CODE,
    action = { barcodeData ->
        // Handle the scanned barcode data here
    }
)
```
#### Camera selection
```
binding.codeReaderView.cameraType = CameraType.DEFAULT_FRONT_CAMERA
binding.codeReaderView.cameraType = CameraType.DEFAULT_BACK_CAMERA
```
## Supported barcode formats
- Code 128 (FORMAT_CODE_128)
- Code 39 (FORMAT_CODE_39)
- Code 93 (FORMAT_CODE_93)
- Codabar (FORMAT_CODABAR)
- EAN-13 (FORMAT_EAN_13)
- EAN-8 (FORMAT_EAN_8)
- ITF (FORMAT_ITF)
- UPC-A (FORMAT_UPC_A)
- UPC-E (FORMAT_UPC_E)
- QR Code (FORMAT_QR_CODE)
- PDF417 (FORMAT_PDF417)
- Aztec (FORMAT_AZTEC)
- Data Matrix (FORMAT_DATA_MATRIX)
