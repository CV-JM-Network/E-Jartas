package com.jmnetwork.e_jartas.view.manajemenTiang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.BottomSheetProviderBinding
import com.jmnetwork.e_jartas.model.ProviderData
import com.jmnetwork.e_jartas.viewModel.ManajemenTiangViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import es.dmoral.toasty.Toasty

class BottomSheetItemProvider : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetProviderBinding
    private lateinit var viewModel: ManajemenTiangViewModel

    private var providerData: ProviderData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetProviderBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[ManajemenTiangViewModel::class.java]

        arguments?.let { it ->
            providerData = it.getInt("idProvider").let { idProvider ->
                viewModel.providerData.value?.values?.find { it.idProvider == idProvider }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnHapusItem.setOnClickListener {
                val dialog = MaterialDialog.Builder(requireActivity())
                    .setTitle("Hapus Data")
                    .setMessage(getString(R.string.confirm_delete))
                    .setCancelable(true)

                    .setPositiveButton(getString(R.string.no), R.drawable.ic_close) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton(getString(R.string.yes), R.drawable.ic_trash) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        viewModel.deleteProvider(providerData!!.idProvider) { status, message ->
                            when (status) {
                                "success" -> {
                                    Toasty.success(requireContext(), message, Toasty.LENGTH_SHORT).show()
                                    dismiss()
                                }

                                else -> {
                                    Toasty.error(requireContext(), message, Toasty.LENGTH_SHORT).show()
                                    dismiss()
                                }
                            }
                        }
                    }
                    .build()
                dialog.show()
            }

            btnEditItem.setOnClickListener {}

            swBlacklist.apply {
                isChecked = providerData?.blackList == "ya"
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.blacklistProvider(providerData!!.idProvider, isChecked) { status, message ->
                        when (status) {
                            "success" -> {
                                val toastMessage = if (isChecked) "Blacklist" else "Buka Blacklist"
                                Toasty.success(requireContext(), toastMessage, Toasty.LENGTH_SHORT).show()
                            }

                            else -> {
                                swBlacklist.isChecked = !isChecked
                                Toasty.error(requireContext(), message, Toasty.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}