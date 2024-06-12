package com.jmnetwork.e_jartas.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.jmnetwork.e_jartas.databinding.FragmentScanQrBinding
import com.jmnetwork.e_jartas.viewModel.ScanQrViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty

class ScanQrFragment : Fragment() {

    private var _binding: FragmentScanQrBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ScanQrViewModel
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[ScanQrViewModel::class.java]

        binding.apply {
            codeScanner = CodeScanner(requireContext(), scannerView).apply {
                camera = CodeScanner.CAMERA_BACK
                formats = CodeScanner.ALL_FORMATS
                autoFocusMode = AutoFocusMode.CONTINUOUS
                scanMode = ScanMode.SINGLE
                isAutoFocusEnabled = true
                isFlashEnabled = false
                startPreview()

                decodeCallback = DecodeCallback { qrString ->
                    requireActivity().runOnUiThread {
                        loadingAnim.visibility = View.VISIBLE
//                        viewModel.validateQRCode(qrString.text) { result ->
//                            when (result) {
//                                "webapp_success" -> {
//                                    val bundle = Bundle()
//                                    bundle.putString("page", "home")
//                                    (activity as MainActivity).loadFragment(HomeFragment(), bundle)
//                                }
//
//                                "webapp_failure" -> {
//                                    loadingAnim.visibility = View.GONE
//                                    startPreview()
//                                }
//                            }
//                        }
                    }
                }

                errorCallback = ErrorCallback {
                    requireActivity().runOnUiThread {
                        Toasty.error(requireContext(), "Camera initialization error: ${it.message}", Toasty.LENGTH_LONG).show()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}