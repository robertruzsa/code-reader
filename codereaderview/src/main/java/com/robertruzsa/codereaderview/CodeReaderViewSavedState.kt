package com.robertruzsa.codereaderview

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class CodeReaderViewSavedState(
    val cameraType: CameraType,
    superState: Parcelable?
) : View.BaseSavedState(superState) {
    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(cameraType.value)
    }
}
