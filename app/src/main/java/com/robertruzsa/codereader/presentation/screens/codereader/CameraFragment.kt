package com.robertruzsa.codereader.presentation.screens.codereader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.robertruzsa.codereader.databinding.FragmentCameraBinding
import com.robertruzsa.codereaderview.BarcodeFormat

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.codeReaderView.setOnBarcodeScannedListener(
            barcodeFormat = BarcodeFormat.QR_CODE,
            action = ::handleBarcode
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleBarcode(barcodeData: String) {
        Toast.makeText(activity, barcodeData, Toast.LENGTH_SHORT).show()
    }
}
